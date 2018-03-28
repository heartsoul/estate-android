package com.glodon.bim.business.setting.contract;

import com.glodon.bim.base.IBasePresenter;
import com.glodon.bim.base.IBaseView;
import com.glodon.bim.business.setting.bean.CheckVersionBean;
import com.glodon.bim.business.setting.bean.FeedBackBean;
import com.glodon.bim.business.setting.bean.FeedBackParams;

import rx.Observable;

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

        /**
         * 到租户选择页面
         */
        void toTenantList();

        /**
         * 检测版本更新
         */
        void checkVersion();

    }

    interface View extends IBaseView {


    }

    interface Model {

        /**
         * 检测版本更新
         * @return
         */
        Observable<CheckVersionBean> checkVersion();
    }
}
