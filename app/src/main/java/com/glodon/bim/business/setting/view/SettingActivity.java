package com.glodon.bim.business.setting.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.glodon.bim.R;
import com.glodon.bim.base.BaseActivity;
import com.glodon.bim.basic.listener.ThrottleClickEvents;
import com.glodon.bim.basic.utils.SharedPreferencesUtil;
import com.glodon.bim.business.main.bean.ProjectListItem;
import com.glodon.bim.business.setting.contract.SettingContract;
import com.glodon.bim.business.setting.presenter.SettingPresenter;
import com.glodon.bim.common.config.CommonConfig;
import com.glodon.bim.customview.ToastManager;

/**
 * 描述：设置界面
 * 作者：zhourf on 2017/10/24
 * 邮箱：zhourf@glodon.com
 */

public class SettingActivity extends BaseActivity implements SettingContract.View, View.OnClickListener {

    private SettingContract.Presenter mPresenter;
    private ImageView mBackView;
    private ImageView mHeadView;
    private TextView mNameView;
    private RelativeLayout mChangePassword,mVersionInfo,mFeedBack,mContactUs,mAboutUs;
    private TextView mVersionInfoText,mPhoneNumber;
    private Button mSignOutBtn;

    private ProjectListItem mProjectInfo;//项目信息
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_activity);

        initView();

        setListener();

        initData();
    }



    private void initView() {
        mBackView = (ImageView) findViewById(R.id.setting_back);
        mHeadView = (ImageView) findViewById(R.id.setting_head_icon);
        mNameView = (TextView) findViewById(R.id.setting_name);
        mChangePassword = (RelativeLayout) findViewById(R.id.setting_change_password);
        mVersionInfo = (RelativeLayout) findViewById(R.id.setting_version_info);
        mFeedBack = (RelativeLayout) findViewById(R.id.setting_feedback);
        mContactUs = (RelativeLayout) findViewById(R.id.setting_contact_us);
        mAboutUs = (RelativeLayout) findViewById(R.id.setting_about_us);
        mVersionInfoText = (TextView) findViewById(R.id.setting_version_info_text);
        mPhoneNumber = (TextView) findViewById(R.id.setting_contact_us_text);
        mPhoneNumber.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        mPhoneNumber.getPaint().setAntiAlias(true);//抗锯齿
        mSignOutBtn = (Button) findViewById(R.id.setting_signout_btn);
    }
    private void setListener() {
        ThrottleClickEvents.throttleClick(mBackView,this);
        ThrottleClickEvents.throttleClick(mChangePassword,this);
        ThrottleClickEvents.throttleClick(mVersionInfo,this);
        ThrottleClickEvents.throttleClick(mFeedBack,this);
        ThrottleClickEvents.throttleClick(mContactUs,this);
        ThrottleClickEvents.throttleClick(mAboutUs,this);
        mSignOutBtn.setOnClickListener(this);
    }

    private void initData() {
        mProjectInfo = (ProjectListItem) getIntent().getSerializableExtra(CommonConfig.PROJECT_LIST_ITEM);
        mNameView.setText(SharedPreferencesUtil.getUserName());
        mPresenter = new SettingPresenter(this);
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
                showToast();
                break;
            case R.id.setting_version_info:

                break;
            case R.id.setting_feedback:
                showToast();
                break;
            case R.id.setting_contact_us:
                call(mPhoneNumber.getText().toString().trim());
                break;
            case R.id.setting_about_us:
                showToast();
                break;
            case R.id.setting_signout_btn:
                mPresenter.signOut();
                break;
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
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+phone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
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

}
