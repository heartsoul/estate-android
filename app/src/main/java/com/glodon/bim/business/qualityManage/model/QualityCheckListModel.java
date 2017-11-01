package com.glodon.bim.business.qualityManage.model;

import com.glodon.bim.basic.config.AppConfig;
import com.glodon.bim.basic.network.NetRequest;
import com.glodon.bim.business.greendao.provider.DaoProvider;
import com.glodon.bim.business.qualityManage.bean.QualityCheckListBean;
import com.glodon.bim.business.qualityManage.contract.QualityCheckListContract;

import rx.Observable;

/**
 * 描述：质量清单列表
 * 作者：zhourf on 2017/10/20
 * 邮箱：zhourf@glodon.com
 */

public class QualityCheckListModel implements QualityCheckListContract.Model{
    @Override
    public Observable<QualityCheckListBean> getQualityCheckList(long deptId,String qcState, int page, int size) {
        return NetRequest.getInstance().getCall(AppConfig.BASE_URL,QualityCheckListApi.class).getQualityCheckList(deptId,qcState,page,size,new DaoProvider().getCookie());
    }
}
