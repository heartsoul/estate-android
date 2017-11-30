package com.glodon.bim.business.qualityManage.bean;

/**
 * 描述：检查单位列表item
 * 作者：zhourf on 2017/11/17
 * 邮箱：zhourf@glodon.com
 */

public class InspectionCompanyItem {
    public String alias;
    public String code;
    public String extData;
    public boolean external;
    public boolean formal;
    public long id;
    public String name;
    public long orderNum;
    public long parentId;
    public String treePath;
    public String type;

    @Override
    public String toString() {
        return "InspectionCompanyItem{" +
                "alias='" + alias + '\'' +
                ", code='" + code + '\'' +
                ", extData='" + extData + '\'' +
                ", external=" + external +
                ", formal=" + formal +
                ", fileId=" + id +
                ", name='" + name + '\'' +
                ", orderNum=" + orderNum +
                ", parentId=" + parentId +
                ", treePath='" + treePath + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
