package com.glodon.bim.business.main.presenter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.glodon.bim.basic.log.LogUtil;
import com.glodon.bim.basic.utils.CameraUtil;
import com.glodon.bim.basic.utils.NetWorkUtils;
import com.glodon.bim.basic.utils.SharedPreferencesUtil;
import com.glodon.bim.business.authority.AuthorityManager;
import com.glodon.bim.business.equipment.bean.CreateEquipmentParams;
import com.glodon.bim.business.equipment.bean.EquipmentListBean;
import com.glodon.bim.business.equipment.bean.EquipmentListBeanItem;
import com.glodon.bim.business.equipment.contract.EquipmentListContract;
import com.glodon.bim.business.equipment.listener.OnOperateEquipmentSheetListener;
import com.glodon.bim.business.equipment.model.CreateEquipmentModel;
import com.glodon.bim.business.equipment.model.EquipmentListModel;
import com.glodon.bim.business.equipment.view.CreateEquipmentActivity;
import com.glodon.bim.business.equipment.view.EquipmentSearchActivity;
import com.glodon.bim.business.main.contract.QualityEquipmentSearchContract;
import com.glodon.bim.business.main.model.QualityEquipmentSearchModel;
import com.glodon.bim.business.qualityManage.view.QualityCheckListSearchActivity;
import com.glodon.bim.business.qualityManage.bean.CreateCheckListParams;
import com.glodon.bim.business.qualityManage.bean.CreateCheckListParamsFile;
import com.glodon.bim.business.qualityManage.bean.QualityCheckListBean;
import com.glodon.bim.business.qualityManage.bean.QualityCheckListBeanItem;
import com.glodon.bim.business.qualityManage.bean.QualityCheckListBeanItemFile;
import com.glodon.bim.business.qualityManage.bean.QualityCheckListDetailBean;
import com.glodon.bim.business.qualityManage.bean.QualityCheckListDetailInspectionInfo;
import com.glodon.bim.business.qualityManage.contract.QualityCheckListContract;
import com.glodon.bim.business.qualityManage.listener.OnOperateSheetListener;
import com.glodon.bim.business.qualityManage.model.CreateCheckListModel;
import com.glodon.bim.business.qualityManage.model.QualityCheckListDetailViewModel;
import com.glodon.bim.business.qualityManage.model.QualityCheckListModel;
import com.glodon.bim.business.qualityManage.util.QualityMangeUtil;
import com.glodon.bim.business.qualityManage.view.CreateCheckListActivity;
import com.glodon.bim.business.qualityManage.view.CreateReviewActivity;
import com.glodon.bim.business.qualityManage.view.PhotoEditActivity;
import com.glodon.bim.business.qualityManage.view.QualityCheckListDetailActivity;
import com.glodon.bim.business.qualityManage.view.SaveDeleteDialog;
import com.glodon.bim.common.config.CommonConfig;
import com.glodon.bim.common.config.RequestCodeConfig;
import com.glodon.bim.customview.ToastManager;
import com.glodon.bim.customview.album.AlbumEditActivity;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by cwj on 2018/3/6.
 * Description:QualityEquipmentSearchPresenter
 */

public class QualityEquipmentSearchPresenter implements QualityEquipmentSearchContract.Presenter {

    private QualityEquipmentSearchContract.View mView;
    private QualityEquipmentSearchContract.Model mModel;
    private CompositeSubscription mSubscription;

    private List<QualityCheckListBeanItem> mQualityList = new ArrayList<>(); //质检清单数据
    private List<EquipmentListBeanItem> mEquipmentList = new ArrayList<>(); //材设清单数据

    private String mCreateType = CommonConfig.CREATE_TYPE_REPAIR;
    private int mClickPosition = 0;
    private String mPhotoPath;

    /**
     * 每次搜索设置为0，质检清单、材设清单搜索线程执行完后加1，保证都搜索完后更新页面
     */
    private int searchCount = 0;
    private String searchKey = "";//搜索的内容

    public QualityEquipmentSearchPresenter(QualityEquipmentSearchContract.View mView) {
        this.mView = mView;
        mModel = new QualityEquipmentSearchModel();
        mSubscription = new CompositeSubscription();
    }

