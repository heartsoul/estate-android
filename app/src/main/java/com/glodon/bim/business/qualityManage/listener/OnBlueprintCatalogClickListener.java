package com.glodon.bim.business.qualityManage.listener;

import com.glodon.bim.business.qualityManage.bean.BlueprintListBeanItem;

/**
 * 描述：选择图纸顶部目录点击的监听
 * 作者：zhourf on 2017/10/26
 * 邮箱：zhourf@glodon.com
 */

public interface OnBlueprintCatalogClickListener {

    void onSelect(BlueprintListBeanItem item, boolean isShow);
}
