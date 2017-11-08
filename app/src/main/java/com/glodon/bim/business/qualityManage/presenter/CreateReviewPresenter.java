package com.glodon.bim.business.qualityManage.presenter;

import android.app.Activity;
import android.content.Intent;

import com.glodon.bim.basic.config.AppConfig;
import com.glodon.bim.basic.log.LogUtil;
import com.glodon.bim.basic.network.NetRequest;
import com.glodon.bim.basic.utils.CameraUtil;
import com.glodon.bim.business.greendao.provider.DaoProvider;
import com.glodon.bim.business.qualityManage.bean.QualityCheckListDetailBean;
import com.glodon.bim.business.qualityManage.bean.QualityCheckListDetailProgressInfo;
import com.glodon.bim.business.qualityManage.bean.QualityGetRepairInfo;
import com.glodon.bim.business.qualityManage.bean.QualityGetReviewInfo;
import com.glodon.bim.business.qualityManage.bean.QualityRepairParams;
import com.glodon.bim.business.qualityManage.bean.QualityReviewParams;
import com.glodon.bim.business.qualityManage.bean.SaveBean;
import com.glodon.bim.business.qualityManage.contract.CreateReviewContract;
import com.glodon.bim.business.qualityManage.model.CreateReviewApi;
import com.glodon.bim.business.qualityManage.model.CreateReviewModel;
import com.glodon.bim.business.qualityManage.view.PhotoEditActivity;
import com.glodon.bim.common.config.CommonConfig;
import com.glodon.bim.customview.album.AlbumData;
import com.glodon.bim.customview.album.AlbumEditActivity;
import com.glodon.bim.customview.album.TNBImageItem;
import com.glodon.bim.customview.photopreview.PhotoPreviewActivity;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
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

public class CreateReviewPresenter implements CreateReviewContract.Presenter{

    private CreateReviewContract.View mView;
    private CreateReviewContract.Model mModel;
    private CompositeSubscription mSubscription;

    private final int REQUEST_CODE_OPEN_ALBUM = 1;
    private final int REQUEST_CODE_TAKE_PHOTO = 2;
    private final int REQUEST_CODE_PHOTO_EDIT = 3;
    private static final int REQUEST_CODE_PHOTO_PREVIEW = 4;//图片预览

    private String mPhotoPath;//拍照的路径

    //图片
    private LinkedHashMap<String, TNBImageItem> mSelectedMap;

    private long deptId,id;//项目id 和检查单id

    private String mCreateType; //创建的类型

    private boolean mIsEditStatus = false;//是否编辑状态
    private String mCode = "";//当前单据的code



    public CreateReviewPresenter(CreateReviewContract.View mView) {
        this.mView = mView;
        mModel = new CreateReviewModel();
        mSubscription = new CompositeSubscription();
    }

    @Override
    public void setSelectedImages(LinkedHashMap<String, TNBImageItem> map) {
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
        deptId = intent.getLongExtra(CommonConfig.QUALITY_CHECK_LIST_DEPTID,0);
        id = intent.getLongExtra(CommonConfig.QUALITY_CHECK_LIST_ID,0);
        mCreateType = intent.getStringExtra(CommonConfig.CREATE_TYPE);
        //展示title和检查单详情
        if(mView!=null){
            mView.showTitleByType(mCreateType);
            mView.showDetail(deptId,id);
        }
        //查询是否存在之前保存过的数据
        getEditInfo();
    }

    //查询是否存在之前保存过的数据
    private void getEditInfo() {
        switch (mCreateType){
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
        Subscription sub = mModel.getRepairInfo(deptId,id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<QualityGetRepairInfo>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(QualityGetRepairInfo info) {
                        if(info != null){
                            mIsEditStatus = true;
                            mCode = info.code;
                            if(mView!=null){
                                mView.showDesAndImages(info.description,info.files);
                            }
                        }
                    }
                });
        mSubscription.add(sub);
    }

