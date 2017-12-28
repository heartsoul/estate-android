package com.glodon.bim.business.setting.bean;

/**
 * 描述：意见反馈返回值
 * 作者：zhourf on 2017/12/6
 * 邮箱：zhourf@glodon.com
 */

public class FeedBackBeanData {
    public String content;
    public String email;
    public String name;
    public String title;
    public long id;

    @Override
    public String toString() {
        return "FeedBackBeanData{" +
                "content='" + content + '\'' +
                ", email='" + email + '\'' +
                ", inspectItem='" + name + '\'' +
                ", title='" + title + '\'' +
                ", id=" + id +
                '}';
    }
}
