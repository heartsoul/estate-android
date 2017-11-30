package com.glodon.bim.business.qualityManage.contract;

import com.glodon.bim.base.IBasePresenter;
import com.glodon.bim.base.IBaseView;
import com.glodon.bim.business.qualityManage.bean.BluePrintBean;
import com.glodon.bim.business.qualityManage.bean.BlueprintListBeanItem;
import com.glodon.bim.business.qualityManage.listener.OnBlueprintCatalogClickListener;
import com.glodon.bim.business.qualityManage.listener.OnBlueprintHintClickListener;
import com.glodon.bim.business.qualityManage.listener.OnChooseBlueprintCataListener;
import com.glodon.bim.business.qualityManage.listener.OnChooseBlueprintObjListener;

import java.util.List;

import rx.Observable;

/**
 * 描述：选择质检项目
 * 作者：zhourf on 2017/10/9
 * 邮箱：zhourf@glodon.com
 */

public interface BluePrintContract {

    interface Presenter extends IBasePresenter {

        /**
         * 获取选择项目监听
         */
        OnChooseBlueprintObjListener getmObjListener();

        /**
         * 获取选择目录监听
         */
        OnChooseBlueprintCataListener getmCataListener();

        /**
         * 横向目录点击监听
         */
        OnBlueprintCatalogClickListener getmCataClickListener();

        /**
         * 点击目录切换的监听
         */
        OnBlueprintHintClickListener getmHintClickListener();

    }

    interface View extends IBaseView {


        /**
         * 初始化列表
         *
         * @param selectId 选中的id
         */
        void updateContentListView(List<BlueprintListBeanItem> list, String selectId);

        /**
         * 更新顶部目录
         */
        void updateCataListView(List<BlueprintListBeanItem> mCatalogList);

        /**
         * 更新被选中的目录的view
         */
        void updateHintListView(List<BlueprintListBeanItem> mHintList, BlueprintListBeanItem item);

        /**
         * 关闭切换目录
         */
        void closeHint();

    }

    interface Model {

        //        /**
//         * 获取图纸项目列表
//         */
        Observable<BluePrintBean> getBluePrint(long projectId,String projectVersionId,String fileId,int pageIndex);
    }
}
