package com.glodon.bim.business.qualityManage.model;

import com.glodon.bim.business.qualityManage.bean.CompanyItem;
import com.glodon.bim.business.qualityManage.bean.CreateCheckListParams;
import com.glodon.bim.business.qualityManage.bean.ImageUploadBean;
import com.glodon.bim.business.qualityManage.bean.ModuleListBean;
import com.glodon.bim.business.qualityManage.bean.PersonItem;
import com.glodon.bim.business.qualityManage.bean.SaveBean;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;

/**
 * 描述：新建检查单
 * 作者：zhourf on 2017/9/11
 * 邮箱：zhourf@glodon.com
 */

public interface CreateCheckListApi {

    /**
     * 获取施工单位列表
     */
    @GET("pmbasic/projects/{id}/supporters")
    Observable<List<CompanyItem>> getCompaniesList(@Path("id") long id, @Query("deptTypeEnums") List<String> deptTypeEnums, @Header("cookie") String cookie);

    /**
     * 查询施工单位的责任人
     */
    @GET("pmbasic/projects/{id}/coperationCorps/{coperationCorpId}/coperationRoles")
    Observable<List<PersonItem>> getPersonList(@Path("id") long id, @Path("coperationCorpId") long coperationCorpId, @Header("cookie") String cookie);

    /**
     * 获取质检项目列表
     */
    @GET("quality/templates")
    Observable<ModuleListBean> getModuleList(@Query("projectType") String projectType, @Query("page") int page, @Query("size") int size, @Header("cookie") String cookie);


    /**
     * 检查单 新增   提交
     */
    @POST("quality/{deptId}/qualityInspection/commit")
    Observable<SaveBean> createSubmit(@Path("deptId") long deptId, @Body CreateCheckListParams props, @Header("cookie") String cookie);


    /**
     * 检查单 新增   保存
     */
    @POST("quality/{deptId}/qualityInspection")
    Observable<SaveBean> createSave(@Path("deptId") long deptId, @Body CreateCheckListParams props, @Header("cookie") String cookie);

    /**
     * 检查单 编辑   提交
     */
    @PUT("quality/{deptId}/qualityInspection/{id}/commit")
    Observable<ResponseBody> editSubmit(@Path("deptId") long deptId, @Path("id") long id, @Body CreateCheckListParams props, @Header("cookie") String cookie);


    /**
     * 检查单 编辑   保存
     */
    @PUT("quality/{deptId}/qualityInspection/{id}")
    Observable<ResponseBody> editSave(@Path("deptId") long deptId, @Path("id") long id, @Body CreateCheckListParams props, @Header("cookie") String cookie);


    /**
     * 检查单 删除
     */
    @DELETE("quality/{deptId}/qualityInspection/{id}")
    Observable<ResponseBody> createDelete(@Path("deptId") long deptId, @Path("id") long id, @Header("cookie") String cookie);

    /**
     * 图片上传  获取operationCode
     */
    @GET("bimpm/attachment/operationCode")
    Observable<ResponseBody> getOperationCode(@Query("containerId") String containerId, @Query("name") String name, @Query("digest") String digest, @Query("length") long length, @Header("cookie") String cookie);

    @Multipart
    @POST("nss/v1/insecure/{operationCode}")
    Observable<ResponseBody> uploadImage(@Path("operationCode") String operationCode,@Part("description") RequestBody description,
                                            @Part MultipartBody.Part file);

    @Multipart
    @POST("nss/v1/insecure/{operationCode}")
    Observable<ResponseBody> uploadImage(@Path("operationCode") String operationCode,
                                         @Part() MultipartBody.Part file);
    @POST("nss/v1/insecure/{operationCode}")
    Observable<ResponseBody> uploadImage(@Path("operationCode") String operationCode,
                                         @Body MultipartBody multipartBody);

//    @Multipart
//    @POST("nss/v1/insecure/{operationCode}")
//    Call<ResponseBody> uploadImage2(@Path("operationCode") String operationCode,@Part MultipartBody.Part file);
@Multipart
@POST
Call<ResponseBody> uploadImage2(@Url String url, @Part MultipartBody.Part file);
}
