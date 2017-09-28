package com.glodon.bim.main;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.glodon.bim.basic.config.AppConfig;
import com.glodon.bim.basic.log.BimUncaughtExceptionHandler;
import com.glodon.bim.basic.utils.ScreenUtil;
import com.glodon.bim.business.greendao.GreenDaoHelper;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

/**
 * 描述：自定义application
 * 作者：zhourf on 2017/9/8
 * 邮箱：zhourf@glodon.com
 */

public class BaseApplication extends MultiDexApplication {

    {
        PlatformConfig.setWeixin("","");
        PlatformConfig.setQQZone("1106433844","S7DqyVuUth0nN9hY");
        PlatformConfig.setSinaWeibo("605411022","147ff00345905bae8e734833586e4b06","http://www.glodon.com");
    }

    private static Application instance;
    public static Application getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        UMShareAPI.get(this);
        Config.DEBUG = true;
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

        ScreenUtil.init(this);
    }

    //分包
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
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
