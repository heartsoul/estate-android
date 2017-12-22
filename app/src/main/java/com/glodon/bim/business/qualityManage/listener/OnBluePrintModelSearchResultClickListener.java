package com.glodon.bim.business.qualityManage.listener;

import com.glodon.bim.business.qualityManage.bean.BluePrintModelSearchBeanItem;

/**
 * 描述：模型图纸搜索结果列表点击的回调
 * 作者：zhourf on 2017/12/22
 * 邮箱：zhourf@glodon.com
 */

public interface OnBluePrintModelSearchResultClickListener {

    void onSelectBluePrint(BluePrintModelSearchBeanItem item);

    void onSelectModel(BluePrintModelSearchBeanItem item);
}
