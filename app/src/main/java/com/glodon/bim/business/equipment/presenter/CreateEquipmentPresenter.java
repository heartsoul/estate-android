package com.glodon.bim.business.equipment.presenter;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;

import com.glodon.bim.basic.utils.LinkedHashList;
import com.glodon.bim.basic.utils.NetWorkUtils;
import com.glodon.bim.basic.utils.SharedPreferencesUtil;
import com.glodon.bim.business.equipment.bean.CreateEquipmentBean;
import com.glodon.bim.business.equipment.bean.CreateEquipmentMandatoryInfo;
import com.glodon.bim.business.equipment.bean.CreateEquipmentMandatoryNotInfo;
import com.glodon.bim.business.equipment.bean.CreateEquipmentParams;
import com.glodon.bim.business.equipment.bean.CreateEquipmentPictureInfo;
import com.glodon.bim.business.equipment.bean.EquipmentDetailBean;
import com.glodon.bim.business.equipment.contract.CreateEquipmentContract;
import com.glodon.bim.business.equipment.model.CreateEquipmentModel;
import com.glodon.bim.business.equipment.view.CreateEquipmentMandatoryActivity;
import com.glodon.bim.business.equipment.view.CreateEquipmentNotMandatoryActivity;
import com.glodon.bim.business.equipment.view.CreateEquipmentPictureActivity;
import com.glodon.bim.business.qualityManage.bean.CreateCheckListParamsFile;
import com.glodon.bim.business.qualityManage.bean.ModelComponent;
import com.glodon.bim.business.qualityManage.bean.ModelListBeanItem;
import com.glodon.bim.business.qualityManage.bean.QualityCheckListBeanItemFile;
import com.glodon.bim.business.qualityManage.listener.OnUploadImageListener;
import com.glodon.bim.business.qualityManage.util.UploadManger;
import com.glodon.bim.business.qualityManage.view.RelevantModelActivity;
import com.glodon.bim.common.config.CommonConfig;
import com.glodon.bim.common.config.RequestCodeConfig;
import com.glodon.bim.customview.ToastManager;
import com.glodon.bim.customview.album.AlbumData;
import com.glodon.bim.customview.album.ImageItem;
import com.glodon.bim.customview.photopreview.PhotoPreviewActivity;

import java.util.List;

import okhttp3.ResponseBody;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * 描述：创建材设进场记录-提交页面
 * 作者：zhourf on 2018/1/9
 * 邮箱：zhourf@glodon.com
 */

public class CreateEquipmentPresenter implements CreateEquipmentContract.Presenter {
    private CreateEquipmentContract.View mView;
    private CreateEquipmentContract.Model mModel;
    private CompositeSubscription mSubscription;
    private CreateEquipmentMandatoryInfo mMandatoryInfo;
    private CreateEquipmentMandatoryNotInfo mMandatoryNotInfo;
    private CreateEquipmentPictureInfo mPictureInfo;
    private CreateEquipmentParams mInput;
    private boolean mIsEdit = false;
    private boolean mIsShowUploadErrorToast = true;
    private int mType = 0;//0创建   1编辑  2详情

    private long id;

    public CreateEquipmentPresenter(CreateEquipmentContract.View mView) {
        this.mView = mView;
        mModel = new CreateEquipmentModel();
        mSubscription = new CompositeSubscription();
        mInput = new CreateEquipmentParams();
    }

    @Override
    public void initData(Intent intent) {
        mType = intent.getIntExtra(CommonConfig.EQUIPMENT_TYPE,CommonConfig.EQUIPMENT_TYPE_CREATE);
        switch (mType){
            case CommonConfig.EQUIPMENT_TYPE_CREATE:
                typeCreate(intent);
                break;
            case CommonConfig.EQUIPMENT_TYPE_EDIT:
                typeEdit(intent);
                break;
            case CommonConfig.EQUIPMENT_TYPE_DETAIL:
                typeDetail(intent);
                break;
        }
    }

