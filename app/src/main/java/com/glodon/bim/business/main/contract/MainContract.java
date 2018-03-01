package com.glodon.bim.business.main.contract;

import com.glodon.bim.base.IBasePresenter;
import com.glodon.bim.base.IBaseView;

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
    }

    interface Model{
    }
}
