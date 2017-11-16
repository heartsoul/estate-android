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
    //根据此id拿头像
    public long handlerId;
    public String handlerName;//处理者
    public String handlerTitle;//处理者岗位
    public long id;
    public String lastRectificationDate;//整改期限
    public String updateTime;//最新时间
    public long commitTime;
}
