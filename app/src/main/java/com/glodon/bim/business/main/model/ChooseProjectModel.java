package com.glodon.bim.business.main.model;

import com.glodon.bim.basic.network.NetRequest;
import com.glodon.bim.business.greendao.provider.DaoProvider;
import com.glodon.bim.business.main.bean.ProjectListBean;
import com.glodon.bim.business.main.contract.ChooseProjectContract;
import com.glodon.bim.business.qualityManage.bean.ProjectVersionBean;
import com.glodon.bim.business.qualityManage.model.ModelApi;

import rx.Observable;

/**
 * 描述：选择项目列表
 * 作者：zhourf on 2017/10/17
 * 邮箱：zhourf@glodon.com
 */

public class ChooseProjectModel implements ChooseProjectContract.Model {

    @Override
    public Observable<ProjectListBean> getAvailableProjects(int page, int size){
        String[] sort = {"createTime,desc"};
        return NetRequest.getInstance().getCall(ChooseTenantApi.class).getAvailableProjects(page,size,sort,new DaoProvider().getCookie());
    }

    /**
     * 获取当前已发布的最新版本
     */
    @Override
    public Observable<ProjectVersionBean> getLatestVersion(long projectId) {
        return NetRequest.getInstance().getCall(ModelApi.class).getLatestVersion(projectId, new DaoProvider().getCookie());
    }
}
