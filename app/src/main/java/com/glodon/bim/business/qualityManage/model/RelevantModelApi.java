package com.glodon.bim.business.qualityManage.model;

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
    @GET("model/{projectId}/{projectVersionId}/bim/view/token")
    Call<ResponseBody> getToken2(@Path("projectId") long projectId, @Path("projectVersionId") String projectVersionId, @Query("fileId") String fileId, @Header("cookie") String cookie);

    //http://bimcop-pre-test.glodon.com/model/5200111/b18d2135c32f4a7abc88dd8a910b22e6/bim/view/token?t=1513157818403&integrateId=1236608133693664&projectId=5200111&versionId=b18d2135c32f4a7abc88dd8a910b22e6

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
}
