package com.glodon.bim.business.main.listener;

import com.glodon.bim.business.main.bean.ProjectListItem;

/**
 * 描述：点击项目的监听
 * 作者：zhourf on 2017/10/18
 * 邮箱：zhourf@glodon.com
 */

public interface OnProjectClickListener {
    /**
     * 点击项目
     */
    void clickTenant(ProjectListItem item);
}
