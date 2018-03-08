package com.glodon.bim.business.equipment.contract;

import com.glodon.bim.base.IBasePresenter;
import com.glodon.bim.base.IBaseView;
import com.glodon.bim.basic.config.AppConfig;
import com.glodon.bim.basic.network.NetRequest;
import com.glodon.bim.business.equipment.bean.CreateEquipmentMandatoryInfo;
import com.glodon.bim.business.equipment.bean.CreateEquipmentMandatoryNotInfo;
import com.glodon.bim.business.equipment.bean.CreateEquipmentPictureInfo;
import com.glodon.bim.business.equipment.bean.EquipmentListBean;
import com.glodon.bim.business.equipment.bean.EquipmentListBeanItem;
import com.glodon.bim.business.equipment.bean.EquipmentNumBean;
import com.glodon.bim.business.equipment.listener.OnOperateEquipmentSheetListener;
import com.glodon.bim.business.equipment.model.EquipmentListApi;
import com.glodon.bim.business.main.bean.ProjectListItem;
import com.glodon.bim.business.qualityManage.bean.ClassifyNum;

import java.util.List;

import rx.Observable;

/**
 * 描述：材设进场记录-清单
 * 作者：zhourf on 2017/10/17
 * 邮箱：zhourf@glodon.com
 */

public interface EquipmentListContract {

    interface View extends IBaseView {
        void updateClassifyCount(List<ClassifyNum> list);

        void create();

        void updateData(List<EquipmentListBeanItem> mDataList);
    }

    interface Presenter extends IBasePresenter {

        void pullDown();

        void pullUp();

        void getClassifyData(String mCurrentState);

        OnOperateEquipmentSheetListener getListener();

        void initData(ProjectListItem mProjectInfo);

        void search(String searchKey);
    }

    interface Model {
        /**
         * 根据id查询详情和保存后的编辑信息
         * 全部
         */
         Observable<EquipmentListBean> getAllEquipmentList(int page,int size,String state);

        /**
         * 根据id查询详情和保存后的编辑信息
         * 合格不合格
         */
         Observable<EquipmentNumBean> getEquipmentListNum();
    }
}
