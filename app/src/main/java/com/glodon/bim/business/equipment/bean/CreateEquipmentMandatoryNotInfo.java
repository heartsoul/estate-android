package com.glodon.bim.business.equipment.bean;

import com.glodon.bim.business.qualityManage.bean.ModelListBeanItem;

import java.io.Serializable;

/**
 * 描述：新增材设进场记录-非必填项
 * 作者：zhourf on 2018/1/12
 * 邮箱：zhourf@glodon.com
 */

public class CreateEquipmentMandatoryNotInfo implements Serializable{
    public long quantity;//进场数量
    public String unit;//单位
    public String specification;//规格
    public String modelNum;//型号
    public String manufacturer;//厂家
    public String brand;//品牌
    public String supplier;//供应商

    //模型
    public ModelListBeanItem model;
}
