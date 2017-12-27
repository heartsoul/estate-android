package com.glodon.bim.business.login.contract;

import com.glodon.bim.base.IBasePresenter;
import com.glodon.bim.base.IBaseView;
import com.glodon.bim.business.login.bean.CheckAccountBean;
import com.glodon.bim.business.login.listener.OnLoginListener;
import com.glodon.bim.common.login.User;

import rx.Observable;

/**
 * 描述：重置密码
 * 作者：zhourf on 2017/9/11
 * 邮箱：zhourf@glodon.com
 */

public interface ResetPasswordContract {

    interface Presenter extends IBasePresenter {

        /**
         * 重置密码
         * @param newPsd 新密码
         */
        void resetPwd(String newPsd);
    }

    interface View extends IBaseView {



    }

    interface Model {
        /**
         * 重置密码
         */
        Observable<CheckAccountBean> resetPwd(String mobile, String code, String pwd);
    }
}
