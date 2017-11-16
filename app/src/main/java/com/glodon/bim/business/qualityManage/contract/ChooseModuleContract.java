package com.glodon.bim.business.qualityManage.contract;

import com.glodon.bim.base.IBasePresenter;
import com.glodon.bim.base.IBaseView;
import com.glodon.bim.business.qualityManage.bean.ModuleListBeanItem;
import com.glodon.bim.business.qualityManage.listener.OnChooseModuleListener;

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
         * 下拉刷新
         */
        void pullDown();

        /**
         * 上拉加载
         */
        void pullUp();

        /**
         * 获取监听
         */
        OnChooseModuleListener getmListener();

    }

    interface View extends IBaseView {


        /**
         * 初始化列表
         * @param selectId 选中的id
         */
        void initListView(List<ModuleListBeanItem> list, long selectId);

        /**
         * 更新列表
         */
        void updateListView(List<ModuleListBeanItem> mDataList);
    }

    interface Model {

        /**
         * 获取质检项目列表
         */
        Observable<List<ModuleListBeanItem>> getModuleList(long deptId,long projectId);
    }
}
