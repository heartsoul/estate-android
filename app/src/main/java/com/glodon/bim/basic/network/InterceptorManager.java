package com.glodon.bim.basic.network;

import com.glodon.bim.base.BaseApplication;
import com.glodon.bim.basic.log.LogUtil;
import com.glodon.bim.basic.utils.NetWorkUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.ResponseBody;

/**
 * 描述：请求拦截器
 * 作者：zhourf on 2018/4/3
 * 邮箱：zhourf@glodon.com
 */

public class InterceptorManager {
    private static InterceptorManager instance ;
    private Interceptor offlineInterceptor;
//    private Interceptor networkInterceptor;

    private InterceptorManager() {
    }

    public static InterceptorManager getInstance() {
        if (instance == null) {
            instance = new InterceptorManager();
        }
        return instance;
    }

    public Interceptor getOfflineInterceptor() {
        if(offlineInterceptor==null){
            offlineInterceptor = new Interceptor() {
                @Override
                public okhttp3.Response intercept(Chain chain) throws IOException {
                    LogUtil.e("baseInterceptor");

                    Request request = chain.request();
                    LogUtil.e(request.toString());
                    LogUtil.e(request.method());
                    LogUtil.e(request.url().encodedPath());
                    LogUtil.e(request.url().queryParameterNames().toString());

                    if (!NetWorkUtils.isNetworkAvailable(BaseApplication.getInstance())) {
                        String msg = OfflineManager.getLocalData(request);
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
                    okhttp3.Response response = chain.proceed(request);
                    return response;
                }
            };
        }
        return offlineInterceptor;
    }

//    public Interceptor getNetworkInterceptor(){
//        if(networkInterceptor==null){
//            networkInterceptor = new Interceptor() {
//                @Override
//                public okhttp3.Response intercept(Chain chain) throws IOException {
//                    LogUtil.e("networkIntercepter");
//                    Request request = chain.request();
//                    okhttp3.Response response = chain.proceed(request);
//                    String serverCache = response.header("Cache-Control");
//                    if (TextUtils.isDigitsOnly(serverCache)) {
//                        String cacheControl = request.cacheControl().toString();
//                        if (TextUtils.isEmpty(cacheControl)) {
//                            int maxAge = 60;
//                            return response.newBuilder()
//                                    .removeHeader("Pragma")
//                                    .removeHeader("Cache-Control")
//                                    .header("Cache-Control", "public,max-age=" + maxAge)
//                                    .build();
//                        } else {
//                            return response.newBuilder()
//                                    .addHeader("Cache-Control", cacheControl)
//                                    .removeHeader("Pragma")
//                                    .build();
//                        }
//                    }
//                    return response;
//                }
//            };
//        }
//        return networkInterceptor;
//    }


}
