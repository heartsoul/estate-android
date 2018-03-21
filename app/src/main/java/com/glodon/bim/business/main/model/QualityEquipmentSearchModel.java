package com.glodon.bim.business.main.model;

import com.glodon.bim.basic.network.NetRequest;
import com.glodon.bim.business.equipment.bean.EquipmentListBean;
import com.glodon.bim.business.greendao.provider.DaoProvider;
import com.glodon.bim.business.main.contract.QualityEquipmentSearchContract;
import com.glodon.bim.business.qualityManage.bean.QualityCheckListBean;

import rx.Observable;

/**
 * Created by cwj on 2018/3/6.
 * Description:QualityEquipmentSearchModel
 */

public class QualityEquipmentSearchModel implements QualityEquipmentSearchContract.Model {

    @Override
    public Observable<QualityCheckListBean> searchQualityData(long deptId, String keywords, int page, int size) {
//        String[] sort = {"createTime,desc"};

        return NetRequest.getInstance().getCall(QualityEquipmentSearchApi.class).searchQualityData(deptId,keywords,page,size,new DaoProvider().getCookie());
    }

    @Override
    public Observable<EquipmentListBean> searchEquipmentData(long deptId, String keywords, int page, int size) {
        return NetRequest.getInstance().getCall(QualityEquipmentSearchApi.class).searchEquipmentData(deptId,keywords,page,size,new DaoProvider().getCookie());
    }
}
