package com.glodon.bim.business.qualityManage.bean;

/**
 * 描述：根据模型获取的材设的单据历史
 * 作者：zhourf on 2018/1/22
 * 邮箱：zhourf@glodon.com
 */

public class EquipmentHistoryItem {
    public boolean committed;
    public String elementId;
    public String elementName;
    public long facilityId;//单据id
    public boolean qualified;

    public double drawingPositionX,drawingPositionY,drawingPositionZ;

    public String qcState;

    public EquipmentHistoryItem(boolean committed, String elementId, String elementName, long facilityId, boolean qualified) {
        this.committed = committed;
        this.elementId = elementId;
        this.elementName = elementName;
        this.facilityId = facilityId;
        this.qualified = qualified;
    }
}
