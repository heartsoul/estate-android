package com.glodon.bim.business.qualityManage.bean;

import java.util.List;

/**
 * 描述：创建检查单的入参
 * 作者：zhourf on 2017/10/27
 * 邮箱：zhourf@glodon.com
 */

public class CreateCheckListParams {
    //编号
    public String code;
    //项目id
    public long projectId;
    //项目名称
    public String projectName;
    public String inspectionDate;
    //检查单位
    public long inspectionCompanyId;//该账户所在单位
    //质检项目id
    public long inspectionProjectId;
    //质检项目名称
    public String inspectionProjectName;
    //施工单位
    public long constructionCompanyId;
    //整改期限
    public boolean isNeedRectification;
    public String lastRectificationDate;
    //现场描述
    public String description;
    public String inspectionType;
    public String versionId;//模型版本id
    public List<CreateCheckListParamsFile> files;
    // 责任人id
    public long responsibleUserId;
    // 责任人名称
    public String responsibleUserName;
    public long buildingId;
    public String buildingName;
    public String elementId;
    public String elementName;
    public String qcState;
}