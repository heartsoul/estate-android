package com.glodon.bim.business.qualityManage.model;

import com.glodon.bim.business.qualityManage.bean.QualityGetRepairInfo;
import com.glodon.bim.business.qualityManage.bean.QualityGetReviewInfo;
import com.glodon.bim.business.qualityManage.bean.QualityRepairParams;
import com.glodon.bim.business.qualityManage.bean.QualityReviewParams;
import com.glodon.bim.business.qualityManage.bean.SaveBean;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 描述：新建复查单 整改单
 * 作者：zhourf on 2017/9/11
 * 邮箱：zhourf@glodon.com
 */

public interface CreateReviewApi {

    /**
     * 整改单  新增  保存
     */
    @POST("quality/{deptId}/qualityRectification")
    Observable<SaveBean> createSaveRepair(@Path("deptId") long deptId, @Body QualityRepairParams props,@Header("cookie") String cookie);
    /**
     * 整改单  新增  保存
     */
    @POST("quality/{deptId}/qualityRectification")
    Call<ResponseBody> createSaveRepair2(@Path("deptId") long deptId, @Body QualityRepairParams props, @Header("cookie") String cookie);

    /**
     * 整改单  编辑  保存
     */
    @PUT("quality/{deptId}/qualityRectification/{id}")
    Observable<ResponseBody> editSaveRepair(@Path("deptId") long deptId,@Path("id") long id, @Body QualityRepairParams props,@Header("cookie") String cookie);
    /**
     * 整改单  编辑  保存
     */
    @PUT("quality/{deptId}/qualityRectification/{id}")
    Call<ResponseBody> editSaveRepair2(@Path("deptId") long deptId,@Path("id") long id, @Body QualityRepairParams props,@Header("cookie") String cookie);


    /**
     * 整改单  新增  提交
     */
    @POST("quality/{deptId}/qualityRectification/commit")
    Observable<SaveBean> createSubmitRepair(@Path("deptId") long deptId, @Body QualityRepairParams props,@Header("cookie") String cookie);
    /**
     * 整改单  新增  提交
     */
    @POST("quality/{deptId}/qualityRectification/commit")
    Call<ResponseBody> createSubmitRepair2(@Path("deptId") long deptId, @Body QualityRepairParams props,@Header("cookie") String cookie);

    /**
     * 整改单  编辑  提交
     */
    @PUT("quality/{deptId}/qualityRectification/{id}/commit")
    Observable<ResponseBody> editSubmitRepair(@Path("deptId") long deptId,@Path("id") long id, @Body QualityRepairParams props,@Header("cookie") String cookie);
    /**
     * 整改单  编辑  提交
     */
    @PUT("quality/{deptId}/qualityRectification/{id}/commit")
    Call<ResponseBody> editSubmitRepair2(@Path("deptId") long deptId,@Path("id") long id, @Body QualityRepairParams props,@Header("cookie") String cookie);

    /**
     * 整改单  删除
     */
    @DELETE("quality/{deptId}/qualityRectification/{id}")
    Observable<ResponseBody> deleteRepair(@Path("deptId") long deptId, @Path("id") long id, @Header("cookie") String cookie);
    /**
     * 整改单  删除
     */
    @DELETE("quality/{deptId}/qualityRectification/{id}")
    Call<ResponseBody> deleteRepair2(@Path("deptId") long deptId, @Path("id") long id, @Header("cookie") String cookie);

    /**
     * 整改单  查询保存但未提交的整改单
     */
    @GET("quality/{deptId}/qualityRectification/staged")
    Observable<QualityGetRepairInfo> getRepairInfo(@Path("deptId") long deptId, @Query("inspectionId") long inspectionId, @Header("cookie") String cookie);
    /**
     * 整改单  查询保存但未提交的整改单
     */
    @GET("quality/{deptId}/qualityRectification/staged")
    Call<ResponseBody> getRepairInfo2(@Path("deptId") long deptId, @Query("inspectionId") long inspectionId, @Header("cookie") String cookie);


    /**
     * 复查单  新增  保存
     */
    @POST("quality/{deptId}/qualityReviews")
    Observable<SaveBean> createSaveReview(@Path("deptId") long deptId, @Body QualityReviewParams props, @Header("cookie") String cookie);
    /**
     * 复查单  新增  保存
     */
    @POST("quality/{deptId}/qualityReviews")
    Call<ResponseBody> createSaveReview2(@Path("deptId") long deptId, @Body QualityReviewParams props, @Header("cookie") String cookie);

    /**
     * 复查单  编辑  保存
     */
    @PUT("quality/{deptId}/qualityReviews/{id}")
    Observable<ResponseBody> editSaveReview(@Path("deptId") long deptId,@Path("id") long id, @Body QualityReviewParams props, @Header("cookie") String cookie);
    /**
     * 复查单  编辑  保存
     */
    @PUT("quality/{deptId}/qualityReviews/{id}")
    Call<ResponseBody> editSaveReview2(@Path("deptId") long deptId,@Path("id") long id, @Body QualityReviewParams props, @Header("cookie") String cookie);

    /**
     * 复查单  新增  提交
     */
    @POST("quality/{deptId}/qualityReviews/commit")
    Observable<SaveBean> createSubmitReview(@Path("deptId") long deptId, @Body QualityReviewParams props, @Header("cookie") String cookie);
    /**
     * 复查单  新增  提交
     */
    @POST("quality/{deptId}/qualityReviews/commit")
    Call<ResponseBody> createSubmitReview2(@Path("deptId") long deptId, @Body QualityReviewParams props, @Header("cookie") String cookie);

    /**
     * 复查单  编辑  提交
     */
    @PUT("quality/{deptId}/qualityReviews/{id}/commit")
    Observable<ResponseBody> editSubmitReview(@Path("deptId") long deptId,@Path("id") long id, @Body QualityReviewParams props, @Header("cookie") String cookie);
    /**
     * 复查单  编辑  提交
     */
    @PUT("quality/{deptId}/qualityReviews/{id}/commit")
    Call<ResponseBody> editSubmitReview2(@Path("deptId") long deptId,@Path("id") long id, @Body QualityReviewParams props, @Header("cookie") String cookie);


    /**
     * 复查单  查询保存后的复查单数据
     */
    @GET("quality/{deptId}/qualityReviews/staged")
    Observable<QualityGetReviewInfo> getReviewInfo(@Path("deptId") long deptId, @Query("inspectionId") long inspectionId,  @Header("cookie") String cookie);
    /**
     * 复查单  查询保存后的复查单数据
     */
    @GET("quality/{deptId}/qualityReviews/staged")
    Call<ResponseBody> getReviewInfo2(@Path("deptId") long deptId, @Query("inspectionId") long inspectionId,  @Header("cookie") String cookie);

    /**
     * 整改单  删除
     */
    @DELETE("quality/{deptId}/qualityReviews/{id}")
    Observable<ResponseBody> deleteReview(@Path("deptId") long deptId, @Path("id") long id, @Header("cookie") String cookie);
    /**
     * 整改单  删除
     */
    @DELETE("quality/{deptId}/qualityReviews/{id}")
    Call<ResponseBody> deleteReview2(@Path("deptId") long deptId, @Path("id") long id, @Header("cookie") String cookie);

}
