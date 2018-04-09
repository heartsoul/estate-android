package com.glodon.bim.business.setting.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.glodon.bim.BuildConfig;
import com.glodon.bim.R;
import com.glodon.bim.base.BaseActivity;
import com.glodon.bim.basic.listener.ThrottleClickEvents;
import com.glodon.bim.basic.utils.SharedPreferencesUtil;
import com.glodon.bim.business.login.view.PictureCodeActivity;
import com.glodon.bim.business.setting.contract.SettingContract;
import com.glodon.bim.business.setting.presenter.SettingPresenter;
import com.glodon.bim.business.setting.util.GlodonUpdateManager;
import com.glodon.bim.common.config.CommonConfig;
import com.glodon.bim.customview.ToastManager;

/**
 * 描述：设置界面
 * 作者：zhourf on 2017/10/24
 * 邮箱：zhourf@glodon.com
 */

public class SettingActivity extends BaseActivity implements SettingContract.View, View.OnClickListener {

    private View mStatusView;
    private SettingContract.Presenter mPresenter;
    private RelativeLayout mBackView;
    private RelativeLayout mChangePassword,mVersionInfo,mFeedBack,mContactUs,mAboutUs,mChangeProject,mOffline;
    private TextView mVersionInfoText,mPhoneNumber;
    private Button mSignOutBtn;
    private ImageView wlanImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_activity);

        initView();
        initStatusBar(mStatusView);
        setListener();

        initData();
    }



    private void initView() {
        mStatusView = findViewById(R.id.setting_status);
        mBackView = (RelativeLayout) findViewById(R.id.setting_back);
        mChangePassword = (RelativeLayout) findViewById(R.id.setting_change_password);
        wlanImage =  findViewById(R.id.setting_wlan_flag);
        mVersionInfo = (RelativeLayout) findViewById(R.id.setting_version_info);
        mFeedBack = (RelativeLayout) findViewById(R.id.setting_feedback);
        mContactUs = (RelativeLayout) findViewById(R.id.setting_contact_us);
        mAboutUs = (RelativeLayout) findViewById(R.id.setting_about_us);
        mChangeProject = (RelativeLayout) findViewById(R.id.setting_change_project);
        mOffline = (RelativeLayout) findViewById(R.id.setting_offline);
        mVersionInfoText = (TextView) findViewById(R.id.setting_version_info_text);
        mPhoneNumber = (TextView) findViewById(R.id.setting_contact_us_text);
        mPhoneNumber.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        mPhoneNumber.getPaint().setAntiAlias(true);//抗锯齿
        mSignOutBtn = (Button) findViewById(R.id.setting_signout_btn);

        mVersionInfoText.setText("当前版本 V"+BuildConfig.VERSION_NAME);
        setWlanView();
    }
    private void setListener() {
        ThrottleClickEvents.throttleClick(mBackView,this);
        ThrottleClickEvents.throttleClick(mChangePassword,this);
        ThrottleClickEvents.throttleClick(mVersionInfo,this);
        ThrottleClickEvents.throttleClick(mFeedBack,this);
        ThrottleClickEvents.throttleClick(mContactUs,this);
        ThrottleClickEvents.throttleClick(mAboutUs,this);
        ThrottleClickEvents.throttleClick(mChangeProject,this);
        ThrottleClickEvents.throttleClick(mOffline,this);
        mSignOutBtn.setOnClickListener(this);
        wlanImage.setOnClickListener(this);
    }

    private void initData() {
        mPresenter = new SettingPresenter(this);
        mPresenter.initData(getIntent());
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id)
        {
            case R.id.setting_back:
                finish();
                break;
            case R.id.setting_change_password:
                Intent intent = new Intent(mActivity, PictureCodeActivity.class);
                String username = SharedPreferencesUtil.getString(CommonConfig.USERNAME,"");
                intent.putExtra(CommonConfig.RESET_USERNAME,username);
                intent.putExtra(CommonConfig.RESET_PASSWORD_TITLE,1);//1 设置  0登录
                startActivity(intent);
                break;
            case R.id.setting_version_info:
                mPresenter.checkVersion();
                break;
            case R.id.setting_feedback:
                Intent feedbackIntent = new Intent(mActivity,FeedBackActivity.class);
                startActivity(feedbackIntent);
                break;
            case R.id.setting_contact_us:
                call(mPhoneNumber.getText().toString().trim());
                break;
            case R.id.setting_about_us:
                showToast();
                break;
            case R.id.setting_change_project:
                mPresenter.toTenantList();
                break;
            case R.id.setting_offline:
                showToast();
                break;
            case R.id.setting_signout_btn:
                mPresenter.signOut();
                break;
            case R.id.setting_wlan_flag:
                switchWlanFlag();
                break;
        }
    }

    /**
     * 点击切换wifi下自动下载
     */
    private void switchWlanFlag() {
        if (SharedPreferencesUtil.getAutoDownload()) {
            SharedPreferencesUtil.saveAutoDownload(false);
            wlanImage.setBackgroundResource(R.drawable.icon_flag_close);
        } else {
            SharedPreferencesUtil.saveAutoDownload(true);
            wlanImage.setBackgroundResource(R.drawable.icon_flag_open);
        }
    }

    private void setWlanView(){
        if (SharedPreferencesUtil.getAutoDownload()) {
            wlanImage.setBackgroundResource(R.drawable.icon_flag_open);
        } else {
            wlanImage.setBackgroundResource(R.drawable.icon_flag_close);
        }
    }

    private void showToast(){
        ToastManager.show("敬请期待!");
    }

    /**
     * 调用拨号界面
     * @param phone 电话号码
     */
    private void call(String phone) {
        if(!TextUtils.isEmpty(phone)) {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    @Override
    public void showLoadingDialog() {

    }

    @Override
    public void dismissLoadingDialog() {

    }

    @Override
    public Activity getActivity() {
        return mActivity;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(mPresenter!=null){
            mPresenter.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mPresenter!=null){
            mPresenter.onDestroy();
        }
    }
}
