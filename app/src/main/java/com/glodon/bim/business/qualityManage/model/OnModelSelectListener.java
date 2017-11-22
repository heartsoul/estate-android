package com.glodon.bim.business.qualityManage.model;

import com.glodon.bim.business.qualityManage.bean.ModelListBeanItem;
import com.glodon.bim.business.qualityManage.bean.ModelSingleListItem;
import com.glodon.bim.business.qualityManage.bean.ModelSpecialListItem;

/**
 * 描述：模型监听
 * 作者：zhourf on 2017/11/22
 * 邮箱：zhourf@glodon.com
 */

public interface OnModelSelectListener {

    void selectModel(ModelListBeanItem item);

    void selectSingle(ModelSingleListItem item);

    void selectSpecial(ModelSpecialListItem item);
}
