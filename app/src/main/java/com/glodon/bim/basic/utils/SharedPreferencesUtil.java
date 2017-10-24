
package com.glodon.bim.basic.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.glodon.bim.base.BaseApplication;

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

}
