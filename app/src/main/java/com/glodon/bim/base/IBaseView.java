package com.glodon.bim.base;

import android.app.Activity;

/**
 * 描述：IView基类
 * 作者：zhourf on 2017/9/11
 * 邮箱：zhourf@glodon.com
 */

public interface IBaseView {

    /**
     * 展示进度条
     */
    void showLoadingDialog();

    /**
     * 隐藏进度
     */
    void dismissLoadingDialog();

    /**
     * 获取上下文
     */
    Activity getActivity();
}
