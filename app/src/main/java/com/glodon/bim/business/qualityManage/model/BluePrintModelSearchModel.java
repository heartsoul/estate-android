package com.glodon.bim.business.qualityManage.model;

import com.glodon.bim.basic.network.NetRequest;
import com.glodon.bim.basic.utils.SharedPreferencesUtil;
import com.glodon.bim.business.greendao.provider.DaoProvider;
import com.glodon.bim.business.qualityManage.bean.BluePrintModelSearchBean;
import com.glodon.bim.business.qualityManage.contract.BluePrintModelSearchContract;

import rx.Observable;

/**
 * 描述：模型图纸搜索
 * 作者：zhourf on 2017/12/21
 * 邮箱：zhourf@glodon.com
 */

public class BluePrintModelSearchModel implements BluePrintModelSearchContract.Model {

    private String cookie;
    private long projectId;
    private String projectVersionId;

    public BluePrintModelSearchModel() {
        cookie = new DaoProvider().getCookie();
        projectId = SharedPreferencesUtil.getProjectId();
        projectVersionId = SharedPreferencesUtil.getProjectVersionId(projectId);
    }

    /**
     * 搜索模型或图纸
     * @param name  搜索关键字
     * @param suffix  模型rvt  图纸dwg
     */
    public Observable<BluePrintModelSearchBean> search( String name, String suffix){
        return NetRequest.getInstance().getCall(BluePrintModelSearchApi.class).search(projectId,projectVersionId,name,suffix,cookie);
    }

}
