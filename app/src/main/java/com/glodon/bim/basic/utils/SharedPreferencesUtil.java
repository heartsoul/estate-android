
package com.glodon.bim.basic.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.glodon.bim.base.BaseApplication;
import com.glodon.bim.business.main.bean.ProjectListItem;

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
    private static final String PROJECT_NAME = "project_name";
    private static final String PROJECT_ID = "project_id";
    private static final String PROJECT_TYPE_CODE = "project_type_code";
    private static final String PROJECT_CREATE_TYPE = "project_create_type";

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
}
