package com.glodon.bim.business.login.listener;

import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 描述：登录回调
 * 作者：zhourf on 2017/9/13
 * 邮箱：zhourf@glodon.com
 */

public interface OnLoginListener {

    /**
     * 登录成功
     * @param cookie  有效token
     */
    void onLoginSuccess(String cookie);

    /**
     * 登录失败
     * @param call 请求
     * @param t 异常
     */
    void onLoginFailed(Call<ResponseBody> call, Throwable t);
}
