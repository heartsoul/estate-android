package com.glodon.bim.business.qualityManage.model;

import com.glodon.bim.business.qualityManage.bean.ModelListBean;
import com.glodon.bim.business.qualityManage.bean.ModelSingleListItem;
import com.glodon.bim.business.qualityManage.bean.ModelSpecialListItem;
import com.glodon.bim.business.qualityManage.bean.ProjectVersionBean;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 描述：模型
 * 作者：zhourf on 2017/9/11
 * 邮箱：zhourf@glodon.com
 */

public interface ModelApi {

    /**
     * 查询单体列表
     * @param id 项目id
     */
    @GET("pmbasic/projects/{fileId}/buildings")
    Observable<List<ModelSingleListItem>> getSingleList(@Path("fileId") long id,@Header("cookie") String cookie);

    /**
     * 查询专业列表
     */
    @GET("pmbasic/specialty")
    Observable<List<ModelSpecialListItem>> getSpecialList(@Header("cookie") String cookie);

    /**
     * 获取当前已发布的最新版本
     */
    @GET("model/{projectId}/projectVersion/latestVersion")
    Observable<ProjectVersionBean> getLatestVersion(@Path("projectId") long projectId,@Header("cookie") String cookie);

    /**
     * 根据专业和单体查询模型列表
     * @param projectId  项目id
     * @param projectVersionId  最新版本
     * @param buildingId  单体
     * @param specialtyCode  专业
     */
    @GET("model/{projectId}/{projectVersionId}/bimFiles")
    Observable<ModelListBean> getModelList(@Path("projectId") long projectId, @Path("projectVersionId") String projectVersionId, @Query("buildingId")long buildingId, @Query("specialtyCode")String specialtyCode, @Header("cookie") String cookie);

    /**
     * 根据专业和单体查询模型列表
     * @param projectId  项目id
     * @param projectVersionId  最新版本
     * @param specialtyCode  专业
     */
    @GET("model/{projectId}/{projectVersionId}/bimFiles")
    Observable<ModelListBean> getModelList(@Path("projectId") long projectId, @Path("projectVersionId") String projectVersionId,@Query("specialtyCode")String specialtyCode, @Header("cookie") String cookie);

    /**
     * 根据专业和单体查询模型列表
     * @param projectId  项目id
     * @param projectVersionId  最新版本
     * @param buildingId  单体
     */
    @GET("model/{projectId}/{projectVersionId}/bimFiles")
    Observable<ModelListBean> getModelList(@Path("projectId") long projectId, @Path("projectVersionId") String projectVersionId, @Query("buildingId")long buildingId, @Header("cookie") String cookie);

}
