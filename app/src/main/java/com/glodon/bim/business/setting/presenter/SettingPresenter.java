package com.glodon.bim.business.setting.presenter;

import android.content.Intent;

import com.glodon.bim.basic.utils.SharedPreferencesUtil;
import com.glodon.bim.business.login.view.LoginActivity;
import com.glodon.bim.business.setting.contract.SettingContract;
import com.glodon.bim.common.config.CommonConfig;

/**
 * 描述：设置
 * 作者：zhourf on 2017/10/24
 * 邮箱：zhourf@glodon.com
 */

public class SettingPresenter implements SettingContract.Presenter {

    private SettingContract.View mView;

    public SettingPresenter(SettingContract.View mView) {
        this.mView = mView;
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

    @Override
    public void signOut() {
        //发送广播
        Intent intent = new Intent(CommonConfig.ACTION_LOG_OUT);
        mView.getActivity().sendBroadcast(intent);
        SharedPreferencesUtil.setString(CommonConfig.USERNAME,"");
        SharedPreferencesUtil.setString(CommonConfig.PASSWORD,"");
    }
}
