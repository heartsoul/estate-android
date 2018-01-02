package com.glodon.bim.business.qualityManage.model;

import com.glodon.bim.business.qualityManage.bean.BluePrintModelSearchBean;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 描述：模型图纸搜索
 * 作者：zhourf on 2017/12/21
 * 邮箱：zhourf@glodon.com
 */

public interface BluePrintModelSearchApi {

    /**
     * 搜索模型或图纸
     * @param projectId  项目id
     * @param projectVersionId  项目版本
     * @param name  搜索关键字
     * @param suffix  模型rvt  图纸dwg
     * @return
     */
    @POST("model/{projectId}/{projectVersionId}/bim/file/query")
    Observable<BluePrintModelSearchBean> search(@Path("projectId")long projectId, @Path("projectVersionId")String projectVersionId, @Query("name") String name, @Query("suffix") String suffix, @Header("cookie")String cookie);

    /**
     * 搜索模型或图纸
     * @param projectId  项目id
     * @param projectVersionId  项目版本
     * @param name  搜索关键字
     * @param suffix  模型rvt  图纸dwg
     * @return
     */
    @POST("model/{projectId}/{projectVersionId}/bim/file/query")
    Call<ResponseBody> search2(@Path("projectId")long projectId, @Path("projectVersionId")String projectVersionId, @Query("name") String name, @Query("suffix") String suffix, @Header("cookie")String cookie);

}
