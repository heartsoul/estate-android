package com.glodon.bim.business.qualityManage.bean;

/**
 * 描述：质检标准
 * 作者：zhourf on 2017/11/20
 * 邮箱：zhourf@glodon.com
 */

public class ModuleStandardItem {
    public String code;
    public String content;
    public long id;
    public String name;
    public Long parentId;
    public String projectProperty;
    public long standardId;

    public boolean hasChild = true;
    public int level;
}
