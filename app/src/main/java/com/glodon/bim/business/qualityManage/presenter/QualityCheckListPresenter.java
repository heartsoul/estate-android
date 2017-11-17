package com.glodon.bim.business.qualityManage.presenter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.glodon.bim.basic.log.LogUtil;
import com.glodon.bim.basic.utils.CameraUtil;
import com.glodon.bim.basic.utils.DateUtil;
import com.glodon.bim.basic.utils.DateUtils;
import com.glodon.bim.basic.utils.NetWorkUtils;
import com.glodon.bim.basic.utils.SharedPreferencesUtil;
import com.glodon.bim.business.authority.AuthorityManager;
import com.glodon.bim.business.main.bean.ProjectListItem;
import com.glodon.bim.business.qualityManage.bean.ClassifyNum;
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
import com.glodon.bim.business.qualityManage.view.CreateCheckListActivity;
import com.glodon.bim.business.qualityManage.view.CreateReviewActivity;
import com.glodon.bim.business.qualityManage.view.PhotoEditActivity;
import com.glodon.bim.business.qualityManage.view.QualityCheckListDetailActivity;
import com.glodon.bim.business.qualityManage.view.SaveDeleteDialog;
import com.glodon.bim.common.config.CommonConfig;
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
 * 描述：质量清单列表
 * 作者：zhourf on 2017/10/20
 * 邮箱：zhourf@glodon.com
 */

public class QualityCheckListPresenter implements QualityCheckListContract.Presenter {
    private final int REQUEST_CODE_CREATE_CHECK_LIST = 2;
    public  final int REQUEST_CODE_DETAIL = 10;
    private final int REQUEST_CODE_TAKE_PHOTO = 11;
    private final int REQUEST_CODE_OPEN_ALBUM = 12;
    private final int REQUEST_CODE_CREATE_REVIEW = 13;
    private final int REQUEST_CODE_CREATE_REPAIR = 14;
    private final int REQUEST_CODE_TO_EDIT = 15;
    private QualityCheckListContract.View mView;
    private QualityCheckListContract.Model mModel;
    private CompositeSubscription mSubscription;
    private ProjectListItem mProjectInfo;
    private List<QualityCheckListBeanItem> mDataList;
    private String mQcState = "";
    private int mCurrentPage = 0;
    private int mSize = 20;
    private List<String> mDateKeyList = new ArrayList<>();  //标记分隔时间
    private List<QualityCheckListBeanItem> mList = new ArrayList<>(); //添加了分隔时间的数据


    private String mPhotoPath;

    private String mCreateType = CommonConfig.CREATE_TYPE_REPAIR;

    private int mClickPosition = 0;

//    private BroadcastReceiver receiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    pullDown();
//                }
//            },1000);
//
//        }
//    };

