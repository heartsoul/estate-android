package com.glodon.bim.business.qualityManage.contract;

import com.glodon.bim.base.IBasePresenter;
import com.glodon.bim.base.IBaseView;
import com.glodon.bim.business.main.bean.ProjectListItem;
import com.glodon.bim.business.qualityManage.bean.ClassifyNum;
import com.glodon.bim.business.qualityManage.bean.ModuleListBeanItem;
import com.glodon.bim.business.qualityManage.bean.QualityCheckListBean;
import com.glodon.bim.business.qualityManage.bean.QualityCheckListBeanItem;
import com.glodon.bim.business.qualityManage.listener.OnOperateSheetListener;

import java.util.List;

import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 描述：质量清单列表
 * 作者：zhourf on 2017/10/9
 * 邮箱：zhourf@glodon.com
 */

public interface QualityCheckListContract {

    interface Presenter extends IBasePresenter {

        /**
         * 初始化传参
         */
        void initData(ProjectListItem mProjectInfo);

        /**
         * 获取监听接口
         */
        OnOperateSheetListener getListener();

        /**
         * 下拉刷新
         */
        void pullDown();

        /**
         * 上拉加载
         */
        void pullUp();

        /**
         * 获取分类的数据
         * @param mCurrentState 当前分类
         */
        void getClassifyData(String mCurrentState);

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
         * 选中的质检项目
         */
        void setModuleSelectInfo(ModuleListBeanItem item);
    }

    interface View extends IBaseView {

        /**
         * 刷新列表
         */
        void updateData(List<QualityCheckListBeanItem> mDataList);

        /**
         * 打开相册手机
         */
        void create();

        /**
         * 更新分类数量
         */
        void updateClassifyCount(List<ClassifyNum> list);
    }

    interface Model {
        /**
         * 获取质检清单列表
         * @param deptId  项目id
         * @param page 分页
         * @param size 每页数量
         */
        Observable<QualityCheckListBean> getQualityCheckList(long deptId,String qcState,int page, int size);

        /**
         * 根据质检项目 获取质检清单
         */
         Observable<QualityCheckListBean> getQualityCheckList(long deptId, String qcState,int page, int size, long qualityCheckpointId, String qualityCheckpointName);

        /**
         * 获取各状态数量
         * @param deptId  项目id
         */
        Observable<List<ClassifyNum>> getStatusNum(long deptId);

        /**
         * 获取各状态数量  根据质检项目id和name
         * @param deptId  项目id
         */
        Observable<List<ClassifyNum>> getStatusNum(long deptId,long qualityCheckpointId,String qualityCheckpointName);
    }
}
