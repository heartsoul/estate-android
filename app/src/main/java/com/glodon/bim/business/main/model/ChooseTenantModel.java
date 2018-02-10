package com.glodon.bim.business.main.model;

import com.glodon.bim.basic.network.NetRequest;
import com.glodon.bim.business.greendao.provider.DaoProvider;
import com.glodon.bim.business.main.bean.ParamSetCurrentTenant;
import com.glodon.bim.business.main.contract.ChooseTenantContract;

import okhttp3.ResponseBody;
import rx.Observable;

/**
 * 描述：选择租户列表
 * 作者：zhourf on 2017/10/17
 * 邮箱：zhourf@glodon.com
 */

public class ChooseTenantModel implements ChooseTenantContract.Model {

    @Override
    public Observable<ResponseBody> setCurrentTenant(long tenantId){
        ParamSetCurrentTenant param = new ParamSetCurrentTenant();
        param.tenantId = tenantId;
        return NetRequest.getInstance().getCall(ChooseTenantApi.class).currentTenant(param,new DaoProvider().getCookie());
//        ChooseTenantApi obj = NetRequest.getInstance().getCall(AppConfig.BASE_URL,ChooseTenantApi.class);
//        NetRequest.getInstance().getResponse(obj.currentTenant(param, new DaoProvider().getCookie()), new NetRequestCallback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                LogUtil.e("---------------");
//                LogUtil.e(response.toString());
//
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                LogUtil.e(t.getMessage());
//            }
//        });
//        return null;
    }
}
