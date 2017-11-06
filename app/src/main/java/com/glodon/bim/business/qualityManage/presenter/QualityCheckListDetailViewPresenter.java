package com.glodon.bim.business.qualityManage.presenter;

import android.content.Intent;

import com.glodon.bim.basic.log.LogUtil;
import com.glodon.bim.business.qualityManage.bean.QualityCheckListDetailBean;
import com.glodon.bim.business.qualityManage.contract.QualityCheckListDetailViewContract;
import com.glodon.bim.business.qualityManage.model.QualityCheckListDetailViewModel;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * 描述：检查单详情
 * 作者：zhourf on 2017/11/3
 * 邮箱：zhourf@glodon.com
 */

public class QualityCheckListDetailViewPresenter implements QualityCheckListDetailViewContract.Presenter {
    private QualityCheckListDetailViewContract.View mView;
    private QualityCheckListDetailViewContract.Model mModel;
    private CompositeSubscription mSubscription;

    public QualityCheckListDetailViewPresenter(QualityCheckListDetailViewContract.View mView) {
        this.mView = mView;
        mModel = new QualityCheckListDetailViewModel();
        mSubscription = new CompositeSubscription();
    }

    @Override
    public void getInspectInfo(long deptId, long id) {
        Subscription sub = mModel.getQualityCheckListDetail(deptId,id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<QualityCheckListDetailBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e("检查单详情 error =",e.getMessage());
                    }

                    @Override
                    public void onNext(QualityCheckListDetailBean bean) {
                        if(bean!=null){
                            mView.updateData(bean);
                        }
                    }
                })
                ;
        mSubscription.add( sub);


    }

    @Override
    public void initData(Intent intent) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void onDestroy() {
        if(mSubscription != null){
            mSubscription.unsubscribe();
            mSubscription = null;
        }
    }


}
