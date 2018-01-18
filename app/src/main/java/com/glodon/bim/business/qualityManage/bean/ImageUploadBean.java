package com.glodon.bim.business.qualityManage.bean;

/**
 * 描述：图片上传后返回值
 * 作者：zhourf on 2017/11/2
 * 邮箱：zhourf@glodon.com
 */

public class ImageUploadBean {
    public int code;
    public ImageUploadBeanData data;
    public String message;

    @Override
    public String toString() {
        return "ImageUploadBean{" +
                "facilityCode=" + code +
                ", data=" + data.toString() +
                ", message='" + message + '\'' +
                '}';
    }
}
