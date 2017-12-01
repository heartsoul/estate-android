package com.glodon.bim.business.qualityManage.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 描述：模型返回值
 * 作者：zhourf on 2017/11/22
 * 邮箱：zhourf@glodon.com
 */

public class ModelListBean implements Serializable{
    public String code;
    public List<ModelListBeanItem> data;
    public String message;
}
