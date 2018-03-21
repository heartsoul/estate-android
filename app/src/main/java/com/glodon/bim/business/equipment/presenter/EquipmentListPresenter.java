package com.glodon.bim.business.equipment.presenter;

import android.app.Activity;
import android.content.Intent;
import android.os.SystemClock;
import android.text.TextUtils;

import com.glodon.bim.basic.log.LogUtil;
import com.glodon.bim.basic.utils.DateUtil;
import com.glodon.bim.basic.utils.NetWorkUtils;
import com.glodon.bim.basic.utils.SharedPreferencesUtil;
import com.glodon.bim.business.equipment.bean.CreateEquipmentParams;
import com.glodon.bim.business.equipment.bean.EquipmentListBean;
import com.glodon.bim.business.equipment.bean.EquipmentListBeanItem;
import com.glodon.bim.business.equipment.bean.EquipmentNumBean;
import com.glodon.bim.business.equipment.contract.EquipmentListContract;
import com.glodon.bim.business.equipment.listener.OnOperateEquipmentSheetListener;
import com.glodon.bim.business.equipment.model.CreateEquipmentModel;
import com.glodon.bim.business.equipment.model.EquipmentListModel;
import com.glodon.bim.business.equipment.view.CreateEquipmentActivity;
import com.glodon.bim.business.main.bean.ProjectListItem;
import com.glodon.bim.business.main.contract.QualityEquipmentSearchContract;
import com.glodon.bim.business.main.model.QualityEquipmentSearchModel;
import com.glodon.bim.business.qualityManage.bean.ClassifyNum;
import com.glodon.bim.business.qualityManage.bean.CreateCheckListParamsFile;
import com.glodon.bim.business.qualityManage.bean.QualityCheckListBeanItemFile;
import com.glodon.bim.common.config.CommonConfig;
import com.glodon.bim.common.config.RequestCodeConfig;
import com.glodon.bim.customview.ToastManager;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
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
    private List<EquipmentListBeanItem> mDataList;
    private String mQcState = CommonConfig.QC_STATE_ALL;
    private int mCurrentPage = 0;
    private int mSize = 20;
    private List<String> mDateKeyList = new ArrayList<>();  //标记分隔时间
    private List<EquipmentListBeanItem> mList = new ArrayList<>(); //添加了分隔时间的数据
    private String searchKey = null;//搜索关键字

    private OnOperateEquipmentSheetListener mListener = new OnOperateEquipmentSheetListener() {
        @Override
        public void delete(EquipmentListBeanItem item, int position) {
            if (NetWorkUtils.isNetworkAvailable(mView.getActivity())) {
                if (mView != null) {
                    mView.showLoadingDialog();
                }
                Subscription sub = new CreateEquipmentModel().delete(item.id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<ResponseBody>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                if (mView != null) {
                                    mView.dismissLoadingDialog();
                                }
                            }

                            @Override
                            public void onNext(ResponseBody responseBody) {
                                if (mView != null) {
                                    mView.dismissLoadingDialog();
                                }
                                pullDown();
                            }
                        });
                mSubscription.add(sub);
            } else {
                ToastManager.showNetWorkToast();
            }
        }

        @Override
        public void detail(EquipmentListBeanItem item, int position) {
            Intent intent = new Intent(mView.getActivity(), CreateEquipmentActivity.class);
            intent.putExtra(CommonConfig.EQUIPMENT_TYPE, CommonConfig.EQUIPMENT_TYPE_DETAIL);
            intent.putExtra(CommonConfig.EQUIPMENT_LIST_ID, item.id);
            mView.getActivity().startActivity(intent);
        }

        @Override
        public void submit(EquipmentListBeanItem item, int position) {
            if (NetWorkUtils.isNetworkAvailable(mView.getActivity())) {
                if (mView != null) {
                    mView.showLoadingDialog();
                }
                CreateEquipmentParams params = new CreateEquipmentParams();
                params.code = item.code;
                params.projectId = item.projectId;
                params.projectName = item.projectName;
                params.batchCode = item.batchCode;
                params.facilityCode = item.facilityCode;
                params.facilityName = item.facilityName;
                params.approachDate = item.approachDate;
                params.quantity = item.quantity;
                params.unit = item.unit;
                params.specification = item.specification;
                params.modelNum = item.modelNum;
                params.manufacturer = item.manufacturer;
                params.brand = item.brand;
                params.supplier = item.supplier;
                params.versionId = item.versionId;
                params.gdocFileId = item.gdocFileId;
                params.buildingId = item.buildingId;
                params.buildingName = item.buildingName;
                params.elementId = item.elementId;
                params.elementName = item.elementName;
                List<CreateCheckListParamsFile> fileList = new ArrayList<>();
                for (QualityCheckListBeanItemFile file : item.files) {
                    CreateCheckListParamsFile f = new CreateCheckListParamsFile();

                    f.extData = file.extData;
                    f.objectId = file.objectId;
                    f.name = file.name;
                    f.url = file.url;
                    fileList.add(f);
                }
                if (fileList.size() > 0) {
                    params.files = fileList;
                }
                params.qualified = item.qualified;
                Subscription sub = new CreateEquipmentModel().editSubmit(item.id, params)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<ResponseBody>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                if (mView != null) {
                                    mView.dismissLoadingDialog();
                                }
                            }

                            @Override
                            public void onNext(ResponseBody responseBody) {
                                ToastManager.showSubmitToast();
                                if (mView != null) {
                                    mView.dismissLoadingDialog();
                                    pullDown();
                                }
                            }
                        });
                mSubscription.add(sub);
            } else {
                ToastManager.showNetWorkToast();
            }
        }

        @Override
        public void toEdit(EquipmentListBeanItem item, int position) {
            Intent intent = new Intent(mView.getActivity(), CreateEquipmentActivity.class);
            intent.putExtra(CommonConfig.EQUIPMENT_TYPE, CommonConfig.EQUIPMENT_TYPE_EDIT);
            intent.putExtra(CommonConfig.EQUIPMENT_LIST_ID, item.id);
            mView.getActivity().startActivityForResult(intent, RequestCodeConfig.REQUEST_CODE_EQUIPMENT_TO_EDIT);
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
        getDataList();
    }

    @Override
    public void search(String searchKey) {
        if (!TextUtils.isEmpty(searchKey)) {
            this.searchKey = searchKey;
            getDataList();
        }
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
        if (NetWorkUtils.isNetworkAvailable(mView.getActivity())) {
            if (mView != null) {
                mView.showLoadingDialog();
            }
            if (!TextUtils.isEmpty(searchKey)) {
                getSearchData();
                return;
            }
            Subscription sub = mModel.getAllEquipmentList(mCurrentPage, mSize, mQcState)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<EquipmentListBean>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            LogUtil.e("----", e.getMessage());
                            if (mView != null) {
                                mView.dismissLoadingDialog();
                            }
                        }

                        @Override
                        public void onNext(EquipmentListBean bean) {
                            LogUtil.toJson(bean);
                            handleResult(bean);
                        }
                    });
            mSubscription.add(sub);
        } else {
            ToastManager.showNetWorkToast();
        }
    }

    /**
     * 搜索数据
     */
    private void getSearchData(){
        QualityEquipmentSearchContract.Model model = new QualityEquipmentSearchModel();
        Subscription sub = model.searchEquipmentData(SharedPreferencesUtil.getProjectId(),searchKey,mCurrentPage, mSize)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<EquipmentListBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e("----", e.getMessage());
                        if (mView != null) {
                            mView.dismissLoadingDialog();
                        }
                    }

                    @Override
                    public void onNext(EquipmentListBean bean) {
                        LogUtil.toJson(bean);
                        handleResult(bean);
                    }
                });
        mSubscription.add(sub);
    }

    private void handleResult(EquipmentListBean bean) {
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

    private void handleDate() {
        mDateKeyList.clear();
        mList.clear();
        if (mDataList != null && mDataList.size() > 0) {
            for (EquipmentListBeanItem item : mDataList) {
                String now = DateUtil.getListDate(Long.parseLong(item.updateTime));
                if (CommonConfig.QC_STATE_EDIT.equals(item.getQcState())) {
                    item.showType = 1;
                } else {
                    item.showType = 2;
                }
                if (mDateKeyList.contains(now)) {
                    mList.add(item);
                } else {
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

    private void updateStatusNum() {
        Subscription sub = mModel.getEquipmentListNum()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<EquipmentNumBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(e.getMessage());
                    }

                    @Override
                    public void onNext(EquipmentNumBean bean) {

                        List<ClassifyNum> classifyNa = new ArrayList<>();
                        ClassifyNum commit = new ClassifyNum();
                        commit.qcState = CommonConfig.QC_STATE_EDIT;
                        commit.count = bean.uncommittedCount;
                        classifyNa.add(commit);
                        ClassifyNum standard = new ClassifyNum();
                        standard.qcState = CommonConfig.QC_STATE_STANDARD;
                        standard.count = bean.qualifiedCount;
                        classifyNa.add(standard);
                        ClassifyNum notStandard = new ClassifyNum();
                        notStandard.qcState = CommonConfig.QC_STATE_NOT_STANDARD;
                        notStandard.count = bean.unqualifiedCount;
                        classifyNa.add(notStandard);
                        updateNumber(classifyNa);
                    }
                });
        mSubscription.add(sub);
    }

    private void updateNumber(List<ClassifyNum> classifyNa) {
        if (classifyNa != null) {
            if (classifyNa.size() == 0) {
                //当从有数字切换到无数字时  需要把所有数字置为0
                List<String> keyList = new ArrayList<>();
                keyList.add(CommonConfig.QC_STATE_EDIT);
                keyList.add(CommonConfig.QC_STATE_STANDARD);
                keyList.add(CommonConfig.QC_STATE_NOT_STANDARD);
                for (String state : keyList) {
                    ClassifyNum cn = new ClassifyNum();
                    cn.count = 0;
                    cn.qcState = state;
                    classifyNa.add(cn);
                }
            }
            if (mView != null) {
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
        switch (requestCode) {
            case RequestCodeConfig.REQUEST_CODE_CREATE_EQUIPMENT_MANDATORY://创建材设进场记录
                if (resultCode == Activity.RESULT_OK) {
                    pullDown();
                }
                break;
            case RequestCodeConfig.REQUEST_CODE_EQUIPMENT_TO_EDIT:
                if (resultCode == Activity.RESULT_OK) {
                    pullDown();
                }
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
