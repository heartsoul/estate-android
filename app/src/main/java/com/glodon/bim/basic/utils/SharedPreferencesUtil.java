
package com.glodon.bim.basic.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.icu.text.DisplayContext;
import android.text.TextUtils;

import com.glodon.bim.base.BaseApplication;
import com.glodon.bim.business.authority.AuthorityBean;
import com.glodon.bim.business.main.bean.ProjectListItem;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

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
    //权限
    public static String QUALITY_CHECK_BEAN = "QUALITY_CHECK_BEAN";//质量检查记录
    public static String QUALITY_ACCEPT_BEAN= "QUALITY_ACCEPT_BEAN";//质量验收记录
    public static String QUALITY_RISK_BEAN = "QUALITY_RISK_BEAN";//质量隐患记录
    public static String QUALITY_FACILITY_BEAN = "QUALITY_FACILITY_BEAN";//材料设备进场验收
    public static String QUALITY_RECTIFICATION_BEAN = "QUALITY_RECTIFICATION_BEAN";//质量整改记录

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
        SharedPreferences preferences= BaseApplication.getInstance().getSharedPreferences(NAME,Context.MODE_PRIVATE);
        Editor editor=preferences.edit();
        editor.putString(PROJECT_NAME, item.name);
        editor.putLong(PROJECT_ID,item.deptId);
        editor.putString(PROJECT_TYPE_CODE,item.projectTypeCode);
        editor.commit();
    }

    /**
     * 获取项目id
     * @return
     */
    public static long getProjectId(){
        SharedPreferences preferences= BaseApplication.getInstance().getSharedPreferences(NAME,Context.MODE_PRIVATE);
        return preferences.getLong(PROJECT_ID,-1);
    }
    /**
     * 获取项目名称
     * @return
     */
    public static String getProjectName(){
        SharedPreferences preferences= BaseApplication.getInstance().getSharedPreferences(NAME,Context.MODE_PRIVATE);
        return preferences.getString(PROJECT_NAME,"");
    }

    /**
     * 获取项目类型
     * @return
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

    //保存当前的用户id
    public static void setUserId(long id) {
        SharedPreferences preferences= BaseApplication.getInstance().getSharedPreferences(NAME,Context.MODE_PRIVATE);
        Editor editor=preferences.edit();
        editor.putLong(USER_ID, id);
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
}
