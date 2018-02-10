package com.glodon.bim.business.qualityManage.model;

import com.glodon.bim.basic.network.NetRequest;
import com.glodon.bim.business.greendao.provider.DaoProvider;
import com.glodon.bim.business.qualityManage.bean.BluePrintDotItem;
import com.glodon.bim.business.qualityManage.bean.ProjectVersionBean;
import com.glodon.bim.business.qualityManage.bean.RelevantBluePrintToken;
import com.glodon.bim.business.qualityManage.contract.RelevantBluePrintContract;

import java.util.List;

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
        return NetRequest.getInstance().getCall( RelevantBluePrintApi.class).getLatestVersion(projectId, cookie);
    }
    @Override
    public Observable<RelevantBluePrintToken> getToken(long projectId, String projectVersionId, String fileId){
        return NetRequest.getInstance().getCall(RelevantBluePrintApi.class).getToken(projectId,projectVersionId,fileId,cookie);
    }

    @Override
    public Observable<List<BluePrintDotItem>> getBluePrintDots(long deptId,String drawingGdocFileId){
        return NetRequest.getInstance().getCall(RelevantBluePrintApi.class).getBluePrintDots(deptId,drawingGdocFileId,cookie);
    }
}
