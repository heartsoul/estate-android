package com.glodon.bim.business.qualityManage.presenter;

import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;

import com.glodon.bim.basic.log.LogUtil;
import com.glodon.bim.basic.utils.LinkedHashList;
import com.glodon.bim.basic.utils.NetWorkUtils;
import com.glodon.bim.basic.utils.SharedPreferencesUtil;
import com.glodon.bim.business.equipment.bean.CreateEquipmentParams;
import com.glodon.bim.business.equipment.bean.EquipmentDetailBean;
import com.glodon.bim.business.equipment.model.CreateEquipmentModel;
import com.glodon.bim.business.equipment.view.CreateEquipmentActivity;
import com.glodon.bim.business.qualityManage.bean.CreateCheckListParamsFile;
import com.glodon.bim.business.qualityManage.bean.EquipmentHistoryItem;
import com.glodon.bim.business.qualityManage.bean.ModelComponentWorldPosition;
import com.glodon.bim.business.qualityManage.bean.ModelElementHistory;
import com.glodon.bim.business.qualityManage.bean.ModelElementInfo;
import com.glodon.bim.business.qualityManage.bean.ProjectVersionBean;
import com.glodon.bim.business.qualityManage.bean.QualityCheckListBeanItemFile;
import com.glodon.bim.business.qualityManage.bean.RelevantBluePrintToken;
import com.glodon.bim.business.qualityManage.contract.RelevantModelContract;
import com.glodon.bim.business.qualityManage.model.RelevantModelModel;
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
 * 描述：具体模型展示
 * 作者：zhourf on 2017/12/11
 * 邮箱：zhourf@glodon.com
 */

public class RelevantModelPresenter implements RelevantModelContract.Presenter {
    private RelevantModelContract.View mView;
    private RelevantModelContract.Model mModel;
    private CompositeSubscription mSubscription;
    private long mProjectId;
    private String mProjectVersionId;
    private String mFileId; //模型的id
    private List<ModelElementHistory> mElementHistories;
    private Handler mhandler =new Handler();
    private LinkedHashList<String,ModelElementHistory> mPositionMap;
    //材设历史清单
    private List<EquipmentHistoryItem> mEquipmentHistories;
    private LinkedHashList<String,EquipmentHistoryItem> mEquipmentPositionMap;

    public RelevantModelPresenter(RelevantModelContract.View mView) {
        this.mView = mView;
        mModel = new RelevantModelModel();
        mSubscription = new CompositeSubscription();
        mPositionMap = new LinkedHashList<>();
        mEquipmentPositionMap = new LinkedHashList<>();
    }

    @Override
    public void initData(Intent intent) {
        mProjectId = SharedPreferencesUtil.getProjectId();
        mFileId = intent.getStringExtra(CommonConfig.BLUE_PRINT_FILE_ID);
        getLatestVersion();
    }

