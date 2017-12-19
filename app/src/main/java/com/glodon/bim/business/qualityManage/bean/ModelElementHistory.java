package com.glodon.bim.business.qualityManage.bean;

/**
 * 描述：模型的历史构件信息
 * 作者：zhourf on 2017/12/18
 * 邮箱：zhourf@glodon.com
 */

public class ModelElementHistory {
    public String elementId;
    public String elementName;
    public String gdocFileId;
    public long inspectionId;
    public String qcState;
    public long rectificationId;
    public long reviewId;

    public double drawingPositionX,drawingPositionY,drawingPositionZ;

    public long responsibleUserId;
    public long inspectionUserId;
}
