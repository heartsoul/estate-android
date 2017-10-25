package com.glodon.bim.business.main.contract;

import com.glodon.bim.base.IBasePresenter;
import com.glodon.bim.base.IBaseView;
import com.glodon.bim.business.main.bean.ProjectListBean;
import com.glodon.bim.business.main.bean.ProjectListItem;
import com.glodon.bim.business.main.listener.OnProjectClickListener;

import java.util.List;

import rx.Observable;

/**
 * 描述：选择项目列表
 * 作者：zhourf on 2017/10/17
 * 邮箱：zhourf@glodon.com
 */

public interface ChooseProjectContract {

    interface View extends IBaseView{

        /**
         * 更新列表
         */
        void updateData(List<ProjectListItem> mDataList);

        /**
         * 设置显示样式
         * @param size  数据数量
         */
        void setStyle(int size);
    }

    interface Presenter extends IBasePresenter{

        /**
         * 上拉加载
         */
        void pullUp();

        /**
         * 下拉刷新
         */
        void pullDown();

        /**
         * 获取点击项目的监听
         */
        OnProjectClickListener getListener();
    }

    interface Model{
        /**
         * 获取项目列表
         * @param page 分页
         * @param size 每页数量
         */
        Observable<ProjectListBean> getAvailableProjects(int page, int size);
    }
}
