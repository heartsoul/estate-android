package com.glodon.bim.business.main.bean;

import com.glodon.bim.business.qualityManage.bean.QualityCheckListBeanSort;

import java.util.List;

/**
 * 描述：
 * 作者：zhourf on 2017/10/18
 * 邮箱：zhourf@glodon.com
 */

public class ProjectListBean {
    public List<ProjectListItem> content;
    public boolean first;
    public boolean last;
    public int number;
    public int numberOfElements;
    public int size;
    public List<QualityCheckListBeanSort> sort;
    public int totalElements;
    public int totalPages;
}
