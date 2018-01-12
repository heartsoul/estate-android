package com.glodon.bim.business.qualityManage.bean;

import java.io.Serializable;

/**
 * 描述：模型构件
 * 作者：zhourf on 2017/12/14
 * 邮箱：zhourf@glodon.com
 */

public class ModelComponent implements Serializable{
    public String objectId;
    public String fileId;
    public String elementId;//构件id
    public ModelComponentBoundingBox boundingBox;
    public int click;
    public ModelComponentWorldPosition worldPosition;
    public ModelComponentClientPosition clientPosition;
    public String eventType;

    public String elementName;//构件name
}
