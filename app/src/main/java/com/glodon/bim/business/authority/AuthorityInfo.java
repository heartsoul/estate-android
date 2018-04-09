package com.glodon.bim.business.authority;

/**
 * 描述：权限信息
 * 作者：zhourf on 2018/4/9
 * 邮箱：zhourf@glodon.com
 */

public class AuthorityInfo {
    public String appCode = "Estate";
    public AuthorityBean Quality_Check_Bean;//质量检查记录
    public AuthorityBean Quality_Accept_Bean;//质量验收记录
    public AuthorityBean Quality_Risk_Bean;//质量隐患记录
    public AuthorityBean Quality_Facility_Bean;//材料设备进场验收
    public AuthorityBean Quality_Rectification_Bean;//质量整改记录
}
