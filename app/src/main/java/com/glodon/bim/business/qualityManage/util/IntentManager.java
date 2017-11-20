package com.glodon.bim.business.qualityManage.util;

import android.app.Activity;
import android.content.Intent;

import com.glodon.bim.business.qualityManage.view.ModuleStandardActivity;
import com.glodon.bim.common.config.CommonConfig;

/**
 * 描述：界面跳转
 * 作者：zhourf on 2017/11/20
 * 邮箱：zhourf@glodon.com
 */

public class IntentManager {
    /**
     * 跳转到质检标准
     */
    public static void toModuleStandard(Activity activity,long templateId){
        Intent intent = new Intent(activity, ModuleStandardActivity.class);
        intent.putExtra(CommonConfig.MODULE_TEMPLATEID,templateId);
        activity.startActivity(intent);
    }
}
