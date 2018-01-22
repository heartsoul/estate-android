package com.glodon.bim.business.equipment.presenter;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;

import com.glodon.bim.basic.log.LogUtil;
import com.glodon.bim.basic.utils.SharedPreferencesUtil;
import com.glodon.bim.business.equipment.bean.CreateEquipmentMandatoryInfo;
import com.glodon.bim.business.equipment.bean.CreateEquipmentMandatoryNotInfo;
import com.glodon.bim.business.equipment.contract.CreateEquipmentNotMandatoryContract;
import com.glodon.bim.business.equipment.view.CreateEquipmentPictureActivity;
import com.glodon.bim.business.qualityManage.bean.ModelElementInfo;
import com.glodon.bim.business.qualityManage.bean.ModelListBeanItem;
import com.glodon.bim.business.qualityManage.model.CreateCheckListModel;
import com.glodon.bim.business.qualityManage.view.ModelActivity;
import com.glodon.bim.common.config.CommonConfig;
import com.glodon.bim.common.config.RequestCodeConfig;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * 描述：创建材设进场记录-必填项页面
 * 作者：zhourf on 2018/1/9
 * 邮箱：zhourf@glodon.com
 */

public class CreateEquipmentNotMandatoryPresenter implements CreateEquipmentNotMandatoryContract.Presenter {
    private CreateEquipmentNotMandatoryContract.View mView;
    private CreateEquipmentNotMandatoryContract.Model mModel;
    private ModelListBeanItem mModelSelectInfo;
    private CreateEquipmentMandatoryInfo mCreateEquipmentMandatoryInfo;
    private int mModelType = 4;//0新建检查单 1检查单编辑状态 2详情查看  3图纸模式 4新建材设进场 5新增材设进场编辑状态
    private long mProjectId;
    private String mProjectVersionId;
    private CompositeSubscription mSubscritption;
    private boolean mIsEdit = false;

    public CreateEquipmentNotMandatoryPresenter(CreateEquipmentNotMandatoryContract.View mView) {
        this.mView = mView;
        mProjectId = SharedPreferencesUtil.getProjectId();
        mProjectVersionId = SharedPreferencesUtil.getProjectVersionId(mProjectId);
        mSubscritption = new CompositeSubscription();
    }

    @Override
    public void initData(Intent intent) {
        mCreateEquipmentMandatoryInfo = (CreateEquipmentMandatoryInfo) intent.getSerializableExtra(CommonConfig.EQUIPMENT_MANDATORYINFO);

        //编辑状态
        CreateEquipmentMandatoryNotInfo info = (CreateEquipmentMandatoryNotInfo) intent.getSerializableExtra(CommonConfig.EQUIPMENT_EDIT_NOT_MANDATORY_INFO);
        if(info!=null){
            mIsEdit = true;
            mView.showEditInfo(info);
            mModelSelectInfo = info.model;
        }
        //从模型过来
        mModelSelectInfo = (ModelListBeanItem) intent.getSerializableExtra(CommonConfig.RELEVANT_MODEL);
        if (mModelSelectInfo!=null && mModelSelectInfo.component != null) {
            getElementName(mModelSelectInfo.fileId, mModelSelectInfo.component.elementId);
            mModelType=5;
        }
    }


    @Override
    public void toSkip() {
        Intent intent = new Intent(mView.getActivity(), CreateEquipmentPictureActivity.class);
        intent.putExtra(CommonConfig.EQUIPMENT_MANDATORYINFO, mCreateEquipmentMandatoryInfo);
        intent.putExtra(CommonConfig.EQUIPMENT_NOT_MANDATORYINFO, new CreateEquipmentMandatoryNotInfo());
        mView.getActivity().startActivityForResult(intent, RequestCodeConfig.REQUEST_CODE_EQUIPMENT_MANDATORY_NOT);
    }

    @Override
    public void toNext(CreateEquipmentMandatoryNotInfo info) {
        info.model = mModelSelectInfo;
        if(mIsEdit){
            Intent data = new Intent();
            data.putExtra(CommonConfig.EQUIPMENT_EDIT_NOT_MANDATORY_INFO,info);
            mView.getActivity().setResult(Activity.RESULT_OK,data);
            mView.getActivity().finish();
        }else {
            Intent intent = new Intent(mView.getActivity(), CreateEquipmentPictureActivity.class);
            intent.putExtra(CommonConfig.EQUIPMENT_MANDATORYINFO, mCreateEquipmentMandatoryInfo);
            intent.putExtra(CommonConfig.EQUIPMENT_NOT_MANDATORYINFO, info);
            mView.getActivity().startActivityForResult(intent, RequestCodeConfig.REQUEST_CODE_EQUIPMENT_MANDATORY_NOT);
        }
    }

    @Override
    public void toModel() {
        Intent intent = new Intent(mView.getActivity(), ModelActivity.class);
        LogUtil.e("modelSelectInfo==null?" + (mModelSelectInfo == null));
        if (mModelSelectInfo != null && !TextUtils.isEmpty(mModelSelectInfo.fileId)) {
            intent.putExtra(CommonConfig.MODULE_LIST_POSITION, mModelSelectInfo.fileId);
            intent.putExtra(CommonConfig.MODEL_SELECT_INFO, mModelSelectInfo);
        }
        LogUtil.e("modelType=" + mModelType);
        intent.putExtra(CommonConfig.RELEVANT_TYPE, mModelType);
        mView.getActivity().startActivityForResult(intent, RequestCodeConfig.REQUEST_CODE_EQUIPMENT_CHOOSE_MODEL);
    }

    //获取构件名称
    private void getElementName(String fileId, String elementId) {
        Subscription sub = new CreateCheckListModel().getElementProperty(mProjectId, mProjectVersionId, fileId, elementId)
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
                        if (modelElementInfo != null && modelElementInfo.data != null) {
                            String elementName = modelElementInfo.data.name;
                            mModelSelectInfo.component.elementName = elementName;
                            if (mView != null) {
                                mView.showModelName(elementName);
                            }
                        }
                    }
                });
        mSubscritption.add(sub);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RequestCodeConfig.REQUEST_CODE_EQUIPMENT_MANDATORY_NOT:
                if(resultCode == Activity.RESULT_OK){
                    if(mView!=null){
                        mView.getActivity().setResult(Activity.RESULT_OK);
                        mView.getActivity().finish();
                    }
                }
                break;
            case RequestCodeConfig.REQUEST_CODE_EQUIPMENT_CHOOSE_MODEL:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    mModelSelectInfo = (ModelListBeanItem) data.getSerializableExtra(CommonConfig.MODEL_SELECT_INFO);
                    if (mModelSelectInfo.component != null) {
                        getElementName(mModelSelectInfo.fileId, mModelSelectInfo.component.elementId);
                        mModelType = 5;
                    }
                }
                break;
        }
    }

    @Override
    public void onDestroy() {
        if (mSubscritption != null) {
            mSubscritption.unsubscribe();
            mSubscritption = null;
        }

    }
}
