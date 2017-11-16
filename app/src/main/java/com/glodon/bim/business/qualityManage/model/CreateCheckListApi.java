package com.glodon.bim.business.qualityManage.model;

import com.glodon.bim.business.qualityManage.bean.CompanyItem;
import com.glodon.bim.business.qualityManage.bean.CreateCheckListParams;
import com.glodon.bim.business.qualityManage.bean.ModuleListBeanItem;
import com.glodon.bim.business.qualityManage.bean.PersonItem;
import com.glodon.bim.business.qualityManage.bean.SaveBean;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 描述：新建检查单
 * 作者：zhourf on 2017/9/11
 * 邮箱：zhourf@glodon.com
 */

public interface CreateCheckListApi {

    /**
     * 获取施工单位列表
     */
    @GET("pmbasic/projects/{id}/supporters")
    Observable<List<CompanyItem>> getCompaniesList(@Path("id") long id, @Query("deptTypeEnums") List<String> deptTypeEnums, @Header("cookie") String cookie);

    /**
     * 查询施工单位的责任人
     */
    @GET("pmbasic/projects/{id}/coperationCorps/{coperationCorpId}/coperationRoles")
    Observable<List<PersonItem>> getPersonList(@Path("id") long id, @Path("coperationCorpId") long coperationCorpId, @Query("active") boolean active,@Header("cookie") String cookie);


    /**
     * 获取质检项目列表
     */
    @GET("quality/{deptId}/quality/checkpoints/project/{projectId}")
    Observable<List<ModuleListBeanItem>> getModuleList(@Path("deptId") long deptId,@Path("projectId") long projectId,@Header("cookie") String cookie);


    /**
     * 检查单 新增   提交
     */
    @POST("quality/{deptId}/qualityInspection/commit")
    Observable<SaveBean> createSubmit(@Path("deptId") long deptId, @Body CreateCheckListParams props, @Header("cookie") String cookie);


    /**
     * 检查单 新增   保存
     */
    @POST("quality/{deptId}/qualityInspection")
    Observable<SaveBean> createSave(@Path("deptId") long deptId, @Body CreateCheckListParams props, @Header("cookie") String cookie);

    /**
     * 检查单 编辑   提交
     */
    @PUT("quality/{deptId}/qualityInspection/{id}/commit")
    Observable<ResponseBody> editSubmit(@Path("deptId") long deptId, @Path("id") long id, @Body CreateCheckListParams props, @Header("cookie") String cookie);


    /**
     * 检查单 编辑   保存
     */
    @PUT("quality/{deptId}/qualityInspection/{id}")
    Observable<ResponseBody> editSave(@Path("deptId") long deptId, @Path("id") long id, @Body CreateCheckListParams props, @Header("cookie") String cookie);


    /**
     * 检查单 删除
     */
    @DELETE("quality/{deptId}/qualityInspection/{id}")
    Observable<ResponseBody> createDelete(@Path("deptId") long deptId, @Path("id") long id, @Header("cookie") String cookie);


}
