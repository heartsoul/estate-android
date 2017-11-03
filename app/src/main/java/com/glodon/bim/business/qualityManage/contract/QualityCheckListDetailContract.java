package com.glodon.bim.business.qualityManage.contract;

import com.glodon.bim.base.IBasePresenter;
import com.glodon.bim.base.IBaseView;
import com.glodon.bim.business.main.bean.ProjectListItem;
import com.glodon.bim.business.qualityManage.bean.QualityCheckListBean;
import com.glodon.bim.business.qualityManage.bean.QualityCheckListBeanItem;
import com.glodon.bim.business.qualityManage.bean.QualityCheckListDetailBean;
import com.glodon.bim.business.qualityManage.listener.OnOperateSheetListener;

import java.util.List;

import rx.Observable;

/**
 * 描述：检查单详情
 * 作者：zhourf on 2017/10/9
 * 邮箱：zhourf@glodon.com
 */

public interface QualityCheckListDetailContract {

    interface Presenter extends IBasePresenter {


    }

    interface View extends IBaseView {

    }

    interface Model {
    }
}
