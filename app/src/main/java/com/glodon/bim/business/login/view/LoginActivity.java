package com.glodon.bim.business.login.view;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.glodon.bim.R;
import com.glodon.bim.basic.listener.ThrottleClickEvents;
import com.glodon.bim.business.greendao.GreenDaoHelper;
import com.glodon.bim.business.login.contract.LoginContract;
import com.glodon.bim.business.login.presenter.LoginPresenter;
import com.glodon.bim.main.AppApplication;
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
    protected View onCreateView() {
        View view = LayoutInflater.from(this).inflate(R.layout.activity_login,null);
        mUsernameEt = view.findViewById(R.id.login_username);
        mPasswordEt = view.findViewById(R.id.login_password);
        mLoginBtn = view.findViewById(R.id.login_button);

        return view;
    }

    @Override
    protected void initDataForActivity() {
        //GreenDao初始化
        GreenDaoHelper.initDatabase(AppApplication.getInstance());
        initData();
    }

    private void initData() {
        mPresenter = new LoginPresenter(this);
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

