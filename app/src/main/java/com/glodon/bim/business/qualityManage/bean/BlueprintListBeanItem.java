package com.glodon.bim.business.qualityManage.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 描述：图纸列表
 * 作者：zhourf on 2017/10/27
 * 邮箱：zhourf@glodon.com
 */

public class BlueprintListBeanItem implements Serializable {

    public String alias;
    public String appKey;
    public long buildingId;
    public String buildingName;
    public long createTime;
    public String creatorId;
    public String creatorName;
    public boolean current;
    public String digest;
    public String fileFolderAssocStatus;
    public String fileId;
    public List<String> fileParentList;
    public String filePath;
    public boolean folder;//true目录 false文件
    public long index;
    public long length;
    public String name;//当前名字
    public String parentId;//父目录名字
    public String revisionId;
    public String specialtyCode;
    public String specialtyName;
    public String suffix;
    public String thumbnail;
    public BluePrintBeanDataItemUserPrivilege userPrivilege;
    public long versionIndex;
    public String workspaceId;

    public int viewType = 1;//0目录，1具体的项

    @Override
    public String toString() {
        return "BlueprintListBeanItem{" +
                "alias='" + alias + '\'' +
                ", appKey='" + appKey + '\'' +
                ", buildingId=" + buildingId +
                ", buildingName='" + buildingName + '\'' +
                ", createTime=" + createTime +
                ", creatorId='" + creatorId + '\'' +
                ", creatorName='" + creatorName + '\'' +
                ", current=" + current +
                ", digest='" + digest + '\'' +
                ", fileFolderAssocStatus='" + fileFolderAssocStatus + '\'' +
                ", fileId='" + fileId + '\'' +
                ", fileParentList=" + fileParentList +
                ", filePath='" + filePath + '\'' +
                ", folder=" + folder +
                ", index=" + index +
                ", length=" + length +
                ", fileName='" + name + '\'' +
                ", parentId='" + parentId + '\'' +
                ", revisionId='" + revisionId + '\'' +
                ", specialtyCode='" + specialtyCode + '\'' +
                ", specialtyName='" + specialtyName + '\'' +
                ", suffix='" + suffix + '\'' +
                ", thumbnail='" + thumbnail + '\'' +
                ", userPrivilege=" + userPrivilege +
                ", versionIndex=" + versionIndex +
                ", workspaceId='" + workspaceId + '\'' +
                ", viewType=" + viewType +
                '}';
    }
}
