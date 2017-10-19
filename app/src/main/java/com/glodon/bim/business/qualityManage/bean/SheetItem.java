package com.glodon.bim.business.qualityManage.bean;

/**
 * 描述：单据item
 * 作者：zhourf on 2017/10/19
 * 邮箱：zhourf@glodon.com
 */

public class SheetItem {
    public int showType;//0时间  1单据
    public int timeType;//0今天 1之前
    public int sheetStatus;//0待提交 1待整改 2待复查 3 已整改 4已复查 5已延迟 6已验收
}
