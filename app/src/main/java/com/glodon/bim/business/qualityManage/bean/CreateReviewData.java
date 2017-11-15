package com.glodon.bim.business.qualityManage.bean;

import com.glodon.bim.customview.album.TNBImageItem;

import java.util.LinkedHashMap;

/**
 * 描述：在新建复查单和整改单页面验证参数是否发生变化
 * 作者：zhourf on 2017/11/15
 * 邮箱：zhourf@glodon.com
 */

public class CreateReviewData {
    public String des;
    public LinkedHashMap<String, TNBImageItem> mInitMap;
    public boolean flag = false;
    public String time;
}
