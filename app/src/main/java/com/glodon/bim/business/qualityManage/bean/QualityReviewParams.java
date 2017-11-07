package com.glodon.bim.business.qualityManage.bean;

import java.util.List;

/**
 * 描述：保存提交时复查单参数
 * 作者：zhourf on 2017/11/6
 * 邮箱：zhourf@glodon.com
 */

public class QualityReviewParams {
//    public int buildingId;
//    public String buildingName;
    /**
     * 当前时间戳
     */
    public String code;
    /**
     * 描述信息
     */
    public String description;
//    public String elementId;
//    public String elementName;
    public List<CreateCheckListParamsFile> files;
//    public String gdocFileId;
//    public String inspectionCode;
//    public int inspectionCompanyId;
    /**
     * 检查单的id
     */
    public long inspectionId;
    /**
     * 整改期限
     */
    public String lastRectificationDate;
//    public int projectId;
//    public String projectName;
    /**
     * 最后一条整改单的code
     */
//    public String rectificationCode;
    /**
     * 复查单对应的整改单的id
     */
    public long rectificationId;
//    public long responsibleUserId;
//    public String responsibleUserName;
//    public String reviewDate;
    /**
     * 复查合格 "closed"
     *
     * 复查不合格  "notAccepted"
     */
    public String status;
}
