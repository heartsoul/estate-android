package com.glodon.bim.business.equipment.contract;

import com.glodon.bim.base.IBasePresenter;
import com.glodon.bim.base.IBaseView;
import com.glodon.bim.basic.utils.LinkedHashList;
import com.glodon.bim.business.equipment.bean.CreateEquipmentBean;
import com.glodon.bim.business.equipment.bean.CreateEquipmentMandatoryInfo;
import com.glodon.bim.business.equipment.bean.CreateEquipmentMandatoryNotInfo;
import com.glodon.bim.business.equipment.bean.CreateEquipmentParams;
import com.glodon.bim.business.equipment.bean.CreateEquipmentPictureInfo;
import com.glodon.bim.business.equipment.bean.EquipmentDetailBean;
import com.glodon.bim.customview.album.ImageItem;

import okhttp3.ResponseBody;
import rx.Observable;

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

        /**
         * 编辑信息
         */
        void showEdit();

        /**
         * 详情
         */
        void showDetail();

        /**
         * 展示删除
         */
        void showDelete();

        /**
         * 展示选中的图片
         */
        void showImages(LinkedHashList<String, ImageItem> mSelectedMap);

        /**
         * 返回提示框
         */
        void showBackDialog();
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

        /**
         * 点击返回键
         */
        void back();
    }

    interface Model{
        /**
         * 新增保存
         */
        Observable<CreateEquipmentBean> newSave(CreateEquipmentParams props);

        /**
         * 新增提交
         */
        Observable<CreateEquipmentBean> newSubmit(CreateEquipmentParams props);

        /**
         * 编辑保存
         */
        Observable<ResponseBody> editSave(long id, CreateEquipmentParams props);
        /**
         * 编辑提交
         */
        Observable<ResponseBody> editSubmit(long id, CreateEquipmentParams props);

        /**
         * 删除
         */
        Observable<ResponseBody> delete(long id);

        /**
         * 根据id查询详情和保存后的编辑信息
         */
        Observable<EquipmentDetailBean> detail(long id);
    }
}
