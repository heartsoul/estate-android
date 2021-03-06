package com.glodon.bim.business.main.model;

import com.glodon.bim.basic.network.NetRequest;
import com.glodon.bim.business.greendao.provider.DaoProvider;
import com.glodon.bim.business.main.contract.MainContract;
import com.glodon.bim.business.setting.bean.CheckVersionBean;
import com.glodon.bim.business.setting.model.SettingApi;

import rx.Observable;

/**
 * 描述：主界面
 * 作者：zhourf on 2018/2/5
 * 邮箱：zhourf@glodon.com
 */

public class MainModel implements MainContract.Model {

    private String cookie;

    public MainModel() {
        cookie = new DaoProvider().getCookie();
    }


    @Override
    public Observable<CheckVersionBean> checkVersion() {
        return NetRequest.getInstance().getCall(SettingApi.class).checkVersion("android", cookie);
    }
}
