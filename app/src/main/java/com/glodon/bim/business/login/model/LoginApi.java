package com.glodon.bim.business.login.model;

import com.glodon.bim.business.login.bean.OAuthParams;
import com.glodon.bim.business.login.bean.User;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * 描述：
 * 作者：zhourf on 2017/9/11
 * 邮箱：zhourf@glodon.com
 */

public interface LoginApi {

//    @FormUrlEncoded
//    @POST("uaa/login")
//    Call<ResponseBody> request1(@Field("username") String username, @Field("password") String password);
    @POST("uaa/login")
    Call<ResponseBody> request1();

    @FormUrlEncoded
    @POST("uaa/login")
    Call<ResponseBody> request1(@Field("username") String username, @Field("password") String password);

    @GET
    Call<ResponseBody> request2(@Header("cookie") String cookie,@Url String pathname);

    @GET
    Call<ResponseBody> request3(@Header("cookie") String cookie, @Url String pathname);
    @GET("uaa/oauth/authorize")
    Call<ResponseBody> request3(@Header("cookie") String cookie, @Query("client_id") String client_id, @Query("redirect_uri") String redirect_uri, @Query("response_type") String response_type, @Query("state") String state);

    @GET
    Call<ResponseBody> request4(@Url String pathname);

    @GET("uaa/user")
    Call<ResponseBody> getUserInfo(@Header("cookie") String cookie);
}
