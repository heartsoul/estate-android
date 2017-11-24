package com.glodon.bim.business.qualityManage.contract;

import com.glodon.bim.base.IBasePresenter;
import com.glodon.bim.base.IBaseView;
import com.glodon.bim.business.qualityManage.bean.BlueprintListBeanItem;
import com.glodon.bim.business.qualityManage.bean.ModuleListBeanItem;
import com.glodon.bim.business.qualityManage.bean.QualityCheckListBean;
import com.glodon.bim.business.qualityManage.listener.OnBlueprintCatalogClickListener;
import com.glodon.bim.business.qualityManage.listener.OnBlueprintHintClickListener;
import com.glodon.bim.business.qualityManage.listener.OnChooseBlueprintCataListener;
import com.glodon.bim.business.qualityManage.listener.OnChooseBlueprintObjListener;

import java.util.List;

import rx.Observable;

/**
 * 描述：质检项目  质检清单
 * 作者：zhourf on 2017/10/9
 * 邮箱：zhourf@glodon.com
 */

public interface QulityCheckModuleContract {

    interface Presenter extends IBasePresenter {

        /**
         * 根据id查询该目录下有多少子类节点
         */
        List<ModuleListBeanItem> getChildList(long id);
        /**
         * 打开相机
         */
        void openPhoto();

        /**
         * 打开相册
         */
        void openAlbum();

        /**
         * 打开创建界面
         */
        void toCreate();

    }

    interface View extends IBaseView {

        /**
         * 刷新质检项目分类列表
         */
        void updateList(List<ModuleListBeanItem> mRootList);
    }

    interface Model {
        /**
         * 获取质检清单列表
         */
        Observable<QualityCheckListBean> getQualityCheckList(long deptId, String qcState, int page, int size, long qualityCheckpointId, String qualityCheckpointName);
    }
}
