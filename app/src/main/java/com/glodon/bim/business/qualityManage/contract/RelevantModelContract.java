package com.glodon.bim.business.qualityManage.contract;

import com.glodon.bim.base.IBasePresenter;
import com.glodon.bim.base.IBaseView;
import com.glodon.bim.business.qualityManage.bean.ProjectVersionBean;
import com.glodon.bim.business.qualityManage.bean.RelevantBluePrintToken;

import rx.Observable;

/**
 * 描述：具体模型展示
 * 作者：zhourf on 2017/10/9
 * 邮箱：zhourf@glodon.com
 */

public interface RelevantModelContract {

    interface Presenter extends IBasePresenter {


    }

    interface View extends IBaseView {

        /**
         * 给h5传递基本信息
         * @param token  图纸的token
         */
        void sendBasicInfo(String token);
    }

    interface Model {
        /**
         * 获取当前已发布的最新版本
         */
        Observable<ProjectVersionBean> getLatestVersion(long projectId);

        /**
         * 获取图纸的token
         * @param projectId  项目id
         * @param projectVersionId  项目的版本
         * @param fileId 图纸文件的id
         */
        Observable<RelevantBluePrintToken> getToken(long projectId, String projectVersionId, String fileId);
    }
}
