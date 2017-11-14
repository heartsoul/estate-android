package com.glodon.bim.business.qualityManage.presenter;

import android.app.Activity;
import android.content.Intent;

import com.glodon.bim.basic.log.LogUtil;
import com.glodon.bim.basic.utils.CameraUtil;
import com.glodon.bim.basic.utils.NetWorkUtils;
import com.glodon.bim.basic.utils.SharedPreferencesUtil;
import com.glodon.bim.business.qualityManage.bean.CompanyItem;
import com.glodon.bim.business.qualityManage.bean.CreateCheckListParams;
import com.glodon.bim.business.qualityManage.bean.CreateCheckListParamsFile;
import com.glodon.bim.business.qualityManage.bean.ModuleListBeanItem;
import com.glodon.bim.business.qualityManage.bean.PersonItem;
import com.glodon.bim.business.qualityManage.bean.SaveBean;
import com.glodon.bim.business.qualityManage.contract.CreateCheckListContract;
import com.glodon.bim.business.qualityManage.listener.OnUploadImageListener;
import com.glodon.bim.business.qualityManage.model.CreateCheckListModel;
import com.glodon.bim.business.qualityManage.util.UploadManger;
import com.glodon.bim.business.qualityManage.view.ChooseModuleActivity;
import com.glodon.bim.business.qualityManage.view.PhotoEditActivity;
import com.glodon.bim.common.config.CommonConfig;
import com.glodon.bim.customview.ToastManager;
import com.glodon.bim.customview.album.AlbumData;
import com.glodon.bim.customview.album.AlbumEditActivity;
import com.glodon.bim.customview.album.TNBImageItem;
import com.glodon.bim.customview.photopreview.PhotoPreviewActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
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
//    private int mModuleSelectPosition = -1;
    private ModuleListBeanItem mModuleSelectInfo;

    //新建检查单参数
    private CreateCheckListParams mInput;
    private String mPhotoPath;//拍照的路径

    private String mCode = "";//当前单据code

    //编辑状态下  前面传递过来的参数
    private CreateCheckListParams mEditParams;

    private boolean mIsChange = false;//是否保存了

    @Override
    public boolean isChange() {
        return mIsChange;
    }

    public CreateCheckListPresenter(CreateCheckListContract.View mView) {
        this.mView = mView;
        mModel = new CreateCheckListModel();
        mSubscritption = new CompositeSubscription();
        mCompanyNameList = new ArrayList<>();
        mPersonNameList = new ArrayList<>();
        mProjectId = SharedPreferencesUtil.getProjectId();
        mInput = new CreateCheckListParams();
        mInput.projectId = mProjectId;
        mSelectedMap = new LinkedHashMap<>();
        mModuleSelectInfo = new ModuleListBeanItem();
    }


    @Override
    public void setEditState(CreateCheckListParams mParams) {
        mInput = mParams;
        mCode = mParams.code;
        mInspectId = mParams.inspectId;
    }

    @Override
    public void initData(Intent intent) {
        mEditParams = (CreateCheckListParams) intent.getSerializableExtra(CommonConfig.CREATE_CHECK_LIST_PROPS);
        getCompanyList(mEditParams);

    }

    //初始化图片
    private void initImages() {
        if(mEditParams!=null && mEditParams.files!=null && mEditParams.files.size()>0) {
            for (CreateCheckListParamsFile file : mEditParams.files) {
                TNBImageItem item = new TNBImageItem();
                item.imagePath = file.url;
                item.objectId = file.objectId;
                item.urlFile = file;
                mSelectedMap.put(item.imagePath, item);
                mView.showImages(mSelectedMap);
            }
        }
    }

    //初始化责任人  编辑状态下
    private void initPerson() {
        if (mEditParams != null) {

            //设置质检项目
            mModuleSelectInfo.name = mEditParams.qualityCheckpointName;
            mModuleSelectInfo.id = mEditParams.qualityCheckpointId;
            //设置责任人
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
                                for (int i = 0; i < mPersonList.size(); i++) {
                                    PersonItem item = mPersonList.get(i);
                                    mPersonNameList.add(item.name);
                                    if (mEditParams.responsibleUserName.equals(item.name)) {
                                        mPersonSelectPosition = i;
                                    }
                                }

                            }
                        }
                    });
            mSubscritption.add(sub);
        }
    }

    //获取施工单位列表
    private void getCompanyList(final CreateCheckListParams mParams) {
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

                            //设置编辑状态的施工单位
                            if (mParams != null) {
                                CompanyItem item = new CompanyItem();
                                item.id = mParams.constructionCompanyId;
                                item.name = mParams.constructionCompanyName;
                                mView.showCompany(item);
                                for (int i = 0; i < mCompanyList.size(); i++) {
                                    if (item.id == mCompanyList.get(i).id) {
                                        mCompanySelectPosition = i;
                                        break;
                                    }
                                }
                                initPerson();
                                initImages();
                            } else {
                                mView.showCompany(mCompanyList.get(0));
                            }
                        }
                    }
                });
        mSubscritption.add(sub);
    }


    //获取责任人列表
    @Override
    public void getPersonList() {
        if(mCompanyList!=null && mCompanySelectPosition<mCompanyList.size()) {
            Subscription sub = mModel.gePersonList(mProjectId, mCompanyList.get(mCompanySelectPosition).coperationId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<List<PersonItem>>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            LogUtil.e("person error=" + e.getMessage());
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
        }else{
            ToastManager.show("请先选择施工单位!");
        }
    }

    @Override
    public void setPersonSelectedPosition(int position) {
        this.mPersonSelectPosition = position;
    }


    @Override
    public void submit(final CreateCheckListParams params) {
        if (NetWorkUtils.isNetworkAvailable(mView.getActivity())) {
            if (mView != null) {
                mView.showLoadingDialog();
            }
            if (mSelectedMap.size() > 0) {
                mIsShowUploadErrorToast = true;
                new UploadManger(mSelectedMap).uploadImages(new OnUploadImageListener() {
                    @Override
                    public void onUploadFinished(List<CreateCheckListParamsFile> list) {
                        int i = 0;
                        for (Map.Entry<String, TNBImageItem> entry : mSelectedMap.entrySet()) {
                            TNBImageItem item = entry.getValue();
                            item.objectId = list.get(i).objectId;
                            item.urlFile = list.get(i);
                            i++;
                        }
                        params.files = list;
                        toSubmit(params);
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
                toSubmit(params);
            }
        } else {
            ToastManager.showNetWorkToast();
        }

    }

    private void toSubmit(CreateCheckListParams params) {
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
                            if (mView != null) {
                                mView.dismissLoadingDialog();
                            }
                        }

                        @Override
                        public void onNext(SaveBean responseBody) {
                            if (mView != null) {
                                mView.dismissLoadingDialog();
                            }
                            LogUtil.e("submit---response", responseBody.id + "");
                            ToastManager.showSubmitToast();
                            if (mView != null) {
                                mView.getActivity().setResult(Activity.RESULT_OK);
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
                            if (mView != null) {
                                mView.dismissLoadingDialog();
                            }
                        }

                        @Override
                        public void onNext(ResponseBody responseBody) {
                            if (mView != null) {
                                mView.dismissLoadingDialog();
                            }
                            try {
                                LogUtil.e("edit submit---response", responseBody.string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            ToastManager.showSubmitToast();
                            if (mView != null) {
                                mView.getActivity().setResult(Activity.RESULT_OK);
                                mView.getActivity().finish();
                            }
                        }
                    });
            mSubscritption.add(sub);
        }
    }

    private boolean mIsShowUploadErrorToast = true;

    @Override
    public void save(final CreateCheckListParams params) {
        if (NetWorkUtils.isNetworkAvailable(mView.getActivity())) {
            if (mView != null) {
                mView.showLoadingDialog();
            }
            if (mSelectedMap.size() > 0) {
                mIsShowUploadErrorToast = true;
                new UploadManger(mSelectedMap).uploadImages(new OnUploadImageListener() {
                    @Override
                    public void onUploadFinished(List<CreateCheckListParamsFile> list) {
                        params.files = list;
                        toSave(params);
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
                toSave(params);
            }
        } else {
            ToastManager.showNetWorkToast();
        }
    }

    private void toSave(CreateCheckListParams params) {
        assembleParams(params);
        if (mInspectId == -1) {
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
                            if (mView != null) {
                                mView.dismissLoadingDialog();
                            }
                        }

                        @Override
                        public void onNext(SaveBean responseBody) {
                            if (mView != null) {
                                mView.dismissLoadingDialog();
                            }
                            mIsChange=true;
                            mInspectId = responseBody.id;
                            mCode = responseBody.code;
                            ToastManager.showSaveToast();
                            if (mView != null) {
                                mView.showDeleteButton();
                            }
                        }
                    });
            mSubscritption.add(sub);
        } else {
            //编辑
            Subscription sub = mModel.editSave(mProjectId, mInspectId, mInput)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<ResponseBody>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            LogUtil.e("edit save error---response", e.getMessage());
                            if (mView != null) {
                                mView.dismissLoadingDialog();
                            }
                        }

                        @Override
                        public void onNext(ResponseBody responseBody) {
                            if (mView != null) {
                                mView.dismissLoadingDialog();
                            }
                            mIsChange=true;
                            if (responseBody != null) {
                                ToastManager.showSaveToast();
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
        if (NetWorkUtils.isNetworkAvailable(mView.getActivity())) {
            if (mView != null) {
                mView.showLoadingDialog();
            }
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
                            if (mView != null) {
                                mView.dismissLoadingDialog();
                            }
                        }

                        @Override
                        public void onNext(ResponseBody responseBody) {
                            if (mView != null) {
                                mView.dismissLoadingDialog();
                            }
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
        } else {
            ToastManager.showNetWorkToast();
        }
    }

    //组织保存和提交的数据
    private void assembleParams(CreateCheckListParams params) {

        //施工单位
        mInput.constructionCompanyId = mCompanyList.get(mCompanySelectPosition).id;
        mInput.constructionCompanyName = mCompanyList.get(mCompanySelectPosition).name;
        //责任人
        mInput.responsibleUserId = mPersonList.get(mPersonSelectPosition).userId;
        mInput.responsibleUserName = mPersonList.get(mPersonSelectPosition).name;
        mInput.responsibleUserTitle = mPersonList.get(mPersonSelectPosition).title;
//        mInput.responsibleUserId = 111;
//        mInput.responsibleUserName = "张三";
//        mInput.responsibleUserTitle = " 掌柜";
        //现场描述
        mInput.description = params.description;
        //图片描述
        mInput.files = params.files;
        //项目名称
        mInput.projectName = SharedPreferencesUtil.getProjectName();
        //新建的时间
//        mInput.inspectionDate = SystemClock.currentThreadTimeMillis() + "";
        //是否整改
        mInput.needRectification = params.needRectification;
        mInput.lastRectificationDate = params.lastRectificationDate;
        //质检项目

        mInput.qualityCheckpointName = mModuleSelectInfo.name;
        mInput.qualityCheckpointId = mModuleSelectInfo.id;

        mInput.code = mCode;
    }

    @Override
    public void toModuleList() {
        Intent intent = new Intent(mView.getActivity(), ChooseModuleActivity.class);
        intent.putExtra(CommonConfig.MODULE_LIST_POSITION, mModuleSelectInfo.id);
        mView.getActivity().startActivityForResult(intent, REQUEST_CODE_CHOOSE_MODULE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_CHOOSE_MODULE:
                if (resultCode == Activity.RESULT_OK && data != null) {
//                    mModuleSelectPosition = data.getIntExtra(CommonConfig.MODULE_LIST_POSITION, -1);
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
