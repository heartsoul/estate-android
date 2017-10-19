package com.glodon.bim.business.qualityManage.bean;

import java.util.List;

/**
 * 描述：质检清单列表返回值
 * 作者：zhourf on 2017/10/19
 * 邮箱：zhourf@glodon.com
 */

public class QualityCheckListBean {
    public List<QualityCheckListBeanItem> content;
    public boolean first;
    public boolean last;
    public int number;
    public int numberOfElements;
    public int size;
    public List<String> sort;
    public int totalElements;
    public int totalPages;
}
