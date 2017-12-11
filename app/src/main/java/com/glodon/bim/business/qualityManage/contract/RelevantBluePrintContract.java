package com.glodon.bim.business.qualityManage.contract;

import com.glodon.bim.base.IBasePresenter;
import com.glodon.bim.base.IBaseView;
import com.glodon.bim.business.qualityManage.bean.ModelListBean;
import com.glodon.bim.business.qualityManage.bean.ModelListBeanItem;
import com.glodon.bim.business.qualityManage.bean.ModelSingleListItem;
import com.glodon.bim.business.qualityManage.bean.ModelSpecialListItem;
import com.glodon.bim.business.qualityManage.bean.ProjectVersionBean;
import com.glodon.bim.business.qualityManage.model.OnModelSelectListener;

import java.util.List;

import rx.Observable;

/**
 * 描述：具体图纸展示
 * 作者：zhourf on 2017/10/9
 * 邮箱：zhourf@glodon.com
 */

public interface RelevantBluePrintContract {

    interface Presenter extends IBasePresenter {


    }

    interface View extends IBaseView {

        /**
         * 给h5传递基本信息
         * @param mProjectId  项目id
         * @param mProjectVersionId  项目最新版本
         * @param mFileId  图纸文件的id
         */
        void sendBasicInfo(long mProjectId, String mProjectVersionId, String mFileId);
    }

    interface Model {
        /**
         * 获取当前已发布的最新版本
         */
        Observable<ProjectVersionBean> getLatestVersion(long projectId);
    }
}
