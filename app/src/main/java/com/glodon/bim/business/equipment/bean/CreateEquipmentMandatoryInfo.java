package com.glodon.bim.business.equipment.bean;

import java.io.Serializable;

/**
 * 描述：新增材设进场记录-必填项
 * 作者：zhourf on 2018/1/9
 * 邮箱：zhourf@glodon.com
 */

public class CreateEquipmentMandatoryInfo implements Serializable{
    public long acceptanceCompanyId;//验收单位
    public String acceptanceCompanyName;//验收单位
    public String batchCode;//批次编号
    public String facilityCode;//材设编码
    public String facilityName;//材设名称
    public String approachDate;//进场日期

}
