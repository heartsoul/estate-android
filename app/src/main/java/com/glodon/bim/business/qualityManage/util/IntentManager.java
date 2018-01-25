package com.glodon.bim.business.qualityManage.util;

import android.app.Activity;
import android.content.Intent;

import com.glodon.bim.business.qualityManage.view.BluePrintModelSearchActivity;
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
    public static void toModuleStandard(Activity activity,long templateId,String title){
        Intent intent = new Intent(activity, ModuleStandardActivity.class);
        intent.putExtra(CommonConfig.MODULE_TEMPLATEID,templateId);
        intent.putExtra(CommonConfig.MODULE_TITLE,title);
        activity.startActivity(intent);
    }

    /**
     * 跳转到搜索模型或图纸
     * @param type  0图纸  1模型
     */
    public static void toBluePrintModelSearch(Activity activity,int type){
        Intent intent = new Intent(activity,BluePrintModelSearchActivity.class);
        intent.putExtra(CommonConfig.SEARCH_TYPE,type);
        activity.startActivity(intent);
    }


}
