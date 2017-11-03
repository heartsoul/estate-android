package com.glodon.bim.business.qualityManage.presenter;

import android.app.Activity;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

import com.glodon.bim.basic.config.AppConfig;
import com.glodon.bim.basic.log.LogUtil;
import com.glodon.bim.basic.network.NetRequest;
import com.glodon.bim.basic.utils.CameraUtil;
import com.glodon.bim.basic.utils.SharedPreferencesUtil;
import com.glodon.bim.business.qualityManage.bean.CompanyItem;
import com.glodon.bim.business.qualityManage.bean.CreateCheckListParams;
import com.glodon.bim.business.qualityManage.bean.ModuleListBeanItem;
import com.glodon.bim.business.qualityManage.bean.PersonItem;
import com.glodon.bim.business.qualityManage.bean.SaveBean;
import com.glodon.bim.business.qualityManage.contract.CreateCheckListContract;
import com.glodon.bim.business.qualityManage.model.CreateCheckListApi;
import com.glodon.bim.business.qualityManage.model.CreateCheckListModel;
import com.glodon.bim.business.qualityManage.view.ChooseModuleActivity;
import com.glodon.bim.business.qualityManage.view.PhotoEditActivity;
import com.glodon.bim.common.config.CommonConfig;
import com.glodon.bim.customview.ToastManager;
import com.glodon.bim.customview.album.AlbumData;
import com.glodon.bim.customview.album.AlbumEditActivity;
import com.glodon.bim.customview.album.TNBImageItem;
import com.glodon.bim.customview.photopreview.PhotoPreviewActivity;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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
 * 描述：新建检查单
 * 作者：zhourf on 2017/10/26
 * 邮箱：zhourf@glodon.com
 */

public class CreateCheckListPresenter implements CreateCheckListContract.Presenter {

    private final int REQUEST_CODE_CHOOSE_MODULE = 0;//跳转到选择质检项目
    private final int REQUEST_CODE_OPEN_ALBUM = 1;
    private final int REQUEST_CODE_TAKE_PHOTO = 2;
    private final int REQUEST_CODE_PHOTO_EDIT = 3;
    private static final int REQUEST_CODE_PHOTO_PREVIEW = 4;//图片预览
    private CreateCheckListContract.Model mModel;
    private CreateCheckListContract.View mView;
    private CompositeSubscription mSubscritption;

    private long mProjectId;//项目id
    private long mInspectId = -1;//检查单id，新增时没有   编辑时有值
    //施工单位列表
    private List<CompanyItem> mCompanyList;
    private List<String> mCompanyNameList;
    private int mCompanySelectPosition = 0;
    //责任人列表
    private List<PersonItem> mPersonList;
    private List<String> mPersonNameList;
    private int mPersonSelectPosition = -1;
    //图片
    private LinkedHashMap<String, TNBImageItem> mSelectedMap;
    //质检项目
    private int mModuleSelectPosition = -1;
    private ModuleListBeanItem mModuleSelectInfo;

    //新建检查单参数
    private CreateCheckListParams mInput;
    private String mPhotoPath;//拍照的路径

    public CreateCheckListPresenter(CreateCheckListContract.View mView) {
        this.mView = mView;
        mModel = new CreateCheckListModel();
        mSubscritption = new CompositeSubscription();
        mCompanyNameList = new ArrayList<>();
        mPersonNameList = new ArrayList<>();
        mProjectId = SharedPreferencesUtil.getProjectId();
        mInput = new CreateCheckListParams();
        mInput.projectId = mProjectId;
        mInput.code = SystemClock.currentThreadTimeMillis() + "";
        mSelectedMap = new LinkedHashMap<>();
    }

    @Override
    public void initData(Intent intent) {

        getCompanyList();
    }

    //获取施工单位列表
    private void getCompanyList() {
        List<String> list = new ArrayList<>();
        list.add("SGDW");
        Subscription sub = mModel.getCompaniesList(mProjectId, list)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<CompanyItem>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e("---", e.getMessage());
                    }

