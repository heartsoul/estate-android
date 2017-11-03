package com.glodon.bim.business.qualityManage.bean;

import java.util.List;

/**
 * 描述：检查单详情  检查单数据
 * 作者：zhourf on 2017/11/3
 * 邮箱：zhourf@glodon.com
 */

public class QualityCheckListDetailInspectionInfo {
    public String code;
    public String creatorName;  //检查单  创建者
    public String description;  //现场描述
    public List<QualityCheckListBeanItemFile> files; //图片
    public int id;
    public String inspectionDate;   //检查日期
    public String inspectionType;
    public String lastRectificationDate;   //整改期限
    public boolean needRectification;  //是否需要整改
    public int projectId;
    public String qcState;
    public String updateTime;
}
