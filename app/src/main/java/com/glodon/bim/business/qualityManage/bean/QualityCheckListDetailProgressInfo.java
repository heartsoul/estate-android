package com.glodon.bim.business.qualityManage.bean;

import java.util.List;

/**
 * 描述：检查单详情  历史整改复查数据
 * 作者：zhourf on 2017/11/3
 * 邮箱：zhourf@glodon.com
 */

public class QualityCheckListDetailProgressInfo {
    public String billType; //单据类型
    public String code;
    public String description; //描述
    public List<QualityCheckListBeanItemFile> files; //图片
    public String handleDate;//处理日期
    public String handlerName;//处理者
    public int id;
    public String lastRectificationDate;//整改期限
    public String updateTime;//提交时间
}