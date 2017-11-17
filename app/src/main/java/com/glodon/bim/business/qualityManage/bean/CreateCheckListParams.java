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
    public long qualityCheckpointId;
    //质检项目名称
    public String qualityCheckpointName;
    //施工单位
    public long constructionCompanyId;
    public String constructionCompanyName;
    //整改期限
    public boolean needRectification;
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
    public Long buildingId;
    public String buildingName;
    public String elementId;
    public String elementName;
//    public String qcState;

    //检查单id
    public long inspectId;
}
