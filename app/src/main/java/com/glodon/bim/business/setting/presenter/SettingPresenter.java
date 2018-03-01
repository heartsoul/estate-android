package com.glodon.bim.business.setting.presenter;

import android.app.Activity;
import android.content.Intent;
import android.widget.Switch;

import com.glodon.bim.basic.utils.SharedPreferencesUtil;
import com.glodon.bim.business.login.view.LoginActivity;
import com.glodon.bim.business.main.view.ChooseTenantActivity;
import com.glodon.bim.business.setting.contract.SettingContract;
import com.glodon.bim.common.config.CommonConfig;
import com.glodon.bim.common.config.RequestCodeConfig;

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
        switch (requestCode){
            case RequestCodeConfig.REQUEST_CODE_CLOSE_SETTING:
                if(mView!=null && resultCode== Activity.RESULT_OK){
                    mView.getActivity().setResult(Activity.RESULT_OK);
                    mView.getActivity().finish();
                }
                break;
        }
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void signOut() {
        //跳转到登录页面
        SharedPreferencesUtil.clear();
        Intent intent = new Intent(mView.getActivity(), LoginActivity.class);
        mView.getActivity().startActivity(intent);
        //发送广播,关闭其他界面
        Intent data = new Intent(CommonConfig.ACTION_LOG_OUT);
        mView.getActivity().sendBroadcast(data);


    }

    @Override
    public void toTenantList() {
        Intent intent = new Intent(mView.getActivity(), ChooseTenantActivity.class);
        intent.putExtra(CommonConfig.CHANGE_PROJECT,true);
        mView.getActivity().startActivityForResult(intent, RequestCodeConfig.REQUEST_CODE_CLOSE_SETTING);
    }
}