    private OnOperateSheetListener mListener = new OnOperateSheetListener() {


        @Override
        public void delete(final int position) {
            new SaveDeleteDialog(mView.getActivity())
                    .getDeleteDialog(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(NetWorkUtils.isNetworkAvailable(mView.getActivity())) {
                                CreateCheckListModel model = new CreateCheckListModel();
                                model.setInspectionType(mList.get(position).inspectionType);
                                Subscription sub = model.createDelete(SharedPreferencesUtil.getProjectId(), mList.get(position).id)
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
                                                    pullDown();
                                                }

                                            }
                                        });
                                mSubscription.add(sub);
                            }else{
                                ToastManager.showNetWorkToast();
                            }
                        }
                    }).show();

        }

        @Override
        public void detail(int position) {
            //排除待提交状态和权限
            if(CommonConfig.QC_STATE_STAGED.equals(mList.get(position).qcState) &&(AuthorityManager.isQualityCheckSubmit() && AuthorityManager.isMe(mList.get(position).creatorId))){

                if(NetWorkUtils.isNetworkAvailable(mView.getActivity())) {
                    if (mView != null) {
                        mView.showLoadingDialog();
                    }
                    //待提交时 先获取详情  然后 进入编辑
                    Subscription sub = new QualityCheckListDetailViewModel().getQualityCheckListDetail(SharedPreferencesUtil.getProjectId(), mList.get(position).id)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Subscriber<QualityCheckListDetailBean>() {
                                @Override
                                public void onCompleted() {

                                }

                                @Override
                                public void onError(Throwable e) {
                                    LogUtil.e("检查单详情 error =", e.getMessage());
                                    if(mView!=null){
                                        mView.dismissLoadingDialog();
                                    }
                                }

                                @Override
                                public void onNext(QualityCheckListDetailBean bean) {
                                    if(mView!=null){
                                        mView.dismissLoadingDialog();
                                    }
                                    if (bean != null) {
                                        QualityCheckListDetailInspectionInfo info = bean.inspectionInfo;
                                        Intent intent = new Intent(mView.getActivity(), CreateCheckListActivity.class);
                                        intent.putExtra(CommonConfig.CREATE_CHECK_LIST_PROPS, getProps(info));
                                        mView.getActivity().startActivityForResult(intent, REQUEST_CODE_TO_EDIT);
                                    }

                                }
                            });
                    mSubscription.add(sub);
                }else{
                    ToastManager.showNetWorkToast();
                }
            }else {

                Intent intent = new Intent(mView.getActivity(), QualityCheckListDetailActivity.class);
                intent.putExtra(CommonConfig.QUALITY_CHECK_LIST_DEPTID, SharedPreferencesUtil.getProjectId());
                intent.putExtra(CommonConfig.QUALITY_CHECK_LIST_ID, mList.get(position).id);
                mView.getActivity().startActivityForResult(intent, REQUEST_CODE_DETAIL);
            }
        }

        @Override
        public void submit(final int position) {
            if(NetWorkUtils.isNetworkAvailable(mView.getActivity())) {
                if (mView != null) {
                    mView.showLoadingDialog();
                }
                Subscription sub = new QualityCheckListDetailViewModel().getQualityCheckListDetail(SharedPreferencesUtil.getProjectId(), mList.get(position).id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<QualityCheckListDetailBean>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                LogUtil.e("检查单详情 error =", e.getMessage());
                                if(mView!=null){
                                    mView.dismissLoadingDialog();
                                }
                            }

                            @Override
                            public void onNext(QualityCheckListDetailBean bean) {
                                if(mView!=null){
                                    mView.dismissLoadingDialog();
                                }
                                if (bean != null) {
                                    QualityCheckListDetailInspectionInfo info = bean.inspectionInfo;
                                    toSubmit(position, getProps(info));
                                }

                            }
                        });
                mSubscription.add(sub);
            }else{
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

    private CreateCheckListParams getProps(QualityCheckListDetailInspectionInfo info){
        CreateCheckListParams props = new CreateCheckListParams();
        props.inspectId = info.id;
        props.responsibleUserTitle = info.responsibleUserTitle;
        props.code = info.code;
        props.inspectionType = info.inspectionType;
        List<CreateCheckListParamsFile> fileList = new ArrayList<>();
        for(QualityCheckListBeanItemFile file:info.files){
            CreateCheckListParamsFile f = new CreateCheckListParamsFile();
//                                    public String extData;
//                                    public long id;
//                                    public String name;
//                                    public String objectId;
//                                    public String url;
            f.extData = file.extData;
            f.objectId = file.objectId;
            f.name = file.name;
            f.url = file.url;
            fileList.add(f);
        }
        if(fileList.size()>0){
            props.files = fileList;
        }
        props.buildingId = info.buildingId;
        props.buildingName = info.buildingName;
        props.constructionCompanyId = info.constructionCompanyId;
        props.constructionCompanyName = info.constructionCompanyName;
        props.inspectionCompanyId = info.inspectionCompanyId;
        props.inspectionCompanyName = info.inspectionCompanyName;
        props.description = info.description;
        props.elementId = info.elementId;
        props.elementName = info.elementName;
        props.lastRectificationDate = info.lastRectificationDate+"";
        props.needRectification = info.needRectification;
        props.projectId  = info.projectId;
        props.projectName = info.projectName;
        props.qualityCheckpointId = info.qualityCheckpointId;
        props.qualityCheckpointName = info.qualityCheckpointName;
        props.responsibleUserName = info.responsibleUserName;
        props.responsibleUserId = info.responsibleUserId;
        return  props;
    }

    //提交
    private void toSubmit(int position,CreateCheckListParams props){
        CreateCheckListModel model = new CreateCheckListModel();
        model.setInspectionType(mList.get(position).inspectionType);
        Subscription sub = model.editSubmit(SharedPreferencesUtil.getProjectId(),mList.get(position).id,props)
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
                        if(responseBody!=null){
                            pullDown();
                        }
                    }
                });
        mSubscription.add(sub);
    }

    @Override
    public OnOperateSheetListener getListener(){
        return mListener;
    }



    public QualityCheckListPresenter(QualityCheckListContract.View mView) {
        this.mView = mView;
        mModel = new QualityCheckListModel();
        mSubscription = new CompositeSubscription();
        mDataList = new ArrayList<>();

//        mView.getActivity().registerReceiver(receiver,new IntentFilter(CommonConfig.ACTION_BRUSH_CHECK_LIST));
    }

    @Override
    public void initData(ProjectListItem projectInfo) {
        mProjectInfo = projectInfo;
        getDataList();
    }

    @Override
    public void pullDown() {
        mCurrentPage = 0;
        mDataList.clear();
        getDataList();
    }

    /**
     * 初始化数据
     */
    private void getDataList() {
        if(NetWorkUtils.isNetworkAvailable(mView.getActivity())) {
            if (mView != null) {
                mView.showLoadingDialog();
            }
            if(mProjectInfo!=null) {
                Subscription sub = mModel.getQualityCheckList(mProjectInfo.deptId, mQcState, mCurrentPage, mSize)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<QualityCheckListBean>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                LogUtil.e("----", e.getMessage());
                                if (mView != null) {
                                    mView.dismissLoadingDialog();
                                }
                            }

                            @Override
                            public void onNext(QualityCheckListBean bean) {
                                if (bean != null && bean.content != null && bean.content.size() > 0) {
                                    mDataList.addAll(bean.content);
                                    if (mCurrentPage < bean.totalPages) {
                                        mCurrentPage++;
                                    }
                                }
                                if (mView != null) {
                                    handleDate();
                                    mView.updateData(mList);
                                }
                                if (mView != null) {
                                    mView.dismissLoadingDialog();
                                }

                                updateStatusNum();
                            }
                        });
                mSubscription.add(sub);
            }
        }else{
            ToastManager.showNetWorkToast();
        }
    }


    private void handleDate(){
        mDateKeyList.clear();
        mList.clear();
        if(mDataList!=null && mDataList.size()>0){
            for(QualityCheckListBeanItem item :mDataList){
                String now = DateUtil.getListDate(Long.parseLong(item.updateTime));
                if(mDateKeyList.contains(now)){
                    mList.add(item);
                }else{
                    QualityCheckListBeanItem bean = new QualityCheckListBeanItem();
                    bean.showType = 0;
                    if(DateUtils.isToday(item.updateTime)){
                        bean.timeType = 0;
                    }else {
                        bean.timeType =1;
                    }
                    bean.updateTime = item.updateTime;
                    mDateKeyList.add(now);
                    mList.add(bean);
                    mList.add(item);
                }
            }
        }
    }


    @Override
    public void pullUp() {
        getDataList();
    }

    @Override
    public void getClassifyData(String mCurrentState) {
        mCurrentPage = 0;
        mQcState = mCurrentState;
        mDataList.clear();
        getDataList();
    }

    @Override
    public void openPhoto() {
        mPhotoPath = CameraUtil.getFilePath();
        CameraUtil.openCamera(mPhotoPath, mView.getActivity(), REQUEST_CODE_TAKE_PHOTO);
    }

    @Override
    public void openAlbum() {
        Intent intent = new Intent(mView.getActivity(), AlbumEditActivity.class);
        intent.putExtra(CommonConfig.ALBUM_FROM_TYPE,0);
        intent.putExtra(CommonConfig.CREATE_TYPE,mCreateType);//表示创建什么单据
        intent.putExtra(CommonConfig.QUALITY_CHECK_LIST_ID,mList.get(mClickPosition).id);
        mView.getActivity().startActivityForResult(intent,REQUEST_CODE_OPEN_ALBUM);
    }

    @Override
    public void toCreate() {
        Intent intent = new Intent(mView.getActivity(), CreateReviewActivity.class);
        intent.putExtra(CommonConfig.CREATE_TYPE,mCreateType);
        intent.putExtra(CommonConfig.SHOW_PHOTO,false);
        intent.putExtra(CommonConfig.QUALITY_CHECK_LIST_DEPTID,SharedPreferencesUtil.getProjectId());
        intent.putExtra(CommonConfig.QUALITY_CHECK_LIST_ID,mList.get(mClickPosition).id);
        int requestCode = 0;
        if(mCreateType.equals(CommonConfig.CREATE_TYPE_REPAIR)){
            requestCode = REQUEST_CODE_CREATE_REPAIR;
        }else if(mCreateType.equals(CommonConfig.CREATE_TYPE_REVIEW)){
            requestCode = REQUEST_CODE_CREATE_REVIEW;
        }
        mView.getActivity().startActivityForResult(intent,requestCode);
    }

    private void updateStatusNum(){
        Subscription sub = mModel.getStatusNum(mProjectInfo.deptId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<ClassifyNum>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(e.getMessage());
                    }

                    @Override
                    public void onNext(List<ClassifyNum> classifyNa) {
                        if(classifyNa!=null && classifyNa.size()>0){
                            if(mView!=null)
                            {
                                mView.updateClassifyCount(classifyNa);
                            }
                        }
                    }
                });
        mSubscription.add(sub);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode)
        {
            case REQUEST_CODE_CREATE_CHECK_LIST:
                if(resultCode==Activity.RESULT_OK) {
                    pullDown();
                }
                break;
            case REQUEST_CODE_DETAIL://检查单详情
                if(resultCode==Activity.RESULT_OK) {
                    pullDown();
                }
                break;
            case REQUEST_CODE_TAKE_PHOTO:
                if (resultCode == Activity.RESULT_OK) {
                    //正常返回
                    Intent intent = new Intent(mView.getActivity(), PhotoEditActivity.class);
                    intent.putExtra(CommonConfig.IMAGE_PATH,mPhotoPath);
                    intent.putExtra(CommonConfig.CREATE_TYPE,mCreateType);//表示创建检查单
                    intent.putExtra(CommonConfig.QUALITY_CHECK_LIST_ID,mList.get(mClickPosition).id);
                    int code = 0;
                    if(mCreateType.equals(CommonConfig.CREATE_TYPE_REPAIR)){
                        code = REQUEST_CODE_CREATE_REPAIR;
                    }else if(mCreateType.equals(CommonConfig.CREATE_TYPE_REVIEW)){
                        code = REQUEST_CODE_CREATE_REVIEW;
                    }
                    mView.getActivity().startActivityForResult(intent,code);

                }
                break;
            case REQUEST_CODE_OPEN_ALBUM:
                pullDown();
                break;
            case REQUEST_CODE_CREATE_REPAIR:
                if(resultCode==Activity.RESULT_OK) {
                    pullDown();
                }
                break;
            case REQUEST_CODE_CREATE_REVIEW:
                if(resultCode==Activity.RESULT_OK) {
                    pullDown();
                }
                break;
            case REQUEST_CODE_TO_EDIT:
                pullDown();
                break;
        }
    }

    @Override
    public void onDestroy() {
        if (mSubscription != null) {
            mSubscription.unsubscribe();
            mSubscription = null;
        }
//        if(mView!=null)
//        {
//            mView.getActivity().unregisterReceiver(receiver);
//        }
    }

    @Override
    public void initData(Intent intent) {

    }

}
