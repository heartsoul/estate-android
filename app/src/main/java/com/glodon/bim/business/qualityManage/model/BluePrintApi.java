package com.glodon.bim.business.qualityManage.model;

import com.glodon.bim.business.qualityManage.bean.BluePrintBean;
import com.glodon.bim.business.qualityManage.bean.BlueprintListBeanItem;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 描述：图纸
 * 作者：zhourf on 2017/9/11
 * 邮箱：zhourf@glodon.com
 */

public interface BluePrintApi {


//    /**
//     * 获取图纸项目列表
//     */
//    @GET("quality/{deptId}/quality/checkpoints/project/{projectId}")
//    Observable<List<BlueprintListBeanItem>> getBluePrintList(@Path("deptId") long deptId, @Path("projectId") long projectId, @Header("cookie") String cookie);

    /**
     * 获取图纸项目列表
     */
    @GET("model/{projectId}/{projectVersionId}/bim/file/children")
    Observable<BluePrintBean> getBluePrint(
            @Path("projectId") long projectId,
            @Path("projectVersionId") String projectVersionId,
            @Query("fileId") String fileId,
            @Query("pageIndex") int pageIndex,
            @Header("cookie") String cookie);
    /**
     * 获取图纸项目列表
     */
    @GET("model/{projectId}/{projectVersionId}/bim/file/children")
    Observable<BluePrintBean> getBluePrint(
            @Path("projectId") long projectId,
            @Path("projectVersionId") String projectVersionId,
            @Query("pageIndex") int pageIndex,
            @Header("cookie") String cookie);

//    /**
//     * 获取图纸项目列表
//     */
//    @GET("doc/{deptId}/doc/file/children")
//    Observable<BluePrintBean> getBluePrint(
//            @Path("deptId") long deptId,
//            @Query("fileId") String fileId,
//            @Query("pageIndex") int pageIndex,
//            @Header("cookie") String cookie);
//    /**
//     * 获取图纸项目列表
//     */
//    @GET("doc/{deptId}/doc/file/children")
//    Observable<BluePrintBean> getBluePrint(
//            @Path("deptId") long deptId,
//            @Query("pageIndex") int pageIndex,
//            @Header("cookie") String cookie);

}
