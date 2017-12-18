package com.glodon.bim.business.qualityManage.bean;

import java.util.List;

/**
 * 描述：模型构件信息
 * 作者：zhourf on 2017/12/18
 * 邮箱：zhourf@glodon.com
 */

public class ModelElementInfoData {
    public ModelComponentBoundingBox boundingBox;
    public String elementId;
    public String familyGuid;
    public String name;
    public List<ModelElementInfoDataProperty> properties;
}
