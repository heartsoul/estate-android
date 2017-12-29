package com.glodon.bim.business.qualityManage.contract;

import com.glodon.bim.base.IBasePresenter;
import com.glodon.bim.base.IBaseView;
import com.glodon.bim.business.qualityManage.bean.ModuleStandardItem;

import java.util.List;

import rx.Observable;

/**
 * 描述：质检标准
 * 作者：zhourf on 2017/10/9
 * 邮箱：zhourf@glodon.com
 */

public interface ModuleStandardContract {

    interface Presenter extends IBasePresenter {
        /**
         * 查询第一层
         */
        List<ModuleStandardItem> getRootList();

        /**
         * 查询子层
         */
        List<ModuleStandardItem> getListByParentId(long parentId);
    }

    interface View extends IBaseView {


        /**
         * 更新列表
         */
        void updateListView(List<ModuleStandardItem> mDataList);
    }

    interface Model {

        /**
         * 获取质检项目列表
         */
        Observable<List<ModuleStandardItem>> getModuleStandardList(long templateId);
    }
}
