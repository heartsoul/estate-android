package com.glodon.bim.basic.network;

import retrofit2.Call;
import retrofit2.Response;

/**
 * 描述：网络请求的回调接口
 * 作者：zhourf on 2017/9/8
 * 邮箱：zhourf@glodon.com
 */

public interface NetRequestCallback<T> {
    /**
     * 成功的回调
     * @param call 请求
     * @param response 返回数据
     */
     void onResponse(Call<T> call, Response<T> response);

    /**
     * 失败的回调
     * @param call 请求
     * @param t 异常信息
     */
     void onFailure(Call<T> call, Throwable t);
}
