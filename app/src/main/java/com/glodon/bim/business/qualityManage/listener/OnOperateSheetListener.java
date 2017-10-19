package com.glodon.bim.business.qualityManage.listener;

/**
 * 描述：单据操作监听
 * 作者：zhourf on 2017/10/19
 * 邮箱：zhourf@glodon.com
 */

public interface OnOperateSheetListener {

    /**
     * 点击删除
     */
    void delete(int position);

    /**
     * 点击进入详情
     */
    void detail(int position);

    /**
     * 点击提交
     */
    void submit(int position);

    /**
     * 新建整改单
     */
    void repair(int position);

    /**
     * 新建复查单
     */
    void review(int position);
}
