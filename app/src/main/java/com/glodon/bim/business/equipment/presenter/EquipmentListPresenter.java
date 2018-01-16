package com.glodon.bim.business.equipment.presenter;

import android.content.Intent;
import android.os.SystemClock;

import com.glodon.bim.basic.log.LogUtil;
import com.glodon.bim.basic.utils.DateUtil;
import com.glodon.bim.basic.utils.NetWorkUtils;
import com.glodon.bim.business.equipment.bean.EquipmentListBean;
import com.glodon.bim.business.equipment.bean.EquipmentListBeanItem;
import com.glodon.bim.business.equipment.contract.EquipmentListContract;
import com.glodon.bim.business.equipment.listener.OnOperateEquipmentSheetListener;
import com.glodon.bim.business.equipment.model.EquipmentListModel;
import com.glodon.bim.business.main.bean.ProjectListItem;
import com.glodon.bim.business.qualityManage.bean.ClassifyNum;
import com.glodon.bim.common.config.CommonConfig;
import com.glodon.bim.common.config.RequestCodeConfig;
import com.glodon.bim.customview.ToastManager;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * 描述：材设进场记录-清单
 * 作者：zhourf on 2018/1/15
 * 邮箱：zhourf@glodon.com
 */

public class EquipmentListPresenter implements EquipmentListContract.Presenter {
    private EquipmentListContract.View mView;
    private EquipmentListContract.Model mModel;
    private CompositeSubscription mSubscription;
    private ProjectListItem mProjectInfo;
    private List<EquipmentListBeanItem> mDataList;
    private String mQcState = "";
    private int mCurrentPage = 0;
    private int mSize = 20;
    private List<String> mDateKeyList = new ArrayList<>();  //标记分隔时间
    private List<EquipmentListBeanItem> mList = new ArrayList<>(); //添加了分隔时间的数据
    private int mClickPosition = 0;
    private OnOperateEquipmentSheetListener mListener = new OnOperateEquipmentSheetListener() {
        @Override
        public void delete(EquipmentListBeanItem item, int position) {

        }

        @Override
        public void detail(EquipmentListBeanItem item, int position) {

        }

        @Override
        public void submit(EquipmentListBeanItem item, int position) {

        }

        @Override
        public void toEdit(EquipmentListBeanItem item, int position) {

        }
    };

    public EquipmentListPresenter(EquipmentListContract.View mView) {
        this.mView = mView;
        mModel = new EquipmentListModel();
        mSubscription = new CompositeSubscription();
        mDataList = new ArrayList<>();
    }

    @Override
    public void initData(ProjectListItem projectInfo) {
        mProjectInfo = projectInfo;
        getDataList();
    }



    @Override
    public void pullDown() {
        mCurrentPage = 0;
        mDataList.clear();
        getDataList();
    }

