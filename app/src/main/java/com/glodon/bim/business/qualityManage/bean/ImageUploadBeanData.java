package com.glodon.bim.business.qualityManage.bean;

/**
 * 描述：图片上传后返回值
 * 作者：zhourf on 2017/11/2
 * 邮箱：zhourf@glodon.com
 */

public class ImageUploadBeanData {
    public String appKey;
    public String appkey;
    public int createTime;
    public String digest;
    public String extension;
    public String id;
    public int length;
    public String name;

    @Override
    public String toString() {
        return "ImageUploadBeanData{" +
                "appKey='" + appKey + '\'' +
                ", appkey='" + appkey + '\'' +
                ", createTime=" + createTime +
                ", digest='" + digest + '\'' +
                ", extension='" + extension + '\'' +
                ", fileId='" + id + '\'' +
                ", length=" + length +
                ", fileName='" + name + '\'' +
                '}';
    }
}
