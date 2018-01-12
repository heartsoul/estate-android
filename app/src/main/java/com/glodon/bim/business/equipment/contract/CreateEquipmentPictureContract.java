package com.glodon.bim.business.equipment.contract;

import com.glodon.bim.base.IBasePresenter;
import com.glodon.bim.base.IBaseView;
import com.glodon.bim.business.equipment.bean.MandatoryInfo;

/**
 * 描述：创建材设进场记录-拍照页面
 * 作者：zhourf on 2017/10/17
 * 邮箱：zhourf@glodon.com
 */

public interface CreateEquipmentPictureContract {

    interface View extends IBaseView{


    }

    interface Presenter extends IBasePresenter{

        /**
         * 图片预览
         * @param position  当前位置
         */
        void toPreview(int position);

        /**
         * 拍照
         */
        void takePhoto();

        /**
         * 相册
         */
        void openAlbum();
    }

    interface Model{
    }
}
