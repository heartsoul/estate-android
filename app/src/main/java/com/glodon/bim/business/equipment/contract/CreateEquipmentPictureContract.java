package com.glodon.bim.business.equipment.contract;

import com.glodon.bim.base.IBasePresenter;
import com.glodon.bim.base.IBaseView;
import com.glodon.bim.basic.utils.LinkedHashList;
import com.glodon.bim.business.equipment.bean.CreateEquipmentPictureInfo;
import com.glodon.bim.customview.album.ImageItem;

/**
 * 描述：创建材设进场记录-拍照页面
 * 作者：zhourf on 2017/10/17
 * 邮箱：zhourf@glodon.com
 */

public interface CreateEquipmentPictureContract {

    interface View extends IBaseView{

        /**
         * 展示选中的图片
         */
        void showImages(LinkedHashList<String, ImageItem> mSelectedMap);

        /**
         * 编辑状态显示数据
         */
        void showPhotoInfo(CreateEquipmentPictureInfo info);
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

        /**
         * 下一步
         * @param isUpToStandard  是否合格
         */
        void next(boolean isUpToStandard);
    }

    interface Model{
    }
}
