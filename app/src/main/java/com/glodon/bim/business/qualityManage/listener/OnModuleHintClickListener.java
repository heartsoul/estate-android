package com.glodon.bim.business.qualityManage.listener;

import com.glodon.bim.business.qualityManage.bean.ModuleListBeanItem;

/**
 * 描述：选择质检项目目录切换点击的监听
 * 作者：zhourf on 2017/10/26
 * 邮箱：zhourf@glodon.com
 */

public interface OnModuleHintClickListener {

    void onSelect(ModuleListBeanItem item);
}
