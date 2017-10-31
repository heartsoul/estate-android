package com.glodon.bim.business.qualityManage.model;

import com.glodon.bim.basic.config.AppConfig;
import com.glodon.bim.basic.network.NetRequest;
import com.glodon.bim.business.greendao.provider.DaoProvider;
import com.glodon.bim.business.qualityManage.bean.CompanyItem;
import com.glodon.bim.business.qualityManage.bean.CreateCheckListParams;
import com.glodon.bim.business.qualityManage.bean.PersonItem;
import com.glodon.bim.business.qualityManage.contract.CreateCheckListContract;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Path;
import rx.Observable;

/**
 * 描述：新建检查单
 * 作者：zhourf on 2017/10/26
 * 邮箱：zhourf@glodon.com
 */

public class CreateCheckListModel implements CreateCheckListContract.Model {

    @Override
    public Observable<List<CompanyItem>> getCompaniesList(long id,  List<String> deptTypeEnums){
        return NetRequest.getInstance().getCall(AppConfig.BASE_URL,CreateCheckListApi.class).getCompaniesList(id,deptTypeEnums,new DaoProvider().getCookie());
    }
    @Override
    public Observable<List<PersonItem>> gePersonList(long id, long coperationCorpId){
        return NetRequest.getInstance().getCall(AppConfig.BASE_URL,CreateCheckListApi.class).getPersonList(id,coperationCorpId,new DaoProvider().getCookie());
    }
    @Override
    public Observable<ResponseBody> createSubmit(long deptId,CreateCheckListParams props){
//        return NetRequest.getInstance().getCall(AppConfig.BASE_URL,CreateCheckListApi.class).createSubmit(deptId,props,new DaoProvider().getCookie());
        return NetRequest.getInstance().getCall("http://192.168.72.48/",CreateCheckListApi.class).createSubmit(deptId,props,new DaoProvider().getCookie());
    }
    @Override
    public Observable<ResponseBody> createSave(long deptId,CreateCheckListParams props){
//        return NetRequest.getInstance().getCall(AppConfig.BASE_URL,CreateCheckListApi.class).createSubmit(deptId,props,new DaoProvider().getCookie());
        return NetRequest.getInstance().getCall("http://192.168.72.48/",CreateCheckListApi.class).createSave(deptId,props,new DaoProvider().getCookie());
    }
}
