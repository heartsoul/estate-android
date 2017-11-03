package com.glodon.bim.business.qualityManage.contract;

import com.glodon.bim.base.IBasePresenter;
import com.glodon.bim.base.IBaseView;
import com.glodon.bim.business.qualityManage.bean.QualityCheckListDetailBean;

import rx.Observable;

/**
 * 描述：检查单详情
 * 作者：zhourf on 2017/10/9
 * 邮箱：zhourf@glodon.com
 */

public interface QualityCheckListDetailViewContract {

    interface Presenter extends IBasePresenter {

        /**
         * 获取检查单详情
         * @param deptId  项目id
         * @param id 检查单id
         */
        void getInspectInfo(long deptId, long id);
    }

    interface View extends IBaseView {

        /**
         * 检查单详情数据
         */
        void updateData(QualityCheckListDetailBean qualityCheckListDetailBean);
    }

    interface Model {
        /**
         * 获取检查单详情
         * @param deptId 项目id
         * @param id 检查单id
         */
        Observable<QualityCheckListDetailBean> getQualityCheckListDetail(long deptId, long id);
    }
}
