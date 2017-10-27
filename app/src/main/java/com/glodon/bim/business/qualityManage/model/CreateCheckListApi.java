package com.glodon.bim.business.qualityManage.model;

import com.glodon.bim.business.qualityManage.bean.CompanyItem;
import com.glodon.bim.business.qualityManage.bean.CreateCheckListParams;
import com.glodon.bim.business.qualityManage.bean.ModuleListBean;
import com.glodon.bim.business.qualityManage.bean.PersonItem;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
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
    Observable<List<PersonItem>> getPersonList(@Path("id") long id, @Path("coperationCorpId") long coperationCorpId, @Header("cookie") String cookie);

    /**
     * 获取质检项目列表
     */
    @GET("quality/templates")
    Observable<ModuleListBean> getModuleList(@Query("projectType") String projectType, @Query("page") int  page, @Query("size") int size, @Header("cookie") String cookie);


    /**
     * 检查单 新增   提交
     */
    @GET("quality/{deptId}/qualityInspection/commit")
    Observable<ResponseBody> createSubmit(@Path("deptId") long deptId, @Body CreateCheckListParams props, @Header("cookie") String cookie);

}
