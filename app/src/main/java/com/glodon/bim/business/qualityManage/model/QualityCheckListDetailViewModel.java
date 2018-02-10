package com.glodon.bim.business.qualityManage.model;

import com.glodon.bim.basic.network.NetRequest;
import com.glodon.bim.business.greendao.provider.DaoProvider;
import com.glodon.bim.business.qualityManage.bean.QualityCheckListDetailBean;
import com.glodon.bim.business.qualityManage.contract.QualityCheckListDetailViewContract;

import rx.Observable;

/**
 * 描述：检查单详情
 * 作者：zhourf on 2017/11/3
 * 邮箱：zhourf@glodon.com
 */

public class QualityCheckListDetailViewModel implements QualityCheckListDetailViewContract.Model{

    @Override
    public Observable<QualityCheckListDetailBean> getQualityCheckListDetail(long deptId,long id){
        return NetRequest.getInstance().getCall(QualityCheckListApi.class).getQualityCheckListDetail(deptId,id,new DaoProvider().getCookie());
    }


}
