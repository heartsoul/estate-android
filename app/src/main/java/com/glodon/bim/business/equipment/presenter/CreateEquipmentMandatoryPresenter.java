package com.glodon.bim.business.equipment.presenter;

import android.app.Activity;
import android.content.Intent;

import com.glodon.bim.basic.log.LogUtil;
import com.glodon.bim.basic.utils.NetWorkUtils;
import com.glodon.bim.business.equipment.bean.CreateEquipmentMandatoryInfo;
import com.glodon.bim.business.equipment.contract.CreateEquipmentMandatoryContract;
import com.glodon.bim.business.equipment.model.CreateEquipmentMandatoryModel;
import com.glodon.bim.business.equipment.view.CreateEquipmentNotMandatoryActivity;
import com.glodon.bim.business.qualityManage.bean.InspectionCompanyItem;
import com.glodon.bim.business.qualityManage.bean.ModelListBeanItem;
import com.glodon.bim.common.config.CommonConfig;
import com.glodon.bim.common.config.RequestCodeConfig;
import com.glodon.bim.customview.ToastManager;

import java.util.List;

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

public class CreateEquipmentMandatoryPresenter implements CreateEquipmentMandatoryContract.Presenter {
    private CreateEquipmentMandatoryContract.View mView;
    private CreateEquipmentMandatoryContract.Model mModel;
    private boolean mIsEdit = false;
    private ModelListBeanItem mModelSelectInfo;
    private CompositeSubscription mSubscription;
    private List<InspectionCompanyItem> mInspectionCompanyItems;//验收单位列表


    public CreateEquipmentMandatoryPresenter(CreateEquipmentMandatoryContract.View mView) {
        this.mView = mView;
        mModel = new CreateEquipmentMandatoryModel();
        mSubscription = new CompositeSubscription();
    }

    @Override
    public void initData(Intent intent) {
        //编辑状态
        CreateEquipmentMandatoryInfo info = (CreateEquipmentMandatoryInfo) intent.getSerializableExtra(CommonConfig.EQUIPMENT_EDIT_MANDATORY_INFO);
        if(info!=null){
            mView.showMandatoryInfo(info);
            mIsEdit = true;
        }
        getAcceptanceCompany();
        //从模型过来
        mModelSelectInfo = (ModelListBeanItem) intent.getSerializableExtra(CommonConfig.RELEVANT_MODEL);

    }

    //获取验收单单位列表
    private void getAcceptanceCompany(){
        if(NetWorkUtils.isNetworkAvailable(mView.getActivity())){
            if(mView!=null){
                mView.showLoadingDialog();
            }
            Subscription sub = mModel.getAcceptanceCompanies()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<List<InspectionCompanyItem>>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            LogUtil.e(e.getMessage());
                            if(mView!=null)
                            {
                                mView.dismissLoadingDialog();
                            }
                        }

                        @Override
                        public void onNext(List<InspectionCompanyItem> inspectionCompanyItems) {
                            mInspectionCompanyItems = inspectionCompanyItems;
                            if(!mIsEdit && mInspectionCompanyItems!=null && mInspectionCompanyItems.size()>0){
                                if(mView!=null){
                                    mView.showAccpecionCompany(mInspectionCompanyItems.get(0));
                                }
                            }
                            if(mView!=null)
                            {
                                mView.dismissLoadingDialog();
                            }
                        }
                    });
            mSubscription.add(sub);
        }else{
            ToastManager.showNetWorkToast();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode)
        {
            case RequestCodeConfig.REQUEST_CODE_EQUIPMENT_MANDATORY:
                if(resultCode == Activity.RESULT_OK){
                    if(mView!=null){
                        mView.getActivity().finish();
                    }
                }
                break;
        }
    }

    @Override
    public void onDestroy() {
        if(mSubscription!=null){
            mSubscription.unsubscribe();
            mSubscription = null;
        }
    }

    @Override
    public void toNotMandatory(CreateEquipmentMandatoryInfo info) {
        if(mIsEdit){
            Intent data = new Intent();
            data.putExtra(CommonConfig.EQUIPMENT_EDIT_MANDATORY_INFO,info);
            mView.getActivity().setResult(Activity.RESULT_OK,data);
            mView.getActivity().finish();
        }else {
            Intent intent = new Intent(mView.getActivity(), CreateEquipmentNotMandatoryActivity.class);
            intent.putExtra(CommonConfig.EQUIPMENT_MANDATORYINFO, info);
            //从模型选择过来
            if(mModelSelectInfo!=null){
                intent.putExtra(CommonConfig.RELEVANT_MODEL,mModelSelectInfo);
            }
            mView.getActivity().startActivityForResult(intent, RequestCodeConfig.REQUEST_CODE_EQUIPMENT_MANDATORY);
        }
    }

    @Override
    public void showAcceptionCompany(InspectionCompanyItem mSelectAcceptionItem) {
        if(mView!=null){
            int position = -10;
            if(mSelectAcceptionItem!=null){
                for(int i =0;i<mInspectionCompanyItems.size();i++){
                    if(mSelectAcceptionItem.id == mInspectionCompanyItems.get(i).id){
                        position = i;
                        break;
                    }
                }
            }
            mView.showCompanyList(mInspectionCompanyItems,position);
        }
    }
}
