package com.glodon.bim.business.main.model;

import com.glodon.bim.business.equipment.bean.EquipmentListBean;
import com.glodon.bim.business.main.bean.ProjectListBean;
import com.glodon.bim.business.qualityManage.bean.QualityCheckListBean;

import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by cwj on 2018/3/21.
 * Description:QualityEquipmentSearchApi
 */

public interface QualityEquipmentSearchApi {

    @GET("/quality/{deptId}/qualityInspection/fuzzyMatchResults")
    Observable<QualityCheckListBean> searchQualityData(
            @Path("deptId") long deptId,
            @Query("keywords") String keywords,
            @Query("page") int page,
            @Query("size") int size,
            @Header("cookie") String cookie);

    @GET("/quality/{deptId}/facilityAcceptance/fuzzyMatchResults")
    Observable<EquipmentListBean> searchEquipmentData(
            @Path("deptId") long deptId,
            @Query("keywords") String keywords,
            @Query("page") int page,
            @Query("size") int size,
            @Header("cookie") String cookie);
}
