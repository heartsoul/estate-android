package com.glodon.bim.business.login.presenter;

import android.app.Activity;
import android.content.Intent;

import com.glodon.bim.basic.log.LogUtil;
import com.glodon.bim.basic.utils.NetWorkUtils;
import com.glodon.bim.business.login.bean.CheckAccountBean;
import com.glodon.bim.business.login.contract.SmsCodeContract;
import com.glodon.bim.business.login.model.SmsCodeModel;
import com.glodon.bim.business.login.view.ResetPasswordActivity;
import com.glodon.bim.common.config.CommonConfig;
import com.glodon.bim.common.config.RequestCodeConfig;
import com.glodon.bim.customview.ToastManager;
import com.google.gson.GsonBuilder;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * 描述：短信验证码
 * 作者：zhourf on 2017/12/27
 * 邮箱：zhourf@glodon.com
 */

public class SmsCodePresenter implements SmsCodeContract.Presenter {

    private SmsCodeContract.View mView;
    private SmsCodeContract.Model mModel;
    private String mMobile = "";
    private CompositeSubscription mSubscriptions;

    public SmsCodePresenter(SmsCodeContract.View mView) {
        this.mView = mView;
        mSubscriptions = new CompositeSubscription();
        mModel = new SmsCodeModel();
    }

    @Override
    public void initData(Intent intent) {
        mMobile = intent.getStringExtra(CommonConfig.SMSCODE_MOBILE);
        mView.showHint(mMobile);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode)
        {
            case RequestCodeConfig.REQUEST_CODE_TORESET:
                if(resultCode== Activity.RESULT_OK && mView!=null){
                    mView.getActivity().setResult(Activity.RESULT_OK);
                    mView.getActivity().finish();
                }
                break;
        }
    }

    @Override
    public void onDestroy() {
        if (mSubscriptions != null) {
            mSubscriptions.unsubscribe();
            mSubscriptions = null;
        }
    }

    @Override
    public void checkCode(final String code) {
        if(NetWorkUtils.isNetworkAvailable(mView.getActivity())) {
            if(mView!=null){
                mView.showLoadingDialog();
            }
            Subscription sub = mModel.checkSmsCode(mMobile, code)
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
                            LogUtil.e("check result= " + new GsonBuilder().create().toJson(bean));
                            if (bean != null && mView != null) {
                                if (bean.success) {
                                    //验证通过
                                    toResetPwdActivity(mMobile, code);
                                } else {
                                    //验证失败
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

    private void toResetPwdActivity(String mobile, String code) {
        Intent intent = new Intent(mView.getActivity(), ResetPasswordActivity.class);
        intent.putExtra(CommonConfig.SMSCODE_MOBILE, mobile);
        intent.putExtra(CommonConfig.SMSCODE_CODE, code);
        mView.getActivity().startActivityForResult(intent,RequestCodeConfig.REQUEST_CODE_TORESET);
    }
}
