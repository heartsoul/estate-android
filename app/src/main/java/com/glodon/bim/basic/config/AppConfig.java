package com.glodon.bim.basic.config;

/**
 * 描述：app配置信息
 * 作者：zhourf on 2017/9/8
 * 邮箱：zhourf@glodon.com
 */

public class AppConfig {

    /**
     * 控制是否显示控制台log
     */
    public static boolean LOGCAT_SHOW = true;

    /**
     * 控制是否生成log日志文件   true 生成  false 不生成
     */
    public static boolean LOG_ERR_SAVE = true;

    /**
     * log的存放目录
     */
    public static String BIM_LOG_DIRECTORY = "/sdcard/bimLog";
}
