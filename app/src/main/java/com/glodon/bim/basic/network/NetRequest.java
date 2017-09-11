package com.glodon.bim.basic.network;

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

    //单例模式
    private static NetRequest instance = new NetRequest();

    private NetRequest(){}

    /**
     * 获取对象
     * @return 当前对象
     */
    public static NetRequest getInstance(){
        return instance;
    }

    /**
     * 获取接口实例
     * @param baseUrl  域名
     * @param tClass  定义的接口
     * @param <T>  返回值类的泛型
     * @return 接口实例
     */
    public <T> T getCall(String baseUrl,Class<T> tClass){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        return retrofit.create(tClass);

    }

    /**
     * 获取网络请求结果  非rx方式
     * @param call 请求
     * @param callback 回调
     * @param <T> 结果泛型
     */
    public <T> void getResponse(Call<T> call, final NetRequestCallback<T> callback){
        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                callback.onResponse(call,response);
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                callback.onFailure(call,t);
            }
        });
    }

}
