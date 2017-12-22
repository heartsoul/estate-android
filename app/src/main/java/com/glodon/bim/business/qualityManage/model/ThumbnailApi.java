package com.glodon.bim.business.qualityManage.model;

import com.glodon.bim.business.qualityManage.bean.BluePrintBean;
import com.glodon.bim.business.qualityManage.bean.ThumbnailBean;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 描述：缩略图
 * 作者：zhourf on 2017/9/11
 * 邮箱：zhourf@glodon.com
 */

public interface ThumbnailApi {

    /**
     * 获取图纸项目列表
     */
    @GET("model/{projectId}/{projectVersion}/viewing/files/{fileId}/thumbnailUrl")
    Call<ThumbnailBean> getBluePrint(
            @Path("projectId") long projectId,
            @Path("projectVersion") String projectVersion,
            @Path("fileId") String fileId,
            @Header("cookie") String cookie);

}
