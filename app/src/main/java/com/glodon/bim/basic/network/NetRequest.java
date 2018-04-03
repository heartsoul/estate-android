package com.glodon.bim.basic.network;

import android.content.Context;
import android.text.TextUtils;

import com.glodon.bim.base.BaseApplication;
import com.glodon.bim.basic.config.AppConfig;
import com.glodon.bim.basic.log.LogUtil;
import com.glodon.bim.basic.utils.NetWorkUtils;
import com.glodon.bim.business.main.bean.ProjectListBean;
import com.glodon.bim.business.main.bean.ProjectListItem;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
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

    /**
     * 获取网络请求结果  非rx方式
     *
     * @param call     请求
     * @param callback 回调
     * @param <T>      结果泛型
     */
    public <T> void getResponse(Call<T> call, final NetRequestCallback<T> callback) {
        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                callback.onResponse(call, response);
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                callback.onFailure(call, t);
            }
        });
    }

    private OkHttpClient getOkHttpClient(final Context context) {

        //只有 网络拦截器环节 才会写入缓存写入缓存,在有网络的时候 设置缓存时间
        //设置缓存路径 内置存储
        //File httpCacheDirectory = new File(context.getCacheDir(), "responses");
        //外部存储
        File httpCacheDirectory = new File(context.getExternalCacheDir(), "responses");
        //设置缓存 10M
        int cacheSize = DEFAULT_HTTP_CACHE_SIZE;
        Cache cache = new Cache(httpCacheDirectory, cacheSize);
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.cache(cache);
        builder.addInterceptor(baseInterceptor);
//        builder.addNetworkInterceptor(networkInterceptor);
        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        return builder.build();
    }

    private Interceptor baseInterceptor = new Interceptor() {
        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {
            LogUtil.e("baseInterceptor");

            Request request = chain.request();
            LogUtil.e(request.toString());
            LogUtil.e(request.method());
            LogUtil.e(request.url().encodedPath());
            LogUtil.e(request.url().queryParameterNames().toString());

            if (!NetWorkUtils.isNetworkAvailable(BaseApplication.getInstance())) {
                String msg = getMsg();
                okhttp3.Response response = new okhttp3.Response.Builder()
                        .code(200)
                        .message(msg)
                        .protocol(Protocol.HTTP_1_0)
                        .request(chain.request())
                        .body(ResponseBody.create(MediaType.parse("application/json"),msg.getBytes()))
                        .addHeader("content-type","application/json")
                        .addHeader("charset","UTF-8")
                        .build();
                return response;
            }
//            return chain.proceed(request);
            okhttp3.Response response = chain.proceed(request);
//            LogUtil.e("response="+response.body().string());
            return response;
        }
    };

    private String getMsg(){
        ProjectListBean bean = new ProjectListBean();
        bean.totalPages = 4;
        bean.size = 20;
        List<ProjectListItem> list = new ArrayList<>();
        ProjectListItem item0 = new ProjectListItem();
        item0.name = "item0";
        item0.id = 100;
        item0.deptId = 100;
        list.add(item0);
        ProjectListItem item1 = new ProjectListItem();
        item1.name = "item1";
        item1.id = 101;
        item1.deptId = 101;
        list.add(item1);
        bean.content = list;
        return new GsonBuilder().create().toJson(bean);
    }


    private Interceptor networkInterceptor = new Interceptor() {
        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {
            LogUtil.e("networkIntercepter");
            Request request = chain.request();
            okhttp3.Response response = chain.proceed(request);
            String serverCache = response.header("Cache-Control");
            if (TextUtils.isDigitsOnly(serverCache)) {
                String cacheControl = request.cacheControl().toString();
                if (TextUtils.isEmpty(cacheControl)) {
                    int maxAge = 60;
                    return response.newBuilder()
                            .removeHeader("Pragma")
                            .removeHeader("Cache-Control")
                            .header("Cache-Control", "public,max-age=" + maxAge)
                            .build();
                } else {
                    return response.newBuilder()
                            .addHeader("Cache-Control", cacheControl)
                            .removeHeader("Pragma")
                            .build();
                }
            }
            return response;
        }
    };
}
