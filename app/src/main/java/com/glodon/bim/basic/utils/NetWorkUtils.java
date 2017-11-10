
package com.glodon.bim.basic.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.glodon.bim.basic.log.LogUtil;
import com.google.gson.Gson;

import org.json.JSONObject;

/**
 * 描述：网络状态判断
 * 作者：zhourf on 2017/10/23
 * 邮箱：zhourf@glodon.com
 */
public final class NetWorkUtils {

    /**
     * 判断网络是否可用
     */
    public static boolean isNetworkAvailable(Context context) {
        try {
            ConnectivityManager connectivity = (ConnectivityManager)context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity == null) {
                return false;
            } else {
                NetworkInfo[] info = connectivity.getAllNetworkInfo();
                if (info != null) {
                    for (NetworkInfo networkInfo : info) {
                        if (networkInfo.getState() == NetworkInfo.State.CONNECTED) {
                            LogUtil.d("networkUtils",
                                    "info[i].getState()=" + networkInfo.getState());
                            return true;
                        }
                    }
                }
            }
        } catch (Exception e) {
            LogUtil.d("networkUtils", "isNetworkAvailable is failed:" + e.getMessage());
        }
        return false;
    }

    /**
     * 是否WiFi环境
     */
    public static boolean isWifi(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager)context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetInfo != null && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI;
    }
    
    /**
     * 是否移动环境
     */
    public static boolean isMOBILE(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager)context
        .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetInfo != null && activeNetInfo.getType() == ConnectivityManager.TYPE_MOBILE;
    }

    /**
     * 普通对象转为json对象
     *
     * @param object 入参对象
     * @return JSONObject
     */
    public static JSONObject beanToJson(Object object) {
        if (object == null) {
            return new JSONObject();
        }
        JSONObject jsonObject = null;
        try {
            if (object instanceof String){
                jsonObject = new JSONObject(object.toString());
            }else {
                jsonObject = new JSONObject(new Gson().toJson(object));
            }
        } catch (Exception e) {
            LogUtil.e("tnp", "beanToJson is exception: " + e.getMessage());
        }
        return jsonObject;
    }
}
