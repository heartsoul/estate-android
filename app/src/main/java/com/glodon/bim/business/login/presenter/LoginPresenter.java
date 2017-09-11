package com.glodon.bim.business.login.presenter;

import android.content.Intent;

import com.glodon.bim.business.login.contract.LoginContract;
import com.glodon.bim.business.login.model.LoginModel;

/**
 * 描述：登录 逻辑控制
 * 作者：zhourf on 2017/9/11
 * 邮箱：zhourf@glodon.com
 */

public class LoginPresenter implements LoginContract.Presenter{
    private LoginContract.View mView;
    private LoginContract.Model mModel;

    public LoginPresenter(LoginContract.View mView) {
        this.mView = mView;
        mModel = new LoginModel();
    }

    @Override
    public void initData(Intent intent) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void onDestroy() {

    }
}
