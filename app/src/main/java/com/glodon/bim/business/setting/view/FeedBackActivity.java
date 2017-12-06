package com.glodon.bim.business.setting.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.glodon.bim.R;
import com.glodon.bim.base.BaseActivity;
import com.glodon.bim.basic.utils.RegularUtil;
import com.glodon.bim.business.qualityManage.view.SaveDeleteDialog;
import com.glodon.bim.business.setting.bean.FeedBackParams;
import com.glodon.bim.business.setting.contract.FeedBackContract;
import com.glodon.bim.business.setting.presenter.FeedBackPresenter;

/**
 * 描述：意见反馈
 * 作者：zhourf on 2017/9/8
 * 邮箱：zhourf@glodon.com
 */
public class FeedBackActivity extends BaseActivity implements View.OnClickListener, FeedBackContract.View {

    private View mStatusView;
    private RelativeLayout mBackView;
    private TextView mSubmitView;

    private EditText mNameView, mEmailView, mTitleView, mTextView;

    private FeedBackContract.Presenter mPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quality_setting_feedback_activity);

        initView();
        initStatusBar(mStatusView);
        setListener();
        initData();
    }

    private void initView() {
        mStatusView = findViewById(R.id.feedback_header_top);
        mBackView = (RelativeLayout) findViewById(R.id.feedback_header_back);
        mSubmitView = (TextView) findViewById(R.id.feedback_header_submit);

        mNameView = (EditText) findViewById(R.id.feedback_name);
        mEmailView = (EditText) findViewById(R.id.feedback_email);
        mTitleView = (EditText) findViewById(R.id.feedback_title);
        mTextView = (EditText) findViewById(R.id.feedback_text);
    }


    private void setListener() {
        mBackView.setOnClickListener(this);
        mSubmitView.setOnClickListener(this);
    }

    private void initData() {
        mPresenter = new FeedBackPresenter(this);
        mPresenter.initData(getIntent());

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.feedback_header_back:
                mActivity.finish();
                break;
            case R.id.feedback_header_submit:
                if(verifyData()){
                    mPresenter.addFeedBack(getParams());
                }
                break;
        }
    }

    private boolean verifyData() {
        String name = mNameView.getText().toString().trim();
        if(TextUtils.isEmpty(name)){
            SaveDeleteDialog mHintDialog = new SaveDeleteDialog(getActivity());
            mHintDialog.getHintDialog("姓名不能为空!");
            mHintDialog.show();
            return false;
        }

        String email = mEmailView.getText().toString().trim();
        if(TextUtils.isEmpty(email) ||(!RegularUtil.checkEmail(email))){
            SaveDeleteDialog mHintDialog = new SaveDeleteDialog(getActivity());
            mHintDialog.getHintDialog("请输入正确格式的邮件地址!");
            mHintDialog.show();
            return false;
        }

        String content = mTextView.getText().toString().trim();
        if(TextUtils.isEmpty(content)){
            SaveDeleteDialog mHintDialog = new SaveDeleteDialog(getActivity());
            mHintDialog.getHintDialog("留言不能为空!");
            mHintDialog.show();
            return false;
        }
        return true;
    }

    private FeedBackParams getParams(){
        FeedBackParams mParams = new FeedBackParams();
        mParams.content = mTextView.getText().toString().trim();
        mParams.name = mNameView.getText().toString().trim();
        mParams.title = mTitleView.getText().toString().trim();
        mParams.email = mEmailView.getText().toString().trim();
        return mParams;
    }

    @Override
    public void showLoadingDialog() {
        showLoadDialog(true);
    }

    @Override
    public void dismissLoadingDialog() {
        dismissLoadDialog();
    }

    @Override
    public Activity getActivity() {
        return mActivity;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPresenter.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.onDestroy();
            mPresenter = null;
        }
    }
}
