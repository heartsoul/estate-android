package com.glodon.bim.business.qualityManage.listener;

import com.glodon.bim.business.qualityManage.bean.CreateCheckListParamsFile;

import java.util.List;

/**
 * 描述：图片上传的监听
 * 作者：zhourf on 2017/11/9
 * 邮箱：zhourf@glodon.com
 */

public interface OnUploadImageListener {

    void onUploadFinished(List<CreateCheckListParamsFile> list);

    void onUploadError(Throwable t);
}
