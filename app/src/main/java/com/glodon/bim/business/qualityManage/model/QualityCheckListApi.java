package com.glodon.bim.business.qualityManage.model;

import com.glodon.bim.business.qualityManage.bean.QualityCheckListBean;
import com.glodon.bim.common.login.User;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;

/**
 * 描述：质检清单接口
 * 作者：zhourf on 2017/9/11
 * 邮箱：zhourf@glodon.com
 */

public interface QualityCheckListApi {


//    /**
//     * oAuth2认证第一步请求
//     */
//    @FormUrlEncoded
//    @POST("uaa/login")
//    Call<ResponseBody> request1(@Field("username") String username, @Field("password") String password);
//
//    /**
//     * oAuth2认证第二步请求
//     */
//    @GET
//    Call<ResponseBody> request2(@Header("cookie") String cookie, @Url String pathname);
//
//    /**
//     * oAuth2认证第三步请求
//     * @param cookie  注意是步骤一二请求返回的cookie用;连接在一起
//     */
//    @GET
//    Call<ResponseBody> request3(@Header("cookie") String cookie, @Url String pathname);
//
//    /**
//     * oAuth2认证第四步请求
//     * @param cookie  注意是步骤一二请求返回的cookie用;连接在一起
//     */
//    @GET
//    Call<ResponseBody> request4(@Header("cookie") String cookie, @Url String pathname);

    /**
     * 获取质检清单
     */
    @GET("quality/{deptId}/qualityInspection")
    Observable<QualityCheckListBean> getQualityCheckList(@Path("deptId") long deptId, @Query("page") int page, @Query("size") int size,@Header("cookie") String cookie);
}
