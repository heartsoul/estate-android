package com.glodon.bim.business.main.contract;

import com.glodon.bim.base.IBasePresenter;
import com.glodon.bim.base.IBaseView;
import com.glodon.bim.business.main.bean.ProjectItem;

import java.util.List;

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
        void updateData(List<ProjectItem> mDataList);

        /**
         * 设置显示样式
         * @param size  数据数量
         */
        void setStyle(int size);
    }

    interface Presenter extends IBasePresenter{

    }

    interface Model{

    }
}
