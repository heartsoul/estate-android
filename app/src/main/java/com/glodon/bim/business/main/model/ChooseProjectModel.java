package com.glodon.bim.business.main.model;

import com.glodon.bim.basic.config.AppConfig;
import com.glodon.bim.basic.log.LogUtil;
import com.glodon.bim.basic.network.NetRequest;
import com.glodon.bim.business.greendao.provider.DaoProvider;
import com.glodon.bim.business.main.bean.ProjectListBean;
import com.glodon.bim.business.main.contract.ChooseProjectContract;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
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
        return NetRequest.getInstance().getCall(AppConfig.BASE_URL,ChooseTenantApi.class).getAvailableProjects(page,size,sort,new DaoProvider().getCookie());
    }
}
