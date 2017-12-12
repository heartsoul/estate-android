package com.glodon.bim.business.qualityManage.model;

import com.glodon.bim.basic.config.AppConfig;
import com.glodon.bim.basic.network.NetRequest;
import com.glodon.bim.business.greendao.provider.DaoProvider;
import com.glodon.bim.business.qualityManage.bean.ProjectVersionBean;
import com.glodon.bim.business.qualityManage.bean.RelevantBluePrintToken;
import com.glodon.bim.business.qualityManage.contract.RelevantBluePrintContract;

import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 描述：具体图纸展示
 * 作者：zhourf on 2017/12/11
 * 邮箱：zhourf@glodon.com
 */

public class RelevantBluePrintModel implements RelevantBluePrintContract.Model{
    public String cookie;

    public RelevantBluePrintModel() {
        cookie = new DaoProvider().getCookie();
    }

    @Override
    public Observable<ProjectVersionBean> getLatestVersion(long projectId) {
        return NetRequest.getInstance().getCall(AppConfig.BASE_URL, RelevantBluePrintApi.class).getLatestVersion(projectId, cookie);
    }
    @Override
    public Observable<RelevantBluePrintToken> getToken(long projectId, String projectVersionId, String fileId){
        return NetRequest.getInstance().getCall(AppConfig.BASE_URL,RelevantBluePrintApi.class).getToken(projectId,projectVersionId,fileId,cookie);
    }
}
