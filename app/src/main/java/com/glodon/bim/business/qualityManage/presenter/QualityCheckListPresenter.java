package com.glodon.bim.business.qualityManage.presenter;

import android.content.Intent;

import com.glodon.bim.basic.log.LogUtil;
import com.glodon.bim.business.main.bean.ProjectListItem;
import com.glodon.bim.business.qualityManage.bean.QualityCheckListBean;
import com.glodon.bim.business.qualityManage.bean.QualityCheckListBeanItem;
import com.glodon.bim.business.qualityManage.contract.QualityCheckListContract;
import com.glodon.bim.business.qualityManage.listener.OnOperateSheetListener;
import com.glodon.bim.business.qualityManage.model.QualityCheckListModel;

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
    private QualityCheckListContract.View mView;
    private QualityCheckListContract.Model mModel;
    private CompositeSubscription mSubscription;
    private ProjectListItem mProjectInfo;
    private List<QualityCheckListBeanItem> mDataList;
    private OnOperateSheetListener mListener = new OnOperateSheetListener() {
        @Override
        public void delete(int position) {

        }

        @Override
        public void detail(int position) {

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

    private void getDataList() {
        Subscription sub = mModel.getQualityCheckList(mProjectInfo.deptId, 0, 30).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
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
                            if(mView!=null) {
                                mView.updateData(mDataList);
                            }
                        }
                    }
                });
        mSubscription.add(sub);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

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
