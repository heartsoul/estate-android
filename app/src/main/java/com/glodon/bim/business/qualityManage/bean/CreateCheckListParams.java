package com.glodon.bim.business.qualityManage.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 描述：创建检查单的入参
 * 作者：zhourf on 2017/10/27
 * 邮箱：zhourf@glodon.com
 */

public class CreateCheckListParams implements Serializable {
    //编号
    public String code;
    //项目id
    public long projectId;
    //项目名称
    public String projectName;
    //检查单位
    public long inspectionCompanyId;//该账户所在单位
    public String inspectionCompanyName;//该账户所在单位
    //质检项目id
    public Long qualityCheckpointId;
    //质检项目名称
    public String qualityCheckpointName;
    //施工单位
    public long constructionCompanyId;
    public String constructionCompanyName;
    //整改期限
    public boolean needRectification = true;
    public String lastRectificationDate;
    //现场描述
    public String description;
    public String inspectionType;
    public List<CreateCheckListParamsFile> files;
    // 责任人id
    public long responsibleUserId;
    // 责任人名称
    public String responsibleUserName;
    public String responsibleUserTitle;
    //模型
    public String versionId;//模型版本id
    public String gdocFileId;//模型的id
    public Long buildingId;//单体id
    public String buildingName;//单体name
    public String elementId;//构件id
    public String elementName;//构件name
    //图纸
    public String drawingGdocFileId;//图纸id
    public String drawingName;//图纸名
    public String drawingPositionX;//位置的x信息
    public String drawingPositionY;//位置的y信息

//    public String qcState;

    //检查单id
    public long inspectId;

    @Override
    public String toString() {
        return "CreateCheckListParams{" +
                "facilityCode='" + code + '\'' +
                ", projectId=" + projectId +
                ", projectName='" + projectName + '\'' +
                ", inspectionCompanyId=" + inspectionCompanyId +
                ", inspectionCompanyName='" + inspectionCompanyName + '\'' +
                ", qualityCheckpointId=" + qualityCheckpointId +
                ", qualityCheckpointName='" + qualityCheckpointName + '\'' +
                ", constructionCompanyId=" + constructionCompanyId +
                ", constructionCompanyName='" + constructionCompanyName + '\'' +
                ", needRectification=" + needRectification +
                ", lastRectificationDate='" + lastRectificationDate + '\'' +
                ", description='" + description + '\'' +
                ", inspectionType='" + inspectionType + '\'' +
                ", files=" + files +
                ", responsibleUserId=" + responsibleUserId +
                ", responsibleUserName='" + responsibleUserName + '\'' +
                ", responsibleUserTitle='" + responsibleUserTitle + '\'' +
                ", versionId='" + versionId + '\'' +
                ", gdocFileId='" + gdocFileId + '\'' +
                ", buildingId=" + buildingId +
                ", buildingName='" + buildingName + '\'' +
                ", elementId='" + elementId + '\'' +
                ", elementName='" + elementName + '\'' +
                ", drawingGdocFileId='" + drawingGdocFileId + '\'' +
                ", drawingName='" + drawingName + '\'' +
                ", drawingPositionX='" + drawingPositionX + '\'' +
                ", drawingPositionY='" + drawingPositionY + '\'' +
                ", inspectId=" + inspectId +
                '}';
    }
}
