package com.glodon.bim.business.qualityManage.model;

import com.glodon.bim.basic.network.NetRequest;
import com.glodon.bim.business.greendao.provider.DaoProvider;
import com.glodon.bim.business.qualityManage.bean.ModuleStandardItem;
import com.glodon.bim.business.qualityManage.contract.ModuleStandardContract;

import java.util.List;

import rx.Observable;

/**
 * 描述：质检标准
 * 作者：zhourf on 2017/11/20
 * 邮箱：zhourf@glodon.com
 */

public class ModuleStandardModel implements ModuleStandardContract.Model {
    @Override
    public Observable<List<ModuleStandardItem>> getModuleStandardList(long templateId) {
        return NetRequest.getInstance().getCall(CreateCheckListApi.class).getModuleStandard(templateId,new DaoProvider().getCookie());
    }
}
