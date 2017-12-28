package com.glodon.bim.business.qualityManage.bean;

import java.io.Serializable;

/**
 * 描述：质检项目列表
 * 作者：zhourf on 2017/10/27
 * 邮箱：zhourf@glodon.com
 */

public class ModuleListBeanItem implements Serializable {

    public Long id;
    public long parentId;
    public long specialtyId;
    public String treePath;
//
//    public String code;
//    public String inspectItem;
//    public String projectType;
//    public String requirement;
//    public String specialtyName;

//    public int id;
//    public int parentId;
//    public int specialtyId;
//    public String treePath;
    public String inspectItem;
    public String inspectItemDesc;
    public String specialty;


    public int viewType = 1;//0目录，1具体的项

    @Override
    public String toString() {
        return "ModuleListBeanItem{" +
                "id=" + id +
                ", parentId=" + parentId +
                ", specialtyId=" + specialtyId +
                ", treePath='" + treePath + '\'' +
                ", inspectItem='" + inspectItem + '\'' +
                ", inspectItemDesc='" + inspectItemDesc + '\'' +
                ", specialty='" + specialty + '\'' +
                ", viewType=" + viewType +
                '}';
    }
}
