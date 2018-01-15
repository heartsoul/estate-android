package com.glodon.bim.business.equipment.contract;

import com.glodon.bim.base.IBasePresenter;
import com.glodon.bim.base.IBaseView;
import com.glodon.bim.business.equipment.bean.CreateEquipmentMandatoryInfo;
import com.glodon.bim.business.equipment.bean.CreateEquipmentMandatoryNotInfo;
import com.glodon.bim.business.equipment.bean.CreateEquipmentPictureInfo;

/**
 * 描述：创建材设进场记录-提交页面
 * 作者：zhourf on 2017/10/17
 * 邮箱：zhourf@glodon.com
 */

public interface CreateEquipmentContract {

    interface View extends IBaseView{

        /**
         * 基本信息
         */
        void showBasicInfo(CreateEquipmentMandatoryInfo mMandatoryInfo);

        /**
         * 其他信息
         */
        void showOtherInfo(CreateEquipmentMandatoryNotInfo mMandatoryNotInfo);

        /**
         * 图片信息
         */
        void showPictureInfo(CreateEquipmentPictureInfo mPictureInfo);
    }

    interface Presenter extends IBasePresenter{
        /**
         * 提交
         */
        void submit();

        /**
         * 去修改必填项
         */
        void toBasic();

        /**
         * 去修改非必填项
         */
        void toOther();

        /**
         * 去图片编辑
         */
        void toPicture();

        /**
         * 去预览图片
         * @param index   当前的编号
         */
        void toPreview(int index);

        /**
         * 去模型预览
         */
        void toModel();

        /**
         * 保存
         */
        void save();

        /**
         * 删除
         */
        void delete();
    }

    interface Model{
    }
}
