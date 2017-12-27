package com.glodon.bim.business.login.presenter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.glodon.bim.basic.config.AppConfig;
import com.glodon.bim.basic.log.LogUtil;
import com.glodon.bim.basic.network.NetRequest;
import com.glodon.bim.basic.utils.NetWorkUtils;
import com.glodon.bim.business.login.bean.CheckAccountBean;
import com.glodon.bim.business.login.contract.PictureCodeContract;
import com.glodon.bim.business.login.model.LoginApi;
import com.glodon.bim.business.login.model.PictureCodeModel;
import com.glodon.bim.business.login.view.SmsCodeActivity;
import com.glodon.bim.common.config.CommonConfig;
import com.glodon.bim.common.config.RequestCodeConfig;
import com.glodon.bim.customview.ToastManager;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
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

public class PictureCodePresenter implements PictureCodeContract.Presenter{
    private PictureCodeContract.View mView;
    private PictureCodeContract.Model mModel;
    private String mSignupKey = "";
    private CompositeSubscription mSubscriptions;

    public PictureCodePresenter(PictureCodeContract.View mView) {
        this.mView = mView;
        mModel = new PictureCodeModel();
        mSubscriptions = new CompositeSubscription();
    }

    @Override
    public void initData(Intent intent) {
        getPictureCode();
    }

    public void getPictureCode(){

        if(NetWorkUtils.isNetworkAvailable(mView.getActivity())) {
            NetRequest.getInstance().getCall(AppConfig.BASE_URL, LoginApi.class)
                    .getPictureCode()
                    .enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response != null && response.headers() != null) {
                                mSignupKey = response.headers().get("Signup-Key");
                            }
                            if (response.body() != null) {
                                try {
                                    byte[] bytess = response.body().bytes();
                                    if (bytess != null) {
                                        Bitmap bm = BitmapFactory.decodeByteArray(bytess, 0, bytess.length);
                                        if (mView != null) {
                                            mView.showPictureCode(bm);
                                        }
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {

                        }
                    });
        }else{
            ToastManager.showNetWorkToast();
        }
    }

    @Override
    public void checkAccount(final String account, final String code) {
        if(NetWorkUtils.isNetworkAvailable(mView.getActivity())) {
            if(mView!=null){
                mView.showLoadingDialog();
            }
            Subscription sub = mModel.checkAccount(account)
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
                            if (bean != null) {
                                if (bean.success) {
                                    //账户存在  获取短信验证码
                                    getSmsCode(account, code);
                                } else {
                                    //账户不存在
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

    private void getSmsCode(final String account, String code){
        Subscription sub = mModel.getPhoneCode(account,code,mSignupKey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CheckAccountBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(e.getMessage());
                    }

                    @Override
                    public void onNext(CheckAccountBean bean) {

                        LogUtil.e("result = "+new GsonBuilder().create().toJson(bean));
                        if(bean!=null){
                            if(bean.success){
                                //进入下一个页面
                                if(mView!=null) {
                                    Intent intent = new Intent(mView.getActivity(), SmsCodeActivity.class);
                                    intent.putExtra(CommonConfig.SMSCODE_MOBILE,account);
                                    mView.getActivity().startActivityForResult(intent, RequestCodeConfig.REQUEST_CODE_TOSMS);
                                }
                                //发送短信成功  更改图片验证码
                                getPictureCode();
                            }else{
                                //验证失败
                                if(bean.statusCode.equals("680")){
                                    ToastManager.show(bean.errorMessage);
                                }else{
                                    ToastManager.show("图片验证错误，请重试！");
                                }

                            }
                        }
                    }
                });
        mSubscriptions.add(sub);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode)
        {
            case RequestCodeConfig.REQUEST_CODE_TOSMS:
                if(resultCode== Activity.RESULT_OK && mView!=null){
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
}
