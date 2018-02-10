package com.glodon.bim.business.qualityManage.model;

import com.glodon.bim.basic.network.NetRequest;
import com.glodon.bim.business.greendao.provider.DaoProvider;
import com.glodon.bim.business.qualityManage.bean.ClassifyNum;
import com.glodon.bim.business.qualityManage.bean.QualityCheckListBean;
import com.glodon.bim.business.qualityManage.contract.QualityCheckListContract;

import java.util.List;

import rx.Observable;

/**
 * 描述：质量清单列表
 * 作者：zhourf on 2017/10/20
 * 邮箱：zhourf@glodon.com
 */

public class QualityCheckListModel implements QualityCheckListContract.Model{
    @Override
    public Observable<QualityCheckListBean> getQualityCheckList(long deptId,String qcState, int page, int size) {
        String[] sort = {"updateTime,desc"};
        return NetRequest.getInstance().getCall(QualityCheckListApi.class).getQualityCheckList(deptId,qcState,page,size,sort,new DaoProvider().getCookie());
    }

    /**
     * 获取质检清单
     */
    public Observable<QualityCheckListBean> getQualityCheckList(long deptId, String qcState,int page, int size, long qualityCheckpointId, String qualityCheckpointName){
        String[] sort = {"updateTime,desc"};
        return NetRequest.getInstance().getCall(QualityCheckListApi.class).getQualityCheckList(deptId,qcState,page,size,sort,qualityCheckpointId,qualityCheckpointName,new DaoProvider().getCookie());
    }

    @Override
    public Observable<List<ClassifyNum>> getStatusNum(long deptId){
        return NetRequest.getInstance().getCall(QualityCheckListApi.class).getStatusNum(deptId,new DaoProvider().getCookie());
    }
    @Override
    public Observable<List<ClassifyNum>> getStatusNum(long deptId,long qualityCheckpointId,String qualityCheckpointName){
        return NetRequest.getInstance().getCall(QualityCheckListApi.class).getStatusNum(deptId,qualityCheckpointId,qualityCheckpointName,new DaoProvider().getCookie());
    }
}
