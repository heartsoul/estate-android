package com.glodon.bim.business.qualityManage.contract;

import com.glodon.bim.base.IBasePresenter;
import com.glodon.bim.base.IBaseView;
import com.glodon.bim.business.qualityManage.bean.BlueprintListBeanItem;
import com.glodon.bim.business.qualityManage.bean.ModelListBeanItem;
import com.glodon.bim.business.qualityManage.bean.ModelSingleListItem;
import com.glodon.bim.business.qualityManage.bean.ModelSpecialListItem;
import com.glodon.bim.business.qualityManage.listener.OnBlueprintCatalogClickListener;
import com.glodon.bim.business.qualityManage.listener.OnBlueprintHintClickListener;
import com.glodon.bim.business.qualityManage.listener.OnChooseBlueprintCataListener;
import com.glodon.bim.business.qualityManage.listener.OnChooseBlueprintObjListener;
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

    }
}
