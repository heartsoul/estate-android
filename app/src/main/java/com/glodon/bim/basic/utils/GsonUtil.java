package com.glodon.bim.basic.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;

/**
 * 描述：
 * 作者：zhourf on 2018/4/9
 * 邮箱：zhourf@glodon.com
 */

public class GsonUtil {

    public static <T> String toJsonString(T t){
        return new GsonBuilder().create().toJson(t);
    }

    public static <T> T toJsonObj(String json,Class<T> tClass){

        return new GsonBuilder().create().fromJson(json,tClass);
    }
    public static <T> T toJsonObj(String json, Type typeOfT){

        return new Gson().fromJson(json,typeOfT);
    }
}
