
package com.glodon.bim.basic.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

import com.glodon.bim.base.BaseApplication;
import com.glodon.bim.basic.log.LogUtil;
import com.glodon.bim.business.authority.AuthorityBean;
import com.glodon.bim.business.main.bean.ProjectListItem;
import com.glodon.bim.common.config.CommonConfig;
import com.glodon.bim.common.login.User;
import com.glodon.bim.common.login.UserTenant;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：SharedPreference工具类
 * 作者：zhourf on 2017/10/24
 * 邮箱：zhourf@glodon.com
 */
public class SharedPreferencesUtil {
    /**
     * 文件名
     */
    private static final String NAME = "bim_data";
    //项目信息
    private static final String PROJECT_NAME = "project_name";
    private static final String PROJECT_ID = "project_id";
    private static final String PROJECT_TYPE_CODE = "project_type_code";
    private static final String PROJECT_CREATE_TYPE = "project_create_type";
    //用户信息
    private static final String USER_ID = "userId";
    private static final String MODULE_INFO_NAME = "MODULE_INFO_NAME";
    private static final String MODULE_INFO_ID = "MODULE_INFO_ID";
    private static final String USER_NAME = "user_name";
    private static final String PROJECT_INFO = "project_info";
    private static final String TENANT_INFO = "tenant_info";
    private static final String USER_INFO = "user_info";

    //权限
    public static String QUALITY_CHECK_BEAN = "QUALITY_CHECK_BEAN";//质量检查记录
    public static String QUALITY_ACCEPT_BEAN= "QUALITY_ACCEPT_BEAN";//质量验收记录
    public static String QUALITY_RISK_BEAN = "QUALITY_RISK_BEAN";//质量隐患记录
    public static String QUALITY_FACILITY_BEAN = "QUALITY_FACILITY_BEAN";//材料设备进场验收
    public static String QUALITY_RECTIFICATION_BEAN = "QUALITY_RECTIFICATION_BEAN";//质量整改记录
    //搜索
    private static final String SEARCH_KEY_BLUEPRINT = "SEARCH_KEY_BLUEPRINT";//图纸搜索历史
    private static final String SEARCH_KEY_MODEL = "SEARCH_KEY_MODEL";//模型搜索历史

