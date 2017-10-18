package com.glodon.bim.business.main.contract;

import com.glodon.bim.base.IBasePresenter;
import com.glodon.bim.base.IBaseView;
import com.glodon.bim.common.login.UserTenant;

import java.util.List;

import okhttp3.ResponseBody;
import rx.Observable;

/**
 * 描述：选择租户列表
 * 作者：zhourf on 2017/10/17
 * 邮箱：zhourf@glodon.com
 */

public interface ChooseTenantContract {

    interface View extends IBaseView{

        /**
         * 更新列表
         */
        void updateData(List<UserTenant> mDataList);

    }

    interface Presenter extends IBasePresenter{

        /**
         * 点击租户
         */
        void clickTenant(UserTenant tenant);
    }

    interface Model{
        /**
         * 设置选中租户
         */
        Observable<ResponseBody> setCurrentTenant(long tenantId);
    }
}
