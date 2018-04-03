package com.glodon.bim.basic.network;

import android.content.Context;

import com.glodon.bim.base.BaseApplication;
import com.glodon.bim.basic.config.AppConfig;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 描述：网络请求类
 * 作者：zhourf on 2017/9/8
 * 邮箱：zhourf@glodon.com
 */

public class NetRequest {
    private static final int DEFAULT_TIMEOUT = 5;
    private static final int DEFAULT_HTTP_CACHE_SIZE = 10 * 1024 * 1024;
    //单例模式
    private static NetRequest instance = new NetRequest();

    private NetRequest() {
    }

    /**
     * 获取对象
     *
     * @return 当前对象
     */
    public static NetRequest getInstance() {
        return instance;
    }

    /**
     * 获取接口实例
     *
     * @param baseUrl 域名
     * @param tClass  定义的接口
     * @param <T>     返回值类的泛型
     * @return 接口实例
     */
    public <T> T getCall(String baseUrl, Class<T> tClass) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        return retrofit.create(tClass);

    }

    /**
     * 获取接口实例
     *
     * @param tClass 定义的接口
     * @param <T>    返回值类的泛型
     * @return 接口实例
     */
    public <T> T getCall(Class<T> tClass) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        return retrofit.create(tClass);

    }

    public <T> T getCall2(Class<T> tClass) {
        Retrofit retrofit = new Retrofit.Builder()
                .client(getOkHttpClient(BaseApplication.getInstance()))
                .baseUrl(AppConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        return retrofit.create(tClass);

    }

    private OkHttpClient getOkHttpClient(final Context context) {

        //外部存储
        File httpCacheDirectory = new File(context.getExternalCacheDir(), "responses");
        //设置缓存 10M
        int cacheSize = DEFAULT_HTTP_CACHE_SIZE;
        Cache cache = new Cache(httpCacheDirectory, cacheSize);
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.cache(cache);
        builder.addInterceptor(InterceptorManager.getInstance().getOfflineInterceptor());
//        builder.addNetworkInterceptor(networkInterceptor);
        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        return builder.build();
    }






}
