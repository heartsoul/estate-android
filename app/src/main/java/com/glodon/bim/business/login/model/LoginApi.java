package com.glodon.bim.business.login.model;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * 描述：
 * 作者：zhourf on 2017/9/11
 * 邮箱：zhourf@glodon.com
 */

public interface LoginApi {

    @FormUrlEncoded
    @POST("uaa/login")
    Call<ResponseBody> request1(@Field("username") String username, @Field("password") String password);

    @GET
    Call<ResponseBody> request2(@Header("cookie") String cookie, @Url String pathname);

    @GET
    Call<ResponseBody> request3(@Header("cookie") String cookie, @Url String pathname);

    @GET
    Call<ResponseBody> request4(@Header("cookie") String cookie, @Url String pathname);

    @GET("uaa/user")
    Call<ResponseBody> getUserInfo(@Header("cookie") String cookie);
}
