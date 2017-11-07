package com.glodon.bim.business.qualityManage.bean;

import java.util.List;

/**
 * 描述：复查单保存后的信息
 * 作者：zhourf on 2017/11/6
 * 邮箱：zhourf@glodon.com
 */

public class QualityGetReviewInfo {
    public int buildingId;
    public String buildingName;
    public String code;
    public String description;
    public String elementId;
    public String elementName;
    public List<QualityCheckListBeanItemFile> files;
    public String gdocFileId;
    public long id;

    public String inspectionCode;
    public long inspectionId;
    /**
     * 整改期限
     */
    public String lastRectificationDate;
    public long projectId;
    public String projectName;
    public String qcState;
    public String rectificationCode;
    public long rectificationId;
    public String reviewDate;
    /**
     * 复查合格 "closed"
     *
     * 复查不合格  "notAccepted"
     */
    public String status;
}
