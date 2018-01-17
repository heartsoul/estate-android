package com.glodon.bim.business.main.contract;

import com.glodon.bim.base.IBasePresenter;
import com.glodon.bim.base.IBaseView;
import com.glodon.bim.business.main.bean.ChooseCategoryItem;

import java.util.List;

/**
 * 描述：项目功能列表
 * 作者：zhourf on 2017/10/17
 * 邮箱：zhourf@glodon.com
 */

public interface ChooseCategoryItemContract {

    interface View extends IBaseView{

        /**
         * 目录列表
         */
        void updateList(List<ChooseCategoryItem> mDataList);

        void createBoth();

        void createQuality();

        void createEquipment();

        void createNone();
    }

    interface Presenter extends IBasePresenter{
//        /**
//         * 跳转到图纸
//         */
//        void toBluePrint();
//
//        /**
//         * 跳转到模型
//         */
//        void toModel();

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

        /**
         * 打开质检清单
         * @param type 0 质检清单   1质检项目
         */
        void toQualityChickList(int type);

        /**
         * 跳转到账户设置
         */
        void toSetting();

        void checkAuthority();
    }

    interface Model{

    }
}
