package com.glodon.bim.business.qualityManage.presenter;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;

import com.glodon.bim.basic.log.LogUtil;
import com.glodon.bim.basic.utils.CameraUtil;
import com.glodon.bim.basic.utils.NetWorkUtils;
import com.glodon.bim.basic.utils.SharedPreferencesUtil;
import com.glodon.bim.business.qualityManage.bean.BlueprintListBeanItem;
import com.glodon.bim.business.qualityManage.bean.CompanyItem;
import com.glodon.bim.business.qualityManage.bean.CreateCheckListParams;
import com.glodon.bim.business.qualityManage.bean.CreateCheckListParamsFile;
import com.glodon.bim.business.qualityManage.bean.InspectionCompanyItem;
import com.glodon.bim.business.qualityManage.bean.ModelListBeanItem;
import com.glodon.bim.business.qualityManage.bean.ModuleListBeanItem;
import com.glodon.bim.business.qualityManage.bean.PersonItem;
import com.glodon.bim.business.qualityManage.bean.SaveBean;
import com.glodon.bim.business.qualityManage.contract.CreateCheckListContract;
import com.glodon.bim.business.qualityManage.listener.OnUploadImageListener;
import com.glodon.bim.business.qualityManage.model.CreateCheckListModel;
import com.glodon.bim.business.qualityManage.util.UploadManger;
import com.glodon.bim.business.qualityManage.view.BluePrintActivity;
import com.glodon.bim.business.qualityManage.view.ChooseModuleActivity;
import com.glodon.bim.business.qualityManage.view.ModelActivity;
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
    private final int REQUEST_CODE_CHOOSE_BLUE_PRINT = 5;//跳转到选择图纸
    private final int REQUEST_CODE_CHOOSE_MODEL = 6;//跳转到选择模型
    private CreateCheckListContract.Model mModel;
    private CreateCheckListContract.View mView;
    private CompositeSubscription mSubscritption;

    private long mProjectId;//项目id
    private long mInspectId = -1;//检查单id，新增时没有   编辑时有值
    //检查单位列表
    private List<InspectionCompanyItem> mInspectionCompanyList;
    private List<String> mInspectionCompanyNameList;
    private int mInspectionCompanySelectPosition = 0;
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
    private ModuleListBeanItem mModuleSelectInfo;
    private String mCurrentModuleName;//当前的质检项目名称
    private Long mCurrentModuleId;//当前的质检项目id

    //图纸
    private BlueprintListBeanItem mBluePrintSelectInfo;
    private String mCurrentBluePrintName;//当前的图纸名称
    private String mCurrentBluePrintId;//当前的图纸id

    //图纸
    private ModelListBeanItem mModelSelectInfo;
    private String mCurrentModelName;//当前的图纸名称
    private String mCurrentModelId;//当前的图纸id

    //新建检查单参数
    private CreateCheckListParams mInput;
    private String mPhotoPath;//拍照的路径

    private String mCode = "";//当前单据code

    //编辑状态下  前面传递过来的参数
    private CreateCheckListParams mEditParams;

    //初始的参数
    private CreateCheckListParams mInitParams;
    private boolean mIsChange = false;//是否保存了

    private String mInspectionType;

