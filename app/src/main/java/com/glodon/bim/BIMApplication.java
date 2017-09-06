package com.glodon.bim;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

/**
 * 描述：全局Application
 * 作者：zhourf on 2017/9/6
 * 邮箱：zhourf@glodon.com
 */
public class BIMApplication extends MultiDexApplication {

    /**
     * 分割 Dex 支持  方法数超过65535时处理
     * @param base  当前上下文
     */
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}
