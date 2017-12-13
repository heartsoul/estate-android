package com.glodon.bim.business.qualityManage.model;

import com.glodon.bim.basic.config.AppConfig;
import com.glodon.bim.basic.network.NetRequest;
import com.glodon.bim.business.greendao.provider.DaoProvider;
import com.glodon.bim.business.qualityManage.bean.ProjectVersionBean;
import com.glodon.bim.business.qualityManage.bean.RelevantBluePrintToken;
import com.glodon.bim.business.qualityManage.contract.RelevantBluePrintContract;
import com.glodon.bim.business.qualityManage.contract.RelevantModelContract;

import rx.Observable;

/**
 * 描述：具体模型展示
 * 作者：zhourf on 2017/12/11
 * 邮箱：zhourf@glodon.com
 */

public class RelevantModelModel implements RelevantModelContract.Model{
    public String cookie;

    public RelevantModelModel() {
        cookie = new DaoProvider().getCookie();
    }

    @Override
    public Observable<ProjectVersionBean> getLatestVersion(long projectId) {
        return NetRequest.getInstance().getCall(AppConfig.BASE_URL, RelevantModelApi.class).getLatestVersion(projectId, cookie);
    }
    @Override
    public Observable<RelevantBluePrintToken> getToken(long projectId, String projectVersionId, String fileId){
        return NetRequest.getInstance().getCall(AppConfig.BASE_URL,RelevantModelApi.class).getToken(projectId,projectVersionId,fileId,cookie);
    }
}
