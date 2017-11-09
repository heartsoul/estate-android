package com.glodon.bim.business.authority;

import android.content.Intent;

import com.glodon.bim.base.BaseApplication;
import com.glodon.bim.basic.config.AppConfig;
import com.glodon.bim.basic.log.LogUtil;
import com.glodon.bim.basic.network.NetRequest;
import com.glodon.bim.basic.utils.SharedPreferencesUtil;
import com.glodon.bim.business.greendao.provider.DaoProvider;
import com.glodon.bim.common.config.AuthorityConfig;
import com.glodon.bim.common.config.CommonConfig;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 描述：权限操作
 * 作者：zhourf on 2017/11/8
 * 邮箱：zhourf@glodon.com
 */

public class AuthorityManager {
//    public static final String  Quality_Check = "Estate_Integration_Quality_Check";//质量检查记录
//    public static final String  Quality_Accept = "Estate_Integration_Quality_Accept";//质量验收记录
//    public static final String  Quality_Risk= "Estate_Integration_Quality_Risk";//质量隐患记录
//    public static final String  Quality_Facility= "Estate_Integration_Quality_Facility";//材料设备进场验收
//    public static final String  Quality_Rectification= "Estate_Integration_Quality_Rectification";//质量整改记录

    //监理  Unit  施工  grant
//    moduleCode=Estate_Integration_Quality_Check  actionRights= DeleteUnit  Print  BrowseGrant  BrowseAll  DeleteAll  ModifyUnit  DeleteGrant  BrowseUnit  Download  ModifyAll  ModifyGrant
//    moduleCode=Estate_Integration_Quality_Accept  actionRights= DeleteUnit  Print  BrowseGrant  BrowseAll  DeleteAll  ModifyUnit  DeleteGrant  BrowseUnit  Download  ModifyAll  ModifyGrant
//    moduleCode=Estate_Integration_Quality_Risk  actionRights= DeleteUnit  Print  BrowseGrant  BrowseAll  DeleteAll  ModifyUnit  DeleteGrant  BrowseUnit  Download  ModifyAll  ModifyGrant
//    moduleCode=Estate_Integration_Quality_Facility  actionRights= Print  BrowseAll  DeleteAll  Download  ModifyAll
//    moduleCode=Estate_Integration_Quality_Rectification  actionRights= DeleteUnit  Print  BrowseGrant  BrowseAll  DeleteAll  ModifyUnit  DeleteGrant  BrowseUnit  Download  ModifyAll  ModifyGrant


    public static final String appCode = "Estate";
    public static AuthorityBean Quality_Check_Bean ;//质量检查记录
    public static AuthorityBean Quality_Accept_Bean;//质量验收记录
    public static AuthorityBean Quality_Risk_Bean ;//质量隐患记录
    public static AuthorityBean Quality_Facility_Bean ;//材料设备进场验收
    public static AuthorityBean Quality_Rectification_Bean ;//质量整改记录

