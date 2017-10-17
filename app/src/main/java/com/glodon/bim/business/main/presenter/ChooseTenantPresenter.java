package com.glodon.bim.business.main.presenter;

import android.content.Intent;

import com.glodon.bim.business.main.contract.ChooseTenantContract;
import com.glodon.bim.business.main.model.ChooseTenantModel;
import com.glodon.bim.common.login.User;
import com.glodon.bim.common.login.UserTenant;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：选择租户列表
 * 作者：zhourf on 2017/10/17
 * 邮箱：zhourf@glodon.com
 */

public class ChooseTenantPresenter implements ChooseTenantContract.Presenter {

    private ChooseTenantContract.View mView;
    private ChooseTenantContract.Model mModel;
    private List<UserTenant> mDataList;

    public ChooseTenantPresenter(ChooseTenantContract.View mView) {
        this.mView = mView;
        mModel = new ChooseTenantModel();
        mDataList = new ArrayList<>();
    }

    @Override
    public void initData(Intent intent) {
        User user = (User) intent.getSerializableExtra("user");
        if(user!=null && user.accountInfo!=null && user.accountInfo.userTenants!=null){
            mDataList.addAll(user.accountInfo.userTenants);
        }
        mView.updateData(mDataList);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void onDestroy() {

    }
}
