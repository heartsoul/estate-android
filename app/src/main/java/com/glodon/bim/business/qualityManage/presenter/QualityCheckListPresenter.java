package com.glodon.bim.business.qualityManage.presenter;

import android.content.Intent;

import com.glodon.bim.basic.log.LogUtil;
import com.glodon.bim.basic.utils.SharedPreferencesUtil;
import com.glodon.bim.business.main.bean.ProjectListItem;
import com.glodon.bim.business.qualityManage.bean.QualityCheckListBean;
import com.glodon.bim.business.qualityManage.bean.QualityCheckListBeanItem;
import com.glodon.bim.business.qualityManage.contract.QualityCheckListContract;
import com.glodon.bim.business.qualityManage.listener.OnOperateSheetListener;
import com.glodon.bim.business.qualityManage.model.QualityCheckListModel;
import com.glodon.bim.business.qualityManage.view.QualityCheckListDetailActivity;
import com.glodon.bim.common.config.CommonConfig;

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

    private QualityCheckListContract.View mView;
    private QualityCheckListContract.Model mModel;
    private CompositeSubscription mSubscription;
    private ProjectListItem mProjectInfo;
    private List<QualityCheckListBeanItem> mDataList;
    private String mQcState = "";
    private int mCurrentPage = 0;
    private int mSize = 20;
    private OnOperateSheetListener mListener = new OnOperateSheetListener() {


        @Override
        public void delete(int position) {

        }

        @Override
        public void detail(int position) {
            Intent intent = new Intent(mView.getActivity(), QualityCheckListDetailActivity.class);
            intent.putExtra(CommonConfig.QUALITY_CHECK_LIST_DEPTID, SharedPreferencesUtil.getProjectId());
            intent.putExtra(CommonConfig.QUALITY_CHECK_LIST_ID,mDataList.get(position).id);
            mView.getActivity().startActivityForResult(intent,REQUEST_CODE_DETAIL);
        }

        @Override
        public void submit(int position) {

        }

        @Override
        public void repair(int position) {

        }

        @Override
        public void review(int position) {

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
                            mView.updateData(mDataList);
                        }
                    }
                });
        mSubscription.add(sub);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode)
        {
            case REQUEST_CODE_DETAIL://检查单详情

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
