package com.glodon.bim.business.qualityManage.contract;

import com.glodon.bim.base.IBasePresenter;
import com.glodon.bim.base.IBaseView;
import com.glodon.bim.business.qualityManage.bean.EquipmentHistoryItem;
import com.glodon.bim.business.qualityManage.bean.ModelElementHistory;
import com.glodon.bim.business.qualityManage.bean.ModelElementInfo;
import com.glodon.bim.business.qualityManage.bean.ProjectVersionBean;
import com.glodon.bim.business.qualityManage.bean.RelevantBluePrintToken;

import java.util.List;

import rx.Observable;

/**
 * 描述：具体模型展示
 * 作者：zhourf on 2017/10/9
 * 邮箱：zhourf@glodon.com
 */

public interface RelevantModelContract {

    interface Presenter extends IBasePresenter {

        /**
         * 获取质量历史的构件信息
         */
        void getElements();

        /**
         * 获取材设单据信息
         */
        void getEquipmentList();

        /**
         * 查看详情
         */
        void detail(EquipmentHistoryItem item);
    }

    interface View extends IBaseView {

        /**
         * 给h5传递基本信息
         * @param token  图纸的token
         */
        void sendBasicInfo(String token);

        /**
         * 展示历史构件信息
         */
        void showModelHistory(List<ModelElementHistory> list);

        /**
         * 获取token失败
         */
        void showTokenError();

        /**
         * 展示材设列表颜色
         */
        void showEquipmentList(List<EquipmentHistoryItem> items);

        /**
         * 清除点的信息
         */
        void clearDots();
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
         * App端查询模型文件对应的所有关联构件
         */
         Observable<List<ModelElementHistory>> getElements(long deptId, String gdocFileId);

        /**
         * 根据构件id获取构件名称
         */
         Observable<ModelElementInfo> getElementProperty(long projectId, String versionId, String fileId, String elementId);

        /**
         * 根据模型获取材设单据列表
         */
        Observable<List<EquipmentHistoryItem>> getEquipmentList(long deptId, String gdocFileId);
    }
}
