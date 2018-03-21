package com.glodon.bim.business.main.contract;

import com.glodon.bim.base.IBasePresenter;
import com.glodon.bim.base.IBaseView;
import com.glodon.bim.business.equipment.bean.EquipmentListBean;
import com.glodon.bim.business.equipment.bean.EquipmentListBeanItem;
import com.glodon.bim.business.equipment.listener.OnOperateEquipmentSheetListener;
import com.glodon.bim.business.qualityManage.bean.QualityCheckListBean;
import com.glodon.bim.business.qualityManage.bean.QualityCheckListBeanItem;
import com.glodon.bim.business.qualityManage.listener.OnOperateSheetListener;

import java.util.List;

import rx.Observable;

/**
 * Created by cwj on 2018/3/6.
 * Description:质检清单、材设混合搜索
 */

public interface QualityEquipmentSearchContract {

    interface View extends IBaseView {

        /**
         * 隐藏搜索历史列表
         */
        void hideHistory();

        /**
         * 显示搜索结果
         */
        void showResult(List<QualityCheckListBeanItem> mQualityData, List<EquipmentListBeanItem> mEquipmentData);

        /**
         * 打开相册手机
         */
        void create();
    }

    interface Presenter extends IBasePresenter {

        /**
         * 搜索事件
         *
         * @param key
         */
        void search(String key);

        /**
         * 操作质检清单
         *
         * @return
         */
        OnOperateSheetListener getOnOperateSheetListener();

        /**
         * 操作材设清单
         *
         * @return
         */
        OnOperateEquipmentSheetListener getOperateEquipmentSheetListener();

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

        /**
         * 查看更多质检清单
         */
        void searchMoreQualityCheckList();

        /**
         * 查看更多材设清单
         */
        void searchMoreEquipmentList();
    }

    interface Model {
        //搜索质检清单
        Observable<QualityCheckListBean> searchQualityData(long deptId, String keywords, int page, int size);

        //搜索材设清单
        Observable<EquipmentListBean> searchEquipmentData(long deptId, String keywords, int page, int size);

    }

}


