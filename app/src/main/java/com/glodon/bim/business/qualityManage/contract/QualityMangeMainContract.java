package com.glodon.bim.business.qualityManage.contract;

import com.glodon.bim.base.IBasePresenter;
import com.glodon.bim.base.IBaseView;

/**
 * 描述：质量管理主界面
 * 作者：zhourf on 2017/10/9
 * 邮箱：zhourf@glodon.com
 */

public interface QualityMangeMainContract {

    interface Presenter extends IBasePresenter {

        /**
         * 跳转到图纸
         */
        void toBluePrint();

        /**
         * 跳转到模型
         */
        void toModel();

        /**
         * 打开相机
         */
        void openPhoto();

        /**
         * 打开相册
         */
        void openAlbum();

        /**
         * 打开创建界面
         */
        void toCreate();
    }

    interface View extends IBaseView {

    }

    interface Model {
    }
}