    /**
     * 是否显示新增检查单验收单按钮
     */
    public static boolean isShowCreateButton(){
        if(Quality_Check_Bean!=null && Quality_Check_Bean.actionRights!=null && Quality_Check_Bean.actionRights.size()>0 &&isHasModifyRight(Quality_Check_Bean.actionRights)){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 待提交 提交功能
     */
    public static boolean isQualityCheckSubmit(){
        if(Quality_Check_Bean!=null && Quality_Check_Bean.actionRights!=null && Quality_Check_Bean.actionRights.size()>0 &&(Quality_Check_Bean.actionRights.contains(AuthorityConfig.ModifyUnit) ||Quality_Check_Bean.actionRights.contains(AuthorityConfig.ModifyAll) )){
            return true;
        }else{
            return false;
        }
    }
    /**
     * 待提交 删除功能
     */
    public static boolean isQualityCheckDelete(){
        if(Quality_Check_Bean!=null && Quality_Check_Bean.actionRights!=null && Quality_Check_Bean.actionRights.size()>0 &&(Quality_Check_Bean.actionRights.contains(AuthorityConfig.DeleteUnit) ||Quality_Check_Bean.actionRights.contains(AuthorityConfig.DeleteAll) )){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 是否有新建整改单权限
     */
    public static boolean isCreateRepair(){
        if(Quality_Rectification_Bean!=null && Quality_Rectification_Bean.actionRights!=null && Quality_Rectification_Bean.actionRights.size()>0 &&Quality_Rectification_Bean.actionRights.contains(AuthorityConfig.ModifyGrant)){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 是否有新建复查单权限
     */
    public static boolean isCreateReview(){
        if(Quality_Risk_Bean!=null && Quality_Risk_Bean.actionRights!=null && Quality_Risk_Bean.actionRights.size()>0 &&(Quality_Risk_Bean.actionRights.contains(AuthorityConfig.ModifyUnit)||Quality_Risk_Bean.actionRights.contains(AuthorityConfig.ModifyAll))){
            return true;
        }else{
            return false;
        }
    }
    //是否有新增、编辑权限
    private static boolean isHasModifyRight(List<String> actionRights){
        if (actionRights.contains(AuthorityConfig.ModifyAll)) return true;
//        if (Quality_Check_Bean.actionRights.contains(AuthorityConfig.ModifyGrant)) return true;
        if (actionRights.contains(AuthorityConfig.ModifyUnit)) return true;
        return false;
    }


    //判断是不是我
    public static boolean isMe(long currentId){
        return SharedPreferencesUtil.getUserId() == currentId;
    }

    /**
     * 获取所有权限
     */
    public static void getAuthorities(){
        getAuthorityCheck();
        getAuthorityAccept();
        getAuthorityRisk();
        getAuthorityFacility();
        getAuthorityRectification();
    }
    //质量检查记录
    public static void getAuthorityCheck(){
        NetRequest.getInstance().getCall(AppConfig.BASE_URL,AuthorityApi.class).getAuthority(appCode, AuthorityConfig.Quality_Check, SharedPreferencesUtil.getProjectId(),new DaoProvider().getCookie())
                .enqueue(new Callback<AuthorityBean>() {
                    @Override
                    public void onResponse(Call<AuthorityBean> call, Response<AuthorityBean> response) {
                        Quality_Check_Bean = response.body();
                        LogUtil.e(response.body().toString());
                        Intent intent = new Intent();
                        intent.setAction(CommonConfig.ACTION_GET_AUTHORITY_CHECK);
                        BaseApplication.getInstance().sendBroadcast(intent);
                    }

                    @Override
                    public void onFailure(Call<AuthorityBean> call, Throwable t) {
                        LogUtil.e(t.getMessage());
                    }
                });
    }
    //质量验收记录
    public static void getAuthorityAccept(){
        NetRequest.getInstance().getCall(AppConfig.BASE_URL,AuthorityApi.class).getAuthority(appCode, AuthorityConfig.Quality_Accept, SharedPreferencesUtil.getProjectId(),new DaoProvider().getCookie())
                .enqueue(new Callback<AuthorityBean>() {
                    @Override
                    public void onResponse(Call<AuthorityBean> call, Response<AuthorityBean> response) {
                        Quality_Accept_Bean = response.body();
                        LogUtil.e(response.body().toString());
                    }

                    @Override
                    public void onFailure(Call<AuthorityBean> call, Throwable t) {
                        LogUtil.e(t.getMessage());
                    }
                });
    }
    //质量隐患记录
    public static void getAuthorityRisk(){
        NetRequest.getInstance().getCall(AppConfig.BASE_URL,AuthorityApi.class).getAuthority(appCode, AuthorityConfig.Quality_Risk, SharedPreferencesUtil.getProjectId(),new DaoProvider().getCookie())
                .enqueue(new Callback<AuthorityBean>() {
                    @Override
                    public void onResponse(Call<AuthorityBean> call, Response<AuthorityBean> response) {
                        Quality_Risk_Bean = response.body();
                        LogUtil.e(response.body().toString());
                    }

                    @Override
                    public void onFailure(Call<AuthorityBean> call, Throwable t) {
                        LogUtil.e(t.getMessage());
                    }
                });
    }
    //材料设备进场验收
    public static void getAuthorityFacility(){
        NetRequest.getInstance().getCall(AppConfig.BASE_URL,AuthorityApi.class).getAuthority(appCode, AuthorityConfig.Quality_Facility, SharedPreferencesUtil.getProjectId(),new DaoProvider().getCookie())
                .enqueue(new Callback<AuthorityBean>() {
                    @Override
                    public void onResponse(Call<AuthorityBean> call, Response<AuthorityBean> response) {
                        Quality_Facility_Bean = response.body();
                        LogUtil.e(response.body().toString());
                    }

                    @Override
                    public void onFailure(Call<AuthorityBean> call, Throwable t) {
                        LogUtil.e(t.getMessage());
                    }
                });
    }
    //质量整改记录
    public static void getAuthorityRectification(){
        NetRequest.getInstance().getCall(AppConfig.BASE_URL,AuthorityApi.class).getAuthority(appCode, AuthorityConfig.Quality_Rectification, SharedPreferencesUtil.getProjectId(),new DaoProvider().getCookie())
                .enqueue(new Callback<AuthorityBean>() {
                    @Override
                    public void onResponse(Call<AuthorityBean> call, Response<AuthorityBean> response) {
                        Quality_Rectification_Bean = response.body();
                        LogUtil.e(response.body().toString());
                    }

                    @Override
                    public void onFailure(Call<AuthorityBean> call, Throwable t) {
                        LogUtil.e(t.getMessage());
                    }
                });
    }


}
