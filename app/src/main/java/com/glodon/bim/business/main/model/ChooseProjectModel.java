package com.glodon.bim.business.main.model;

import com.glodon.bim.basic.config.AppConfig;
import com.glodon.bim.basic.network.NetRequest;
import com.glodon.bim.business.greendao.provider.DaoProvider;
import com.glodon.bim.business.main.bean.ParamGetProjectList;
import com.glodon.bim.business.main.bean.ProjectListBean;
import com.glodon.bim.business.main.contract.ChooseProjectContract;

import rx.Observable;

/**
 * 描述：选择项目列表
 * 作者：zhourf on 2017/10/17
 * 邮箱：zhourf@glodon.com
 */

public class ChooseProjectModel implements ChooseProjectContract.Model {

    @Override
    public Observable<ProjectListBean> getAvailableProjects(int page, int size){
        ParamGetProjectList param = new ParamGetProjectList();
        param.page = page;
        param.size = size;
        return NetRequest.getInstance().getCall(AppConfig.BASE_URL,ChooseTenantApi.class).getAvailableProjects(page,size,new DaoProvider().getCookie());
    }
}
