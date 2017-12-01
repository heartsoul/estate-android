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
        return "ModuleListBeanItem{" +
                "code='" + code + '\'' +
                ", fileId=" + id +
                ", fileName='" + name + '\'' +
                ", parentId=" + parentId +
                ", projectType='" + projectType + '\'' +
                ", requirement='" + requirement + '\'' +
                ", specialtyId=" + specialtyId +
                ", specialtyName='" + specialtyName + '\'' +
                ", treePath='" + treePath + '\'' +
                '}';
    }
}