//    private String mCompanyEmptyText = "您需要去PC端添加施工单位数据";
//    private String mPersonEmptyText = "您需要去PC端添加责任人数据";

    @Override
    public boolean isChange() {
        return mIsChange;
    }

    public CreateCheckListPresenter(CreateCheckListContract.View mView) {
        this.mView = mView;
        mModel = new CreateCheckListModel();
        mSubscritption = new CompositeSubscription();
        mCompanyNameList = new ArrayList<>();
        mInspectionCompanyNameList = new ArrayList<>();
        mPersonNameList = new ArrayList<>();
        mProjectId = SharedPreferencesUtil.getProjectId();
        mInput = new CreateCheckListParams();
        mInput.projectId = mProjectId;
        mSelectedMap = new LinkedHashMap<>();
        mModuleSelectInfo = new ModuleListBeanItem();
        mBluePrintSelectInfo = new BlueprintListBeanItem();
        mInitParams = new CreateCheckListParams();
        mCurrentModuleId = new Long(-1);
        mModelSelectInfo = new ModelListBeanItem();
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
        if (mEditParams != null) {
            mInitParams.code = mEditParams.code;
            mInitParams.projectId = mEditParams.projectId;
            mInitParams.projectName = mEditParams.projectName;
            mInitParams.qualityCheckpointId = mEditParams.qualityCheckpointId;
            mInitParams.qualityCheckpointName = mEditParams.qualityCheckpointName;
            mInitParams.inspectionCompanyId = mEditParams.inspectionCompanyId;
            mInitParams.constructionCompanyId = mEditParams.constructionCompanyId;
            mInitParams.needRectification = mEditParams.needRectification;
            mInitParams.lastRectificationDate = mEditParams.lastRectificationDate;
            mInitParams.description = mEditParams.description;
            mInitParams.inspectionType = mEditParams.inspectionType;
            mInitParams.responsibleUserId = mEditParams.responsibleUserId;
            mInitParams.files = mEditParams.files;

            mInspectionType = mEditParams.inspectionType;
            mView.setTitle(mInspectionType);
        }else{
            //当从质检项目列表创建检查单时  取传递的质检项目name和id
            String name = SharedPreferencesUtil.getSelectModuleName();
            if(!TextUtils.isEmpty(name)){
                mModuleSelectInfo.id = SharedPreferencesUtil.getSelectModuleId();
                mModuleSelectInfo.name = SharedPreferencesUtil.getSelectModuleName();
                mCurrentModuleName = SharedPreferencesUtil.getSelectModuleName();
                mCurrentModuleId = SharedPreferencesUtil.getSelectModuleId();
                mInitParams.qualityCheckpointId = mCurrentModuleId;
                mInitParams.qualityCheckpointName = mCurrentModuleName;
                if(mView!=null){
                    mView.showModuleName(mCurrentModuleName,mCurrentModuleId);
                }
            }
        }
        initInspectionCompany(mEditParams);
        getCompanyList(mEditParams);

    }

    //初始化图片
    private void initImages() {
        if (mEditParams != null && mEditParams.files != null && mEditParams.files.size() > 0) {
            for (CreateCheckListParamsFile file : mEditParams.files) {
                TNBImageItem item = new TNBImageItem();
                item.imagePath = file.url;
                item.objectId = file.objectId;
                item.urlFile = file;
                mSelectedMap.put(item.imagePath, item);
            }
            mView.showImages(mSelectedMap);
        }
    }

    //获取检查单位
    private void initInspectionCompany(final CreateCheckListParams mParams) {
        Subscription sub = mModel.getInspectionCompanies(mProjectId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<InspectionCompanyItem>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(e.getMessage());
                    }

                    @Override
                    public void onNext(List<InspectionCompanyItem> inspectionCompanyItems) {
                        mInspectionCompanyList = inspectionCompanyItems;
                        mInspectionCompanyNameList.clear();
                        if (inspectionCompanyItems != null && inspectionCompanyItems.size() > 0) {

                            for (InspectionCompanyItem item : mInspectionCompanyList) {
                                mInspectionCompanyNameList.add(item.name);
                            }

                            //设置编辑状态的施工单位
                            if (mParams != null) {
                                InspectionCompanyItem item = new InspectionCompanyItem();
                                item.id = mParams.inspectionCompanyId;
                                item.name = mParams.inspectionCompanyName;
                                mView.showInspectionCompany(item);
                                for (int i = 0; i < mInspectionCompanyList.size(); i++) {
                                    if (item.id == mInspectionCompanyList.get(i).id) {
                                        mInspectionCompanySelectPosition = i;
                                        break;
                                    }
                                }
                            } else {
                                mView.showInspectionCompany(mInspectionCompanyList.get(0));
                                mInitParams.inspectionCompanyId = mInspectionCompanyList.get(0).id;
                                mInitParams.inspectionCompanyName = mInspectionCompanyList.get(0).name;
                            }
                        }
                    }
                });
        mSubscritption.add(sub);
    }

    //初始化责任人  编辑状态下
    private void initPerson() {
        if (mEditParams != null) {

            //设置质检项目
            mModuleSelectInfo.name = mEditParams.qualityCheckpointName;
            mModuleSelectInfo.id = mEditParams.qualityCheckpointId;
            mCurrentModuleName =mModuleSelectInfo.name;
            mCurrentModuleId = mModuleSelectInfo.id;
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
                                mInitParams.constructionCompanyName = mCompanyList.get(0).name;
                                mInitParams.constructionCompanyId = mCompanyList.get(0).id;
                            }
                        }else{
                            //施工单位为空  则特殊处理
                            mView.showEmptyCompany();
                        }
                    }
                });
        mSubscritption.add(sub);
    }


    //获取责任人列表
    @Override
    public void getPersonList() {
        if (mCompanyList != null && mCompanySelectPosition < mCompanyList.size()) {
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
                            }else{
                                mView.showPersonEmpty();
                            }
                        }
                    });
            mSubscritption.add(sub);
        } else {
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
//                                sendBrushBroadcast();
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
//                                sendBrushBroadcast();
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
                        int i = 0;
                        for (Map.Entry<String, TNBImageItem> entry : mSelectedMap.entrySet()) {
                            TNBImageItem item = entry.getValue();
                            item.objectId = list.get(i).objectId;
                            item.urlFile = list.get(i);
                            i++;
                            entry.setValue(item);
                        }
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
                            mIsChange = true;
                            mInspectId = responseBody.id;
                            mCode = responseBody.code;
                            ToastManager.showSaveToast();
                            if (mView != null) {
                                mView.showDeleteButton();
                            }
                            mInitParams.code = mCode;
                            resetInitParams();
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
                            mIsChange = true;
                            if (responseBody != null) {
                                ToastManager.showSaveToast();
                            }
                            resetInitParams();
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
//                                sendBrushBroadcast();
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
        //检查单位
        if (mInspectionCompanyList != null && mInspectionCompanyList.size() > 0) {
            mInput.inspectionCompanyId = mInspectionCompanyList.get(mInspectionCompanySelectPosition).id;
            mInput.inspectionCompanyName = mInspectionCompanyList.get(mInspectionCompanySelectPosition).name;
        }
        //施工单位
        if (mCompanyList != null && mCompanyList.size() > 0 && mCompanySelectPosition<mCompanyList.size()) {
            mInput.constructionCompanyId = mCompanyList.get(mCompanySelectPosition).id;
            mInput.constructionCompanyName = mCompanyList.get(mCompanySelectPosition).name;
        }
        //责任人
        if (mPersonList != null && mPersonList.size() > 0 &&mPersonSelectPosition<mPersonList.size()) {
            mInput.responsibleUserId = mPersonList.get(mPersonSelectPosition).userId;
            mInput.responsibleUserName = mPersonList.get(mPersonSelectPosition).name;
            mInput.responsibleUserTitle = mPersonList.get(mPersonSelectPosition).title;
        }
        if (params != null) {
            //现场描述
            mInput.description = params.description;
            //图片描述
            mInput.files = params.files;
            //是否整改
            mInput.needRectification = params.needRectification;
            mInput.lastRectificationDate = params.lastRectificationDate;
        }
        //质检项目
        if (mModuleSelectInfo != null) {
            if(mCurrentModuleName.equals(mModuleSelectInfo.name)) {
                mInput.qualityCheckpointName = mModuleSelectInfo.name;
                mInput.qualityCheckpointId = mModuleSelectInfo.id;
            }else{
                mInput.qualityCheckpointName = mCurrentModuleName;
                mInput.qualityCheckpointId = null;
            }
        }

        //项目名称
        mInput.projectName = SharedPreferencesUtil.getProjectName();

        mInput.code = mCode;
    }

    @Override
    public void toModuleList() {
        Intent intent = new Intent(mView.getActivity(), ChooseModuleActivity.class);
        if(!TextUtils.isEmpty(mCurrentModuleName) && !TextUtils.isEmpty(mModuleSelectInfo.name) && mModuleSelectInfo.id!=null){
            mCurrentModuleName = mView.getModuleName();
            if(mCurrentModuleName.equals(mModuleSelectInfo.name)){
                intent.putExtra(CommonConfig.MODULE_LIST_POSITION, mModuleSelectInfo.id);
            }
        }
        mView.getActivity().startActivityForResult(intent, REQUEST_CODE_CHOOSE_MODULE);
    }

    @Override
    public void toBluePrint() {
        Intent intent = new Intent(mView.getActivity(), BluePrintActivity.class);
        if(!TextUtils.isEmpty(mCurrentBluePrintName) && !TextUtils.isEmpty(mBluePrintSelectInfo.name) && mBluePrintSelectInfo.fileId !=null){
            mCurrentBluePrintName = mView.getBluePrintName();
            if(mCurrentBluePrintName.equals(mBluePrintSelectInfo.name)){
                intent.putExtra(CommonConfig.MODULE_LIST_POSITION, mBluePrintSelectInfo.fileId);
            }
        }
        mView.getActivity().startActivityForResult(intent, REQUEST_CODE_CHOOSE_BLUE_PRINT);
    }

    @Override
    public void toModelList() {
        Intent intent = new Intent(mView.getActivity(), ModelActivity.class);
        if(!TextUtils.isEmpty(mCurrentModelName) && !TextUtils.isEmpty(mModelSelectInfo.fileName) && mModelSelectInfo.fileId !=null){
            mCurrentModelName = mView.getBluePrintName();
            if(mCurrentModelName.equals(mModelSelectInfo.fileName)){
                intent.putExtra(CommonConfig.MODULE_LIST_POSITION, mModelSelectInfo.fileId);
            }
        }
        mView.getActivity().startActivityForResult(intent, REQUEST_CODE_CHOOSE_MODEL);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_CHOOSE_MODULE:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    mModuleSelectInfo = (ModuleListBeanItem) data.getSerializableExtra(CommonConfig.MODULE_LIST_NAME);
                    mCurrentModuleName = mModuleSelectInfo.name;
                    mCurrentModuleId = mModuleSelectInfo.id;
                    if (mView != null && mModuleSelectInfo != null) {
                        mView.showModuleName(mModuleSelectInfo.name,mModuleSelectInfo.id.longValue());
                    }
                }
                break;
            case REQUEST_CODE_CHOOSE_BLUE_PRINT:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    mBluePrintSelectInfo = (BlueprintListBeanItem) data.getSerializableExtra(CommonConfig.MODULE_LIST_NAME);
                    mCurrentBluePrintName = mBluePrintSelectInfo.name;
                    mCurrentBluePrintId= mBluePrintSelectInfo.fileId;
                    if (mView != null && mBluePrintSelectInfo != null) {
                        mView.showBluePrintName(mBluePrintSelectInfo.name,mBluePrintSelectInfo.fileId);
                    }
                }
                break;
            case REQUEST_CODE_CHOOSE_MODEL:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    mModelSelectInfo = (ModelListBeanItem) data.getSerializableExtra(CommonConfig.MODULE_LIST_NAME);
                    mCurrentModelName = mModelSelectInfo.fileName;
                    mCurrentModelId= mModelSelectInfo.fileId;
                    if (mView != null && mModelSelectInfo != null) {
                        mView.showModelName(mModelSelectInfo.fileName,mModelSelectInfo.fileId);
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

    //发送刷新数据的广播
    private void sendBrushBroadcast() {
        Intent data = new Intent(CommonConfig.ACTION_BRUSH_CHECK_LIST);
        mView.getActivity().sendBroadcast(data);
    }

    @Override
    public boolean isDifferent(CreateCheckListParams currentParams) {
        assembleParams(currentParams);

        if (mInitParams != null && mInput != null) {
            if (!isEqual(mInitParams.code, mInput.code)) {
                return true;
            }
//            if (!isEqual(mInitParams.projectId, mInput.projectId)) {
//                return true;
//            }
//            if (!isEqual(mInitParams.projectName, mInput.projectName)) {
//                return true;
//            }
            if (!isEqual(mInitParams.qualityCheckpointName, mInput.qualityCheckpointName)) {
                return true;
            }
            if (!isEqual(mInitParams.inspectionCompanyId, mInput.inspectionCompanyId)) {
                return true;
            }
            if (!isEqual(mInitParams.constructionCompanyId, mInput.constructionCompanyId)) {
                return true;
            }
            if (!isEqual(mInitParams.needRectification, mInput.needRectification)) {
                return true;
            }
            if (mInitParams.needRectification && mInput.needRectification) {
                if (!isEqual(mInitParams.lastRectificationDate, mInput.lastRectificationDate)) {
                    return true;
                }
            }
            if (!isEqual(mInitParams.description, mInput.description)) {
                return true;
            }
//            if (!isEqual(mInitParams.inspectionType, mInput.inspectionType)) {
//                return true;
//            }
            if (!isEqual(mInitParams.responsibleUserId, mInput.responsibleUserId)) {
                return true;
            }

            if (!isEqual(mInitParams.files, mInput.files)) {
                return true;
            }

        }
        return false;
    }

    @Override
    public void setInspectionType(String typeInspection) {
        mInspectionType = typeInspection;
        mModel.setInspectionType(typeInspection);
    }

    @Override
    public void setInspectionCompanySelectedPosition(int position) {
        mInspectionCompanySelectPosition = position;
    }

    @Override
    public void showInspectionCompanyList() {
        if (mView != null) {
            mView.showInspectionCompanyList(mInspectionCompanyNameList, mInspectionCompanySelectPosition);
        }
    }


    @Override
    public void setCurrentModuleName(String name) {
        this.mCurrentModuleName = name;

    }

    @Override
    public void moduleNameChanged(String text) {
        if(!TextUtils.isEmpty(text) && text.equals(mCurrentModuleName) && mCurrentModuleId!=null && mCurrentModuleId.longValue()>0){
            mView.showModuleBenchMark(true,mCurrentModuleId.longValue());
        }else{
            mView.showModuleBenchMark(false,-1);
        }
    }



    //保存后将值付给初始值  以便下一次比较
    private void resetInitParams() {
//        mInitParams.code = mInput.code;
        mInitParams.projectId = mInput.projectId;
        mInitParams.projectName = mInput.projectName;
        mInitParams.qualityCheckpointId = mInput.qualityCheckpointId;
        mInitParams.qualityCheckpointName = mInput.qualityCheckpointName;
        mInitParams.constructionCompanyId = mInput.constructionCompanyId;
        mInitParams.inspectionCompanyId = mInput.inspectionCompanyId;
        mInitParams.needRectification = mInput.needRectification;
        mInitParams.lastRectificationDate = mInput.lastRectificationDate;
        mInitParams.description = mInput.description;
        mInitParams.inspectionType = mInput.inspectionType;
        mInitParams.responsibleUserId = mInput.responsibleUserId;
        mInitParams.files = mInput.files;
    }

    private boolean isEqual(List<CreateCheckListParamsFile> a, List<CreateCheckListParamsFile> inputList) {
        List<CreateCheckListParamsFile> b = new ArrayList<>();
        if (mSelectedMap != null && mSelectedMap.size() > 0) {
            for (Map.Entry<String, TNBImageItem> entry : mSelectedMap.entrySet()) {
                CreateCheckListParamsFile file = new CreateCheckListParamsFile();
                file.objectId = entry.getValue().objectId;
                b.add(file);
            }

        }
        if (a == null && b.size() == 0) {
            return true;
        }
        if (a != null && a.size() == 0 && b.size() == 0) {
            return true;
        }
        if ((a != null) && (b != null) && (a.size() == b.size()) && (a.size() > 0)) {
            for (int i = 0; i < a.size(); i++) {
                if (!isEqual(a.get(i).objectId, b.get(i).objectId)) {
                    return false;
                }
            }
        } else {
            return false;
        }
        return true;
    }

    private boolean isEqual(boolean a, boolean b) {
        return a == b;
    }

    private boolean isEqual(long a, long b) {
        return a == b;
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
}
