package com.glodon.bim.business.setting.contract;

import com.glodon.bim.base.IBasePresenter;
import com.glodon.bim.base.IBaseView;

/**
 * 描述：设置
 * 作者：zhourf on 2017/9/11
 * 邮箱：zhourf@glodon.com
 */

public interface SettingContract {

    interface Presenter extends IBasePresenter {


        /**
         * 点击忘记密码
         */
        void signOut();
    }

    interface View extends IBaseView {


    }

    interface Model {

    }
}
