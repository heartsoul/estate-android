package com.glodon.bim.business.equipment.bean;

import com.glodon.bim.business.qualityManage.bean.QualityCheckListBeanSort;

import java.util.List;

/**
 * 描述：质检清单列表返回值
 * 作者：zhourf on 2017/10/19
 * 邮箱：zhourf@glodon.com
 */

public class EquipmentListBean {
    public List<EquipmentListBeanItem> content;
    public boolean first;
    public boolean last;
    public int number;
    public int numberOfElements;
    public int size;
    public List<QualityCheckListBeanSort> sort;
    public int totalElements;
    public int totalPages;
}
