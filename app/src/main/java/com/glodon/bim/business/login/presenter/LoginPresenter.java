package com.glodon.bim.business.login.presenter;

import android.content.Intent;
import android.text.TextUtils;

import com.glodon.bim.basic.log.LogUtil;
import com.glodon.bim.basic.utils.NetWorkUtils;
import com.glodon.bim.basic.utils.SharedPreferencesUtil;
import com.glodon.bim.business.login.contract.LoginContract;
import com.glodon.bim.business.login.listener.OnLoginListener;
import com.glodon.bim.business.login.model.LoginModel;
import com.glodon.bim.business.main.view.ChooseTenantActivity;
import com.glodon.bim.common.config.CommonConfig;
import com.glodon.bim.common.login.User;
import com.glodon.bim.customview.ToastManager;

import okhttp3.ResponseBody;
import retrofit2.Call;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * 描述：登录 逻辑控制
 * 作者：zhourf on 2017/9/11
 * 邮箱：zhourf@glodon.com
 */

public class LoginPresenter implements LoginContract.Presenter {
    private LoginContract.View mView;
    private LoginContract.Model mModel;
    private CompositeSubscription mSubscriptions;
    private int mErrorTimes = 0;

    public LoginPresenter(LoginContract.View mView) {
        this.mView = mView;
        mModel = new LoginModel();
        mSubscriptions = new CompositeSubscription();
    }

    @Override
    public void initData(Intent intent) {
        String username = SharedPreferencesUtil.getString(CommonConfig.USERNAME,"");
        String password = SharedPreferencesUtil.getString(CommonConfig.PASSWORD,"");
        if(!TextUtils.isEmpty(username)&&!TextUtils.isEmpty(password)){
            login(username,password);
        }
    }

    @Override
    public void clickLoginBtn(final String username, final String password) {
        login(username,password);
    }

    private void login(final String username, final String password){
        if(NetWorkUtils.isNetworkAvailable(mView.getActivity())) {
            mView.showLoadingDialog();
            mModel.login(username, password, new OnLoginListener() {
                @Override
                public void onLoginSuccess(String cookie) {
                    //更新数据库
                    mModel.updateCookieDb(cookie);
                    //保存用户信息
                    saveUserInfo(username, password);
                    //拿用户信息
                    mSubscriptions.add(mModel.getUserInfo(cookie)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Subscriber<User>() {
                                @Override
                                public void onCompleted() {

                                }

                                @Override
                                public void onError(Throwable e) {
                                    if (mView != null) {
                                        mView.dismissLoadingDialog();
                                    }
                                }

                                @Override
                                public void onNext(User user) {
                                    LogUtil.d(user.toString());
                                    if (mView != null) {
                                        mView.dismissLoadingDialog();
                                    }

                                    Intent intent = new Intent(mView.getActivity(), ChooseTenantActivity.class);
                                    intent.putExtra("user", user);
                                    mView.getActivity().startActivity(intent);
//                                mView.getActivity().finish();
                                }
                            }));
                }

                @Override
                public void onLoginFailed(Call<ResponseBody> call, Throwable t) {
                    mErrorTimes++;
                    if (mView != null) {
                        mView.dismissLoadingDialog();
                    }
                    if (mErrorTimes > 3) {
                        mView.showErrorDialog();
                    } else {
                        ToastManager.show("账号或密码错误！");
                    }
                }
            });
        }else{
            ToastManager.showNetWorkToast();
        }
    }

    @Override
    public void forgetPassword() {

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
        mView = null;
        mModel = null;
    }

    /**
     * 保存用户名密码
     */
    private void saveUserInfo(String username,String password){
        SharedPreferencesUtil.setString(CommonConfig.USERNAME,username);
        SharedPreferencesUtil.setString(CommonConfig.PASSWORD,password);
    }
}