    //新建
    private void typeCreate(Intent intent){
        mMandatoryInfo = (CreateEquipmentMandatoryInfo) intent.getSerializableExtra(CommonConfig.EQUIPMENT_MANDATORYINFO);
        mMandatoryNotInfo = (CreateEquipmentMandatoryNotInfo) intent.getSerializableExtra(CommonConfig.EQUIPMENT_NOT_MANDATORYINFO);
        mPictureInfo = (CreateEquipmentPictureInfo) intent.getSerializableExtra(CommonConfig.EQUIPMENT_PICTURE_INFO);

        if(mView!=null){
            if(mMandatoryInfo!=null) {
                mView.showBasicInfo(mMandatoryInfo);
            }
            if(mMandatoryNotInfo!=null) {
                mView.showOtherInfo(mMandatoryNotInfo);
            }
            if(mPictureInfo!=null) {
                mView.showPictureInfo(mPictureInfo);
            }
        }
    }
    //编辑
    private  void typeEdit(Intent intent){
        mIsEdit = true;
        if(mView!=null) {
            mView.showEdit();
        }
        id = intent.getIntExtra(CommonConfig.EQUIPMENT_LIST_ID,0);
        if(NetWorkUtils.isNetworkAvailable(mView.getActivity())){
            if(mView!=null){
                mView.showLoadingDialog();
            }
            Subscription sub = mModel.detail(id)
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
                                showInfo(bean);
                            }
                            if (mView != null) {
                                mView.dismissLoadingDialog();
                            }
                        }
                    });
                    mSubscription.add(sub);
        }else{
            ToastManager.showNetWorkToast();
        }
    }

    //详情
    private void typeDetail(Intent intent){
        id = intent.getIntExtra(CommonConfig.EQUIPMENT_LIST_ID,0);
        if(mView!=null) {
            mView.showDetail();
        }
        if(NetWorkUtils.isNetworkAvailable(mView.getActivity())){
            if(mView!=null){
                mView.showLoadingDialog();
            }
            Subscription sub = mModel.detail(id)
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
                                showInfo(bean);
                            }
                            if (mView != null) {
                                mView.dismissLoadingDialog();
                            }
                        }
                    });
            mSubscription.add(sub);
        }else{
            ToastManager.showNetWorkToast();
        }
    }

    private void showInfo(EquipmentDetailBean bean){
        mMandatoryInfo = new CreateEquipmentMandatoryInfo();
        mMandatoryInfo.approachDate = bean.approachDate;
        mMandatoryInfo.batchCode = bean.batchCode;
        mMandatoryInfo.facilityCode = bean.facilityCode;
        mMandatoryInfo.facilityName = bean.facilityName;
        if(mView!=null){
            mView.showBasicInfo(mMandatoryInfo);
        }

        mMandatoryNotInfo = new CreateEquipmentMandatoryNotInfo();
        mMandatoryNotInfo.quantity = bean.quantity;
        mMandatoryNotInfo.unit = bean.unit;
        mMandatoryNotInfo.specification = bean.specification;
        mMandatoryNotInfo.modelNum = bean.modelNum;
        mMandatoryNotInfo.manufacturer = bean.manufacturer;
        mMandatoryNotInfo.brand = bean.brand;
        mMandatoryNotInfo.supplier = bean.supplier;
        if(!TextUtils.isEmpty(bean.elementName)){
            ModelListBeanItem model = new ModelListBeanItem();
            model.fileId = bean.gdocFileId;
            model.buildingName = bean.buildingName;
            model.buildingId = bean.buildingId;
            ModelComponent component = new ModelComponent();
            component.elementId = bean.elementId;
            component.elementName = bean.elementName;
            model.component = component;
            mMandatoryNotInfo.model = model;
        }
        if(mView!=null){
            mView.showOtherInfo(mMandatoryNotInfo);
        }

        mPictureInfo = new CreateEquipmentPictureInfo();
        mPictureInfo.qualified = bean.qualified;
        mPictureInfo.mSelectedMap = initImages(bean.files);
        if(mView!=null){
            mView.showPictureInfo(mPictureInfo);
        }
    }

    //初始化图片
    private LinkedHashList<String, ImageItem> initImages(List<QualityCheckListBeanItemFile> files) {
        LinkedHashList<String, ImageItem> mSelectedMap=new LinkedHashList<>();
        if (files != null && files.size() > 0) {
            for (QualityCheckListBeanItemFile file : files) {
                ImageItem item = new ImageItem();
                item.imagePath = file.url;
                item.objectId = file.objectId;
//                item.urlFile = file;
                mSelectedMap.put(item.imagePath, item);
            }
        }
        return mSelectedMap;
    }

    @Override
    public void submit() {
        assemBleData();
        if (NetWorkUtils.isNetworkAvailable(mView.getActivity())) {
            if (mView != null) {
                mView.showLoadingDialog();
            }
            if (mPictureInfo!=null && mPictureInfo.mSelectedMap.size() > 0) {
                mIsShowUploadErrorToast = true;
                new UploadManger(mPictureInfo.mSelectedMap).uploadImages(new OnUploadImageListener() {
                    @Override
                    public void onUploadFinished(List<CreateCheckListParamsFile> list) {
                        int i = 0;
                        for (ImageItem entry : mPictureInfo.mSelectedMap.getValueList()) {
                            ImageItem item = entry;
                            item.objectId = list.get(i).objectId;
                            item.urlFile = list.get(i);
                            i++;
                        }
                        mInput.files = list;
                        toSubmit();
                    }

                    @Override
                    public void onUploadError(Throwable t) {
                        if (mView != null) {
                            mView.dismissLoadingDialog();
                        }
                        if (mIsShowUploadErrorToast) {
                            mIsShowUploadErrorToast = false;
                            ToastManager.show("图片上传失败！");
                        }

                    }
                });
            } else {
                toSubmit();
            }
        } else {
            ToastManager.showNetWorkToast();
        }
    }

    private void toSubmit() {
        if(mIsEdit){
            Subscription sub = mModel.editSubmit(id,mInput)
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
                                mView.getActivity().setResult(Activity.RESULT_OK);
                                mView.getActivity().finish();
                            }
                        }
                    });
            mSubscription.add(sub);
        }else{
            Subscription sub = mModel.newSubmit(mInput)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<CreateEquipmentBean>() {
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
                        public void onNext(CreateEquipmentBean bean) {
                            ToastManager.showSubmitToast();
                            if (mView != null) {
                                mView.dismissLoadingDialog();
                                mView.getActivity().setResult(Activity.RESULT_OK);
                                mView.getActivity().finish();
                            }
                        }
                    });
            mSubscription.add(sub);
        }
    }


    @Override
    public void save() {
        assemBleData();
        if (NetWorkUtils.isNetworkAvailable(mView.getActivity())) {
            if (mView != null) {
                mView.showLoadingDialog();
            }
            if (mPictureInfo!=null && mPictureInfo.mSelectedMap.size() > 0) {
                mIsShowUploadErrorToast = true;
                new UploadManger(mPictureInfo.mSelectedMap).uploadImages(new OnUploadImageListener() {
                    @Override
                    public void onUploadFinished(List<CreateCheckListParamsFile> list) {
                        int i = 0;
                        for (ImageItem entry : mPictureInfo.mSelectedMap.getValueList()) {
                            ImageItem item = entry;
                            item.objectId = list.get(i).objectId;
                            item.urlFile = list.get(i);
                            i++;
                        }
                        mInput.files = list;
                        toSave();
                    }

                    @Override
                    public void onUploadError(Throwable t) {
                        if (mView != null) {
                            mView.dismissLoadingDialog();
                        }
                        if (mIsShowUploadErrorToast) {
                            mIsShowUploadErrorToast = false;
                            ToastManager.show("图片上传失败！");
                        }

                    }
                });
            } else {
                toSave();
            }
        } else {
            ToastManager.showNetWorkToast();
        }
    }

    private void toSave(){
        if(mIsEdit){
            Subscription sub = mModel.editSave(id,mInput)
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
                            ToastManager.showSaveToast();
                            if (mView != null) {
                                mView.dismissLoadingDialog();
                            }
                        }
                    });
            mSubscription.add(sub);
        }else{
            Subscription sub = mModel.newSave(mInput)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<CreateEquipmentBean>() {
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
                        public void onNext(CreateEquipmentBean bean) {
                            if(bean!=null){
                                id = bean.id;
                                if(mView!=null){
                                    mView.showDelete();
                                }
                                mIsEdit = true;
                                mInput.code = bean.code;
                                ToastManager.showSaveToast();
                            }
                            if (mView != null) {
                                mView.dismissLoadingDialog();
                            }
                        }
                    });
            mSubscription.add(sub);
        }
    }

    @Override
    public void delete() {
        if (NetWorkUtils.isNetworkAvailable(mView.getActivity())) {
            if (mView != null) {
                mView.showLoadingDialog();
            }
            Subscription sub = mModel.delete(id)
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
                            if (mView != null) {
                                mView.getActivity().setResult(Activity.RESULT_OK);
                                mView.getActivity().finish();
                            }
                        }
                    });
            mSubscription.add(sub);
        }else{
            ToastManager.showNetWorkToast();
        }
    }

    private void assemBleData(){
        if(mMandatoryInfo!=null){
            mInput.batchCode = mMandatoryInfo.batchCode;
            mInput.approachDate = mMandatoryInfo.approachDate;
            mInput.facilityCode = mMandatoryInfo.facilityCode;
            mInput.facilityName = mMandatoryInfo.facilityName;
        }

        if(mMandatoryNotInfo!=null){
            mInput.quantity = mMandatoryNotInfo.quantity;
            mInput.unit = mMandatoryNotInfo.unit;
            mInput.specification = mMandatoryNotInfo.specification;
            mInput.modelNum = mMandatoryNotInfo.modelNum;
            mInput.manufacturer = mMandatoryNotInfo.manufacturer;
            mInput.brand = mMandatoryNotInfo.brand;
            mInput.supplier = mMandatoryNotInfo.supplier;
            if(mMandatoryNotInfo!=null && mMandatoryNotInfo.model!=null && mMandatoryNotInfo.model.component!=null){
                mInput.versionId = SharedPreferencesUtil.getProjectVersionId();
                ModelListBeanItem model = mMandatoryNotInfo.model;
                mInput.gdocFileId = model.fileId;
                mInput.buildingId = model.buildingId;
                mInput.buildingName = model.buildingName;
                mInput.elementId = model.component.elementId;
                mInput.elementName = model.component.elementName;
            }
        }

        if(mPictureInfo!=null){
            mInput.qualified = mPictureInfo.qualified;
        }
    }

    @Override
    public void toBasic() {
        Intent intent = new Intent(mView.getActivity(), CreateEquipmentMandatoryActivity.class);
        intent.putExtra(CommonConfig.EQUIPMENT_EDIT_MANDATORY_INFO,mMandatoryInfo);
        mView.getActivity().startActivityForResult(intent,RequestCodeConfig.REQUEST_CODE_EQUIPMENT_EDIT_MANDATORY_INFO);
    }

    @Override
    public void toOther() {
        Intent intent = new Intent(mView.getActivity(), CreateEquipmentNotMandatoryActivity.class);
        intent.putExtra(CommonConfig.EQUIPMENT_EDIT_NOT_MANDATORY_INFO,mMandatoryNotInfo);
        mView.getActivity().startActivityForResult(intent,RequestCodeConfig.REQUEST_CODE_EQUIPMENT_EDIT_NOT_MANDATORY_INFO);
    }

    @Override
    public void toPicture() {
        Intent intent = new Intent(mView.getActivity(), CreateEquipmentPictureActivity.class);
        intent.putExtra(CommonConfig.EQUIPMENT_EDIT_PICTURE_INFO,mPictureInfo);
        mView.getActivity().startActivityForResult(intent,RequestCodeConfig.REQUEST_CODE_EQUIPMENT_EDIT_PICTURE_INFO);
    }

    @Override
    public void toPreview(int position) {
        Intent intent = new Intent(mView.getActivity(), PhotoPreviewActivity.class);
        intent.putExtra(CommonConfig.ALBUM_DATA, new AlbumData(mPictureInfo.mSelectedMap));
        intent.putExtra(CommonConfig.ALBUM_POSITION, position);
        intent.putExtra(CommonConfig.ALBUM_SHOW_DELETE, false);
        mView.getActivity().startActivityForResult(intent, RequestCodeConfig.REQUEST_CODE_PHOTO_PREVIEW);
    }

    @Override
    public void toModel() {
        //跳转到模型
        if(mMandatoryNotInfo!=null && mMandatoryNotInfo.model!=null && mMandatoryNotInfo.model.component!=null) {
            Intent intent = new Intent(mView.getActivity(), RelevantModelActivity.class);
            intent.putExtra(CommonConfig.RELEVANT_TYPE, 2);

            intent.putExtra(CommonConfig.MODEL_SELECT_INFO, mMandatoryNotInfo.model);
            intent.putExtra(CommonConfig.BLUE_PRINT_FILE_ID, mMandatoryNotInfo.model.fileId);
            mView.getActivity().startActivity(intent);
        }
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode)
        {
            case RequestCodeConfig.REQUEST_CODE_EQUIPMENT_EDIT_MANDATORY_INFO:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    mMandatoryInfo = (CreateEquipmentMandatoryInfo) data.getSerializableExtra(CommonConfig.EQUIPMENT_EDIT_MANDATORY_INFO);
                    if(mMandatoryInfo!=null && mView!=null){
                        mView.showBasicInfo(mMandatoryInfo);
                    }
                }
                break;
            case RequestCodeConfig.REQUEST_CODE_EQUIPMENT_EDIT_NOT_MANDATORY_INFO:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    mMandatoryNotInfo = (CreateEquipmentMandatoryNotInfo) data.getSerializableExtra(CommonConfig.EQUIPMENT_EDIT_NOT_MANDATORY_INFO);
                    if(mMandatoryNotInfo!=null && mView!=null){
                        mView.showOtherInfo(mMandatoryNotInfo);
                    }
                }
                break;
            case RequestCodeConfig.REQUEST_CODE_EQUIPMENT_EDIT_PICTURE_INFO:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    mPictureInfo = (CreateEquipmentPictureInfo) data.getSerializableExtra(CommonConfig.EQUIPMENT_EDIT_PICTURE_INFO);
                    if(mPictureInfo!=null && mView!=null){
                        mView.showPictureInfo(mPictureInfo);
                    }
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
}
