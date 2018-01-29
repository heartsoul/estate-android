package com.glodon.bim.business.qualityManage.model;

import com.glodon.bim.basic.config.AppConfig;
import com.glodon.bim.basic.network.NetRequest;
import com.glodon.bim.basic.utils.SharedPreferencesUtil;
import com.glodon.bim.business.greendao.provider.DaoProvider;
import com.glodon.bim.business.qualityManage.bean.ModuleListBeanItem;
import com.glodon.bim.business.qualityManage.contract.ChooseModuleContract;

import java.util.List;

import rx.Observable;

/**
 * 描述：选择质检项目
 * 作者：zhourf on 2017/10/27
 * 邮箱：zhourf@glodon.com
 */

public class ChooseModuleModel implements ChooseModuleContract.Model {

    @Override
    public Observable<List<ModuleListBeanItem>> getModuleList(long deptId){
        return NetRequest.getInstance().getCall(AppConfig.BASE_URL,CreateCheckListApi.class).getModuleList(deptId,true,new DaoProvider().getCookie());
//        String versionId = SharedPreferencesUtil.getProjectVersionId(deptId);
//        return NetRequest.getInstance().getCall(AppConfig.BASE_URL,CreateCheckListApi.class).getModuleList(deptId,versionId,new DaoProvider().getCookie());
    }
}
