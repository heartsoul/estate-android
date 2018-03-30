package com.glodon.bim.business.greendao;

import android.content.Context;

import com.glodon.bim.base.BaseApplication;
import com.glodon.bim.gen.DaoMaster;
import com.glodon.bim.gen.DaoSession;

/**
 * 描述：数据库管理类
 * 作者：zhourf on 2017/9/14
 * 邮箱：zhourf@glodon.com
 */

public class GreenDaoHelper {

    private static DaoMaster mDaoMaster;
    private static DaoSession mDaoSession;

    /**
     * 初始化greenDao，这个操作建议在Application初始化的时候添加；
     */
    public static void initDatabase(Context context) {
        DaoMaster.DevOpenHelper mHelper = new DaoMaster.DevOpenHelper(context, "cache-db", null);
        // 注意：该数据库连接属于 DaoMaster，所以多个 Session 指的是相同的数据库连接。
        mDaoMaster = new DaoMaster(mHelper.getWritableDatabase());
        mDaoSession = mDaoMaster.newSession();
    }


    public static DaoSession getDaoSession() {
        if (mDaoSession == null) {
            initDatabase(BaseApplication.getInstance());
        }
        return mDaoSession;
    }

    public static DaoMaster getDaoMaster() {
        return mDaoMaster;
    }


}