package com.glodon.bim.business.login.model;

import com.glodon.bim.basic.network.NetRequest;
import com.glodon.bim.business.login.contract.LoginContract;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 描述：登录
 * 作者：zhourf on 2017/9/11
 * 邮箱：zhourf@glodon.com
 */

public class LoginModel implements LoginContract.Model {

    private String baseUrl = "http://192.168.81.41/";
//    private String baseUrl = "http://192.168.93.39/";

    @Override
    public void login(final String username, final String password) {


        LoginApi loginService = ServiceGenerator.createService2(LoginApi.class, username, password);
        Call<ResponseBody> call = loginService.request1(username, password);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                System.out.println("1----------response header--------");
                System.out.println(response.headers().toString());
                System.out.println("1---------response body---------");

                final String cookie2 = response.headers().get("Set-Cookie");
                LoginApi loginService2 = ServiceGenerator.createService2(LoginApi.class, username, password);
                String location = response.headers().get("Location");
                String path = location.substring(ServiceGenerator.API_BASE_URL.length());
                Call<ResponseBody> call2 = loginService2.request2(cookie2,location);
                call2.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        System.out.println("2----------response header--------");
                        System.out.println(response.headers().toString());
                        System.out.println("2---------response body---------");

                        LoginApi loginService3 = ServiceGenerator.createService2(LoginApi.class, username, password);
                        String location = response.headers().get("Location");
//                        String path = location.substring(ServiceGenerator.API_BASE_URL.length());
                        String path = location.substring(location.indexOf("?")+1);
                        final String cookie3 = response.headers().get("Set-Cookie");
                        System.out.println("path="+path);
                        System.out.println("cookie = "+cookie2);
                        System.out.println("cookie = "+cookie3);
                        System.out.println("cookie = "+getCookie(cookie2));
                        System.out.println("cookie = "+getCookie(cookie3));
//                        Call<ResponseBody> call3 = loginService3.request3(getCookie(cookie2)+";"+getCookie(cookie3)
//                                ,getParams(path,"client_id")
//                        ,getParams(path,"redirect_uri")
//                        ,getParams(path,"response_type")
//                        ,getParams(path,"state"));
                        Call<ResponseBody> call3 = loginService3.request3(getCookie(cookie2)+";"+getCookie(cookie3),location);
                        call3.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                                System.out.println("3----------response header--------");
                                System.out.println(response.headers().toString());
                                System.out.println("3---------response body---------");


                                LoginApi loginService4 = ServiceGenerator.createService2(LoginApi.class, username, password);
//                                LoginApi loginService4 = NetRequest.getInstance().getCall(baseUrl,LoginApi.class);
                                String location = response.headers().get("Location");
//                                String path = location.substring(baseUrl.length());
//                                System.out.println("path="+path);
                                Call<ResponseBody> call4 = loginService4.request4(getCookie(cookie2)+";"+getCookie(cookie3),location);
                                call4.enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        System.out.println("4----------response header--------");
                                        System.out.println(response.headers().toString());
                                        System.out.println("4---------response body---------");

//                                        LoginApi loginService5 = ServiceGenerator.createService2(LoginApi.class, username, password);
//                                        Call<ResponseBody> call5 = loginService5.getUserInfo(getCookie(response.headers().get("Set-Cookie")));
//                                        call5.enqueue(new Callback<ResponseBody>() {
//                                            @Override
//                                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                                                System.out.println("5----------response header--------");
//                                                System.out.println(response.headers().toString());
//                                                System.out.println("5---------response body---------");
//                                                try {
//                                                    System.out.println(new String(response.body().bytes()));
//                                                } catch (IOException e) {
//                                                    e.printStackTrace();
//                                                }
//
//                                            }
//
//                                            @Override
//                                            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                                                System.out.println("error="+t.getMessage());
//                                            }
//                                        });

                                        NetRequest.getInstance().getCall(baseUrl,LoginApi.class).getUserInfo(getCookie(response.headers().get("Set-Cookie")))
                                                .enqueue(new Callback<ResponseBody>() {
                                                    @Override
                                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                        System.out.println("5-------request header");
                                                        System.out.println(call.request().headers().toString());
                                                        System.out.println("5----------response header--------");
                                                        System.out.println(response.headers().toString());
                                                        System.out.println("5---------response body---------");
                                                        try {
                                                            System.out.println(new String(response.body().bytes()));
                                                        } catch (IOException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                                                        System.out.println("error5="+t.getMessage());
                                                    }
                                                });
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                                        System.out.println("error"+t.getMessage());
                                    }
                                });
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {

                            }
                        });

                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });

    }

    private String getCookie(String cookie){
        String[] temps = cookie.split(";");
        return temps[0];
    }

    private String getParams(String url,String key){
        String[] temps = url.split("&");
        HashMap<String ,String> pa = new HashMap<>();
        for(String str :temps){
            pa.put(str.split("=")[0],str.split("=")[1]);
        }
        return pa.get(key);
    }

}
