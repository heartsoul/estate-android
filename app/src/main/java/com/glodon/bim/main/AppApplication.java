package com.glodon.bim.main;

import android.app.Application;

import com.glodon.bim.basic.config.AppConfig;
import com.glodon.bim.basic.log.BimUncaughtExceptionHandler;

/**
 * 描述：
 * 作者：zhourf on 2017/9/8
 * 邮箱：zhourf@glodon.com
 */

public class AppApplication extends Application {
    private static Application instance;


    public static Application getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    public void init() {
        instance = this;

        if (AppConfig.LOG_ERR_SAVE) {
            BimUncaughtExceptionHandler.getInstance().init(instance);
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }
}
