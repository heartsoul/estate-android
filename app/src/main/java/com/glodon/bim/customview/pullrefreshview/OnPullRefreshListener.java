package com.glodon.bim.customview.pullrefreshview;

/**
 * 描述：上下拉刷新接口回调
 * 作者：zhourf on 2017/9/20
 * 邮箱：zhourf@glodon.com
 */

public interface OnPullRefreshListener {

    /**
     * 下拉刷新
     */
    void onPullDown();

    /**
     * 上拉加载
     */
    void onPullUp();
}
