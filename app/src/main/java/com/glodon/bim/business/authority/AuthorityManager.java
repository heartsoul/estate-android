package com.glodon.bim.business.authority;

import com.glodon.bim.basic.log.LogUtil;
import com.glodon.bim.basic.network.NetRequest;
import com.glodon.bim.basic.utils.SharedPreferencesUtil;
import com.glodon.bim.business.greendao.provider.DaoProvider;
import com.glodon.bim.business.main.listener.OnGetAuthorityListener;
import com.glodon.bim.common.config.AuthorityConfig;

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
//    moduleCode=Estate_Integration_Quality_Facility  actionRights= Print  BrowseSelf（浏览自己） BrowseAll（浏览所有）  DeleteAll（删除）  Download  ModifyAll(创建 修改)
//    moduleCode=Estate_Integration_Quality_Rectification  actionRights= DeleteUnit  Print  BrowseGrant  BrowseAll  DeleteAll  ModifyUnit  DeleteGrant  BrowseUnit  Download  ModifyAll  ModifyGrant


    public static final String appCode = "Estate";
    public static AuthorityBean Quality_Check_Bean ;//质量检查记录
    public static AuthorityBean Quality_Accept_Bean;//质量验收记录
    public static AuthorityBean Quality_Risk_Bean ;//质量隐患记录
    public static AuthorityBean Quality_Facility_Bean ;//材料设备进场验收
    public static AuthorityBean Quality_Rectification_Bean ;//质量整改记录


    //---------------------------质量--------------------------------------
    /**
     * 质量  是否显示新增检查单验收单按钮
     */
    public static boolean isShowCreateButton(){
        if(Quality_Check_Bean!=null && Quality_Check_Bean.actionRights!=null && Quality_Check_Bean.actionRights.size()>0 &&isHasModifyRight(Quality_Check_Bean.actionRights)){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 质量  待提交 提交功能
     */
    public static boolean isQualityCheckSubmit(){
        Quality_Check_Bean = SharedPreferencesUtil.getQualityCheckBean();
        if(Quality_Check_Bean!=null && Quality_Check_Bean.actionRights!=null && Quality_Check_Bean.actionRights.size()>0 &&(Quality_Check_Bean.actionRights.contains(AuthorityConfig.ModifyUnit) ||Quality_Check_Bean.actionRights.contains(AuthorityConfig.ModifyAll) )){
            return true;
        }else{
            return false;
        }
    }
    /**
     * 质量  待提交 删除功能
     */
    public static boolean isQualityCheckDelete(){
        Quality_Check_Bean = SharedPreferencesUtil.getQualityCheckBean();
        if(Quality_Check_Bean!=null && Quality_Check_Bean.actionRights!=null && Quality_Check_Bean.actionRights.size()>0 &&(Quality_Check_Bean.actionRights.contains(AuthorityConfig.DeleteUnit) ||Quality_Check_Bean.actionRights.contains(AuthorityConfig.DeleteAll) )){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 质量  是否有新建整改单权限
     */
    public static boolean isCreateRepair(){
        Quality_Rectification_Bean = SharedPreferencesUtil.getQualityRectificationBean();
        if(Quality_Rectification_Bean!=null && Quality_Rectification_Bean.actionRights!=null && Quality_Rectification_Bean.actionRights.size()>0 &&Quality_Rectification_Bean.actionRights.contains(AuthorityConfig.ModifyGrant)){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 质量  是否有新建复查单权限
     */
    public static boolean isCreateReview(){
        Quality_Risk_Bean = SharedPreferencesUtil.getQualityRiskBean();
        if(Quality_Risk_Bean!=null && Quality_Risk_Bean.actionRights!=null && Quality_Risk_Bean.actionRights.size()>0 &&(Quality_Risk_Bean.actionRights.contains(AuthorityConfig.ModifyUnit)||Quality_Risk_Bean.actionRights.contains(AuthorityConfig.ModifyAll))){
            return true;
        }else{
            return false;
        }
    }
    //质量   是否有新增、编辑权限
    private static boolean isHasModifyRight(List<String> actionRights){
        if (actionRights.contains(AuthorityConfig.ModifyAll)) return true;
//        if (Quality_Check_Bean.actionRights.contains(AuthorityConfig.ModifyGrant)) return true;
        if (actionRights.contains(AuthorityConfig.ModifyUnit)) return true;
        return false;
    }

    /**
     * 判断是否有浏览质量的权限
     */
    public static boolean isQualityBrowser(){
        Quality_Rectification_Bean = SharedPreferencesUtil.getQualityFacilityBean();
        return Quality_Rectification_Bean!=null &&
                Quality_Rectification_Bean.actionRights!=null &&
                Quality_Rectification_Bean.actionRights.size()>0 &&
                (
                        Quality_Rectification_Bean.actionRights.contains(AuthorityConfig.BrowseAll)||
                        Quality_Rectification_Bean.actionRights.contains(AuthorityConfig.BrowseSelf)||
                        Quality_Rectification_Bean.actionRights.contains(AuthorityConfig.BrowseGrant)
                );
    }

    //判断是不是我
    public static boolean isMe(long currentId){
        return SharedPreferencesUtil.getUserId() == currentId;
    }
    //---------------------------质量--------------------------------------


    //---------------------------材设--------------------------------------

    /**
     * 判断是否有删除材设的权限
     */
    public static boolean isEquipmentDelete(){
        Quality_Facility_Bean = SharedPreferencesUtil.getQualityFacilityBean();
        return Quality_Facility_Bean!=null &&
                Quality_Facility_Bean.actionRights!=null &&
                Quality_Facility_Bean.actionRights.size()>0 &&
                (Quality_Facility_Bean.actionRights.contains(AuthorityConfig.DeleteUnit) ||Quality_Facility_Bean.actionRights.contains(AuthorityConfig.DeleteAll) );
    }

    /**
     * 判断是否有创建和修改材设的权限
     */
    public static boolean isEquipmentModify(){
        Quality_Facility_Bean = SharedPreferencesUtil.getQualityFacilityBean();
        return Quality_Facility_Bean!=null &&
                Quality_Facility_Bean.actionRights!=null &&
                Quality_Facility_Bean.actionRights.size()>0 &&
                (Quality_Facility_Bean.actionRights.contains(AuthorityConfig.ModifyAll)||Quality_Facility_Bean.actionRights.contains(AuthorityConfig.ModifyUnit)||Quality_Facility_Bean.actionRights.contains(AuthorityConfig.ModifySelf) );
    }

    /**
     * 判断是否有浏览材设的权限
     */
    public static boolean isEquipmentBrowser(){
        Quality_Facility_Bean = SharedPreferencesUtil.getQualityFacilityBean();
        return Quality_Facility_Bean!=null &&
                Quality_Facility_Bean.actionRights!=null &&
                Quality_Facility_Bean.actionRights.size()>0 &&
                (
                        Quality_Facility_Bean.actionRights.contains(AuthorityConfig.BrowseAll)||
                        Quality_Facility_Bean.actionRights.contains(AuthorityConfig.BrowseSelf)||
                        Quality_Facility_Bean.actionRights.contains(AuthorityConfig.BrowseGrant)
                );
    }

    //---------------------------材设--------------------------------------

    private static int count = 0;
    /**
     * 获取所有权限
     */
    public static void getAuthorities(OnGetAuthorityListener listener){
        count=0;
        getAuthorityCheck(listener);
        getAuthorityAccept(listener);
        getAuthorityRisk(listener);
        getAuthorityFacility(listener);
        getAuthorityRectification(listener);
    }
    //质量检查记录
    public static void getAuthorityCheck(final OnGetAuthorityListener listener){
        NetRequest.getInstance().getCall(AuthorityApi.class).getAuthority(appCode, AuthorityConfig.Quality_Check, SharedPreferencesUtil.getProjectId(),new DaoProvider().getCookie())
                .enqueue(new Callback<AuthorityBean>() {
                    @Override
                    public void onResponse(Call<AuthorityBean> call, Response<AuthorityBean> response) {
                        Quality_Check_Bean = response.body();
                        LogUtil.e("质量检查记录="+Quality_Check_Bean.toString());
                        SharedPreferencesUtil.setQualityCheckBean(Quality_Check_Bean);
//                        Intent intent = new Intent();
//                        intent.setAction(CommonConfig.ACTION_GET_AUTHORITY_CHECK);
//                        BaseApplication.getInstance().sendBroadcast(intent);

                        count++;
                        if(count==5){
                            listener.finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<AuthorityBean> call, Throwable t) {
                        LogUtil.e(t.getMessage());
                        count++;
                        if(count==5){
                            listener.finish();
                        }
                    }
                });
    }
    //质量验收记录
    public static void getAuthorityAccept(final OnGetAuthorityListener listener){
        NetRequest.getInstance().getCall(AuthorityApi.class).getAuthority(appCode, AuthorityConfig.Quality_Accept, SharedPreferencesUtil.getProjectId(),new DaoProvider().getCookie())
                .enqueue(new Callback<AuthorityBean>() {
                    @Override
                    public void onResponse(Call<AuthorityBean> call, Response<AuthorityBean> response) {
                        Quality_Accept_Bean = response.body();
                        LogUtil.e("质量验收记录="+Quality_Accept_Bean.toString());
                        SharedPreferencesUtil.setQualityAcceptBean(Quality_Accept_Bean);
                        count++;
                        if(count==5){
                            listener.finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<AuthorityBean> call, Throwable t) {
                        LogUtil.e(t.getMessage());
                        count++;
                        if(count==5){
                            listener.finish();
                        }
                    }
                });
    }
    //质量隐患记录
    public static void getAuthorityRisk(final OnGetAuthorityListener listener){
        NetRequest.getInstance().getCall(AuthorityApi.class).getAuthority(appCode, AuthorityConfig.Quality_Risk, SharedPreferencesUtil.getProjectId(),new DaoProvider().getCookie())
                .enqueue(new Callback<AuthorityBean>() {
                    @Override
                    public void onResponse(Call<AuthorityBean> call, Response<AuthorityBean> response) {
                        Quality_Risk_Bean = response.body();
                        LogUtil.e("质量隐患记录="+Quality_Risk_Bean.toString());
                        SharedPreferencesUtil.setQualityRiskBean(Quality_Risk_Bean);
                        count++;
                        if(count==5){
                            listener.finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<AuthorityBean> call, Throwable t) {
                        LogUtil.e(t.getMessage());
                        count++;
                        if(count==5){
                            listener.finish();
                        }
                    }
                });
    }
    //材料设备进场验收
    public static void getAuthorityFacility(final OnGetAuthorityListener listener){
        NetRequest.getInstance().getCall(AuthorityApi.class).getAuthority(appCode, AuthorityConfig.Quality_Facility, SharedPreferencesUtil.getProjectId(),new DaoProvider().getCookie())
                .enqueue(new Callback<AuthorityBean>() {
                    @Override
                    public void onResponse(Call<AuthorityBean> call, Response<AuthorityBean> response) {
                        Quality_Facility_Bean = response.body();
                        LogUtil.e("材料设备进场验收="+Quality_Facility_Bean.toString());
                        SharedPreferencesUtil.setQualityFacilityBean(Quality_Facility_Bean);
                        count++;
                        if(count==5){
                            listener.finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<AuthorityBean> call, Throwable t) {
                        LogUtil.e(t.getMessage());
                        count++;
                        if(count==5){
                            listener.finish();
                        }
                    }
                });
    }
    //质量整改记录
    public static void getAuthorityRectification(final OnGetAuthorityListener listener){
        NetRequest.getInstance().getCall(AuthorityApi.class).getAuthority(appCode, AuthorityConfig.Quality_Rectification, SharedPreferencesUtil.getProjectId(),new DaoProvider().getCookie())
                .enqueue(new Callback<AuthorityBean>() {
                    @Override
                    public void onResponse(Call<AuthorityBean> call, Response<AuthorityBean> response) {
                        Quality_Rectification_Bean = response.body();
                        LogUtil.e("质量整改记录="+Quality_Rectification_Bean.toString());
                        SharedPreferencesUtil.setQualityRectificationBean(Quality_Rectification_Bean);
                        count++;
                        if(count==5){
                            listener.finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<AuthorityBean> call, Throwable t) {
                        LogUtil.e(t.getMessage());
                        count++;
                        if(count==5){
                            listener.finish();
                        }
                    }
                });
    }


}
