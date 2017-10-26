
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


}
