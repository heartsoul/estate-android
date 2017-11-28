package com.glodon.bim.business.qualityManage.model;

import com.glodon.bim.business.qualityManage.bean.ClassifyNum;
import com.glodon.bim.business.qualityManage.bean.QualityCheckListBean;
import com.glodon.bim.business.qualityManage.bean.QualityCheckListDetailBean;
import com.glodon.bim.common.login.User;

import java.util.List;

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



    /**
     * 获取质检清单
     */
    @GET("quality/{deptId}/qualityInspection/all")
    Observable<QualityCheckListBean> getQualityCheckList(@Path("deptId") long deptId, @Query("qcState") String qcState, @Query("page") int page, @Query("size") int size, @Query("sort") String[] sort, @Header("cookie") String cookie);

    /**
     * 根据质检项目 获取质检清单
     */
    @GET("quality/{deptId}/qualityInspection/all")
    Observable<QualityCheckListBean> getQualityCheckList(@Path("deptId") long deptId, @Query("qcState") String qcState, @Query("page") int page, @Query("size") int size, @Query("sort") String[] sort,@Query("qualityCheckpointId")long qualityCheckpointId,@Query("qualityCheckpointName")String qualityCheckpointName, @Header("cookie") String cookie);

    /**
     * 获取检查单详情
     */
    @GET("quality/{deptId}/qualityInspection/{id}/detail")
    Observable<QualityCheckListDetailBean> getQualityCheckListDetail(@Path("deptId") long deptId, @Path("id") long id, @Header("cookie") String cookie);

    /**
     * 获取检查单详情
     */
    @GET("quality/{deptId}/qualityInspection/all/qcState/summary")
    Observable<List<ClassifyNum>> getStatusNum(@Path("deptId") long deptId, @Header("cookie") String cookie);

    /**
     * 获取检查单详情  根据质检项目
     */
    @GET("quality/{deptId}/qualityInspection/all/qcState/summary")
    Observable<List<ClassifyNum>> getStatusNum(@Path("deptId") long deptId,@Query("qualityCheckpointId")long qualityCheckpointId,@Query("qualityCheckpointName")String qualityCheckpointName, @Header("cookie") String cookie);


}
