package com.glodon.bim.business.qualityManage.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 描述：质检项目列表
 * 作者：zhourf on 2017/10/27
 * 邮箱：zhourf@glodon.com
 */

public class ModuleListBean implements Serializable {

    public List<ModuleListBeanItem> content;
    public boolean first;
    public boolean last;
    public int number;
    public int numberOfElements;
    public int size;
    public List<String> sort;
    public int totalElements;
    public int totalPages;
}
