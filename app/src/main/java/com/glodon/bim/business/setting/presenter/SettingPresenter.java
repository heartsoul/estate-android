package com.glodon.bim.business.setting.presenter;

import android.app.Activity;
import android.content.Intent;

import com.glodon.bim.basic.log.LogUtil;
import com.glodon.bim.basic.utils.SharedPreferencesUtil;
import com.glodon.bim.business.login.view.LoginActivity;
import com.glodon.bim.business.main.view.ChooseTenantActivity;
import com.glodon.bim.business.setting.bean.CheckVersionBean;
import com.glodon.bim.business.setting.contract.SettingContract;
import com.glodon.bim.business.setting.model.SettingModel;
import com.glodon.bim.business.setting.util.GlodonUpdateManager;
import com.glodon.bim.common.config.CommonConfig;
import com.glodon.bim.common.config.RequestCodeConfig;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * 描述：设置
 * 作者：zhourf on 2017/10/24
 * 邮箱：zhourf@glodon.com
 */

public class SettingPresenter implements SettingContract.Presenter {

    private SettingContract.View mView;
    private SettingContract.Model mModel;
    private CompositeSubscription mSubscription;

    public SettingPresenter(SettingContract.View mView) {
        this.mView = mView;
        mModel = new SettingModel();
        mSubscription = new CompositeSubscription();

    }

    @Override
    public void initData(Intent intent) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RequestCodeConfig.REQUEST_CODE_CLOSE_SETTING:
                if (mView != null && resultCode == Activity.RESULT_OK) {
                    mView.getActivity().setResult(Activity.RESULT_OK);
                    mView.getActivity().finish();
                }
                break;
        }
    }

    @Override
    public void onDestroy() {
        if (mSubscription != null) {
            mSubscription.unsubscribe();
            mSubscription = null;
        }
        mModel = null;
        mView = null;
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
        intent.putExtra(CommonConfig.CHANGE_PROJECT, true);
        mView.getActivity().startActivityForResult(intent, RequestCodeConfig.REQUEST_CODE_CLOSE_SETTING);
    }

    /**
     * 检测版本更新
     */
    @Override
    public void checkVersion() {
        mView.showLoadingDialog();
        Subscription sub = mModel.checkVersion()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CheckVersionBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(e.getMessage());
                        if (mView != null) {
                            mView.dismissLoadingDialog();
                        }
                    }

                    @Override
                    public void onNext(CheckVersionBean checkVersionBean) {
                        if (mView != null) {
                            mView.dismissLoadingDialog();
                        }
                        if (checkVersionBean != null) {
                            GlodonUpdateManager.getInstance().showUpdateDialog(mView.getActivity(), checkVersionBean);
                        }
                    }
                });
        mSubscription.add(sub);
    }
}