    /**
     * 初始化数据
     */
    private void getDataList() {
        if(NetWorkUtils.isNetworkAvailable(mView.getActivity())) {
            if (mView != null) {
                mView.showLoadingDialog();
            }
//            Subscription sub = mModel.getEquipmentList(mProjectInfo.deptId, mQcState, mCurrentPage, mSize)
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new Subscriber<EquipmentListBean>() {
//                        @Override
//                        public void onCompleted() {
//
//                        }
//
//                        @Override
//                        public void onError(Throwable e) {
//                            LogUtil.e("----", e.getMessage());
//                            if (mView != null) {
//                                mView.dismissLoadingDialog();
//                            }
//                        }
//
//                        @Override
//                        public void onNext(EquipmentListBean bean) {
//                            handleResult(bean);
//                        }
//                    });
//            mSubscription.add(sub);
        }else{
            ToastManager.showNetWorkToast();
        }
        EquipmentListBean bean = new EquipmentListBean();
        bean.content = getList();
        handleResult(bean);
    }

    private List<EquipmentListBeanItem> getList(){
        List<EquipmentListBeanItem> list = new ArrayList<>();
        for(int i = 0;i<6;i++){
            EquipmentListBeanItem item = new EquipmentListBeanItem();
            item.name = "name "+i;
            item.code = "code "+i;
            item.index = "index "+i;
            if(i%2==0) {
                item.qcState = CommonConfig.QC_STATE_EDIT;
            }else{
                item.qcState = CommonConfig.QC_STATE_NOT_STANDARD;
                item.isStandard = false;
            }
            item.updateTime = (SystemClock.currentThreadTimeMillis()-i*1000*3600*24)+"";
            list.add(item);
        }
        for(int i = 6;i<10;i++){
            EquipmentListBeanItem item = new EquipmentListBeanItem();
            item.name = "name "+i;
            item.code = "code "+i;
            item.index = "index "+i;
            item.qcState = CommonConfig.QC_STATE_STANDARD;
            item.isStandard = true;
            item.updateTime = (SystemClock.currentThreadTimeMillis()-i*1000*3600*12)+"";
            list.add(item);
        }

        return list;
    }
    private void handleResult(EquipmentListBean bean){
        if (bean != null && bean.content != null && bean.content.size() > 0) {
            mDataList.addAll(bean.content);
            if (mCurrentPage < bean.totalPages) {
                mCurrentPage++;
            }
        }
        if (mView != null) {
            handleDate();
            mView.updateData(mList);
        }
        if (mView != null) {
            mView.dismissLoadingDialog();
        }

        updateStatusNum();
    }

    private void handleDate(){
        mDateKeyList.clear();
        mList.clear();
        if(mDataList!=null && mDataList.size()>0){
            for(EquipmentListBeanItem item :mDataList){
                String now = DateUtil.getListDate(Long.parseLong(item.updateTime));
                if(CommonConfig.QC_STATE_EDIT.equals(item.qcState)){
                    item.showType = 1;
                }else{
                    item.showType = 2;
                }
                if(mDateKeyList.contains(now)){
                    mList.add(item);
                }else{
                    EquipmentListBeanItem bean = new EquipmentListBeanItem();
                    bean.showType = 0;

                    bean.updateTime = item.updateTime;
                    mDateKeyList.add(now);
                    mList.add(bean);
                    mList.add(item);
                }
            }
        }
    }


    @Override
    public void pullUp() {
        getDataList();
    }

    @Override
    public void getClassifyData(String mCurrentState) {
        mCurrentPage = 0;
        mQcState = mCurrentState;
        mDataList.clear();
        getDataList();
    }

    private void updateStatusNum(){
//        Subscription sub = mModel.getStatusNum(mProjectInfo.deptId)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<List<ClassifyNum>>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        LogUtil.e(e.getMessage());
//                    }
//
//                    @Override
//                    public void onNext(List<ClassifyNum> classifyNa) {
//                        updateNumber(classifyNa);
//                    }
//                });
//        mSubscription.add(sub);
        List<ClassifyNum> classifyNa = new ArrayList<>();
        ClassifyNum num = new ClassifyNum();
        num.count = 22;
        num.qcState = CommonConfig.QC_STATE_EDIT;
        classifyNa.add(num);
        ClassifyNum num1 = new ClassifyNum();
        num1.count = 12;
        num1.qcState = CommonConfig.QC_STATE_STANDARD;
        classifyNa.add(num1);
        ClassifyNum num2 = new ClassifyNum();
        num2.count = 3;
        num2.qcState = CommonConfig.QC_STATE_NOT_STANDARD;
        classifyNa.add(num2);
        updateNumber(classifyNa);
    }

    private void updateNumber(List<ClassifyNum> classifyNa){
        if(classifyNa!=null){
            if(classifyNa.size()==0){
                //当从有数字切换到无数字时  需要把所有数字置为0
                List<String> keyList = new ArrayList<>();
                keyList.add(CommonConfig.QC_STATE_EDIT);
                keyList.add(CommonConfig.QC_STATE_STANDARD);
                keyList.add(CommonConfig.QC_STATE_NOT_STANDARD);
                for(String state:keyList){
                    ClassifyNum cn = new ClassifyNum();
                    cn.count = 0;
                    cn.qcState = state;
                    classifyNa.add(cn);
                }
            }
            if(mView!=null)
            {
                mView.updateClassifyCount(classifyNa);
            }
        }
    }

    @Override
    public OnOperateEquipmentSheetListener getListener() {
        return mListener;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case RequestCodeConfig.REQUEST_CODE_CREATE_EQUIPMENT_MANDATORY://创建材设进场记录

                break;
        }
    }

    @Override
    public void onDestroy() {
        if (mSubscription != null) {
            mSubscription.unsubscribe();
            mSubscription = null;
        }
    }

    @Override
    public void initData(Intent intent) {

    }
}
