package com.glodon.bim.business.qualityManage.bean;

import java.util.List;

/**
 * 描述：整改单保存后的信息
 * 作者：zhourf on 2017/11/6
 * 邮箱：zhourf@glodon.com
 */

public class QualityGetRepairInfo {
    public int buildingId;
    public String buildingName;
    public String code;
    public String creatorName;
    public long creatorId;
    public String description;
    public String elementId;
    public String elementName;
    public List<QualityCheckListBeanItemFile> files;
    public String gdocFileId;
    public long id;

    public String inspectionCode;
    public long inspectionCompanyId;
    public long inspectionId;
    public long projectId;
    public String projectName;
    public String qcState;
    public String rectificationDate;
    public String reviewCode;
    public long reviewId;
}
