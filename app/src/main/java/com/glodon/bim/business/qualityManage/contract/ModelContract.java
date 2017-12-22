package com.glodon.bim.business.qualityManage.contract;

import com.glodon.bim.base.IBasePresenter;
import com.glodon.bim.base.IBaseView;
import com.glodon.bim.business.qualityManage.bean.ModelListBean;
import com.glodon.bim.business.qualityManage.bean.ModelListBeanItem;
import com.glodon.bim.business.qualityManage.bean.ModelSingleListItem;
import com.glodon.bim.business.qualityManage.bean.ModelSpecialListItem;
import com.glodon.bim.business.qualityManage.bean.ProjectVersionBean;
import com.glodon.bim.business.qualityManage.model.OnModelSelectListener;

import java.util.List;

import rx.Observable;

/**
 * 描述：选择模型
 * 作者：zhourf on 2017/10/9
 * 邮箱：zhourf@glodon.com
 */

public interface ModelContract {

    interface Presenter extends IBasePresenter {

        /**
         * 展示专业数据
         */
        void showSpecialList();

        /**
         * 展示单体数据
         */
        void showSingleList();

        /**
         * 获取监听
         */
        OnModelSelectListener getListener();

        void setIsFragment();

        /**
         * 跳转到搜索
         */
        void toSearch();
    }

    interface View extends IBaseView {

        /**
         * 展示专业列表
         */
        void updateSpecialList(List<ModelSpecialListItem> mSpecialList, long mSpecialSelectId);

        /**
         * 展示单体列表
         */
        void updateSingleList(List<ModelSingleListItem> mSingleList, long mSingleSelectId);

        /**
         * 展示模型列表
         */
        void updateModelList(List<ModelListBeanItem> mModelList);

        /**
         * 展示选中的单体
         */
        void showSingle(ModelSingleListItem item);

        /**
         * 展示选中的专业
         */
        void showSpecial(ModelSpecialListItem item);
    }

    interface Model {
        /**
         * 查询单体列表
         * @param id 项目id
         */
        Observable<List<ModelSingleListItem>> getSingleList(long id);
        /**
         * 查询专业列表
         */
        Observable<List<ModelSpecialListItem>> getSpecialList();

        /**
         * 获取当前已发布的最新版本
         */
        Observable<ProjectVersionBean> getLatestVersion(long projectId);

        /**
         * 根据专业和单体查询模型列表
         * @param projectId  项目id
         * @param projectVersionId  最新版本
         * @param buildingId  单体
         * @param specialtyCode  专业
         */
        Observable<ModelListBean> getModelList(long projectId, String projectVersionId, long buildingId, String specialtyCode);
    }
}
