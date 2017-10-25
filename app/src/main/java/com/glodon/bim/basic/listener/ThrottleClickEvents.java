package com.glodon.bim.basic.listener;

import android.view.View;

import com.jakewharton.rxbinding.view.RxView;

import java.util.concurrent.TimeUnit;

import rx.functions.Action1;

/**
 * 描述：防止重复点击的类
 * 作者：zhourf on 2017/9/13
 * 邮箱：zhourf@glodon.com
 */

public class ThrottleClickEvents {

    /**
     * 防止重复点击，默认3秒内只取第一次点击  过滤后面的点击
     *
     * @param view     被点击的view
     * @param listener 点击事件
     */
    public static void throttleClick(final View view, final View.OnClickListener listener) {
        throttleClick(view, listener, 3, TimeUnit.SECONDS);
    }

    /**
     * 防止重复点击，默认3秒内只取第一次点击  过滤后面的点击
     *
     * @param view     被点击的view
     * @param listener 点击事件
     */
    public static void throttleClick(final View view, final View.OnClickListener listener, long windowDuration) {
        throttleClick(view, listener, windowDuration, TimeUnit.SECONDS);
    }

    /**
     * 防止重复点击
     *
     * @param view           被点击的view
     * @param listener       点击事件
     * @param windowDuration 间隔时间
     * @param unit           时间单位
     */
    public static void throttleClick(final View view, final View.OnClickListener listener, long windowDuration, TimeUnit unit) {
        RxView.clicks(view).throttleFirst(windowDuration, unit).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                listener.onClick(view);
            }
        });
    }
}
