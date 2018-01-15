package com.glodon.bim.business.equipment.bean;

import com.glodon.bim.basic.utils.LinkedHashList;
import com.glodon.bim.customview.album.ImageItem;

import java.io.Serializable;

/**
 * 描述：新增材设进场记录-图片
 * 作者：zhourf on 2018/1/15
 * 邮箱：zhourf@glodon.com
 */

public class CreateEquipmentPictureInfo implements Serializable{
    public LinkedHashList<String, ImageItem> mSelectedMap;

    public boolean isUpToStandard;
}