                    @Override
                    public void onNext(List<CompanyItem> companyItems) {

                        mCompanyList = companyItems;
                        mCompanyNameList.clear();
                        if (mCompanyList != null && mCompanyList.size() > 0) {
                            for (CompanyItem item : mCompanyList) {
                                mCompanyNameList.add(item.name);
                            }
                            mView.showCompany(mCompanyList.get(0));
                        }
                    }
                });
        mSubscritption.add(sub);
    }

    //获取责任人列表
    @Override
    public void getPersonList() {
        Subscription sub = mModel.gePersonList(mProjectId, mCompanyList.get(mCompanySelectPosition).coperationId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<PersonItem>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<PersonItem> personItems) {
                        mPersonList = personItems;
                        mPersonNameList.clear();
                        if (mPersonList != null && mPersonList.size() > 0) {
                            for (PersonItem item : mPersonList) {
                                mPersonNameList.add(item.name);
                            }
                            if (mView != null) {
                                mView.showPersonList(mPersonNameList, mPersonSelectPosition);
                            }
                        }
                    }
                });
        mSubscritption.add(sub);
    }

    @Override
    public void setPersonSelectedPosition(int position) {
        this.mPersonSelectPosition = position;
    }


    @Override
    public void submit(CreateCheckListParams params) {
        assembleParams(params);

        if (mInspectId == -1) {
            //新增
            Subscription sub = mModel.createSubmit(mProjectId, mInput)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<SaveBean>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            LogUtil.e("submit---response", e.getMessage());
                        }

                        @Override
                        public void onNext(SaveBean responseBody) {
                            LogUtil.e("submit---response", responseBody.id + "");
                            ToastManager.showSubmitToast();
                            if (mView != null) {
                                mView.getActivity().finish();
                            }
                        }
                    });
            mSubscritption.add(sub);
        } else {
            //编辑
            Subscription sub = mModel.editSubmit(mProjectId, mInspectId, mInput)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<ResponseBody>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            LogUtil.e("edit submit error---response", e.getMessage());
                        }

                        @Override
                        public void onNext(ResponseBody responseBody) {
                            try {
                                LogUtil.e("edit submit---response", responseBody.string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            ToastManager.showSubmitToast();
                            if (mView != null) {
                                mView.getActivity().finish();
                            }
                        }
                    });
            mSubscritption.add(sub);
        }
    }


    private void uploadImage() {
        String url = "";
        for (Map.Entry<String, TNBImageItem> entry : mSelectedMap.entrySet()) {
            url = entry.getValue().imagePath;
        }
        final File file = new File(url);
        String containerId = SystemClock.currentThreadTimeMillis() + "";
        final String name = file.getName();
        String digest = name;
        long length = file.length();
        LogUtil.e("length=",length+"");
        Subscription sub = mModel.getOperationCode(containerId, name, digest, length)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e("code error = ", e.getMessage());
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        try {
                            String operationCode = responseBody.string();
                            LogUtil.e("code = ",operationCode);

                            // 创建 RequestBody，用于封装 请求RequestBody
//                            RequestBody requestFile =
////                                    RequestBody.create(MediaType.parse("multipart/form-data"), file);
//                            RequestBody.create(MediaType.parse("image/png"), file);
//
//                            // MultipartBody.Part is used to send also the actual file name
//                            MultipartBody.Part body =
//                                    MultipartBody.Part.createFormData("image", file.getName(), requestFile);

                            // 添加描述
//                            String descriptionString = file.getName();
//                            RequestBody description =
//                                    RequestBody.create(
//                                            MediaType.parse("multipart/form-data"), descriptionString);

                            //----------------------------------------
//                            RequestBody body = RequestBody.create(MediaType.parse("multipart/form-data"), file);
//                            MultipartBody.Part part = MultipartBody.Part.createFormData("imgfile", file.getName(), body);

//                            MultipartBody.Builder builder = new MultipartBody.Builder();
//                            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
//                            builder.addFormDataPart("file", file.getName(), requestBody);
//                            builder.setType(MultipartBody.FORM);
//
//
//                            Subscription subs = mModel.uploadImage(operationCode,builder.build())
//                                    .subscribeOn(Schedulers.io())
//                                    .observeOn(AndroidSchedulers.mainThread())
//                                    .subscribe(new Subscriber<ResponseBody>() {
//                                        @Override
//                                        public void onCompleted() {
//
//                                        }
//
//                                        @Override
//                                        public void onError(Throwable e) {
//                                            LogUtil.e("upload error = ", e.getMessage());
//                                        }
//
//                                        @Override
//                                        public void onNext(ResponseBody bean) {
//                                            if (bean != null) {
//                                                try {
//                                                    LogUtil.e("upload result=", bean.string());
//                                                } catch (IOException e) {
//                                                    e.printStackTrace();
//                                                }
//                                            }
//
//                                        }
//                                    });
//                            mSubscritption.add(subs);

                            uploadImage2(operationCode,file);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
        mSubscritption.add(sub);
    }


    private void uploadImage2(String operationCode,File file) {


        //Create Upload Server Client
        CreateCheckListApi service = NetRequest.getInstance().getCall(AppConfig.BASE_UPLOAD_URL,CreateCheckListApi.class);

        //File creating from selected URL

        // create RequestBody instance from file
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpg"), file);

        MultipartBody.Part body =
                MultipartBody.Part.createFormData("uploaded_file", file.getName(), requestFile);
//        ("nss/v1/insecure/{operationCode}")
        String temp = "nss/v1/insecure/objects?operationCode="+operationCode;
        String url="";
        try {
            url = URLEncoder.encode(temp,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Call<ResponseBody> resultCall = service.uploadImage2(temp,body);

        resultCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    LogUtil.e("upload result888=", response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                LogUtil.e("error 98989=",t.getMessage());
            }
        });
    }

    @Override
    public void save(CreateCheckListParams params) {
//        uploadImage();
        assembleParams(params);
        if(mInspectId==-1) {
            //新增
            Subscription sub = mModel.createSave(mProjectId, mInput)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<SaveBean>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            LogUtil.e("save error---response", e.getMessage());
                        }

                        @Override
                        public void onNext(SaveBean responseBody) {
                            LogUtil.e("save---response", responseBody.id + "");
                            mInspectId = responseBody.id;
                            ToastManager.showSaveToast();
                            if (mView != null) {
                                mView.showDeleteButton();
                            }
                        }
                    });
            mSubscritption.add(sub);
        }else{
            //编辑
            Subscription sub = mModel.editSave(mProjectId, mInspectId,mInput)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<ResponseBody>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            LogUtil.e("edit save error---response", e.getMessage());
                        }

                        @Override
                        public void onNext(ResponseBody responseBody) {
                            try {
                                LogUtil.e("edit save---response", responseBody.string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
//                            mInspectId = responseBody.id;
                            ToastManager.showSaveToast();
                            if (mView != null) {
                                mView.showDeleteButton();
                            }
                        }
                    });
            mSubscritption.add(sub);
        }
    }

    @Override
    public void openAlbum() {
        Intent intent = new Intent(mView.getActivity(), AlbumEditActivity.class);
        intent.putExtra(CommonConfig.ALBUM_FROM_TYPE, 1);
        intent.putExtra(CommonConfig.ALBUM_DATA, new AlbumData(mSelectedMap));
        mView.getActivity().startActivityForResult(intent, REQUEST_CODE_OPEN_ALBUM);
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
    public void takePhoto() {
        mPhotoPath = CameraUtil.getFilePath();
        CameraUtil.openCamera(mPhotoPath, mView.getActivity(), REQUEST_CODE_TAKE_PHOTO);
    }

    @Override
    public void setSelectedImages(LinkedHashMap<String, TNBImageItem> map) {
        this.mSelectedMap = map;
    }

    @Override
    public void deleteCheckList() {
        Subscription sub = mModel.createDelete(mProjectId, mInspectId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e("delete---response", e.getMessage());
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        try {
                            LogUtil.e("delete---response", responseBody.string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (mView != null) {
                            mView.getActivity().finish();
                        }
                    }
                });
        mSubscritption.add(sub);
    }

    //组织保存和提交的数据
    private void assembleParams(CreateCheckListParams params) {

        //施工单位
        mInput.constructionCompanyId = mCompanyList.get(mCompanySelectPosition).id;
        //责任人
        mInput.responsibleUserId = mPersonList.get(mPersonSelectPosition).userId;
        mInput.responsibleUserName = mPersonList.get(mPersonSelectPosition).name;
        //现场描述
        mInput.description = params.description;
        //图片描述
        mInput.files = params.files;
        //项目名称
        mInput.projectName = SharedPreferencesUtil.getProjectName();
        //新建的时间
        mInput.inspectionDate = SystemClock.currentThreadTimeMillis() + "";
        //是否整改
        mInput.isNeedRectification = params.isNeedRectification;
        mInput.lastRectificationDate = params.lastRectificationDate;
        //质检项目
        mInput.inspectionProjectName = mModuleSelectInfo.name;
        mInput.inspectionProjectId = mModuleSelectInfo.id;
    }

    @Override
    public void toModuleList() {
        Intent intent = new Intent(mView.getActivity(), ChooseModuleActivity.class);
        intent.putExtra(CommonConfig.MODULE_LIST_POSITION, mModuleSelectPosition);
        mView.getActivity().startActivityForResult(intent, REQUEST_CODE_CHOOSE_MODULE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_CHOOSE_MODULE:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    mModuleSelectPosition = data.getIntExtra(CommonConfig.MODULE_LIST_POSITION, -1);
                    mModuleSelectInfo = (ModuleListBeanItem) data.getSerializableExtra(CommonConfig.MODULE_LIST_NAME);
                    if (mView != null && mModuleSelectInfo != null) {
                        mView.showModuleName(mModuleSelectInfo.name);
                    }
                }
                break;
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
        if (mSubscritption != null) {
            mSubscritption.unsubscribe();
            mSubscritption = null;
        }
    }

    @Override
    public void showCompanyList() {
        if (mView != null) {
            mView.showCompanyList(mCompanyNameList, mCompanySelectPosition);
        }
    }

    @Override
    public void setCompanySelectedPosition(int position) {
        this.mCompanySelectPosition = position;
    }
}
