package com.glodon.bim.business.qualityManage.model;

import com.glodon.bim.business.qualityManage.bean.BluePrintDotItem;
import com.glodon.bim.business.qualityManage.bean.ProjectVersionBean;
import com.glodon.bim.business.qualityManage.bean.RelevantBluePrintToken;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 描述：具体图纸展示
 * 作者：zhourf on 2017/9/11
 * 邮箱：zhourf@glodon.com
 */

public interface RelevantBluePrintApi {


    /**
     * 获取当前已发布的最新版本
     */
    @GET("model/{projectId}/projectVersion/latestVersion")
    Observable<ProjectVersionBean> getLatestVersion(@Path("projectId") long projectId, @Header("cookie") String cookie);

    /**
     * 获取图纸的token
     */
    @GET("model/{projectId}/{projectVersionId}/bim/view/token")
    Observable<RelevantBluePrintToken> getToken(@Path("projectId") long projectId, @Path("projectVersionId") String projectVersionId, @Query("fileId") String fileId, @Header("cookie") String cookie);

    @GET("quality/{deptId}/qualityInspection/all/drawings/{drawingGdocFileId}/drawingPositions")
    Observable<List<BluePrintDotItem>> getBluePrintDots(@Path("deptId") long deptId, @Path("drawingGdocFileId") String drawingGdocFileId, @Header("cookie") String cookie);
    @GET("quality/{deptId}/qualityInspection/all/drawings/{drawingGdocFileId}/drawingPositions")
    Call<ResponseBody> getBluePrintDots2(@Path("deptId") long deptId,@Path("drawingGdocFileId") String drawingGdocFileId,@Header("cookie") String cookie);
}
