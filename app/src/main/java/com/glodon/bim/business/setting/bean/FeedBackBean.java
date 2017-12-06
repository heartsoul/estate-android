package com.glodon.bim.business.setting.bean;

/**
 * 描述：意见反馈返回值
 * 作者：zhourf on 2017/12/6
 * 邮箱：zhourf@glodon.com
 */

public class FeedBackBean {
    public long code;
    public String message;
    public FeedBackBeanData data;

    @Override
    public String toString() {
        return "FeedBackBean{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data.toString() +
                '}';
    }
}
