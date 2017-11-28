package com.glodon.bim.business.qualityManage.bean;

import java.io.Serializable;

/**
 * 描述：图纸列表
 * 作者：zhourf on 2017/10/27
 * 邮箱：zhourf@glodon.com
 */

public class BlueprintListBeanItem implements Serializable {

    public String code;
    public Long id;
    public String name;
    public long parentId;
    public String projectType;
    public String requirement;
    public long specialtyId;
    public String specialtyName;
    public String treePath;


    public int viewType = 1;//0目录，1具体的项
    @Override
    public String toString() {
        return "BlueprintListBeanItem{" +
                "code='" + code + '\'' +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", parentId=" + parentId +
                ", projectType='" + projectType + '\'' +
                ", requirement='" + requirement + '\'' +
                ", specialtyId=" + specialtyId +
                ", specialtyName='" + specialtyName + '\'' +
                ", treePath='" + treePath + '\'' +
                '}';
    }
}