    private void getLatestVersion() {
        if (NetWorkUtils.isNetworkAvailable(mView.getActivity())) {
            if (mView != null) {
                mView.showLoadingDialog();
            }
            Subscription sub = mModel.getLatestVersion(SharedPreferencesUtil.getProjectId())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<ProjectVersionBean>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            LogUtil.e(e.getMessage());
                            if (mView != null) {
                                mView.dismissLoadingDialog();
                            }
                        }

                        @Override
                        public void onNext(ProjectVersionBean projectVersionBean) {
                            if (projectVersionBean != null && projectVersionBean.data != null) {
                                mProjectVersionId = projectVersionBean.data.versionId;
                                getTokey(mProjectId, mProjectVersionId, mFileId);
                            }
                            if (mView != null) {
                                mView.dismissLoadingDialog();
                            }
                        }
                    });
            mSubscription.add(sub);
        } else {
            ToastManager.showNetWorkToast();
        }
    }

    private void getTokey(long mProjectId, String mProjectVersionId, String mFileId) {
        Subscription sub = mModel.getToken(mProjectId, mProjectVersionId, mFileId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<RelevantBluePrintToken>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(e.getMessage());
                        if(mView!=null){
                            mView.showTokenError();
                        }
                    }

                    @Override
                    public void onNext(RelevantBluePrintToken bean) {
                        if(bean!=null){
                            LogUtil.e("info="+bean.toString());
                        }
                        if(bean!=null && !TextUtils.isEmpty(bean.data)){

                            if(mView!=null){
                                mView.sendBasicInfo(bean.data);
                            }
                        }
                    }
                });
        mSubscription.add(sub);
    }

    public void getElements(){
        Subscription sub = mModel.getElements(mProjectId,mFileId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<ModelElementHistory>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(e.getMessage());
                    }

                    @Override
                    public void onNext(List<ModelElementHistory> modelElementHistories) {
                        LogUtil.e("size="+(modelElementHistories==null));
                        if(modelElementHistories!=null){
                            LogUtil.e("size="+modelElementHistories.size());

                        }
                        if(modelElementHistories!=null && modelElementHistories.size()>0){
                            mElementHistories = modelElementHistories;
                            int i = 0;
                            for(final ModelElementHistory element:modelElementHistories)
                            {
                                mhandler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        getElementName(element,element.gdocFileId,element.elementId);
                                    }
                                },20*i);
                                i++;
                            }
                        }
                    }
                });
        mSubscription.add(sub);
    }

    @Override
    public void getEquipmentList() {
//        Subscription sub = mModel.getEquipmentList(mProjectId,mFileId)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<List<EquipmentHistoryItem>>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onNext(List<EquipmentHistoryItem> items) {
//                        handleEquipmentlist(items);
//                    }
//                });
//        mSubscription.add(sub);
        List<EquipmentHistoryItem> items = new ArrayList<>();
        EquipmentHistoryItem item1 = new EquipmentHistoryItem(false,"332707","name1",1,true);
        EquipmentHistoryItem item2 = new EquipmentHistoryItem(true,"333105","name1",2,true);
        EquipmentHistoryItem item3 = new EquipmentHistoryItem(true,"313096","name1",3,false);
        items.add(item1);
        items.add(item2);
        items.add(item3);
        handleEquipmentlist(items);
    }

    @Override
    public void submit(EquipmentHistoryItem item) {
        if(NetWorkUtils.isNetworkAvailable(mView.getActivity())){
            if(mView!=null){
                mView.showLoadingDialog();
            }
            //获取详情
            Subscription sub = new CreateEquipmentModel().detail(item.facilityId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<EquipmentDetailBean>() {
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
                        public void onNext(EquipmentDetailBean bean) {
                            if(bean!=null){
                                toSubmit(bean);
                            }else{
                                if (mView != null) {
                                    mView.dismissLoadingDialog();
                                }
                            }

                        }
                    });
            mSubscription.add(sub);
        }else{
            ToastManager.showNetWorkToast();
        }
    }

    //提交
    private void toSubmit(EquipmentDetailBean item) {
        if (NetWorkUtils.isNetworkAvailable(mView.getActivity())) {

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
            params.versionId = SharedPreferencesUtil.getProjectVersionId();
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
                                mView.clearDots();
                            }
                            getEquipmentList();
                        }
                    });
            mSubscription.add(sub);
        } else {
            ToastManager.showNetWorkToast();
        }
    }

    @Override
    public void delete(EquipmentHistoryItem item) {
        if (NetWorkUtils.isNetworkAvailable(mView.getActivity())) {
            if (mView != null) {
                mView.showLoadingDialog();
            }
            Subscription sub = new CreateEquipmentModel().delete(item.facilityId)
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
                                mView.clearDots();
                            }
                            getEquipmentList();

                        }
                    });
            mSubscription.add(sub);
        } else {
            ToastManager.showNetWorkToast();
        }
    }

    @Override
    public void edit(EquipmentHistoryItem item) {
        Intent intent = new Intent(mView.getActivity(), CreateEquipmentActivity.class);
        intent.putExtra(CommonConfig.EQUIPMENT_TYPE, CommonConfig.EQUIPMENT_TYPE_EDIT);
        intent.putExtra(CommonConfig.EQUIPMENT_LIST_ID,item.facilityId);
        mView.getActivity().startActivityForResult(intent, RequestCodeConfig.REQUEST_CODE_EQUIPMENT_TO_EDIT);
    }

    @Override
    public void detail(EquipmentHistoryItem item) {
        Intent intent = new Intent(mView.getActivity(), CreateEquipmentActivity.class);
        intent.putExtra(CommonConfig.EQUIPMENT_TYPE, CommonConfig.EQUIPMENT_TYPE_DETAIL);
        intent.putExtra(CommonConfig.EQUIPMENT_LIST_ID,item.facilityId);
        mView.getActivity().startActivity(intent);
    }


    //处理单据状态
    private void handleEquipmentlist(List<EquipmentHistoryItem> items) {
        if(items!=null && items.size()>0){
            for(int i = 0;i<items.size();i++){
                EquipmentHistoryItem item = items.get(i);
                if(item.committed){
                    item.qcState = item.qualified?CommonConfig.QC_STATE_STANDARD:CommonConfig.QC_STATE_NOT_STANDARD;
                }else{
                    item.qcState = CommonConfig.QC_STATE_EDIT;
                }
            }
            LogUtil.toJson(items);
            if(items!=null && items.size()>0){
                mEquipmentHistories = items;
                int i = 0;
                for(final EquipmentHistoryItem element:items)
                {
                    mhandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getEquipmentElementName(element,mFileId,element.elementId);
                        }
                    },20*i);
                    i++;
                }
            }
        }
    }

    //获取构件名称
    private void getEquipmentElementName(final EquipmentHistoryItem element, String fileId, final String elementId) {
        Subscription sub = mModel.getElementProperty(mProjectId, SharedPreferencesUtil.getString(mProjectId + "", ""), fileId, elementId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ModelElementInfo>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(e.getMessage());
                    }

                    @Override
                    public void onNext(ModelElementInfo modelElementInfo) {
                        if (modelElementInfo != null && modelElementInfo.data != null && modelElementInfo.data.boundingBox != null) {

                            ModelComponentWorldPosition min = modelElementInfo.data.boundingBox.min;
                            ModelComponentWorldPosition max = modelElementInfo.data.boundingBox.max;
                            if (max != null && min != null) {
                                element.drawingPositionX = (max.x + min.x) / 2;
                                element.drawingPositionY = (max.y + min.y) / 2;
                                element.drawingPositionZ = max.z > min.z ? max.z : min.z;
                                mEquipmentPositionMap.put(elementId, element);
                                if (mEquipmentPositionMap.size() == mEquipmentHistories.size()) {
                                    List<EquipmentHistoryItem> list = new ArrayList<>();
                                    for (EquipmentHistoryItem history : mEquipmentPositionMap.getValueList()) {
                                        list.add(history);
                                    }
                                    if (mView != null) {
                                        mView.showEquipmentList(list);
                                    }
                                }
                            }
                        }
                    }
                });
        mSubscription.add(sub);
    }

    //获取构件名称
    private void getElementName(final ModelElementHistory element, String fileId, final String elementId) {
        Subscription sub = mModel.getElementProperty(mProjectId, SharedPreferencesUtil.getString(mProjectId + "", ""), fileId, elementId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ModelElementInfo>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(e.getMessage());
                    }

                    @Override
                    public void onNext(ModelElementInfo modelElementInfo) {
                        if (modelElementInfo != null && modelElementInfo.data != null && modelElementInfo.data.boundingBox!=null) {

                            ModelComponentWorldPosition min = modelElementInfo.data.boundingBox.min;
                            ModelComponentWorldPosition max = modelElementInfo.data.boundingBox.max;
                            if(max!=null && min!=null) {
                                element.drawingPositionX = (max.x + min.x) / 2;
                                element.drawingPositionY = (max.y + min.y) / 2;
                                element.drawingPositionZ = max.z > min.z ? max.z : min.z;
                                mPositionMap.put(elementId, element);
                                if (mPositionMap.size() == mElementHistories.size()) {
                                    List<ModelElementHistory> list = new ArrayList<>();
                                    for(ModelElementHistory history:mPositionMap.getValueList()){
                                        list.add(history);
                                    }
                                    if(mView!=null)
                                    {
                                        mView.showModelHistory(list);
                                    }
                                }
                            }
                        }
                    }
                });
        mSubscription.add(sub);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void onDestroy() {
        if (mSubscription != null) {
            mSubscription.unsubscribe();
            mSubscription = null;
        }
    }
}
