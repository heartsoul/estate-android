package com.glodon.bim.business.qualityManage.model;

import com.glodon.bim.business.qualityManage.bean.CompanyItem;
import com.glodon.bim.business.qualityManage.bean.CreateCheckListParams;
import com.glodon.bim.business.qualityManage.bean.InspectionCompanyItem;
import com.glodon.bim.business.qualityManage.bean.ModelElementInfo;
import com.glodon.bim.business.qualityManage.bean.ModuleListBeanItem;
import com.glodon.bim.business.qualityManage.bean.ModuleStandardItem;
import com.glodon.bim.business.qualityManage.bean.PersonItem;
import com.glodon.bim.business.qualityManage.bean.SaveBean;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
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
 * 描述：新建检查单/验收单
 * 作者：zhourf on 2017/9/11
 * 邮箱：zhourf@glodon.com
 */

public interface CreateCheckListApi {

    /**
     * 获取施工单位列表
     */
    @GET("pmbasic/projects/{fileId}/supporters")
    Observable<List<CompanyItem>> getCompaniesList(@Path("fileId") long id, @Query("deptTypeEnums") List<String> deptTypeEnums, @Header("cookie") String cookie);

    /**
     * 查询施工单位的责任人
     */
    @GET("pmbasic/projects/{fileId}/coperationCorps/{coperationCorpId}/coperationRoles")
    Observable<List<PersonItem>> getPersonList(@Path("fileId") long id, @Path("coperationCorpId") long coperationCorpId, @Query("active") boolean active,@Header("cookie") String cookie);


    /**
     * 获取质检项目列表/{deptId}/checkpoints/templates
     */
    @GET("quality/{deptId}/quality/checkpoints")
    Observable<List<ModuleListBeanItem>> getModuleList(@Path("deptId") long deptId,@Header("cookie") String cookie);

//    /**
//     * 获取质检项目列表
//     */
//    @GET("quality/{deptId}/quality/checkpoints/summary")
//    Observable<List<ModuleListBeanItem>> getModuleList(@Path("deptId") long deptId, @Query("versionId")String versionId,@Header("cookie") String cookie);

    /**
     * 获取质检项目标准
     */
    @GET("quality/acceptanceStandard/templates/{templateId}/standards/items")
    Observable<List<ModuleStandardItem>> getModuleStandard(@Path("templateId") long templateId, @Header("cookie") String cookie);


    /**
     * 检查单 新增   提交
     */
    @POST("quality/{deptId}/qualityInspection/commit")
    Observable<SaveBean> createSubmitInspection(@Path("deptId") long deptId, @Body CreateCheckListParams props, @Header("cookie") String cookie);


    /**
     * 检查单 新增   保存
     */
    @POST("quality/{deptId}/qualityInspection")
    Observable<SaveBean> createSaveInspection(@Path("deptId") long deptId, @Body CreateCheckListParams props, @Header("cookie") String cookie);

    /**
     * 检查单 编辑   提交
     */
    @PUT("quality/{deptId}/qualityInspection/{fileId}/commit")
    Observable<ResponseBody> editSubmitInspection(@Path("deptId") long deptId, @Path("fileId") long id, @Body CreateCheckListParams props, @Header("cookie") String cookie);


    /**
     * 检查单 编辑   保存
     */
    @PUT("quality/{deptId}/qualityInspection/{fileId}")
    Observable<ResponseBody> editSaveInspection(@Path("deptId") long deptId, @Path("fileId") long id, @Body CreateCheckListParams props, @Header("cookie") String cookie);


    /**
     * 检查单 删除
     */
    @DELETE("quality/{deptId}/qualityInspection/{fileId}")
    Observable<ResponseBody> createDeleteInspection(@Path("deptId") long deptId, @Path("fileId") long id, @Header("cookie") String cookie);

    /**
     * 验收单 新增   提交
     */
    @POST("quality/{deptId}/qualityAcceptance/commit")
    Observable<SaveBean> createSubmitAcceptance(@Path("deptId") long deptId, @Body CreateCheckListParams props, @Header("cookie") String cookie);

    /**
     * 验收单 新增   保存
     */
    @POST("quality/{deptId}/qualityAcceptance")
    Observable<SaveBean> createSaveAcceptance(@Path("deptId") long deptId, @Body CreateCheckListParams props, @Header("cookie") String cookie);

    /**
     * 验收单 编辑   提交
     */
    @PUT("quality/{deptId}/qualityAcceptance/{fileId}/commit")
    Observable<ResponseBody> editSubmitAcceptance(@Path("deptId") long deptId, @Path("fileId") long id, @Body CreateCheckListParams props, @Header("cookie") String cookie);


    /**
     * 验收单 编辑   保存
     */
    @PUT("quality/{deptId}/qualityAcceptance/{fileId}")
    Observable<ResponseBody> editSaveAcceptance(@Path("deptId") long deptId, @Path("fileId") long id, @Body CreateCheckListParams props, @Header("cookie") String cookie);


    /**
     * 验收单 删除
     */
    @DELETE("quality/{deptId}/qualityAcceptance/{fileId}")
    Observable<ResponseBody> createDeleteAcceptance(@Path("deptId") long deptId, @Path("fileId") long id, @Header("cookie") String cookie);

    /**
     * 获取项目下检查单位列表
     */
    @GET("quality/{deptId}/qualityInspection/inspectionCompanys")
    Observable<List<InspectionCompanyItem>> getInspectionCompanies(@Path("deptId") long deptId, @Header("cookie") String cookie);

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