    //查询复查单编辑数据
    private void getReviewInfo() {
        Subscription sub = mModel.getReviewInfo(deptId,id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<QualityGetReviewInfo>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(QualityGetReviewInfo info) {
                        if(info != null){
                            mIsEditStatus = true;
                            mCode = info.code;
                            if(mView!=null){
                                mView.showDesAndImages(info.description,info.files);
                                mView.showRectificationInfo(info.status,info.lastRectificationDate);
                            }
                        }
                    }
                });
        mSubscription.add(sub);
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

    @Override
    public void submit(String des, String mCurrentStatus, String mSelectedTime) {
        switch (mCreateType){
            case CommonConfig.CREATE_TYPE_REPAIR:
                if(mIsEditStatus){
                    editSubmitRepair(des);
                }else{
                    createSubmitRepair(des);
                }
                break;
            case CommonConfig.CREATE_TYPE_REVIEW:
                if(mIsEditStatus){
                    editSubmitReview(des,mCurrentStatus,mSelectedTime);
                }else{
                    createSubmitReview(des,mCurrentStatus,mSelectedTime);
                }
                break;
        }
    }

    private void editSubmitRepair(String des) {
        QualityRepairParams props = getRepairParams(des);
        Subscription sub = mModel.editSubmitRepair(deptId,id,props)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SaveBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(SaveBean saveBean) {
                        if(saveBean!=null){
                            if(mView!=null){
                                mView.getActivity().finish();
                            }
                        }
                    }
                });
        mSubscription.add(sub);
    }

    private void createSubmitRepair(String des) {
        QualityRepairParams props = getRepairParams(des);
        Subscription sub = mModel.createSubmitRepair(deptId,props)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SaveBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e("e="+e.getMessage());
                    }

                    @Override
                    public void onNext(SaveBean saveBean) {
                        if(saveBean!=null){
                            if(mView!=null){
                                mView.getActivity().finish();
                            }
                        }
                    }
                });
        mSubscription.add(sub);
    }

    //组织整改单参数
    private QualityRepairParams getRepairParams(String des){
        QualityRepairParams props = new QualityRepairParams();
        props.code = mCode;
        props.description = des;
        props.inspectionId = id;
        QualityCheckListDetailBean info = mView.getDetailInfo();
        if(info!=null && info.progressInfos!=null && info.progressInfos.size()>0){
            List<QualityCheckListDetailProgressInfo> list = info.progressInfos;
//            props.flawCode = list.get(list.size()-1).code;
            props.flawId = list.get(list.size()-1).id;
        }else{
//            props.flawCode = info.inspectionInfo.code;
            props.flawId = info.inspectionInfo.id;
        }
        return props;
    }

    private void editSubmitReview(String des, String mCurrentStatus, String mSelectedTime) {
        QualityReviewParams props = getReviewParams(des,mCurrentStatus,mSelectedTime);
        Subscription sub = mModel.editSubmitReview(deptId,id,props)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SaveBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(SaveBean saveBean) {
                        if(saveBean!=null){
                            if(mView!=null){
                                mView.getActivity().finish();
                            }
                        }
                    }
                });
        mSubscription.add(sub);
    }

    private void createSubmitReview(String des, String mCurrentStatus, String mSelectedTime) {
        QualityReviewParams props = getReviewParams(des,mCurrentStatus,mSelectedTime);
        Subscription sub = mModel.createSubmitReview(deptId,props)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SaveBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(SaveBean saveBean) {
                        if(saveBean!=null){
                            if(mView!=null){
                                mView.getActivity().finish();
                            }
                        }
                    }
                });
        mSubscription.add(sub);
    }

    private QualityReviewParams getReviewParams(String des, String mCurrentStatus, String mSelectedTime){
        QualityReviewParams props = new QualityReviewParams();
        props.code = mCode;
        props.description = des;
        props.inspectionId = id;
        props.status = mCurrentStatus;
        props.lastRectificationDate = mSelectedTime;
        QualityCheckListDetailBean info = mView.getDetailInfo();
        if(info!=null && info.progressInfos!=null && info.progressInfos.size()>0){
            List<QualityCheckListDetailProgressInfo> list = info.progressInfos;
//            props.rectificationCode = list.get(list.size()-1).code;
            props.rectificationId = list.get(list.size()-1).id;
        }
        return props;
    }

    @Override
    public void save(String des, String mCurrentStatus, String mSelectedTime) {
        switch (mCreateType){
            case CommonConfig.CREATE_TYPE_REPAIR:
                if(mIsEditStatus){
                    editSaveRepair(des);
                }else{
                    createSaveRepair(des);
                }
                break;
            case CommonConfig.CREATE_TYPE_REVIEW:
                if(mIsEditStatus){
                    editSaveReview(des,mCurrentStatus,mSelectedTime);
                }else{
                    createSaveReview(des,mCurrentStatus,mSelectedTime);
                }
                break;
        }
    }

    private void editSaveRepair(String des) {
        QualityRepairParams props = getRepairParams(des);
        Subscription sub = mModel.editSaveRepair(deptId,id,props)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SaveBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(SaveBean saveBean) {
                        if(saveBean!=null){
                            mCode = saveBean.code;
                            if(mView!=null){
                                mView.showDelete();
                            }
                        }
                    }
                });
        mSubscription.add(sub);
    }

    private void createSaveRepair(String des) {
        QualityRepairParams props = getRepairParams(des);
//        Subscription sub = mModel.createSaveRepair(deptId,props)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<SaveBean>() {
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
//                    public void onNext(SaveBean saveBean) {
//                        if(saveBean!=null){
//                            mCode = saveBean.code;
//                            if(mView!=null){
//                                mView.showDelete();
//                            }
//                        }
//                    }
//                });
//        mSubscription.add(sub);
        NetRequest.getInstance().getCall(AppConfig.BASE_URL, CreateReviewApi.class).createSaveRepair2(deptId,props,new DaoProvider().getCookie())
        .enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    LogUtil.e(response.errorBody().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                LogUtil.e("er="+t.getMessage());
            }
        });

    }

    private void editSaveReview(String des, String mCurrentStatus, String mSelectedTime) {
        QualityReviewParams props = getReviewParams(des,mCurrentStatus,mSelectedTime);
        Subscription sub = mModel.editSaveReview(deptId,id,props)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SaveBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(SaveBean saveBean) {
                        if(saveBean!=null){
                            mCode = saveBean.code;
                            if(mView!=null){
                                mView.showDelete();
                            }
                        }
                    }
                });
        mSubscription.add(sub);
    }

    private void createSaveReview(String des, String mCurrentStatus, String mSelectedTime) {
        QualityReviewParams props = getReviewParams(des,mCurrentStatus,mSelectedTime);
        Subscription sub = mModel.createSaveReview(deptId,props)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SaveBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(SaveBean saveBean) {
                        if(saveBean!=null){
                            mCode = saveBean.code;
                            if(mView!=null){
                                mView.showDelete();
                            }
                        }
                    }
                });
        mSubscription.add(sub);
    }

    @Override
    public void delete() {
        switch (mCreateType){
            case CommonConfig.CREATE_TYPE_REPAIR:
                deleteRepairInfo();
                break;
            case CommonConfig.CREATE_TYPE_REVIEW:
                deleteReviewInfo();
                break;
        }
    }

    //删除复查单
    private void deleteReviewInfo() {
        Subscription sub = mModel.deleteReview(deptId,id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        if(mView!=null){
                            mView.getActivity().finish();
                        }
                    }
                });
        mSubscription.add(sub);
    }

    //删除整改单
    private void deleteRepairInfo() {
        Subscription sub = mModel.deleteRepair(deptId,id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        if(mView!=null){
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
                    TNBImageItem item = new TNBImageItem();
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
        if(mSubscription!=null){
            mSubscription.unsubscribe();
            mSubscription = null;
        }
        mView = null;
        mModel = null;
    }


}
