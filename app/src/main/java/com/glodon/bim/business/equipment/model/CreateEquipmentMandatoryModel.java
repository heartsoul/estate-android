package com.glodon.bim.business.equipment.model;

import com.glodon.bim.basic.network.NetRequest;
import com.glodon.bim.basic.utils.SharedPreferencesUtil;
import com.glodon.bim.business.equipment.contract.CreateEquipmentMandatoryContract;
import com.glodon.bim.business.greendao.provider.DaoProvider;
import com.glodon.bim.business.qualityManage.bean.InspectionCompanyItem;

import java.util.List;

import rx.Observable;

/**
 * 描述：创建材设进场记录-必填项页面
 * 作者：zhourf on 2018/1/9
 * 邮箱：zhourf@glodon.com
 */

public class CreateEquipmentMandatoryModel implements CreateEquipmentMandatoryContract.Model{
    /**
     * 获取项目下验收单位列表
     */
    public Observable<List<InspectionCompanyItem>> getAcceptanceCompanies(){
        return NetRequest.getInstance().getCall(CreateEquipmentApi.class).getAcceptanceCompanies(
                SharedPreferencesUtil.getProjectId(),new DaoProvider().getCookie()
        );
    }
}
