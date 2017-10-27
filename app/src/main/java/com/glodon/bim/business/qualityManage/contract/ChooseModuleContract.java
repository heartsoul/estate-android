package com.glodon.bim.business.qualityManage.contract;

import com.glodon.bim.base.IBasePresenter;
import com.glodon.bim.base.IBaseView;
import com.glodon.bim.business.qualityManage.bean.ModuleListBean;
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
         * @param selectPosition 选中的position
         */
        void initListView(List<ModuleListBeanItem> list, int selectPosition);

        /**
         * 更新列表
         */
        void updateListView(List<ModuleListBeanItem> mDataList);
    }

    interface Model {

        /**
         * 获取质检项目列表
         * @param projectType  项目类型
         * @param page 分页
         * @param size 每页数量
         */
        Observable<ModuleListBean> getModuleList(String projectType, int  page, int size);
    }
}
