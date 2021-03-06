package com.glodon.bim.business.qualityManage.bean;

import java.util.List;

/**
 * 描述：检查单详情  检查单数据
 * 作者：zhourf on 2017/11/3
 * 邮箱：zhourf@glodon.com
 */

public class QualityCheckListDetailInspectionInfo {

    public String code;
    //施工单位
    public long constructionCompanyId;
    public String constructionCompanyName;
    public String createTime;//保存时间
    //创建者
    public long creatorId;
    public String creatorName;  //检查单  创建者
    public String description;  //现场描述


    public List<QualityCheckListBeanItemFile> files; //图片

    public long id;
    public long inspectionCompanyId;
    public String inspectionCompanyName;
    public long inspectionDate;   //检查日期
    //检查  或 验收
    public String inspectionType;//inspection检查  acceptance验收
    //岗位
    public String inspectionUserTitle;

    public long lastRectificationDate;   //整改期限
    public boolean needRectification;  //是否需要整改

    public long projectId;
    public String projectName;
    public String qcState;
    //质检项目
    public Long qualityCheckpointId;
    public String qualityCheckpointName;
    //责任人
    public long responsibleUserId;
    public String responsibleUserName;
    public String responsibleUserTitle;
    //提交时间
    public String updateTime;//过期
    public long commitTime;

    //图纸
    public String drawingGdocFileId;//图纸id
    public String drawingName;//图纸名
    public String drawingPositionX;//位置的x信息
    public String drawingPositionY;//位置的y信息

    //模型
    public String gdocFileId;
    //关联模型   构件
    public String elementId;
    public String elementName;
    //模型单体
    public long buildingId;
    public String buildingName;
}
