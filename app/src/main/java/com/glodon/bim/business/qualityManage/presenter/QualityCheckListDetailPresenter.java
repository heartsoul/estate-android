package com.glodon.bim.business.qualityManage.presenter;

import android.content.Intent;

import com.glodon.bim.business.qualityManage.bean.QualityCheckListDetailBean;
import com.glodon.bim.business.qualityManage.contract.QualityCheckListDetailContract;
import com.glodon.bim.business.qualityManage.model.QualityCheckListDetailModel;

import rx.subscriptions.CompositeSubscription;

/**
 * 描述：检查单详情
 * 作者：zhourf on 2017/11/3
 * 邮箱：zhourf@glodon.com
 */

public class QualityCheckListDetailPresenter implements QualityCheckListDetailContract.Presenter {
    private QualityCheckListDetailContract.View mView;
    private QualityCheckListDetailContract.Model mModel;
    private CompositeSubscription mSubscription;

    public QualityCheckListDetailPresenter(QualityCheckListDetailContract.View mView) {
        this.mView = mView;
        mModel = new QualityCheckListDetailModel();
        mSubscription = new CompositeSubscription();
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
