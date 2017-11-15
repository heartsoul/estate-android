package com.glodon.bim.business.qualityManage.contract;

import com.glodon.bim.base.IBasePresenter;
import com.glodon.bim.base.IBaseView;
import com.glodon.bim.business.qualityManage.bean.QualityCheckListBeanItemFile;
import com.glodon.bim.business.qualityManage.bean.QualityCheckListDetailBean;
import com.glodon.bim.business.qualityManage.bean.QualityGetRepairInfo;
import com.glodon.bim.business.qualityManage.bean.QualityGetReviewInfo;
import com.glodon.bim.business.qualityManage.bean.QualityRepairParams;
import com.glodon.bim.business.qualityManage.bean.QualityReviewParams;
import com.glodon.bim.business.qualityManage.bean.SaveBean;
import com.glodon.bim.business.qualityManage.view.QualityCheckListDetailView;
import com.glodon.bim.customview.album.TNBImageItem;

import java.util.LinkedHashMap;
import java.util.List;

import okhttp3.ResponseBody;
import rx.Observable;

/**
 * 描述：新建复查单  整改单
 * 作者：zhourf on 2017/10/9
 * 邮箱：zhourf@glodon.com
 */

public interface CreateReviewContract {

    interface Presenter extends IBasePresenter {

        /**
         * 设定选中的图片
         */
        void setSelectedImages(LinkedHashMap<String, TNBImageItem> map);

        /**
         * 打开相机
         */
        void takePhoto();

        /**
         * 打开相册
         */
        void openAlbum();

        /**
         * 打开图片预览
         */
        void toPreview(int i);

        /**
         * 点击提交
         */
        void submit(String trim, String mCurrentStatus, String mSelectedTime);

        /**
         * 点击保存
         */
        void save(String trim, String mCurrentStatus, String mSelectedTime);

        /**
         * 点击删除
         */
        void delete();

        /**
         * 判断选中的图片是否相同
         * @return
         */
        boolean isEqual(String des,boolean flag,String time);
    }

    interface View extends IBaseView {
        /**
         * 展示选中的图片
         */
        void showImages(LinkedHashMap<String, TNBImageItem> mSelectedMap);

        /**
         * 检查单详情
         */
        void showDetail(long deptId, long id);

        /**
         * 创建的类型  整改  /复查
         */
        void showTitleByType(String mCreateType);

        /**
         * 展示描述和图片
         */
        void showDesAndImages(String description, List<QualityCheckListBeanItemFile> files);

        /**
         * 展示复查合格和整改期限
         */
        void showRectificationInfo(String status, String lastRectificationDate);

        /**
         * 获取检查单详情
         */
        QualityCheckListDetailBean getDetailInfo();

        /**
         * 保存后展示删除按钮
         */
        void showDelete();
    }

    interface Model {
        /**
         * 整改单  新增  保存
         */
        Observable<SaveBean> createSaveRepair(long deptId, QualityRepairParams props);

        /**
         * 整改单  编辑  保存
         */
        Observable<ResponseBody> editSaveRepair(long deptId, long id, QualityRepairParams props);


        /**
         * 整改单  新增  提交
         */
        Observable<SaveBean> createSubmitRepair(long deptId, QualityRepairParams props);

        /**
         * 整改单  编辑  提交
         */
        Observable<ResponseBody> editSubmitRepair(long deptId, long id, QualityRepairParams props);

        /**
         * 整改单  删除
         */
        Observable<ResponseBody> deleteRepair(long deptId, long id);

        /**
         * 整改单  查询保存但未提交的整改单
         */
        Observable<QualityGetRepairInfo> getRepairInfo(long deptId, long inspectionId);


        /**
         * 复查单  新增  保存
         */
        Observable<SaveBean> createSaveReview(long deptId, QualityReviewParams props);

        /**
         * 复查单  编辑  保存
         */
        Observable<ResponseBody> editSaveReview(long deptId, long id, QualityReviewParams props);

        /**
         * 复查单  新增  提交
         */
        Observable<SaveBean> createSubmitReview(long deptId, QualityReviewParams props);

        /**
         * 复查单  编辑  提交
         */
        Observable<ResponseBody> editSubmitReview(long deptId, long id, QualityReviewParams props);


        /**
         * 复查单  查询保存后的复查单数据
         */
        Observable<QualityGetReviewInfo> getReviewInfo(long deptId, long inspectionId);

        /**
         * 整改单  删除
         */
        Observable<ResponseBody> deleteReview(long deptId, long id);
    }
}
