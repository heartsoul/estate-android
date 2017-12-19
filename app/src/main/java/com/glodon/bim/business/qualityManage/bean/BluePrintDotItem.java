package com.glodon.bim.business.qualityManage.bean;

/**
 * 描述：图纸-历史检查单
 * 作者：zhourf on 2017/12/15
 * 邮箱：zhourf@glodon.com
 */

public class BluePrintDotItem {
    public String drawingGdocFileId;
    public String drawingPositionX;
    public String drawingPositionY;
    public long inspectionId;
    public String qcState;
    public long rectificationId;
    public long reviewId;

    public long responsibleUserId;
    public long inspectionUserId;
}
