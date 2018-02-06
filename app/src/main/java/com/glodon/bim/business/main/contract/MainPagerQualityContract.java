package com.glodon.bim.business.main.contract;

import com.glodon.bim.base.IBasePresenter;
import com.glodon.bim.base.IBaseView;
import com.glodon.bim.business.main.bean.MainPagerQualityEquipmentItem;
import com.glodon.bim.business.main.listener.OnPagerItemClickListener;

import java.util.List;

/**
 * 描述：首页质量
 * 作者：zhourf on 2017/10/17
 * 邮箱：zhourf@glodon.com
 */

public interface MainPagerQualityContract {

    interface View extends IBaseView{

        /**
         * 更新列表
         */
        void updateList(List<MainPagerQualityEquipmentItem> mDataList, OnPagerItemClickListener mListener);
    }

    interface Presenter extends IBasePresenter{

    }

    interface Model{
    }
}
