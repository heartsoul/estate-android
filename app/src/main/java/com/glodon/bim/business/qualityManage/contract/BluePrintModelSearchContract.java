package com.glodon.bim.business.qualityManage.contract;

import com.glodon.bim.base.IBasePresenter;
import com.glodon.bim.base.IBaseView;
import com.glodon.bim.business.qualityManage.bean.BluePrintBean;
import com.glodon.bim.business.qualityManage.bean.BluePrintModelSearchBean;
import com.glodon.bim.business.qualityManage.bean.BluePrintModelSearchBeanItem;
import com.glodon.bim.business.qualityManage.bean.BlueprintListBeanItem;
import com.glodon.bim.business.qualityManage.listener.OnBlueprintCatalogClickListener;
import com.glodon.bim.business.qualityManage.listener.OnBlueprintHintClickListener;
import com.glodon.bim.business.qualityManage.listener.OnChooseBlueprintCataListener;
import com.glodon.bim.business.qualityManage.listener.OnChooseBlueprintObjListener;

import java.util.List;

import rx.Observable;

/**
 * 描述：模型图纸搜索
 * 作者：zhourf on 2017/10/9
 * 邮箱：zhourf@glodon.com
 */

public interface BluePrintModelSearchContract {

    interface Presenter extends IBasePresenter {

        /**
         * 上拉加载更多
         */
        void pullUp();

        /**
         * 根据关键字搜索
         */
        void search(String key);
    }

    interface View extends IBaseView {

        /**
         * 隐藏搜索历史
         */
        void hideHistory();

        /**
         * 展示搜索结果
         */
        void showResult(List<BluePrintModelSearchBeanItem> mDataList);
    }

    interface Model {

        /**
         * 搜索模型或图纸
         * @param name  搜索关键字
         * @param suffix  模型rvt  图纸dwg
         * @return
         */
        Observable<BluePrintModelSearchBean> search(String name, String suffix);
    }
}
