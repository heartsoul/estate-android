package com.glodon.bim.basic.utils;

import com.glodon.bim.base.BaseApplication;

/**
 * 描述：资源获取
 * 作者：zhourf on 2018/2/10
 * 邮箱：zhourf@glodon.com
 */

public class ResourceUtil {
    /**
     * 获取字符串
     */
    public static String getResourceString(int resourceId){
        return BaseApplication.getInstance().getResources().getString(resourceId);
    }

    /**
     * 获取颜色
     */
    public static int getResourceColor(int resourceId){
        return BaseApplication.getInstance().getResources().getColor(resourceId);
    }
}
