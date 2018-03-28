package com.glodon.bim.business.setting.model;

import com.glodon.bim.basic.network.NetRequest;
import com.glodon.bim.business.greendao.provider.DaoProvider;
import com.glodon.bim.business.setting.bean.CheckVersionBean;
import com.glodon.bim.business.setting.contract.SettingContract;

import rx.Observable;

/**
 * Created by cwj on 2018/3/27.
 * Description:SettingModel
 */

public class SettingModel implements SettingContract.Model {

    private String cookie;

    public SettingModel() {
        cookie = new DaoProvider().getCookie();
    }

    @Override
    public Observable<CheckVersionBean> checkVersion() {
        return NetRequest.getInstance().getCall(SettingApi.class).checkVersion("android", cookie);
    }
}
