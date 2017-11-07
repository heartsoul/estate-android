package com.glodon.bim.business.qualityManage.bean;

import java.util.List;

/**
 * 描述：保存提交时整改单参数
 * 作者：zhourf on 2017/11/6
 * 邮箱：zhourf@glodon.com
 */

public class QualityRepairParams {
//    public int buildingId;
//    public String buildingName;
    /**
     * 当前时间戳
     */
    public String code;
    /**
     * 描述
     */
    public String description;
//    public String elementId;
//    public String elementName;
    /**
     * 图片
     */
    public List<CreateCheckListParamsFile> files;
    /**
     * 最后一条如果是复查单  传此code
     * 如果没有  则是检查单的code
     */
//    public String flawCode;
    public long flawId;

//    public String inspectionCode;
//    public int inspectionCompanyId;
    /**
     * 检查单的id
     */
    public long inspectionId;
//    public int projectId;
//    public String projectName;
//    public String rectificationDate;
}
