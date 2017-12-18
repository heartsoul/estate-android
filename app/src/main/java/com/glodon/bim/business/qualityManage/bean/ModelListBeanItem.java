package com.glodon.bim.business.qualityManage.bean;

import java.io.Serializable;

/**
 * 描述：模型
 * 作者：zhourf on 2017/11/22
 * 邮箱：zhourf@glodon.com
 */

public class ModelListBeanItem implements Serializable{
    public String fileId;//模型id
    public String fileName;//模型name

    public Long buildingId;//单体id
    public String buildingName;//单体name

    public ModelComponent component;//构件信息
}
