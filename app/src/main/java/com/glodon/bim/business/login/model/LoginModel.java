package com.glodon.bim.business.login.model;

import android.text.TextUtils;

import com.glodon.bim.basic.config.AppConfig;
import com.glodon.bim.basic.log.LogUtil;
import com.glodon.bim.basic.network.NetRequest;
import com.glodon.bim.basic.network.OAuth2Request;
import com.glodon.bim.business.login.contract.LoginContract;
import com.glodon.bim.business.login.listener.OnLoginListener;
import com.glodon.bim.business.login.provider.LoginProviderInput;
import com.glodon.bim.common.login.User;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;

/**
 * 描述：登录
 * 作者：zhourf on 2017/9/11
 * 邮箱：zhourf@glodon.com
 */

public class LoginModel implements LoginContract.Model {


    private final String SET_COOKIE = "Set-Cookie";
    private final String LOCATION = "Location";


    /**
     * oAuth2流程第一步
     */
    @Override
    public void login(final String username, final String password, final OnLoginListener listener) {
        OAuth2Request.createOAuthRequest(LoginApi.class, username, password)
                .request1(username, password)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                        System.out.println("1--");
//                        System.out.println(response.headers().toString());
//                        System.out.println("1--");
                        request2(response, username, password, listener);
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        if (listener != null) {
                            listener.onLoginFailed(call, t);
                        }
                        LogUtil.e("error="+t.getMessage());

                    }
                });
    }

    /**
     * oAuth2流程第二步
     */
    private void request2(Response<ResponseBody> response, final String username, final String password, final OnLoginListener listener) {
        final String cookie2 = response.headers().get(SET_COOKIE);
        OAuth2Request.createOAuthRequest(LoginApi.class, username, password)
                .request2(cookie2, response.headers().get(LOCATION))
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                        System.out.println("2--");
//                        System.out.println(response.headers().toString());
//                        System.out.println("2--");
                        request3(response, username, password, cookie2, listener);
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        if (listener != null) {
                            listener.onLoginFailed(call, t);
                        }
//                        System.out.println("er/ror  2--"+t.getMessage());
                    }
                });
    }

    /**
     * oAuth2流程第三步
     */
    private void request3(Response<ResponseBody> response, final String username, final String password, final String cookie2, final OnLoginListener listener) {
        final String cookie3 = response.headers().get(SET_COOKIE);

        OAuth2Request.createOAuthRequest(LoginApi.class, username, password)
                .request3(getCookie(cookie2) + ";" + getCookie(cookie3), response.headers().get(LOCATION))
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                        System.out.println("3--");
//                        System.out.println(response.headers().toString());
//                        System.out.println("3--");
                        request4(response, username, password, cookie2, cookie3, listener);
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        if (listener != null) {
                            listener.onLoginFailed(call, t);
                        }
//                        System.out.println("error  3--"+t.getMessage());
                    }
                });
    }

    /**
     * oAuth2流程第四步
     */
    private void request4(Response<ResponseBody> response, String username, String password, String cookie2, String cookie3, final OnLoginListener listener) {
        String location = response.headers().get(LOCATION);
//        System.out.println("location--"+location);
        if(TextUtils.isEmpty(location)){
            if (listener != null) {

                Throwable t = new Throwable("账户密码错误!");
                listener.onLoginFailed(null, t);
            }
        }else {
            OAuth2Request.createOAuthRequest(LoginApi.class, username, password)
                    .request4(getCookie(cookie2) + ";" + getCookie(cookie3), location)
                    .enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                            System.out.println("4--");
//                            System.out.println(response.headers().toString());
//                            System.out.println("4--");
                            if (listener != null) {
                                listener.onLoginSuccess(getCookie(response.headers().get(SET_COOKIE)));
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            if (listener != null) {
                                listener.onLoginFailed(call, t);
                            }
//                            System.out.println("error  4--"+t.getMessage());
                        }
                    });
        }
    }

    /**
     * 获取用户信息
     */
    @Override
    public Observable<User> getUserInfo(String cookie) {
        return NetRequest.getInstance().getCall(AppConfig.BASE_URL, LoginApi.class).getUserInfo(cookie);
    }

    @Override
    public void updateCookieDb(String cookie) {
        if(!TextUtils.isEmpty(cookie)) {
            new LoginProviderInput().updateCookieDb(cookie);
        }
    }


    private String getCookie(String cookie) {
        if(!TextUtils.isEmpty(cookie) && cookie.contains(";")) {
            String[] temps = cookie.split(";");
            return temps[0];
        }
        return null;
    }

}
