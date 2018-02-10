package com.glodon.bim.business.equipment.model;

import com.glodon.bim.business.equipment.bean.EquipmentListBean;
import com.glodon.bim.business.equipment.bean.EquipmentNumBean;

import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 描述：材设记录列表
 * 作者：zhourf on 2018/1/18
 * 邮箱：zhourf@glodon.com
 */

public interface EquipmentListApi {


    /**
     * 根据id查询详情和保存后的编辑信息
     * 全部
     */
    @GET("quality/{deptId}/facilityAcceptance")
    Observable<EquipmentListBean> getEquipmentList(@Path("deptId") long deptId,
                                                   @Query("page") int page,
                                                   @Query("size") int size,
                                                   @Query("sort") String[] sort,
                                                   @Header("cookie") String cookie);

    /**
     * 根据id查询详情和保存后的编辑信息
     * 待提交
     */
    @GET("quality/{deptId}/facilityAcceptance")
    Observable<EquipmentListBean> getToCommitEquipmentList(@Path("deptId") long deptId,
                                                   @Query("page") int page,
                                                   @Query("size") int size,
                                                   @Query("committed") boolean committed,
                                                   @Query("sort") String[] sort,
                                                   @Header("cookie") String cookie);
    /**
     * 根据id查询详情和保存后的编辑信息
     * 合格不合格
     */
    @GET("quality/{deptId}/facilityAcceptance")
    Observable<EquipmentListBean> getqualifyEquipmentList(@Path("deptId") long deptId,
                                                           @Query("page") int page,
                                                           @Query("size") int size,
                                                          @Query("committed") boolean committed,
                                                           @Query("qualified") boolean qualified,
                                                           @Query("sort") String[] sort,
                                                           @Header("cookie") String cookie);

    /**
     * 根据id查询详情和保存后的编辑信息
     * 合格不合格
     */
    @GET("quality/{deptId}/facilityAcceptance/state/summary")
    Observable<EquipmentNumBean> getEquipmentListNum(@Path("deptId") long deptId,
                                                     @Header("cookie") String cookie);
}
