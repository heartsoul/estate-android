package com.glodon.bim.business.login.contract;

import com.glodon.bim.main.IBasePresenter;
import com.glodon.bim.main.IBaseView;

import okhttp3.ResponseBody;
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
    }

    interface View extends IBaseView {

    }

    interface Model {
        /**
         * 登录
         *
         * @param username 用户名
         * @param password 密码
         */
        void login(String username, String password);
    }
}
