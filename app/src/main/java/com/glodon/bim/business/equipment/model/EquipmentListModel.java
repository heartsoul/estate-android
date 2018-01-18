package com.glodon.bim.business.equipment.model;

import com.glodon.bim.basic.config.AppConfig;
import com.glodon.bim.basic.network.NetRequest;
import com.glodon.bim.basic.utils.SharedPreferencesUtil;
import com.glodon.bim.business.equipment.bean.EquipmentListBean;
import com.glodon.bim.business.equipment.contract.EquipmentListContract;
import com.glodon.bim.business.greendao.provider.DaoProvider;
import com.glodon.bim.business.qualityManage.bean.ClassifyNum;
import com.glodon.bim.common.config.CommonConfig;

import java.util.List;

import rx.Observable;

/**
 * 描述：材设进场记录-清单
 * 作者：zhourf on 2018/1/15
 * 邮箱：zhourf@glodon.com
 */

public class EquipmentListModel implements EquipmentListContract.Model {
    private String cookie;
    private long projectId;
    private String[] sort;

    public EquipmentListModel() {
        cookie = new DaoProvider().getCookie();
        projectId = SharedPreferencesUtil.getProjectId();
        sort = new String[]{"approachDate,desc"};
    }
    /**
     * 根据id查询详情和保存后的编辑信息
     * 全部
     */
    public Observable<EquipmentListBean> getAllEquipmentList(int page,int size,String state){
        switch (state){
            case CommonConfig.QC_STATE_ALL:
                return getEquipmentList(page,size);
            case CommonConfig.QC_STATE_EDIT:
                return getToCommitEquipmentList(page,size);
            case CommonConfig.QC_STATE_STANDARD:
                return getQualifyEquipmentList(page,size,true);
            case CommonConfig.QC_STATE_NOT_STANDARD:
                return getQualifyEquipmentList(page,size,false);
        }
        return getEquipmentList(page,size);
    }

    /**
     * 根据id查询详情和保存后的编辑信息
     * 全部
     */
    private Observable<EquipmentListBean> getEquipmentList(int page, int size ){
        return NetRequest.getInstance().getCall(AppConfig.BASE_URL,EquipmentListApi.class).getEquipmentList(projectId,page,size,sort,cookie);
    }

    /**
     * 根据id查询详情和保存后的编辑信息
     * 待提交
     */
    private Observable<EquipmentListBean> getToCommitEquipmentList(int page,int size){
        return NetRequest.getInstance().getCall(AppConfig.BASE_URL,EquipmentListApi.class).getToCommitEquipmentList(projectId,page,size,false,sort,cookie);
    }
    /**
     * 根据id查询详情和保存后的编辑信息
     * 合格不合格
     */
    private Observable<EquipmentListBean> getQualifyEquipmentList(int page,int size,boolean qualified){
        return NetRequest.getInstance().getCall(AppConfig.BASE_URL,EquipmentListApi.class).getqualifyEquipmentList(projectId,page,size,qualified,sort,cookie);
    }
    @Override
    public Observable<List<ClassifyNum>> getStatusNum(long deptId) {
        return null;
    }
}
