package com.glodon.bim.business.login.util;

import android.content.Intent;

import com.glodon.bim.basic.log.LogUtil;
import com.glodon.bim.basic.utils.SharedPreferencesUtil;
import com.glodon.bim.business.login.listener.OnLoginListener;
import com.glodon.bim.business.login.model.LoginModel;
import com.glodon.bim.business.main.model.ChooseTenantModel;
import com.glodon.bim.business.main.view.ChooseProjectActivity;
import com.glodon.bim.common.config.CommonConfig;
import com.glodon.bim.common.config.RequestCodeConfig;
import com.glodon.bim.common.login.User;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 描述：用户登录操作
 * 作者：zhourf on 2018/3/1
 * 邮箱：zhourf@glodon.com
 */

public class LoginManager {

    /**
     * 更新登录信息
     */
    public static void updateLoginInfo(final OnLoginListener listener){
        final String username = SharedPreferencesUtil.getString(CommonConfig.USERNAME, "");
        final String password = SharedPreferencesUtil.getString(CommonConfig.PASSWORD, "");
        final LoginModel mModel = new LoginModel();
        mModel.login(username, password, new OnLoginListener() {
            @Override
            public void onLoginSuccess(String cookie) {
                //更新数据库
                mModel.updateCookieDb(cookie);
                //保存用户信息
                saveUserInfo(username, password);
                //拿用户信息
                mModel.getUserInfo(cookie)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<User>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(User user) {
                                SharedPreferencesUtil.setUserInfo(user);
                                SharedPreferencesUtil.setUserName(user.accountInfo.name);
                            }
                        });
                setTenant(listener,cookie);
            }

            @Override
            public void onLoginFailed(Call<ResponseBody> call, Throwable t) {
                if(listener!=null){
                    listener.onLoginFailed(call, t);
                }
            }
        });
    }

    /**
     * 设定租户
     */
    private static void setTenant(final OnLoginListener listener, final String cookie){
        new ChooseTenantModel().setCurrentTenant(SharedPreferencesUtil.getTenantInfo().tenantId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(e.getMessage());
                        if(listener!=null){
                            listener.onLoginFailed(null,e);
                        }
                    }

                    @Override
                    public void onNext(ResponseBody response) {
                        if(listener!=null){
                            listener.onLoginSuccess(cookie);
                        }
                    }
                });
    }

    /**
     * 保存用户名密码
     */
    private static void saveUserInfo(String username, String password) {
        SharedPreferencesUtil.setString(CommonConfig.USERNAME, username);
        SharedPreferencesUtil.setString(CommonConfig.PASSWORD, password);
    }
}
