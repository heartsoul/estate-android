package com.glodon.bim.business.authority;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * 描述：权限操作
 * 作者：zhourf on 2017/11/8
 * 邮箱：zhourf@glodon.com
 */

public interface AuthorityApi {

    @GET("admin/auths/{appCode}/moduleRights/{moduleCode}")
    Call<AuthorityBean> getAuthority(@Path("appCode") String appCode, @Path("moduleCode") String moduleCode, @Query("deptId") long deptId, @Header("cookie") String cookie);
}
