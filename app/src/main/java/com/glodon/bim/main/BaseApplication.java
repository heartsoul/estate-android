package com.glodon.bim.main;

import android.app.Application;

import com.glodon.bim.basic.config.AppConfig;
import com.glodon.bim.basic.log.BimUncaughtExceptionHandler;
import com.glodon.bim.business.greendao.GreenDaoHelper;

/**
 * 描述：自定义application
 * 作者：zhourf on 2017/9/8
 * 邮箱：zhourf@glodon.com
 */

public class BaseApplication extends Application {
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

        //异常处理  打印日志
        if (AppConfig.LOG_ERR_SAVE) {
            BimUncaughtExceptionHandler.getInstance().init(instance);
        }
        //GreenDao初始化
        GreenDaoHelper.initDatabase(this);
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
