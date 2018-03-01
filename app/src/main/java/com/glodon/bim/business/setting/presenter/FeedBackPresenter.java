package com.glodon.bim.business.setting.presenter;

import android.content.Intent;

import com.glodon.bim.basic.log.LogUtil;
import com.glodon.bim.basic.utils.NetWorkUtils;
import com.glodon.bim.business.setting.bean.FeedBackBean;
import com.glodon.bim.business.setting.bean.FeedBackParams;
import com.glodon.bim.business.setting.contract.FeedBackContract;
import com.glodon.bim.business.setting.model.FeedBackModel;
import com.glodon.bim.customview.ToastManager;
import com.google.gson.GsonBuilder;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * 描述：意见反馈
 * 作者：zhourf on 2017/12/6
 * 邮箱：zhourf@glodon.com
 */

public class FeedBackPresenter implements FeedBackContract.Presenter {
    private FeedBackContract.View mView;
    private FeedBackContract.Model mModel;
    private CompositeSubscription mSubscription;

    public FeedBackPresenter(FeedBackContract.View mView) {
        this.mView = mView;
        mModel = new FeedBackModel();
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
        if (mSubscription != null) {
            mSubscription.unsubscribe();
            mSubscription = null;
        }
        mModel = null;
        mView = null;
    }

    @Override
    public void addFeedBack(FeedBackParams mParams) {
        if (NetWorkUtils.isNetworkAvailable(mView.getActivity())) {
            if (mView != null) {
                mView.showLoadingDialog();
            }
            Subscription sub = mModel.addFeedBack(mParams)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<FeedBackBean>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            LogUtil.e(e.getMessage());
                            if (mView != null) {
                                mView.dismissLoadingDialog();
                            }
//                            ToastManager.show("意见提交失败，请稍后重试！");
                        }

                        @Override
                        public void onNext(FeedBackBean feedBackBean) {
                            if (mView != null) {
                                mView.dismissLoadingDialog();
                            }
                            if (feedBackBean != null) {
//                                LogUtil.e(new GsonBuilder().create().toJson(feedBackBean));
                                ToastManager.show("意见已提交！");

                                if (mView != null) {
                                    mView.getActivity().finish();
                                }
                            }
                        }
                    });
            mSubscription.add(sub);
        } else {
            ToastManager.showNetWorkToast();
        }

    }
}
