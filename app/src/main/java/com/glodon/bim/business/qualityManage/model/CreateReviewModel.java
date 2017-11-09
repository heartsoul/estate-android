package com.glodon.bim.business.qualityManage.model;

import com.glodon.bim.basic.config.AppConfig;
import com.glodon.bim.basic.network.NetRequest;
import com.glodon.bim.business.greendao.provider.DaoProvider;
import com.glodon.bim.business.qualityManage.bean.QualityGetRepairInfo;
import com.glodon.bim.business.qualityManage.bean.QualityGetReviewInfo;
import com.glodon.bim.business.qualityManage.bean.QualityRepairParams;
import com.glodon.bim.business.qualityManage.bean.QualityReviewParams;
import com.glodon.bim.business.qualityManage.bean.SaveBean;
import com.glodon.bim.business.qualityManage.contract.CreateReviewContract;

import okhttp3.ResponseBody;
import rx.Observable;

/**
 * 描述：新建复查单 整改单
 * 作者：zhourf on 2017/11/3
 * 邮箱：zhourf@glodon.com
 */

public class CreateReviewModel implements CreateReviewContract.Model {
    /**
     * 整改单  新增  保存
     */
    @Override
    public Observable<SaveBean> createSaveRepair(long deptId,QualityRepairParams props){
        return NetRequest.getInstance().getCall(AppConfig.BASE_URL,CreateReviewApi.class).createSaveRepair(deptId,props,new DaoProvider().getCookie());
    }

    /**
     * 整改单  编辑  保存
     */
    @Override
    public Observable<ResponseBody> editSaveRepair(long deptId,long id, QualityRepairParams props){
        return NetRequest.getInstance().getCall(AppConfig.BASE_URL,CreateReviewApi.class).editSaveRepair(deptId,id,props,new DaoProvider().getCookie());
    }


    /**
     * 整改单  新增  提交
     */
    @Override
    public Observable<SaveBean> createSubmitRepair(long deptId, QualityRepairParams props){
        return NetRequest.getInstance().getCall(AppConfig.BASE_URL,CreateReviewApi.class).createSubmitRepair(deptId,props,new DaoProvider().getCookie());
    }

    /**
     * 整改单  编辑  提交
     */
    @Override
    public Observable<ResponseBody> editSubmitRepair(long deptId,long id, QualityRepairParams props){
        return NetRequest.getInstance().getCall(AppConfig.BASE_URL,CreateReviewApi.class).editSubmitRepair(deptId,id,props,new DaoProvider().getCookie());
    }

    /**
     * 整改单  删除
     */
    @Override
    public Observable<ResponseBody> deleteRepair(long deptId,  long id){
        return NetRequest.getInstance().getCall(AppConfig.BASE_URL,CreateReviewApi.class).deleteRepair(deptId,id,new DaoProvider().getCookie());
    }

    /**
     * 整改单  查询保存但未提交的整改单
     */
    @Override
    public Observable<QualityGetRepairInfo> getRepairInfo( long deptId,  long inspectionId){
        return NetRequest.getInstance().getCall(AppConfig.BASE_URL,CreateReviewApi.class).getRepairInfo(deptId,inspectionId,new DaoProvider().getCookie());
    }


    /**
     * 复查单  新增  保存
     */
    @Override
    public Observable<SaveBean> createSaveReview(long deptId,QualityReviewParams props){
        return NetRequest.getInstance().getCall(AppConfig.BASE_URL,CreateReviewApi.class).createSaveReview(deptId,props,new DaoProvider().getCookie());
    }

    /**
     * 复查单  编辑  保存
     */
    @Override
    public Observable<ResponseBody> editSaveReview( long deptId, long id, QualityReviewParams props){
        return NetRequest.getInstance().getCall(AppConfig.BASE_URL,CreateReviewApi.class).editSaveReview(deptId,id,props,new DaoProvider().getCookie());
    }

    /**
     * 复查单  新增  提交
     */
    @Override
    public Observable<SaveBean> createSubmitReview(long deptId,QualityReviewParams props){
        return NetRequest.getInstance().getCall(AppConfig.BASE_URL,CreateReviewApi.class).createSubmitReview(deptId,props,new DaoProvider().getCookie());
    }

    /**
     * 复查单  编辑  提交
     */
    @Override
    public Observable<ResponseBody> editSubmitReview(long deptId, long id,  QualityReviewParams props){
        return NetRequest.getInstance().getCall(AppConfig.BASE_URL,CreateReviewApi.class).editSubmitReview(deptId,id,props,new DaoProvider().getCookie());
    }


    /**
     * 复查单  查询保存后的复查单数据
     */
    @Override
    public Observable<QualityGetReviewInfo> getReviewInfo(long deptId,long inspectionId){
        return NetRequest.getInstance().getCall(AppConfig.BASE_URL,CreateReviewApi.class).getReviewInfo(deptId,inspectionId,new DaoProvider().getCookie());
    }

    /**
     * 整改单  删除
     */
    @Override
    public Observable<ResponseBody> deleteReview(long deptId, long id){
        return NetRequest.getInstance().getCall(AppConfig.BASE_URL,CreateReviewApi.class).deleteReview(deptId,id,new DaoProvider().getCookie());
    }
}
