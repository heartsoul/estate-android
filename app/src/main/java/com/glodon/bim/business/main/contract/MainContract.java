package com.glodon.bim.business.main.contract;

import com.glodon.bim.base.IBasePresenter;
import com.glodon.bim.base.IBaseView;
import com.glodon.bim.business.setting.bean.CheckVersionBean;

import rx.Observable;

/**
 * 描述：主界面
 * 作者：zhourf on 2017/10/17
 * 邮箱：zhourf@glodon.com
 */

public interface MainContract {

    interface View extends IBaseView{


    }

    interface Presenter extends IBasePresenter{

        void openPhoto();

        void openAlbum();

        void toCreate();

        void checkVersion();
    }

    interface Model{
        /**
         * 检测版本更新
         * @return
         */
        Observable<CheckVersionBean> checkVersion();
    }
}
