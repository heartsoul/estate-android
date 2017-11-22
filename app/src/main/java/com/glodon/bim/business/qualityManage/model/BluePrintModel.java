package com.glodon.bim.business.qualityManage.model;

import com.glodon.bim.basic.config.AppConfig;
import com.glodon.bim.basic.network.NetRequest;
import com.glodon.bim.business.greendao.provider.DaoProvider;
import com.glodon.bim.business.qualityManage.bean.BlueprintListBeanItem;
import com.glodon.bim.business.qualityManage.bean.ModuleListBeanItem;
import com.glodon.bim.business.qualityManage.contract.BluePrintContract;
import com.glodon.bim.business.qualityManage.contract.ChooseModuleContract;

import java.util.List;

import rx.Observable;

/**
 * 描述：选择图纸项目
 * 作者：zhourf on 2017/10/27
 * 邮箱：zhourf@glodon.com
 */

public class BluePrintModel implements BluePrintContract.Model {

    @Override
    public Observable<List<BlueprintListBeanItem>> getBluePrintList(long deptId, long projectId){
        return NetRequest.getInstance().getCall(AppConfig.BASE_URL,BluePrintApi.class).getBluePrintList(deptId,projectId,new DaoProvider().getCookie());
    }
}
