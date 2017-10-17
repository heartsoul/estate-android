package com.glodon.bim.business.login.contract;

import com.glodon.bim.business.login.listener.OnLoginListener;
import com.glodon.bim.common.login.User;
import com.glodon.bim.base.IBasePresenter;
import com.glodon.bim.base.IBaseView;

import rx.Observable;

/**
 * 描述：登录模块协议
 * 作者：zhourf on 2017/9/11
 * 邮箱：zhourf@glodon.com
 */

public interface LoginContract {

    interface Presenter extends IBasePresenter {

        /**
         * 点击登录按钮
         *
         * @param username 用户名
         * @param password 密码
         */
        void clickLoginBtn(String username, String password);

        /**
         * 点击忘记密码
         */
        void forgetPassword();
    }

    interface View extends IBaseView {

        /**
         * 密码错误>3次后提示框
         */
        void showErrorDialog();

    }

    interface Model {
        /**
         * 登录
         *
         * @param username 用户名
         * @param password 密码
         */
        void login(String username, String password,OnLoginListener listener);

        /**
         * 获取用户信息
         * @param cookie token
         */
        Observable<User> getUserInfo(String cookie);

        /**
         * 更新数据库cookie
         */
        void updateCookieDb(String cookie);
    }
}
