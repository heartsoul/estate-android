package com.glodon.bim.common.config;

/**
 * 描述：权限配置
 * 作者：zhourf on 2017/11/8
 * 邮箱：zhourf@glodon.com
 */

public class AuthorityConfig {

    //模块code
    public static final String  Quality_Check = "Estate_Integration_Quality_Check";//质量检查记录
    public static final String  Quality_Accept = "Estate_Integration_Quality_Accept";//质量验收记录
    public static final String  Quality_Risk= "Estate_Integration_Quality_Risk";//质量隐患记录
    public static final String  Quality_Facility= "Estate_Integration_Quality_Facility";//材料设备进场验收
    public static final String  Quality_Rectification= "Estate_Integration_Quality_Rectification";//质量整改记录

    //具体权限
    public static final String DeleteUnit = "DeleteUnit";
    public static final String Print = "Print";
    public static final String BrowseGrant = "BrowseGrant";
    public static final String BrowseAll = "BrowseAll";
    public static final String DeleteAll = "DeleteAll";
    public static final String ModifyUnit = "ModifyUnit";
    public static final String DeleteGrant = "DeleteGrant";
    public static final String BrowseUnit = "BrowseUnit";
    public static final String Download = "Download";
    public static final String ModifyAll = "ModifyAll";
    public static final String ModifyGrant = "ModifyGrant";

}
