package com.glodon.bim.business.login.presenter;

import android.content.Intent;

import com.glodon.bim.business.login.contract.LoginContract;
import com.glodon.bim.business.login.model.LoginModel;

import java.io.IOException;

import okhttp3.ResponseBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import rx.subscriptions.Subscriptions;

/**
 * 描述：登录 逻辑控制
 * 作者：zhourf on 2017/9/11
 * 邮箱：zhourf@glodon.com
 */

public class LoginPresenter implements LoginContract.Presenter{
    private LoginContract.View mView;
    private LoginContract.Model mModel;
    private CompositeSubscription mSubscriptions;

    public LoginPresenter(LoginContract.View mView) {
        this.mView = mView;
        mModel = new LoginModel();
        mSubscriptions = new CompositeSubscription();
    }

    @Override
    public void initData(Intent intent) {

    }

    @Override
    public void clickLoginBtn(String username, String password) {
        mModel.login(username, password);
//        mSubscriptions.add(mModel.login(username,password)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<ResponseBody>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        System.out.println("---"+e.getMessage());
//                    }
//
//                    @Override
//                    public void onNext(ResponseBody s) {
//                        System.out.println("------------------------");
//                        try {
//                            System.out.println(new String(s.bytes()));
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                        System.out.println("------------------------");
//                    }
//                }));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void onDestroy() {
        if(mSubscriptions!=null){
            mSubscriptions.unsubscribe();
            mSubscriptions = null;
        }
    }
}
