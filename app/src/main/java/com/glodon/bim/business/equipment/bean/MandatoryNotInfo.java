package com.glodon.bim.business.equipment.bean;

import com.glodon.bim.business.qualityManage.bean.ModelListBeanItem;

import java.io.Serializable;

/**
 * 描述：新增材设进场记录-非必填项
 * 作者：zhourf on 2018/1/12
 * 邮箱：zhourf@glodon.com
 */

public class MandatoryNotInfo implements Serializable{
    public long num;
    public String unit;
    public String spec;
    public String modelnum;
    public String factory;
    public String make;
    public String supplier;

    //模型
    public ModelListBeanItem model;
}
