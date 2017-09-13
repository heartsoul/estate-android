package com.glodon.bim.business.login.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.glodon.bim.R;
import com.glodon.bim.basic.listener.ThrottleClickEvents;
import com.glodon.bim.business.login.contract.LoginContract;
import com.glodon.bim.business.login.presenter.LoginPresenter;
import com.glodon.bim.customview.LoadingDialog;
import com.glodon.bim.main.BaseActivity;

/**
 * 描述：登录界面
 * 作者：zhourf on 2017/9/8
 * 邮箱：zhourf@glodon.com
 */
public class LoginActivity extends BaseActivity implements LoginContract.View {

    private LoginContract.Presenter mPresenter;
    private EditText mUsernameEt, mPasswordEt;
    private Button mLoginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initData();
    }

    private void initData() {
        mPresenter = new LoginPresenter(this);
        mUsernameEt = (EditText) findViewById(R.id.login_username);
        mPasswordEt = (EditText) findViewById(R.id.login_password);
        mLoginBtn = (Button) findViewById(R.id.login_button);


        ThrottleClickEvents.throttleClick(mLoginBtn, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.clickLoginBtn(mUsernameEt.getText().toString(), mPasswordEt.getText().toString());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPresenter.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
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
        return this;
    }
}

