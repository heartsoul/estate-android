package com.glodon.bim.business.main.model;

import com.glodon.bim.business.main.bean.ParamSetCurrentTenant;
import com.glodon.bim.business.main.bean.ProjectListBean;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PUT;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 描述：
 * 作者：zhourf on 2017/10/18
 * 邮箱：zhourf@glodon.com
 */

public interface ChooseTenantApi {

    /**
     * 设置当前选中的租户
     */
    @PUT("user/currentTenant")
    Observable<ResponseBody> currentTenant(@Body ParamSetCurrentTenant param, @Header("cookie") String cookie);

    @GET("pmbasic/projects/available")
    Observable<ProjectListBean> getAvailableProjects(@Query("page") int page, @Query("size") int size, @Query("sort") String[] sort, @Header("cookie") String cookie);


}
