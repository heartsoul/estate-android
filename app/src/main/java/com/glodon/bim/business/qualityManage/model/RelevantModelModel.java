package com.glodon.bim.business.qualityManage.model;

import com.glodon.bim.basic.config.AppConfig;
import com.glodon.bim.basic.network.NetRequest;
import com.glodon.bim.business.greendao.provider.DaoProvider;
import com.glodon.bim.business.qualityManage.bean.ModelElementHistory;
import com.glodon.bim.business.qualityManage.bean.ModelElementInfo;
import com.glodon.bim.business.qualityManage.bean.ProjectVersionBean;
import com.glodon.bim.business.qualityManage.bean.RelevantBluePrintToken;
import com.glodon.bim.business.qualityManage.contract.RelevantBluePrintContract;
import com.glodon.bim.business.qualityManage.contract.RelevantModelContract;

import java.util.List;

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
    public Observable<RelevantBluePrintToken> getToken(long projectId, String projectVersionId, String integrateId){
        return NetRequest.getInstance().getCall(AppConfig.BASE_URL,RelevantModelApi.class).getToken(projectId,projectVersionId,integrateId,cookie);
    }

    /**
     * App端查询模型文件对应的所有关联构件
     */
    public Observable<List<ModelElementHistory>> getElements(long deptId, String gdocFileId){
        return NetRequest.getInstance().getCall(AppConfig.BASE_URL,RelevantModelApi.class).getElements(deptId,gdocFileId,cookie);
    }

    /**
     * 根据构件id获取构件名称
     */
    public Observable<ModelElementInfo> getElementProperty(long projectId, String versionId, String fileId, String elementId){
        return NetRequest.getInstance().getCall(AppConfig.BASE_URL,CreateCheckListApi.class).getElementProperty(projectId,versionId,fileId,elementId,new DaoProvider().getCookie());
    }
}
