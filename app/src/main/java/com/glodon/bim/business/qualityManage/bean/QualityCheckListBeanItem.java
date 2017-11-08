package com.glodon.bim.business.qualityManage.bean;

import java.util.List;

/**
 * 描述：单据item
 * 作者：zhourf on 2017/10/19
 * 邮箱：zhourf@glodon.com
 */

public class QualityCheckListBeanItem {

    public String code;
    public String description;
    public List<QualityCheckListBeanItemFile> files;
    public long id;
    public String inspectionDate;
    public String inspectionType;
    public String lastRectificationDate;
    public boolean needRectification;
    public long projectId;
    public String qcState;//质检状态
    public String updateTime;
    /** 检查人 */
    public long creatorId;
    /** 施工单位责任人id */
    public long responsibleUserId;

//    public long constructionCompanyId;
//    public String constructionCompanyName;
//    public List<QualityCheckListBeanItemElement> elementList;
//    public long inspectionCompanyId;
//    public String inspectionCompanyName;
//    public long inspectionProjectId;
//    public String inspectionProjectName;
//    public String projectName;
//    public String versionId;

    public int showType = 1;//卡片样式，0时间  1单据
    public int timeType;//时间类型，0今天 1之前
    public int sheetStatus = 1;//0待提交 1待整改 2待复查 3 已整改 4已复查 5已延迟 6已验收
}
