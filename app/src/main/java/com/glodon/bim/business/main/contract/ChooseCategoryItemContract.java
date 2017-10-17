package com.glodon.bim.business.main.contract;

import com.glodon.bim.base.IBasePresenter;
import com.glodon.bim.base.IBaseView;
import com.glodon.bim.business.main.bean.CategoryItem;
import com.glodon.bim.business.main.bean.ProjectItem;

import java.util.List;

/**
 * 描述：项目功能列表
 * 作者：zhourf on 2017/10/17
 * 邮箱：zhourf@glodon.com
 */

public interface ChooseCategoryItemContract {

    interface View extends IBaseView{

        /**
         * 更新列表
         */
        void updateData(List<CategoryItem> mDataList);

    }

    interface Presenter extends IBasePresenter{

    }

    interface Model{

    }
}
