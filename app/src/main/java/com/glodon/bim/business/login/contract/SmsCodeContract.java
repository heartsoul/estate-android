package com.glodon.bim.business.login.contract;

import com.glodon.bim.base.IBasePresenter;
import com.glodon.bim.base.IBaseView;
import com.glodon.bim.business.login.bean.CheckAccountBean;

import rx.Observable;

/**
 * 描述：短信验证码
 * 作者：zhourf on 2017/9/11
 * 邮箱：zhourf@glodon.com
 */

public interface SmsCodeContract {

    interface Presenter extends IBasePresenter {

        /**
         * 验证验证码
         */
        void checkCode(String code);
    }

    interface View extends IBaseView {

        /**
         * 显示提示
         */
        void showHint(String mMobile);
    }

    interface Model {
        /**
         * 验证短信验证码
         */
        Observable<CheckAccountBean> checkSmsCode(String mobile, String verifyCode);
    }
}