    /**
     * 质检清单操作回调
     */
    private OnOperateSheetListener onOperateSheetListener = new OnOperateSheetListener() {
        @Override
        public void delete(final int position) {
            new SaveDeleteDialog(mView.getActivity())
                    .getDeleteDialog(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (NetWorkUtils.isNetworkAvailable(mView.getActivity())) {
                                CreateCheckListModel model = new CreateCheckListModel();
                                model.setInspectionType(mQualityList.get(position).inspectionType);
                                Subscription sub = model.createDelete(SharedPreferencesUtil.getProjectId(), mQualityList.get(position).id)
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new Subscriber<ResponseBody>() {
                                            @Override
                                            public void onCompleted() {

                                            }

                                            @Override
                                            public void onError(Throwable e) {
                                                LogUtil.e(e.getMessage());

                                            }

                                            @Override
                                            public void onNext(ResponseBody responseBody) {
                                                if (responseBody != null) {
                                                    mQualityList.remove(position);
                                                    mView.showResult(mQualityList, mEquipmentList);
                                                }
                                            }
                                        });
                                mSubscription.add(sub);
                            } else {
                                ToastManager.showNetWorkToast();
                            }
                        }
                    }).show();
        }

        @Override
        public void detail(int position) {
            //排除待提交状态和权限
            if (CommonConfig.QC_STATE_STAGED.equals(mQualityList.get(position).qcState) && (AuthorityManager.isQualityCheckSubmit() && AuthorityManager.isMe(mQualityList.get(position).creatorId))) {

                if (NetWorkUtils.isNetworkAvailable(mView.getActivity())) {
                    if (mView != null) {
                        mView.showLoadingDialog();
                    }
                    //待提交时 先获取详情  然后 进入编辑
                    Subscription sub = new QualityCheckListDetailViewModel().getQualityCheckListDetail(SharedPreferencesUtil.getProjectId(), mQualityList.get(position).id)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Subscriber<QualityCheckListDetailBean>() {
                                @Override
                                public void onCompleted() {

                                }

                                @Override
                                public void onError(Throwable e) {
                                    LogUtil.e("检查单详情 error =", e.getMessage());
                                    if (mView != null) {
                                        mView.dismissLoadingDialog();
                                    }
                                }

                                @Override
                                public void onNext(QualityCheckListDetailBean bean) {
                                    if (mView != null) {
                                        mView.dismissLoadingDialog();
                                    }
                                    if (bean != null) {
                                        QualityCheckListDetailInspectionInfo info = bean.inspectionInfo;
                                        Intent intent = new Intent(mView.getActivity(), CreateCheckListActivity.class);
                                        intent.putExtra(CommonConfig.CREATE_CHECK_LIST_PROPS, QualityMangeUtil.getProps(info));
                                        mView.getActivity().startActivityForResult(intent, RequestCodeConfig.REQUEST_CODE_TO_EDIT);
                                    }

                                }
                            });
                    mSubscription.add(sub);
                } else {
                    ToastManager.showNetWorkToast();
                }
            } else {

                Intent intent = new Intent(mView.getActivity(), QualityCheckListDetailActivity.class);
                intent.putExtra(CommonConfig.QUALITY_CHECK_LIST_DEPTID, SharedPreferencesUtil.getProjectId());
                intent.putExtra(CommonConfig.QUALITY_CHECK_LIST_ID, mQualityList.get(position).id);
                mView.getActivity().startActivityForResult(intent, RequestCodeConfig.REQUEST_CODE_DETAIL);
            }
        }

        @Override
        public void submit(final int position) {
            if (NetWorkUtils.isNetworkAvailable(mView.getActivity())) {
                if (mView != null) {
                    mView.showLoadingDialog();
                }
                Subscription sub = new QualityCheckListDetailViewModel().getQualityCheckListDetail(SharedPreferencesUtil.getProjectId(), mQualityList.get(position).id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<QualityCheckListDetailBean>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                LogUtil.e("检查单详情 error =", e.getMessage());
                                if (mView != null) {
                                    mView.dismissLoadingDialog();
                                }
                            }

                            @Override
                            public void onNext(QualityCheckListDetailBean bean) {
                                if (mView != null) {
                                    mView.dismissLoadingDialog();
                                }
                                if (bean != null) {
                                    QualityCheckListDetailInspectionInfo info = bean.inspectionInfo;
                                    toSubmit(position, QualityMangeUtil.getProps(info));
                                }

                            }
                        });
                mSubscription.add(sub);
            } else {
                ToastManager.showNetWorkToast();
            }
        }

        @Override
        public void repair(int position) {
            mCreateType = CommonConfig.CREATE_TYPE_REPAIR;
            mClickPosition = position;
            mView.create();
        }

        @Override
        public void review(int position) {
            mCreateType = CommonConfig.CREATE_TYPE_REVIEW;
            mClickPosition = position;
            mView.create();
        }
    };

    /**
     * 材设清单操作回调
     */
    private OnOperateEquipmentSheetListener operateEquipmentSheetListener = new OnOperateEquipmentSheetListener() {
        @Override
        public void delete(EquipmentListBeanItem item, final int position) {
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
                                if (responseBody != null) {
                                    mEquipmentList.remove(position);
                                    mView.showResult(mQualityList, mEquipmentList);
                                }
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
                                    loadSearchData();
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

    @Override
    public OnOperateSheetListener getOnOperateSheetListener() {
        return onOperateSheetListener;
    }

    @Override
    public OnOperateEquipmentSheetListener getOperateEquipmentSheetListener() {
        return operateEquipmentSheetListener;
    }

    @Override
    public void initData(Intent intent) {
    }

    @Override
    public void search(String key) {
        searchKey = key;
        loadSearchData();
    }

    /**
     * 搜索
     */
    private void loadSearchData() {
        searchCount = 0;
        if (NetWorkUtils.isNetworkAvailable(mView.getActivity())) {
            if (mView != null) {
                mView.showLoadingDialog();
            }
            searchQualityData();
            searchEquipmentData();
        } else {
            ToastManager.showNetWorkToast();
        }
    }

    /**
     * 搜索质检清单
     */
    private void searchQualityData() {
        QualityEquipmentSearchContract.Model mModel = new QualityEquipmentSearchModel();

        Subscription sub = mModel.searchQualityData(SharedPreferencesUtil.getProjectId(), searchKey, 0, 3)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<QualityCheckListBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e("----", e.getMessage());
                        searchCount++;
                        if (mView != null && searchCount == 2) {
                            mView.dismissLoadingDialog();
                            mView.hideHistory();
                            mView.showResult(mQualityList, mEquipmentList);
                        }
                    }

                    @Override
                    public void onNext(QualityCheckListBean bean) {
                        mQualityList.clear();
                        mQualityList.addAll(bean.content);
                        searchCount++;
                        if (mView != null && searchCount == 2) {
                            mView.dismissLoadingDialog();
                            mView.hideHistory();
                            mView.showResult(mQualityList, mEquipmentList);
                        }

                    }
                });
        mSubscription.add(sub);
    }

    /**
     * 搜索材设清单
     */
    private void searchEquipmentData() {
        QualityEquipmentSearchContract.Model mModel = new QualityEquipmentSearchModel();
        Subscription sub = mModel.searchEquipmentData(SharedPreferencesUtil.getProjectId(), searchKey, 0, 3)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<EquipmentListBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e("----", e.getMessage());
                        searchCount++;
                        if (mView != null && searchCount == 2) {
                            mView.dismissLoadingDialog();
                            mView.hideHistory();
                            mView.showResult(mQualityList, mEquipmentList);
                        }
                    }

                    @Override
                    public void onNext(EquipmentListBean bean) {
                        mEquipmentList.clear();
                        mEquipmentList.addAll(bean.content);
                        for (EquipmentListBeanItem equipmentListBeanItem : mEquipmentList) {
                            if (CommonConfig.QC_STATE_EDIT.equals(equipmentListBeanItem.getQcState())) {
                                equipmentListBeanItem.showType = 1;
                            } else {
                                equipmentListBeanItem.showType = 2;
                            }
                        }
                        searchCount++;

                        if (mView != null && searchCount == 2) {
                            mView.dismissLoadingDialog();
                            mView.hideHistory();
                            mView.showResult(mQualityList, mEquipmentList);
                        }
                    }
                });
        mSubscription.add(sub);
    }

    /**
     * 创建质检清单
     */
    @Override
    public void toCreate() {
        Intent intent = new Intent(mView.getActivity(), CreateReviewActivity.class);
        intent.putExtra(CommonConfig.CREATE_TYPE, mCreateType);
        intent.putExtra(CommonConfig.SHOW_PHOTO, false);
        intent.putExtra(CommonConfig.QUALITY_CHECK_LIST_DEPTID, SharedPreferencesUtil.getProjectId());
        intent.putExtra(CommonConfig.QUALITY_CHECK_LIST_ID, mQualityList.get(mClickPosition).id);
        int requestCode = 0;
        if (mCreateType.equals(CommonConfig.CREATE_TYPE_REPAIR)) {
            requestCode = RequestCodeConfig.REQUEST_CODE_CREATE_REPAIR;
        } else if (mCreateType.equals(CommonConfig.CREATE_TYPE_REVIEW)) {
            requestCode = RequestCodeConfig.REQUEST_CODE_CREATE_REVIEW;
        }
        mView.getActivity().startActivityForResult(intent, requestCode);
    }

    @Override
    public void searchMoreQualityCheckList() {
        Intent intent = new Intent(mView.getActivity(), QualityCheckListSearchActivity.class);
        intent.putExtra(CommonConfig.SEARCH_KEY, searchKey);
        mView.getActivity().startActivity(intent);
    }

    @Override
    public void searchMoreEquipmentList() {
        Intent intent = new Intent(mView.getActivity(), EquipmentSearchActivity.class);
        intent.putExtra(CommonConfig.SEARCH_KEY, searchKey);
        mView.getActivity().startActivity(intent);
    }

    //提交质检清单
    private void toSubmit(int position, CreateCheckListParams props) {
        CreateCheckListModel model = new CreateCheckListModel();
        model.setInspectionType(mQualityList.get(position).inspectionType);
        Subscription sub = model.editSubmit(SharedPreferencesUtil.getProjectId(), mQualityList.get(position).id, props)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(e.getMessage());
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        if (responseBody != null) {
                            loadSearchData();
                        }
                    }
                });
        mSubscription.add(sub);
    }

    @Override
    public void openPhoto() {
        mPhotoPath = CameraUtil.getFilePath();
        CameraUtil.openCamera(mPhotoPath, mView.getActivity(), RequestCodeConfig.REQUEST_CODE_TAKE_PHOTO);

    }

    @Override
    public void openAlbum() {
        Intent intent = new Intent(mView.getActivity(), AlbumEditActivity.class);
        intent.putExtra(CommonConfig.ALBUM_FROM_TYPE, 0);
        intent.putExtra(CommonConfig.CREATE_TYPE, mCreateType);//表示创建什么单据
        intent.putExtra(CommonConfig.QUALITY_CHECK_LIST_ID, mQualityList.get(mClickPosition).id);
        mView.getActivity().startActivityForResult(intent, RequestCodeConfig.REQUEST_CODE_OPEN_ALBUM);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RequestCodeConfig.REQUEST_CODE_CREATE_CHECK_LIST:
                if (resultCode == Activity.RESULT_OK) {

                }
                break;
            case RequestCodeConfig.REQUEST_CODE_DETAIL://检查单详情
                if (resultCode == Activity.RESULT_OK) {
                    loadSearchData();
                }
                break;
            case RequestCodeConfig.REQUEST_CODE_TAKE_PHOTO:
                if (resultCode == Activity.RESULT_OK) {
                    //正常返回
                    Intent intent = new Intent(mView.getActivity(), PhotoEditActivity.class);
                    intent.putExtra(CommonConfig.IMAGE_PATH, mPhotoPath);
                    intent.putExtra(CommonConfig.CREATE_TYPE, mCreateType);//表示创建检查单
                    intent.putExtra(CommonConfig.QUALITY_CHECK_LIST_ID, mQualityList.get(mClickPosition).id);
                    int code = 0;
                    if (mCreateType.equals(CommonConfig.CREATE_TYPE_REPAIR)) {
                        code = RequestCodeConfig.REQUEST_CODE_CREATE_REPAIR;
                    } else if (mCreateType.equals(CommonConfig.CREATE_TYPE_REVIEW)) {
                        code = RequestCodeConfig.REQUEST_CODE_CREATE_REVIEW;
                    }
                    mView.getActivity().startActivityForResult(intent, code);

                }
                break;
            case RequestCodeConfig.REQUEST_CODE_OPEN_ALBUM:
                loadSearchData();
                break;
            case RequestCodeConfig.REQUEST_CODE_CREATE_REPAIR:
                if (resultCode == Activity.RESULT_OK) {
                    loadSearchData();
                }
                break;
            case RequestCodeConfig.REQUEST_CODE_CREATE_REVIEW:
                if (resultCode == Activity.RESULT_OK) {
                    loadSearchData();
                }
                break;
            case RequestCodeConfig.REQUEST_CODE_TO_EDIT:
                loadSearchData();
                break;

            case RequestCodeConfig.REQUEST_CODE_EQUIPMENT_TO_EDIT:
                if (resultCode == Activity.RESULT_OK) {
                    loadSearchData();
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
