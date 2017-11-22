package com.glodon.bim.business.qualityManage.listener;

import com.glodon.bim.business.qualityManage.bean.BlueprintListBeanItem;

/**
 * 描述：选择图纸目录的监听
 * 作者：zhourf on 2017/10/26
 * 邮箱：zhourf@glodon.com
 */

public interface OnChooseBlueprintCataListener {

    void onSelect(BlueprintListBeanItem item);
}
