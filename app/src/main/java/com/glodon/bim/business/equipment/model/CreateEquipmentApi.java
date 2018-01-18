package com.glodon.bim.business.equipment.model;

import com.glodon.bim.business.equipment.bean.CreateEquipmentBean;
import com.glodon.bim.business.equipment.bean.CreateEquipmentParams;
import com.glodon.bim.business.equipment.bean.EquipmentDetailBean;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import rx.Observable;

/**
 * 描述：创建材设记录
 * 作者：zhourf on 2018/1/18
 * 邮箱：zhourf@glodon.com
 */

public interface CreateEquipmentApi {

    /**
     * 新增保存
     */
    @POST("quality/{deptId}/facilityAcceptance")
    Observable<CreateEquipmentBean> newSave(@Path("deptId") long deptId, @Body CreateEquipmentParams props, @Header("cookie") String cookie);

    /**
     * 新增提交
     */
    @POST("quality/{deptId}/facilityAcceptance/commit")
    Observable<CreateEquipmentBean> newSubmit(@Path("deptId") long deptId, @Body CreateEquipmentParams props, @Header("cookie") String cookie);

    /**
     * 编辑保存
     */
    @PUT("quality/{deptId}/facilityAcceptance/{id}")
    Observable<ResponseBody> editSave(@Path("deptId") long deptId, @Path("id") long id, @Body CreateEquipmentParams props, @Header("cookie") String cookie);

    /**
     * 编辑提交
     */
    @PUT("quality/{deptId}/facilityAcceptance/{id}commit")
    Observable<ResponseBody> editSubmit(@Path("deptId") long deptId, @Path("id") long id, @Body CreateEquipmentParams props, @Header("cookie") String cookie);

    /**
     * 删除
     */
    @DELETE("quality/{deptId}/facilityAcceptance/{id}")
    Observable<ResponseBody> delete(@Path("deptId") long deptId, @Path("id") long id, @Header("cookie") String cookie);

    /**
     * 根据id查询详情和保存后的编辑信息
     */
    @GET("quality/{deptId}/facilityAcceptance/{id}")
    Observable<EquipmentDetailBean> detail(@Path("deptId") long deptId, @Path("id") long id, @Header("cookie") String cookie);


}
