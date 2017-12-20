package com.glodon.bim.business.qualityManage.presenter;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;

import com.glodon.bim.basic.log.LogUtil;
import com.glodon.bim.basic.utils.CameraUtil;
import com.glodon.bim.basic.utils.LinkedHashList;
import com.glodon.bim.basic.utils.NetWorkUtils;
import com.glodon.bim.business.qualityManage.bean.CreateCheckListParamsFile;
import com.glodon.bim.business.qualityManage.bean.CreateReviewData;
import com.glodon.bim.business.qualityManage.bean.QualityCheckListBeanItemFile;
import com.glodon.bim.business.qualityManage.bean.QualityCheckListDetailBean;
import com.glodon.bim.business.qualityManage.bean.QualityCheckListDetailProgressInfo;
import com.glodon.bim.business.qualityManage.bean.QualityGetRepairInfo;
import com.glodon.bim.business.qualityManage.bean.QualityGetReviewInfo;
import com.glodon.bim.business.qualityManage.bean.QualityRepairParams;
import com.glodon.bim.business.qualityManage.bean.QualityReviewParams;
import com.glodon.bim.business.qualityManage.bean.SaveBean;
import com.glodon.bim.business.qualityManage.contract.CreateReviewContract;
import com.glodon.bim.business.qualityManage.listener.OnUploadImageListener;
import com.glodon.bim.business.qualityManage.model.CreateReviewModel;
import com.glodon.bim.business.qualityManage.util.UploadManger;
import com.glodon.bim.business.qualityManage.view.PhotoEditActivity;
import com.glodon.bim.common.config.CommonConfig;
import com.glodon.bim.customview.ToastManager;
import com.glodon.bim.customview.album.AlbumData;
import com.glodon.bim.customview.album.AlbumEditActivity;
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
 * 描述：新建复查单 整改单
 * 作者：zhourf on 2017/11/3
 * 邮箱：zhourf@glodon.com
 */

public class CreateReviewPresenter implements CreateReviewContract.Presenter {

    private CreateReviewContract.View mView;
    private CreateReviewContract.Model mModel;
    private CompositeSubscription mSubscription;

    private final int REQUEST_CODE_OPEN_ALBUM = 1;
    private final int REQUEST_CODE_TAKE_PHOTO = 2;
    private final int REQUEST_CODE_PHOTO_EDIT = 3;
    private static final int REQUEST_CODE_PHOTO_PREVIEW = 4;//图片预览

    private String mPhotoPath;//拍照的路径

    //图片
    private LinkedHashList<String, ImageItem> mSelectedMap;
    //初始的图片 用于判断点击返回键时是否弹出提示框
    private CreateReviewData mInitData;

    private long deptId, id, repairId, reviewId;//项目id 和检查单id  整改单id 复查单id

    private String mCreateType; //创建的类型

    private boolean mIsEditStatus = false;//是否编辑状态
    private String mCode = "";//当前单据的code


    public CreateReviewPresenter(CreateReviewContract.View mView) {
        this.mView = mView;
        mModel = new CreateReviewModel();
        mSubscription = new CompositeSubscription();
        mInitData = new CreateReviewData();
    }

    @Override
    public boolean isEqual(String des,boolean flag,String time){
        switch (mCreateType) {
            case CommonConfig.CREATE_TYPE_REPAIR:
                return isPhotoEqual() && isEqual(des,mInitData.des);
            case CommonConfig.CREATE_TYPE_REVIEW:
                if(flag){
                    return isPhotoEqual() && isEqual(des,mInitData.des) &&mInitData.flag;
                }else{
                    return isPhotoEqual() && isEqual(des,mInitData.des) &&!mInitData.flag&&isEqual(time,mInitData.time);
                }
        }
        return true;
    }

    private boolean isEqual(String a, String b) {
        if (TextUtils.isEmpty(a) && TextUtils.isEmpty(b)) {
            return true;
        }
        if (!TextUtils.isEmpty(a) && !TextUtils.isEmpty(b) && a.equals(b)) {
            return true;
        }
        return false;
    }

