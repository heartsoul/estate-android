package com.glodon.bim.business.login.presenter;

import android.app.Activity;
import android.content.Intent;

import com.glodon.bim.basic.log.LogUtil;
import com.glodon.bim.basic.utils.NetWorkUtils;
import com.glodon.bim.business.login.bean.CheckAccountBean;
import com.glodon.bim.business.login.contract.ResetPasswordContract;
import com.glodon.bim.business.login.model.ResetPasswordModel;
import com.glodon.bim.common.config.CommonConfig;
import com.glodon.bim.customview.ToastManager;
import com.google.gson.GsonBuilder;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * 描述：重置密码
 * 作者：zhourf on 2017/12/27
 * 邮箱：zhourf@glodon.com
 */

public class ResetPasswordPresenter implements ResetPasswordContract.Presenter{
    private ResetPasswordContract.View mView;
    private ResetPasswordContract.Model mModel;

    private CompositeSubscription mSubscriptions;
    private String mMobile;
    private String mCode;

    public ResetPasswordPresenter(ResetPasswordContract.View mView) {
        this.mView = mView;
        mSubscriptions = new CompositeSubscription();
        mModel = new ResetPasswordModel();
    }

    @Override
    public void initData(Intent intent) {
        mMobile = intent.getStringExtra(CommonConfig.SMSCODE_MOBILE);
        mCode = intent.getStringExtra(CommonConfig.SMSCODE_CODE);
    }

    public void resetPwd(String newPwd){
        if(NetWorkUtils.isNetworkAvailable(mView.getActivity())) {
            if(mView!=null){
                mView.showLoadingDialog();
            }
            Subscription sub = mModel.resetPwd(mMobile, mCode, newPwd)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<CheckAccountBean>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            LogUtil.e(e.getMessage());
                            if(mView!=null){
                                mView.dismissLoadingDialog();
                            }
                        }

                        @Override
                        public void onNext(CheckAccountBean bean) {
                            LogUtil.e("reset result=" + new GsonBuilder().create().toJson(bean));
                            if (bean != null) {
                                if (bean.success) {
                                    //密码重置成功
                                    ToastManager.show("密码重置成功");
                                    mView.getActivity().setResult(Activity.RESULT_OK);
                                    mView.getActivity().finish();
                                } else {
                                    //密码重置失败
                                    ToastManager.show(bean.errorMessage);
                                }
                            }
                            if(mView!=null){
                                mView.dismissLoadingDialog();
                            }
                        }
                    });
            mSubscriptions.add(sub);
        }else{
            ToastManager.showNetWorkToast();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void onDestroy() {
        if (mSubscriptions != null) {
            mSubscriptions.unsubscribe();
            mSubscriptions = null;
        }
    }
}
