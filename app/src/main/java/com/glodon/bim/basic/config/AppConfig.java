package com.glodon.bim.basic.config;

import com.glodon.bim.R;

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

    /**
     * 头像加载时，加载过程中显示的图片
     */
    public static int LOADING_DRAWABLE = R.drawable.ic_launcher;
    /**
     * 头像加载时，加载失败显示的图片
     */
    public static int LOADING_DRAWABLE_ERROR = R.drawable.ic_launcher;

    /**
     * 环境的url
     */

//    public static String BASE_URL = "http://192.168.72.48/";//欧阳
//    public static String BASE_UPLOAD_URL =  "https://api.glodon.com/";
    public static String BASE_UPLOAD_URL =  "http://172.16.233.183:8093/";//图片上传  测试
//    public static String BASE_UPLOAD_URL = "http://172.16.233.54:443/";
//    public static String BASE_URL = "http://192.168.81.41/"; //开发
    public static String BASE_URL = "http://192.168.81.30/"; //测试
//    public static String BASE_URL = "http://bimcop.glodon.com/"; //正式环境

    public static final String BASE_URL_FEEDBACK = "http://192.168.81.30:8000/";//意见反馈的url
    public static boolean isShow = true;//控制是否可现实模型图纸界面

}