    //判断图片是否相同
    public boolean isPhotoEqual(){
        LinkedHashList<String, ImageItem> mInitMap = mInitData.mInitMap;
        if(mSelectedMap == null && mInitMap == null){
            return true;
        }
        if(mSelectedMap!=null && mInitMap!=null && mSelectedMap.size()==mInitMap.size()){
            List<String> mSelectKey = mSelectedMap.getKeyList();
            List<ImageItem> mSelectValue = mSelectedMap.getValueList();
//            for(Map.Entry<String,ImageItem> entry:mSelectedMap.entrySet()){
//                mSelectKey.add(entry.getKey());
//                mSelectValue.add(entry.getValue());
//            }
            List<String> mInitKey = mInitMap.getKeyList();
            List<ImageItem> mInitValue = mInitMap.getValueList();
//            for(Map.Entry<String,ImageItem> entry:mInitMap.entrySet()){
//                mInitKey.add(entry.getKey());
//                mInitValue.add(entry.getValue());
//            }

            for(int i = 0;i<mInitKey.size();i++){
                if(!mSelectKey.get(i).equals(mInitKey.get(i))){
                    return false;
                }else{
                    ImageItem selectItem = mSelectValue.get(i);
                    ImageItem initItem = mInitValue.get(i);
                    if(!selectItem.imagePath.equals(initItem.imagePath)){
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public void setSelectedImages(LinkedHashList<String, ImageItem> map) {
        this.mSelectedMap = map;
    }

    @Override
    public void takePhoto() {
        mPhotoPath = CameraUtil.getFilePath();
        CameraUtil.openCamera(mPhotoPath, mView.getActivity(), REQUEST_CODE_TAKE_PHOTO);
    }

    @Override
    public void openAlbum() {
        Intent intent = new Intent(mView.getActivity(), AlbumEditActivity.class);
        intent.putExtra(CommonConfig.ALBUM_FROM_TYPE, 1);
        intent.putExtra(CommonConfig.ALBUM_DATA, new AlbumData(mSelectedMap));
        mView.getActivity().startActivityForResult(intent, REQUEST_CODE_OPEN_ALBUM);
    }

    @Override
    public void initData(Intent intent) {
        deptId = intent.getLongExtra(CommonConfig.QUALITY_CHECK_LIST_DEPTID, 0);
        id = intent.getLongExtra(CommonConfig.QUALITY_CHECK_LIST_ID, 0);
        mCreateType = intent.getStringExtra(CommonConfig.CREATE_TYPE);
        //展示title和检查单详情
        if (mView != null) {
            mView.showTitleByType(mCreateType);
            mView.showDetail(deptId, id);
        }
        //查询是否存在之前保存过的数据
        getEditInfo();
    }

    //查询是否存在之前保存过的数据
    private void getEditInfo() {
        switch (mCreateType) {
            case CommonConfig.CREATE_TYPE_REPAIR:
                getRepairInfo();
                break;
            case CommonConfig.CREATE_TYPE_REVIEW:
                getReviewInfo();
                break;
        }
    }

    //查询整改单编辑数据
    private void getRepairInfo() {
        if (NetWorkUtils.isNetworkAvailable(mView.getActivity())) {

            Subscription sub = mModel.getRepairInfo(deptId, id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<QualityGetRepairInfo>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            LogUtil.e(e.getMessage());
                        }

                        @Override
                        public void onNext(QualityGetRepairInfo info) {
                            if (info != null) {
                                mIsEditStatus = true;
                                mCode = info.code;
                                repairId = info.id;
                                if (mView != null) {
                                    mView.showDesAndImages(info.description, info.files);
                                    mView.showDelete();
                                }
                                mInitData.des = info.description;
                                if(info.files!=null && info.files.size()>0){
                                    mInitData.mInitMap = new LinkedHashList<>();
                                    for(QualityCheckListBeanItemFile file:info.files){
                                        ImageItem item = new ImageItem();
                                        item.imagePath = file.url;
                                        item.objectId = file.objectId;
                                        mInitData.mInitMap.put(item.imagePath,item);
                                    }
                                }
                            }
                        }
                    });
            mSubscription.add(sub);
        } else {
            ToastManager.showNetWorkToast();
        }
    }

    //查询复查单编辑数据
    private void getReviewInfo() {
        if (NetWorkUtils.isNetworkAvailable(mView.getActivity())) {

            Subscription sub = mModel.getReviewInfo(deptId, id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<QualityGetReviewInfo>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            LogUtil.e(e.getMessage());
                        }

                        @Override
                        public void onNext(QualityGetReviewInfo info) {
                            if (info != null) {
                                mIsEditStatus = true;
                                mCode = info.code;
                                reviewId = info.id;
                                if (mView != null) {
                                    mView.showDesAndImages(info.description, info.files);
                                    mView.showRectificationInfo(info.status, info.lastRectificationDate);
                                    mView.showDelete();
                                }

                                mInitData.des = info.description;
                                if(info.files!=null && info.files.size()>0){
                                    mInitData.mInitMap = new LinkedHashList<>();
                                    for(QualityCheckListBeanItemFile file:info.files){
                                        ImageItem item = new ImageItem();
                                        item.imagePath = file.url;
                                        item.objectId = file.objectId;
                                        mInitData.mInitMap.put(item.imagePath,item);
                                    }
                                }
                                mInitData.time = info.lastRectificationDate;
                                switch (info.status) {
                                    case CommonConfig.STATUS_CLOSED://合格
                                        mInitData.flag = true;
                                        break;
                                    case CommonConfig.STATUS_NOT_ACCEPTED://不合格
                                        mInitData.flag = false;
                                        break;
                                }

                            }
                        }
                    });
            mSubscription.add(sub);
        } else {
            ToastManager.showNetWorkToast();
        }
    }


    /**
     * 跳转到图片预览
     *
     * @param position 点击的图片的位置
     */
    @Override
    public void toPreview(int position) {
        Intent intent = new Intent(mView.getActivity(), PhotoPreviewActivity.class);
        intent.putExtra(CommonConfig.ALBUM_DATA, new AlbumData(mSelectedMap));
        intent.putExtra(CommonConfig.ALBUM_POSITION, position);
        mView.getActivity().startActivityForResult(intent, REQUEST_CODE_PHOTO_PREVIEW);
    }

    private boolean mIsShowUploadErrorToast = true;
    private List<CreateCheckListParamsFile> mImageList;
    @Override
    public void submit(final String des, final String mCurrentStatus, final String mSelectedTime) {
        if (NetWorkUtils.isNetworkAvailable(mView.getActivity())) {
            if(mView!=null){
                mView.showLoadingDialog();
            }
            if (mSelectedMap!=null && mSelectedMap.size() > 0) {
                mIsShowUploadErrorToast = true;
                new UploadManger(mSelectedMap).uploadImages(new OnUploadImageListener() {
                    @Override
                    public void onUploadFinished(List<CreateCheckListParamsFile> list) {

                        int i = 0;
                        for (ImageItem entry : mSelectedMap.getValueList()) {
                            ImageItem item = entry;
                            item.objectId = list.get(i).objectId;
                            item.urlFile = list.get(i);
                            i++;
                        }
                        mImageList = list;
                        toSubmit(des,mCurrentStatus,mSelectedTime);
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
                mImageList = null;
                toSubmit(des,mCurrentStatus,mSelectedTime);
            }

        } else {
            ToastManager.showNetWorkToast();
        }
    }

    private void toSubmit(String des, String mCurrentStatus, String mSelectedTime){
        switch (mCreateType) {
            case CommonConfig.CREATE_TYPE_REPAIR:
                if (mIsEditStatus) {
                    editSubmitRepair(des);
                } else {
                    createSubmitRepair(des);
                }
                break;
            case CommonConfig.CREATE_TYPE_REVIEW:
                if (mIsEditStatus) {
                    editSubmitReview(des, mCurrentStatus, mSelectedTime);
                } else {
                    createSubmitReview(des, mCurrentStatus, mSelectedTime);
                }
                break;
        }
    }

    private void editSubmitRepair(String des) {
        QualityRepairParams props = getRepairParams(des);
        Subscription sub = mModel.editSubmitRepair(deptId, repairId, props)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseBody>() {
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
                    public void onNext(ResponseBody saveBean) {
                        if (mView != null) {
                            mView.dismissLoadingDialog();
                        }
                        if (saveBean != null) {
                            ToastManager.showSubmitToast();
                            if (mView != null) {
                                mView.getActivity().setResult(Activity.RESULT_OK);
                                mView.getActivity().finish();
                            }
                        }
                    }
                });
        mSubscription.add(sub);
    }

    private void createSubmitRepair(String des) {
        QualityRepairParams props = getRepairParams(des);
        Subscription sub = mModel.createSubmitRepair(deptId, props)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SaveBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e("e=" + e.getMessage());
                        if (mView != null) {
                            mView.dismissLoadingDialog();
                        }
                    }

                    @Override
                    public void onNext(SaveBean saveBean) {
                        if (mView != null) {
                            mView.dismissLoadingDialog();
                        }
                        if (saveBean != null) {
                            ToastManager.showSubmitToast();
                            if (mView != null) {
                                mView.getActivity().setResult(Activity.RESULT_OK);
                                mView.getActivity().finish();
                            }
                        }
                    }
                });
        mSubscription.add(sub);


    }

