package com.glodon.bim.business.setting.model;

import com.glodon.bim.business.setting.bean.CheckVersionBean;

import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by cwj on 2018/3/27.
 * Description:SettingApi
 */

public interface SettingApi {

    /**
     * 检测版本
     *
     * @param sysName 系统
     * @return
     */
    @GET("/basis/sysinfo")
    Observable<CheckVersionBean> checkVersion(@Query("name") String sysName, @Header("cookie") String cookie);

}
