package com.glodon.bim.common.login;

import java.util.List;

/**
 * 描述：用户信息
 * 作者：zhourf on 2017/9/12
 * 邮箱：zhourf@glodon.com
 */

public class User {
    /**
     * 用户名
     */
    public String username;
    public String gldAccountId;
    /**
     * 用户权限列表
     */
    public List<String> authorities;
    /**
     * 用户详细信息
     */
    public AccountInfo accountInfo;
    /**
     * 当前账户信息
     */
    public UserTenant curUserInfo;
    public long curTenantId;
}
