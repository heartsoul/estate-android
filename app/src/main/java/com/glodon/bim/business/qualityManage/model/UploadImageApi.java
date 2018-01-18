package com.glodon.bim.business.qualityManage.model;

import com.glodon.bim.business.qualityManage.bean.UploadImageBean;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * 描述：图片上传
 * 作者：zhourf on 2017/11/9
 * 邮箱：zhourf@glodon.com
 */

public interface UploadImageApi {
    /**
     * 图片上传  获取operationCode
     */
    @GET("bimpm/attachment/operationCode")
    Call<ResponseBody> getOperationCode(@Query("containerId") String containerId, @Query("facilityName") String name, @Query("digest") String digest, @Query("length") long length, @Header("cookie") String cookie);

    /**
     * 根据objectid 获取图片原地址
     */
    @GET("bimpm/attachment/attachment/url")
    Call<ResponseBody> getOriginalUrl(@Query("objectId") String objectId, @Query("thumbnail") boolean name, @Header("cookie") String cookie);

    @Multipart
    @POST
    Call<UploadImageBean> uploadImage2(@Url String url, @Part MultipartBody.Part file);
}
