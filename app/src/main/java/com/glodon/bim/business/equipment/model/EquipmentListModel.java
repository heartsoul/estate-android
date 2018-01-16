package com.glodon.bim.business.equipment.model;

import com.glodon.bim.business.equipment.bean.EquipmentListBean;
import com.glodon.bim.business.equipment.contract.EquipmentListContract;
import com.glodon.bim.business.qualityManage.bean.ClassifyNum;

import java.util.List;

import rx.Observable;

/**
 * 描述：材设进场记录-清单
 * 作者：zhourf on 2018/1/15
 * 邮箱：zhourf@glodon.com
 */

public class EquipmentListModel implements EquipmentListContract.Model {
    @Override
    public Observable<EquipmentListBean> getEquipmentList(long deptId, String mQcState, int mCurrentPage, int mSize) {
        return null;
    }

    @Override
    public Observable<List<ClassifyNum>> getStatusNum(long deptId) {
        return null;
    }
}