    /**
     * 保存字符串
     */
    public static void setString(String key,String value){
        SharedPreferences preferences= BaseApplication.getInstance().getSharedPreferences(NAME,Context.MODE_PRIVATE);
        Editor editor=preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    /**
     * 获取字符串
     */
    public static String getString(String key,String defaultValue){
        SharedPreferences preferences=BaseApplication.getInstance().getSharedPreferences(NAME,Context.MODE_PRIVATE);
        return preferences.getString(key,defaultValue);
    }

    /**
     * 选择的项目信息
     */
    public static void setProjectInfo(ProjectListItem item){
        if(item!=null) {
            SharedPreferences preferences = BaseApplication.getInstance().getSharedPreferences(NAME, Context.MODE_PRIVATE);
            Editor editor = preferences.edit();
            editor.putString(PROJECT_INFO,new GsonBuilder().create().toJson(item));
            editor.putString(PROJECT_NAME, item.name);
            editor.putLong(PROJECT_ID, item.deptId);
            editor.putString(PROJECT_TYPE_CODE, item.projectTypeCode);
            editor.commit();
        }
    }

    /**
     * 保存选择的租户信息
     */
    public static void setTenantInfo(UserTenant item){
        if(item!=null) {
            SharedPreferences preferences = BaseApplication.getInstance().getSharedPreferences(NAME, Context.MODE_PRIVATE);
            Editor editor = preferences.edit();
            editor.putString(TENANT_INFO,new GsonBuilder().create().toJson(item));
            editor.commit();
        }
    }

    /**
     * 保存用户信息
     */
    public static void setUserInfo(User item){
        if(item!=null) {
            SharedPreferences preferences = BaseApplication.getInstance().getSharedPreferences(NAME, Context.MODE_PRIVATE);
            Editor editor = preferences.edit();
            editor.putString(USER_INFO,new GsonBuilder().create().toJson(item));
            editor.commit();
        }
    }
    /**
     * 获取用户信息
     */
    public static User getUserInfo(){
        SharedPreferences preferences = BaseApplication.getInstance().getSharedPreferences(NAME, Context.MODE_PRIVATE);
        String json = preferences.getString(USER_INFO,"");
        if(!TextUtils.isEmpty(json)){
            User user = new GsonBuilder().create().fromJson(json,User.class);
            if(user!=null){
                return user;
            }
        }
        return null;
    }

    /**
     * 删除用户信息
     */
    public static void deleteUserInfo(){
        SharedPreferences preferences = BaseApplication.getInstance().getSharedPreferences(NAME, Context.MODE_PRIVATE);
        Editor editor = preferences.edit();
        editor.putString(USER_INFO,"");
        editor.commit();
    }

    /**
     * 清楚登录信息
     */
    public static void clear(){
        deleteTenantInfo();
        deleteProjectInfo();
        deleteUserInfo();
        setString(CommonConfig.USERNAME,"");
        setString(CommonConfig.PASSWORD,"");
    }

    /**
     * 获取保存的租户信息
     */
    public static UserTenant getTenantInfo(){
        SharedPreferences preferences = BaseApplication.getInstance().getSharedPreferences(NAME, Context.MODE_PRIVATE);
        String json = preferences.getString(TENANT_INFO,"");
        if(!TextUtils.isEmpty(json)){
            UserTenant tenant = new GsonBuilder().create().fromJson(json,UserTenant.class);
            if(tenant!=null){
                return tenant;
            }
        }
        return null;
    }

    /**
     * 获取保存的项目信息
     */
    public static ProjectListItem getProjectInfo(){
        SharedPreferences preferences = BaseApplication.getInstance().getSharedPreferences(NAME, Context.MODE_PRIVATE);
        String json = preferences.getString(PROJECT_INFO,"");
        if(!TextUtils.isEmpty(json)){
            ProjectListItem project = new GsonBuilder().create().fromJson(json,ProjectListItem.class);
            if(project!=null){
                return project;
            }
        }

        return null;
    }

    /**
     * 删除保存的项目信息
     */
    public static void deleteProjectInfo(){
        SharedPreferences preferences = BaseApplication.getInstance().getSharedPreferences(NAME, Context.MODE_PRIVATE);
        Editor editor = preferences.edit();
        editor.putString(PROJECT_INFO,"");
        editor.commit();
    }

    /**
     * 删除保存的租户信息
     */
    public static void deleteTenantInfo(){
        SharedPreferences preferences = BaseApplication.getInstance().getSharedPreferences(NAME, Context.MODE_PRIVATE);
        Editor editor = preferences.edit();
        editor.putString(TENANT_INFO,"");
        editor.commit();
    }

    /**
     * 获取项目id
     */
    public static long getProjectId(){
        SharedPreferences preferences= BaseApplication.getInstance().getSharedPreferences(NAME,Context.MODE_PRIVATE);
        return preferences.getLong(PROJECT_ID,-1);
    }

    /**
     * 获取项目版本id
     */
    public static String getProjectVersionId(long projectId){
        SharedPreferences preferences= BaseApplication.getInstance().getSharedPreferences(NAME,Context.MODE_PRIVATE);
        return preferences.getString(projectId+"","");
    }
    /**
     * 获取项目版本id
     */
    public static String getProjectVersionId(){
        SharedPreferences preferences= BaseApplication.getInstance().getSharedPreferences(NAME,Context.MODE_PRIVATE);
        return preferences.getString(getProjectId()+"","");
    }
    /**
     * 获取项目名称
     */
    public static String getProjectName(){
        SharedPreferences preferences= BaseApplication.getInstance().getSharedPreferences(NAME,Context.MODE_PRIVATE);
        return preferences.getString(PROJECT_NAME,"");
    }

    /**
     * 获取项目类型
     */
    public static String getProjectTypeCode(){
        SharedPreferences preferences= BaseApplication.getInstance().getSharedPreferences(NAME,Context.MODE_PRIVATE);
        return preferences.getString(PROJECT_TYPE_CODE,"");
    }

    /**
     * 设置当前创建的是什么类型   0 检查单  1整改单   2复查单
     */
    public static void setCreateType(String type){
        SharedPreferences preferences= BaseApplication.getInstance().getSharedPreferences(NAME,Context.MODE_PRIVATE);
        Editor editor=preferences.edit();
        editor.putString(PROJECT_CREATE_TYPE, type);
        editor.commit();
    }

    public static String getCreateType(){
        SharedPreferences preferences= BaseApplication.getInstance().getSharedPreferences(NAME,Context.MODE_PRIVATE);
        return preferences.getString(PROJECT_CREATE_TYPE,"0");
    }

    //获取当前的用户id
    public static long  getUserId() {
        SharedPreferences preferences= BaseApplication.getInstance().getSharedPreferences(NAME,Context.MODE_PRIVATE);
        return preferences.getLong(USER_ID,0);
    }

    //获取当前的用户名
    public static String  getUserName() {
        SharedPreferences preferences= BaseApplication.getInstance().getSharedPreferences(NAME,Context.MODE_PRIVATE);
        return preferences.getString(USER_NAME,"");
    }

    //保存当前的用户id
    public static void setUserId(long id) {
        SharedPreferences preferences= BaseApplication.getInstance().getSharedPreferences(NAME,Context.MODE_PRIVATE);
        Editor editor=preferences.edit();
        editor.putLong(USER_ID, id);
        editor.commit();
    }

    //保存当前的用户名
    public static void setUserName(String username) {
        SharedPreferences preferences= BaseApplication.getInstance().getSharedPreferences(NAME,Context.MODE_PRIVATE);
        Editor editor=preferences.edit();
        editor.putString(USER_NAME,username);
        editor.commit();
    }


    //保存质检记录权限
    public static void setQualityCheckBean(AuthorityBean bean){
        setAuthority(QUALITY_CHECK_BEAN,bean);
    }
    //获取质检记录权限
    public static AuthorityBean getQualityCheckBean(){
        return getAuthority(QUALITY_CHECK_BEAN);
    }


    //保存质量验收记录权限
    public static void setQualityAcceptBean(AuthorityBean bean){
        setAuthority(QUALITY_ACCEPT_BEAN,bean);
    }
    //获取质量验收记录权限
    public static AuthorityBean getQualityAcceptBean(){
        return getAuthority(QUALITY_ACCEPT_BEAN);
    }


    //保存质量隐患记录权限
    public static void setQualityRiskBean(AuthorityBean bean){
        setAuthority(QUALITY_RISK_BEAN,bean);
    }
    //获取质量隐患记录权限
    public static AuthorityBean getQualityRiskBean(){
        return getAuthority(QUALITY_RISK_BEAN);
    }


    //保存材料设备进场验收权限
    public static void setQualityFacilityBean(AuthorityBean bean){
        setAuthority(QUALITY_FACILITY_BEAN,bean);
    }
    //获取材料设备进场验收权限
    public static AuthorityBean getQualityFacilityBean(){
        return getAuthority(QUALITY_FACILITY_BEAN);
    }

    //保存质量整改记录权限
    public static void setQualityRectificationBean(AuthorityBean bean){
        setAuthority(QUALITY_RECTIFICATION_BEAN,bean);
    }
    //获取质量整改记录权限
    public static AuthorityBean getQualityRectificationBean(){
        return getAuthority(QUALITY_RECTIFICATION_BEAN);
    }

    private static void setAuthority(String key,AuthorityBean bean){
        String text = new GsonBuilder().create().toJson(bean);
        SharedPreferences preferences= BaseApplication.getInstance().getSharedPreferences(NAME,Context.MODE_PRIVATE);
        Editor editor=preferences.edit();
        editor.putString(key, text);
        editor.commit();
    }

    private static AuthorityBean getAuthority(String key){
        SharedPreferences preferences= BaseApplication.getInstance().getSharedPreferences(NAME,Context.MODE_PRIVATE);
        String text = preferences.getString(key,"");
        if(TextUtils.isEmpty(text)){
            return null;
        }else{
            return new GsonBuilder().create().fromJson(text, new TypeToken<AuthorityBean>(){}.getType());
        }
    }

    /**
     * 保存选中的质检项目的信息
     */
    public static void setSelectModuleInfo(long id,String name){
        SharedPreferences preferences= BaseApplication.getInstance().getSharedPreferences(NAME,Context.MODE_PRIVATE);
        Editor editor=preferences.edit();
        editor.putString(MODULE_INFO_NAME, name);
        editor.putLong(MODULE_INFO_ID,id);
        editor.commit();
    }

    /**
     * 获取选中的质检项目的name
     */
    public static String getSelectModuleName(){
        SharedPreferences preferences= BaseApplication.getInstance().getSharedPreferences(NAME,Context.MODE_PRIVATE);
        return preferences.getString(MODULE_INFO_NAME,"");
    }

    /**
     * 获取选中的质检项目的id
     */
    public static long getSelectModuleId(){
        SharedPreferences preferences= BaseApplication.getInstance().getSharedPreferences(NAME,Context.MODE_PRIVATE);
        return preferences.getLong(MODULE_INFO_ID,-1);
    }

    /**
     * 保存搜索模型的记录
     */
    public static void saveModelSearchKey(String key){
        saveSearchKey(SEARCH_KEY_MODEL,key);
    }
    /**
     * 保存搜索图纸的记录
     */
    public static void saveBluePrintSearchKey(String key){
        saveSearchKey(SEARCH_KEY_BLUEPRINT,key);
    }

    /**
     * 获取模型搜索历史
     */
    public static List<String> getModelSearchKey(){
        return getSearchKey(SEARCH_KEY_MODEL);
    }

    /**
     * 获取图纸搜索历史
     */
    public static List<String> getBluePrintSearchKey(){
        return getSearchKey(SEARCH_KEY_BLUEPRINT);
    }

    /**
     * 保存搜索历史
     */
    private  static void saveSearchKey(String type,String key){
        LogUtil.e("保存key="+key);
        int max = 20;
        SharedPreferences preferences= BaseApplication.getInstance().getSharedPreferences(NAME,Context.MODE_PRIVATE);
        Editor editor=preferences.edit();
        List<String> list = getSearchKey(type);
        if(list==null){
            list = new ArrayList<>();
        }
        if(!list.contains(key)) {
            if (list.size() == max) {
                list.remove(max - 1);
            }
            list.add(0, key);
        }else{
            int position = list.indexOf(key);
            list.remove(position);
            list.add(0,key);
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            if (i == 0) {
                sb.append(list.get(i));
            } else {
                sb.append("," + list.get(i));
            }
        }
        editor.putString(type, sb.toString());
        editor.commit();
    }

    /**
     * 获取搜索历史
     */
    private static List<String> getSearchKey(String type){
        SharedPreferences preferences= BaseApplication.getInstance().getSharedPreferences(NAME,Context.MODE_PRIVATE);
        String keyStr = preferences.getString(type,"");
        LogUtil.e("获取历史："+keyStr);
        if(TextUtils.isEmpty(keyStr)){
            return null;
        }else{
            List<String> list = new ArrayList<>();
            if(keyStr.contains(",")){
                String[] keys = keyStr.split(",");
                for(String key :keys){
                    list.add(key);
                }
            }else{
                list.add(keyStr);
            }
            return list;
        }
    }
}
