package com.glodon.bim.main;

import android.content.Intent;

/**
 * 描述：Presenter基类
 * 作者：zhourf on 2017/9/11
 * 邮箱：zhourf@glodon.com
 */

public interface IBasePresenter {

    /**
     * 初始化数据
     * @param intent intent
     */
    void initData(Intent intent);

    /**
     * Activity回调
     */
    void onActivityResult(int requestCode, int resultCode, Intent data);

    /**
     * Activity Destroy时调用
     */
    void onDestroy();
}
