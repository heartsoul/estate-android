package com.glodon.bim.business.login.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.glodon.bim.R;
import com.glodon.bim.business.login.contract.LoginContract;
import com.glodon.bim.business.login.presenter.LoginPresenter;
import com.glodon.bim.main.BaseActivity;

/**
 * 描述：登录界面
 * 作者：zhourf on 2017/9/8
 * 邮箱：zhourf@glodon.com
 */
public class LoginActivity extends BaseActivity implements LoginContract.View{

    private LoginContract.Presenter mPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

    }

    private void initData(){
        mPresenter = new LoginPresenter(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void showLoadingDialog() {

    }

    @Override
    public void dismissLoadingDialog() {

    }

    @Override
    public Activity getActivity() {
        return this;
    }
}

