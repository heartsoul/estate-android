package com.glodon.bim.business.main.presenter;

import android.content.Intent;

import com.glodon.bim.business.main.contract.ChooseTenantContract;
import com.glodon.bim.business.main.model.ChooseTenantModel;

/**
 * 描述：选择租户列表
 * 作者：zhourf on 2017/10/17
 * 邮箱：zhourf@glodon.com
 */

public class ChooseTenantPresenter implements ChooseTenantContract.Presenter {

    private ChooseTenantContract.View mView;
    private ChooseTenantContract.Model mModel;

    public ChooseTenantPresenter(ChooseTenantContract.View mView) {
        this.mView = mView;
        mModel = new ChooseTenantModel();
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
