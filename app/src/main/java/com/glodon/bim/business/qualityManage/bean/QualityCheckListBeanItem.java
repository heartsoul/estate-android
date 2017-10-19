package com.glodon.bim.business.qualityManage.bean;

import java.util.List;

/**
 * 描述：质检清单列表的item
 * 作者：zhourf on 2017/10/19
 * 邮箱：zhourf@glodon.com
 */

public class QualityCheckListBeanItem {
    public String code;
    public long constructionCompanyId;
    public String constructionCompanyName;
    public String description;
    public List<QualityCheckListBeanItemElement> elementList;
    public List<QualityCheckListBeanItemFile> files;
    public long id;
    public long inspectionCompanyId;
    public String inspectionCompanyName;
    public String inspectionDate;
    public long inspectionProjectId;
    public String inspectionProjectName;
    public String inspectionType;
    public String lastRectificationDate;
    public boolean needRectification;
    public long projectId;
    public String projectName;
    public String versionId;
}
