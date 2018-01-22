package com.glodon.bim.business.qualityManage.model;

import com.glodon.bim.business.qualityManage.bean.EquipmentHistoryItem;
import com.glodon.bim.business.qualityManage.bean.ModelElementHistory;
import com.glodon.bim.business.qualityManage.bean.ModelElementInfo;
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
 * 描述：具体模型展示
 * 作者：zhourf on 2017/9/11
 * 邮箱：zhourf@glodon.com
 */

public interface RelevantModelApi {


    /**
     * 获取当前已发布的最新版本
     */
    @GET("model/{projectId}/projectVersion/latestVersion")
    Observable<ProjectVersionBean> getLatestVersion(@Path("projectId") long projectId, @Header("cookie") String cookie);

    /**
     * 获取模型的token
     */
    @GET("model/{projectId}/{projectVersionId}/bim/view/token")
    Observable<RelevantBluePrintToken> getToken(@Path("projectId") long projectId, @Path("projectVersionId") String projectVersionId, @Query("fileId") String fileId, @Header("cookie") String cookie);

    /**
     * App端查询模型文件对应的所有关联构件
     */
    @GET("quality/{deptId}/qualityInspection/all/model/{gdocFileId}/elements")
    Observable<List<ModelElementHistory>> getElements(@Path("deptId") long deptId,@Path("gdocFileId")String gdocFileId, @Header("cookie") String cookie);

    /**
     * 根据构件id获取构件名称
     */
    @GET("model/{projectId}/{versionId}/model/data/element/property")
    Observable<ModelElementInfo> getElementProperty(@Path("projectId")long projectId,
                                                    @Path("versionId")String versionId,
                                                    @Query("fileId") String fileId,
                                                    @Query("elementId")String elementId,
                                                    @Header("cookie") String cookie);

    /**
     * 根据模型获取材设单据列表
     */
    @GET("quality/{deptId}/facilityAcceptance/model/{gdocFileId}/elements")
    Observable<List<EquipmentHistoryItem>> getEquipmentList(@Path("deptId")long deptId,
                                                            @Path("gdocFileId")String gdocFileId,
                                                            @Header("cookie") String cookie);
}
