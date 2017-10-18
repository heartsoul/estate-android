package com.glodon.bim.business.main.listener;

import com.glodon.bim.common.login.UserTenant;

/**
 * 描述：点击租户的监听
 * 作者：zhourf on 2017/10/18
 * 邮箱：zhourf@glodon.com
 */

public interface OnTenantClickListener {
    /**
     * 点击租户
     */
    void clickTenant(UserTenant tenant);
}
