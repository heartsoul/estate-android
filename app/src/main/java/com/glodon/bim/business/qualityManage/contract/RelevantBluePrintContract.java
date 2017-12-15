package com.glodon.bim.business.qualityManage.contract;

import com.glodon.bim.base.IBasePresenter;
import com.glodon.bim.base.IBaseView;
import com.glodon.bim.business.qualityManage.bean.BluePrintDotItem;
import com.glodon.bim.business.qualityManage.bean.ModelListBean;
import com.glodon.bim.business.qualityManage.bean.ModelListBeanItem;
import com.glodon.bim.business.qualityManage.bean.ModelSingleListItem;
import com.glodon.bim.business.qualityManage.bean.ModelSpecialListItem;
import com.glodon.bim.business.qualityManage.bean.ProjectVersionBean;
import com.glodon.bim.business.qualityManage.bean.RelevantBluePrintToken;
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

        /**
         * 获取点的信息
         */
        void getBluePrintDots();
    }

    interface View extends IBaseView {

        /**
         * 给h5传递基本信息
         * @param token  图纸的token
         */
        void sendBasicInfo(String token);

        /**
         * 设置多点数据
         */
        void setDotsData(List<BluePrintDotItem> bluePrintDotItems);
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

        /**
         * 获取历史点数据
         * @param deptId 项目id
         * @param drawingGdocFileId 图纸文件id
         */
        Observable<List<BluePrintDotItem>> getBluePrintDots(long deptId, String drawingGdocFileId);
    }
}
