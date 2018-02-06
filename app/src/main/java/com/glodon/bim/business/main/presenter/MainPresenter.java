package com.glodon.bim.business.main.presenter;

import android.content.Intent;

import com.glodon.bim.business.main.contract.MainContract;
import com.glodon.bim.business.main.model.MainModel;

/**
 * 描述：主界面
 * 作者：zhourf on 2018/2/5
 * 邮箱：zhourf@glodon.com
 */

public class MainPresenter implements MainContract.Presenter {
    private MainContract.View mView;
    private MainContract.Model mModel;

    public MainPresenter(MainContract.View mView) {
        this.mView = mView;
        mModel = new MainModel();
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