    //组织整改单参数
    private QualityRepairParams getRepairParams(String des) {
        QualityRepairParams props = new QualityRepairParams();
        props.code = mCode;
        props.description = des;
        props.inspectionId = id;
        QualityCheckListDetailBean info = mView.getDetailInfo();
        if (info != null && info.progressInfos != null && info.progressInfos.size() > 0) {
            List<QualityCheckListDetailProgressInfo> list = info.progressInfos;
//            props.flawCode = list.get(list.size()-1).code;
            props.flawId = list.get(list.size()-1).id;
        } else {
//            props.flawCode = info.inspectionInfo.code;
            props.flawId = info.inspectionInfo.id;
        }
        if(mImageList!=null && mImageList.size()>0){
            props.files = mImageList;
        }
        return props;
    }

    private void editSubmitReview(String des, String mCurrentStatus, String mSelectedTime) {
        QualityReviewParams props = getReviewParams(des, mCurrentStatus, mSelectedTime);
        Subscription sub = mModel.editSubmitReview(deptId, reviewId, props)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseBody>() {
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
                    public void onNext(ResponseBody saveBean) {
                        if (mView != null) {
                            mView.dismissLoadingDialog();
                        }
                        if (saveBean != null) {
                            ToastManager.showSubmitToast();
                            if (mView != null) {
                                mView.getActivity().setResult(Activity.RESULT_OK);
                                mView.getActivity().finish();
                            }
                        }
                    }
                });
        mSubscription.add(sub);
    }

    private void createSubmitReview(String des, String mCurrentStatus, String mSelectedTime) {
        QualityReviewParams props = getReviewParams(des, mCurrentStatus, mSelectedTime);
        Subscription sub = mModel.createSubmitReview(deptId, props)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SaveBean>() {
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
                    public void onNext(SaveBean saveBean) {
                        if (mView != null) {
                            mView.dismissLoadingDialog();
                        }
                        if (saveBean != null) {
                            ToastManager.showSubmitToast();
                            if (mView != null) {
                                mView.getActivity().setResult(Activity.RESULT_OK);
                                mView.getActivity().finish();
                            }
                        }
                    }
                });
        mSubscription.add(sub);
    }

    private QualityReviewParams getReviewParams(String des, String mCurrentStatus, String mSelectedTime) {
        QualityReviewParams props = new QualityReviewParams();
        props.code = mCode;
        props.description = des;
        props.inspectionId = id;
        props.status = mCurrentStatus;
        props.lastRectificationDate = mSelectedTime;
        QualityCheckListDetailBean info = mView.getDetailInfo();
        if (info != null && info.progressInfos != null && info.progressInfos.size() > 0) {
            List<QualityCheckListDetailProgressInfo> list = info.progressInfos;
//            props.rectificationCode = list.get(list.size()-1).code;
            props.rectificationId = list.get(list.size()-1).id;
        }
        if(mImageList!=null && mImageList.size()>0){
            props.files = mImageList;
        }
        return props;
    }

    @Override
    public void save(final String des, final String mCurrentStatus, final String mSelectedTime) {
        if (NetWorkUtils.isNetworkAvailable(mView.getActivity())) {
            if(mView!=null) {
                mView.showLoadingDialog();
            }
            if (mSelectedMap!=null && mSelectedMap.size() > 0) {
                mIsShowUploadErrorToast = true;
                new UploadManger(mSelectedMap).uploadImages(new OnUploadImageListener() {
                    @Override
                    public void onUploadFinished(List<CreateCheckListParamsFile> list) {

                        int i = 0;
                        for (ImageItem entry: mSelectedMap.getValueList()){
                            ImageItem item = entry;
                            item.objectId = list.get(i).objectId;
                            item.urlFile = list.get(i);
                            i++;
                        }
                        mImageList = list;
                        toSave(des,mCurrentStatus,mSelectedTime);
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
                mImageList = null;
                toSave(des,mCurrentStatus,mSelectedTime);
            }
        } else {
            ToastManager.showNetWorkToast();
        }
    }

    private void toSave(String des, String mCurrentStatus, String mSelectedTime){
        switch (mCreateType) {
            case CommonConfig.CREATE_TYPE_REPAIR:
                if (mIsEditStatus) {
                    editSaveRepair(des);
                } else {
                    createSaveRepair(des);
                }
                break;
            case CommonConfig.CREATE_TYPE_REVIEW:
                if (mIsEditStatus) {
                    editSaveReview(des, mCurrentStatus, mSelectedTime);
                } else {
                    createSaveReview(des, mCurrentStatus, mSelectedTime);
                }
                break;
        }
    }

    private void editSaveRepair(final String des) {
        QualityRepairParams props = getRepairParams(des);
        Subscription sub = mModel.editSaveRepair(deptId, repairId, props)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseBody>() {
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
                    public void onNext(ResponseBody saveBean) {
                        if (mView != null) {
                            mView.dismissLoadingDialog();
                        }
                        if (saveBean != null) {
                            ToastManager.showSaveToast();
                            if (mView != null) {
                                mView.showDelete();
                            }
                            resetRepairInitData(des);
                        }
                    }
                });
        mSubscription.add(sub);
    }

    private void resetRepairInitData(String des){
        mInitData.des = des;
        mInitData.mInitMap = mSelectedMap;
    }

    private void resetReviewInitData(String des,String status,String time){
        mInitData.des = des;
        mInitData.mInitMap = mSelectedMap;
        switch (status) {
            case CommonConfig.STATUS_CLOSED://合格
                mInitData.flag = true;
                break;
            case CommonConfig.STATUS_NOT_ACCEPTED://不合格
                mInitData.flag = false;
                break;
        }
        mInitData.time = time;
    }

    private void createSaveRepair(final String des) {
        QualityRepairParams props = getRepairParams(des);
        Subscription sub = mModel.createSaveRepair(deptId, props)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SaveBean>() {
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
                    public void onNext(SaveBean saveBean) {
                        if (mView != null) {
                            mView.dismissLoadingDialog();
                        }
                        if (saveBean != null) {
                            mIsEditStatus = true;
                            mCode = saveBean.code;
                            repairId = saveBean.id;
                            ToastManager.showSaveToast();
                            if (mView != null) {
                                mView.showDelete();
                            }
                            resetRepairInitData(des);
                        }
                    }
                });
        mSubscription.add(sub);


    }

    private void editSaveReview(final String des, final String mCurrentStatus, final String mSelectedTime) {
        QualityReviewParams props = getReviewParams(des, mCurrentStatus, mSelectedTime);
        Subscription sub = mModel.editSaveReview(deptId, reviewId, props)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseBody>() {
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
                    public void onNext(ResponseBody saveBean) {
                        if (mView != null) {
                            mView.dismissLoadingDialog();
                        }
                        if (saveBean != null) {
                            ToastManager.showSaveToast();
                            mIsEditStatus = true;
                            if (mView != null) {
                                mView.showDelete();
                            }
                            resetReviewInitData(des, mCurrentStatus, mSelectedTime);
                        }
                    }
                });
        mSubscription.add(sub);
    }

    private void createSaveReview(final String des, final String mCurrentStatus, final String mSelectedTime) {
        QualityReviewParams props = getReviewParams(des, mCurrentStatus, mSelectedTime);
        Subscription sub = mModel.createSaveReview(deptId, props)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SaveBean>() {
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
                    public void onNext(SaveBean saveBean) {
                        if (mView != null) {
                            mView.dismissLoadingDialog();
                        }
                        if (saveBean != null) {
                            mIsEditStatus = true;
                            mCode = saveBean.code;
                            reviewId = saveBean.id;
                            ToastManager.showSaveToast();
                            if (mView != null) {
                                mView.showDelete();
                            }
                            resetReviewInitData(des, mCurrentStatus, mSelectedTime);
                        }
                    }
                });
        mSubscription.add(sub);
    }

    @Override
    public void delete() {
        if(NetWorkUtils.isNetworkAvailable(mView.getActivity())) {
            if(mView!=null){
                mView.showLoadingDialog();
            }
            switch (mCreateType) {
                case CommonConfig.CREATE_TYPE_REPAIR:
                    deleteRepairInfo();
                    break;
                case CommonConfig.CREATE_TYPE_REVIEW:
                    deleteReviewInfo();
                    break;
            }
        }else{
            ToastManager.showNetWorkToast();
        }
    }

    //删除复查单
    private void deleteReviewInfo() {
        Subscription sub = mModel.deleteReview(deptId, reviewId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseBody>() {
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
                    public void onNext(ResponseBody responseBody) {
                        if (mView != null) {
                            mView.dismissLoadingDialog();
                        }
                        if (mView != null) {
                            mView.getActivity().finish();
                        }
                    }
                });
        mSubscription.add(sub);
    }

    //删除整改单
    private void deleteRepairInfo() {
        Subscription sub = mModel.deleteRepair(deptId, repairId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseBody>() {
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
                    public void onNext(ResponseBody responseBody) {
                        if (mView != null) {
                            mView.dismissLoadingDialog();
                        }
                        if (mView != null) {
                            mView.getActivity().finish();
                        }
                    }
                });
        mSubscription.add(sub);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_OPEN_ALBUM:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    AlbumData album = (AlbumData) data.getSerializableExtra(CommonConfig.ALBUM_DATA);
                    if (album != null) {
                        mSelectedMap = album.map;
                        if (mView != null) {
                            mView.showImages(mSelectedMap);
                        }
                    }
                }
                break;
            case REQUEST_CODE_TAKE_PHOTO:
                if (resultCode == Activity.RESULT_OK) {
                    //正常返回
                    Intent intent = new Intent(mView.getActivity(), PhotoEditActivity.class);
                    intent.putExtra(CommonConfig.IMAGE_PATH, mPhotoPath);
                    intent.putExtra(CommonConfig.FROM_CREATE_CHECK_LIST, true);
                    mView.getActivity().startActivityForResult(intent, REQUEST_CODE_PHOTO_EDIT);

                } else if (resultCode == Activity.RESULT_CANCELED) {
                    //返回键返回
                }
                break;
            case REQUEST_CODE_PHOTO_EDIT:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    mPhotoPath = data.getStringExtra(CommonConfig.IAMGE_SAVE_PATH);
                    ImageItem item = new ImageItem();
                    item.imagePath = mPhotoPath;
                    mSelectedMap.put(mPhotoPath, item);
                    if (mView != null) {
                        mView.showImages(mSelectedMap);
                    }
                }
                break;
            case REQUEST_CODE_PHOTO_PREVIEW:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    AlbumData album = (AlbumData) data.getSerializableExtra(CommonConfig.ALBUM_DATA);
                    if (album != null) {
                        mSelectedMap = album.map;
                        if (mView != null) {
                            mView.showImages(mSelectedMap);
                        }
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
        mView = null;
        mModel = null;
    }


}
