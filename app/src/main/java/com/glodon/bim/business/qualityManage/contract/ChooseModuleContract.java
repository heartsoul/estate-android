package com.glodon.bim.business.qualityManage.contract;

import com.glodon.bim.base.IBasePresenter;
import com.glodon.bim.base.IBaseView;
import com.glodon.bim.business.qualityManage.bean.ModuleListBeanItem;
import com.glodon.bim.business.qualityManage.listener.OnChooseModuleCataListener;
import com.glodon.bim.business.qualityManage.listener.OnChooseModuleObjListener;
import com.glodon.bim.business.qualityManage.listener.OnModuleCatalogClickListener;
import com.glodon.bim.business.qualityManage.listener.OnModuleHintClickListener;

import java.util.List;

import rx.Observable;

/**
 * 描述：选择质检项目
 * 作者：zhourf on 2017/10/9
 * 邮箱：zhourf@glodon.com
 */

public interface ChooseModuleContract {

    interface Presenter extends IBasePresenter {

        /**
         * 获取选择项目监听
         */
        OnChooseModuleObjListener getmObjListener();

        /**
         * 获取选择目录监听
         */
        OnChooseModuleCataListener getmCataListener();

        /**
         * 横向目录点击监听
         */
        OnModuleCatalogClickListener getmCataClickListener();

        /**
         * 点击目录切换的监听
         */
        OnModuleHintClickListener getmHintClickListener();

    }

    interface View extends IBaseView {


        /**
         * 初始化列表
         * @param selectId 选中的id
         */
        void updateContentListView(List<ModuleListBeanItem> list, Long selectId);

        /**
         * 更新顶部目录
         */
        void updateCataListView(List<ModuleListBeanItem> mCatalogList);

        /**
         * 更新被选中的目录的view
         */
        void updateHintListView(List<ModuleListBeanItem> mHintList, ModuleListBeanItem item);

        /**
         * 关闭切换目录
         */
        void closeHint();

    }

    interface Model {

        /**
         * 获取质检项目列表
         */
        Observable<List<ModuleListBeanItem>> getModuleList(long deptId,long projectId);
    }
}
