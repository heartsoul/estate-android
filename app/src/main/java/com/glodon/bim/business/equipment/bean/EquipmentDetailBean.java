package com.glodon.bim.business.equipment.bean;

import com.glodon.bim.business.qualityManage.bean.QualityCheckListBeanItemFile;

import java.util.List;

/**
 * 描述：
 * 作者：zhourf on 2018/1/18
 * 邮箱：zhourf@glodon.com
 */

public class EquipmentDetailBean {

    public long id;
    public String code;//单据编号
    public long projectId;//项目id
    public String projectName;//项目名称

    //模型
    public String gdocFileId;//模型的文件id
    public Long buildingId;//单体id
    public String buildingName;//单体name
    public String elementId;//构件id
    public String elementName;//构件name
    //必填项
    public String batchCode;//批次编号
    public String facilityCode;//材设编码
    public String facilityName;//材设名称
    public String approachDate;//进场日期

    //非必填项
    public String quantity;//进场数量
    public String unit;//单位
    public String specification;//规格
    public String modelNum;//型号
    public String manufacturer;//厂家
    public String brand;//品牌
    public String supplier;//供应商

    //图片
    public List<QualityCheckListBeanItemFile> files;//图片
    public boolean qualified;//是否合格
}
