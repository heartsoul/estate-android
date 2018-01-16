package com.glodon.bim.business.equipment.listener;

import com.glodon.bim.business.equipment.bean.EquipmentListBeanItem;

/**
 * 描述：材设清单单据操作监听
 * 作者：zhourf on 2017/10/19
 * 邮箱：zhourf@glodon.com
 */

public interface OnOperateEquipmentSheetListener {

    /**
     * 点击删除
     */
    void delete(EquipmentListBeanItem item,int position);

    /**
     * 点击进入详情
     */
    void detail(EquipmentListBeanItem item,int position);

    /**
     * 点击提交
     */
    void submit(EquipmentListBeanItem item,int position);

    /**
     * 点击进入编辑
     */
    void toEdit(EquipmentListBeanItem item, int position);

}
