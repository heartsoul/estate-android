package com.glodon.bim.business.qualityManage.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 描述：质检项目列表
 * 作者：zhourf on 2017/10/27
 * 邮箱：zhourf@glodon.com
 */

public class ModuleListBeanItem implements Serializable {

    public String code;
    public List<ModuleListBeanItemElementType> elementTypes;
    public int id;
    public String name;
    public String projectType;
    public String requirement;
    public int specialtyId;
    public String specialtyName;

}
