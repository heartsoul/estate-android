package com.glodon.bim.business.qualityManage.presenter;

import android.app.Activity;
import android.content.Intent;

import com.glodon.bim.basic.log.LogUtil;
import com.glodon.bim.basic.utils.CameraUtil;
import com.glodon.bim.basic.utils.DateUtil;
import com.glodon.bim.basic.utils.DateUtils;
import com.glodon.bim.basic.utils.SharedPreferencesUtil;
import com.glodon.bim.business.main.bean.ProjectListItem;
import com.glodon.bim.business.qualityManage.bean.QualityCheckListBean;
import com.glodon.bim.business.qualityManage.bean.QualityCheckListBeanItem;
import com.glodon.bim.business.qualityManage.contract.QualityCheckListContract;
import com.glodon.bim.business.qualityManage.contract.QualityMangeMainContract;
import com.glodon.bim.business.qualityManage.listener.OnOperateSheetListener;
import com.glodon.bim.business.qualityManage.model.QualityCheckListModel;
import com.glodon.bim.business.qualityManage.view.CreateReviewActivity;
import com.glodon.bim.business.qualityManage.view.PhotoEditActivity;
import com.glodon.bim.business.qualityManage.view.QualityCheckListDetailActivity;
import com.glodon.bim.common.config.CommonConfig;
import com.glodon.bim.customview.album.AlbumEditActivity;

import java.util.ArrayList;
import java.util.List;

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
    public static final int REQUEST_CODE_DETAIL = 0;
    private final int REQUEST_CODE_TAKE_PHOTO = 1;
    private final int REQUEST_CODE_OPEN_ALBUM = 2;
    private final int REQUEST_CODE_CREATE_REVIEW = 3;
    private final int REQUEST_CODE_CREATE_REPAIR = 4;
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

    private OnOperateSheetListener mListener = new OnOperateSheetListener() {


        @Override
        public void delete(int position) {

        }

        @Override
        public void detail(int position) {
            Intent intent = new Intent(mView.getActivity(), QualityCheckListDetailActivity.class);
            intent.putExtra(CommonConfig.QUALITY_CHECK_LIST_DEPTID, SharedPreferencesUtil.getProjectId());
            intent.putExtra(CommonConfig.QUALITY_CHECK_LIST_ID,mList.get(position).id);
            mView.getActivity().startActivityForResult(intent,REQUEST_CODE_DETAIL);
        }

        @Override
        public void submit(int position) {

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

    @Override
    public OnOperateSheetListener getListener(){
        return mListener;
    }



    public QualityCheckListPresenter(QualityCheckListContract.View mView) {
        this.mView = mView;
        mModel = new QualityCheckListModel();
        mSubscription = new CompositeSubscription();
        mDataList = new ArrayList<>();
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
        Subscription sub = mModel.getQualityCheckList(mProjectInfo.deptId, mQcState,mCurrentPage, mSize)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<QualityCheckListBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e("----",e.getMessage());
                    }

                    @Override
                    public void onNext(QualityCheckListBean bean) {
                        if(bean != null && bean.content!=null && bean.content.size()>0){
                            mDataList.addAll(bean.content);
                            if(mCurrentPage<bean.totalPages){
                                mCurrentPage++;
                            }
                        }
                        if(mView!=null) {
                            handleDate();
                            mView.updateData(mList);
                        }
                    }
                });
        mSubscription.add(sub);
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode)
        {
            case REQUEST_CODE_DETAIL://检查单详情

                break;
            case REQUEST_CODE_TAKE_PHOTO:
                if (resultCode == Activity.RESULT_OK) {
                    //正常返回
                    Intent intent = new Intent(mView.getActivity(), PhotoEditActivity.class);
                    intent.putExtra(CommonConfig.IMAGE_PATH,mPhotoPath);
                    intent.putExtra(CommonConfig.CREATE_TYPE,mCreateType);//表示创建检查单
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

                break;
            case REQUEST_CODE_CREATE_REPAIR:

                break;
            case REQUEST_CODE_CREATE_REVIEW:

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

    @Override
    public void initData(Intent intent) {

    }

}
