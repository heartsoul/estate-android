package com.glodon.bim.business.qualityManage.presenter;

import android.content.Intent;

import com.glodon.bim.basic.log.LogUtil;
import com.glodon.bim.basic.utils.NetWorkUtils;
import com.glodon.bim.business.qualityManage.bean.ModuleStandardItem;
import com.glodon.bim.business.qualityManage.contract.ModuleStandardContract;
import com.glodon.bim.business.qualityManage.model.ModuleStandardModel;
import com.glodon.bim.common.config.CommonConfig;
import com.glodon.bim.customview.ToastManager;
import com.google.gson.GsonBuilder;

import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * 描述：质检标准
 * 作者：zhourf on 2017/11/20
 * 邮箱：zhourf@glodon.com
 */

public class ModuleStandardPresenter implements ModuleStandardContract.Presenter {
    private ModuleStandardContract.View mView;
    private ModuleStandardContract.Model mModel;
    private CompositeSubscription mSubscription;


    public ModuleStandardPresenter(ModuleStandardContract.View mView) {
        this.mView = mView;
        mModel = new ModuleStandardModel();
        mSubscription = new CompositeSubscription();
    }

    @Override
    public void initData(Intent intent) {
        if(NetWorkUtils.isNetworkAvailable(mView.getActivity())) {
            mView.showLoadingDialog();
            long templateId = intent.getLongExtra(CommonConfig.MODULE_TEMPLATEID, -1);
            LogUtil.e("templateId="+templateId);
            Subscription sub = mModel.getModuleStandardList(templateId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<List<ModuleStandardItem>>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            LogUtil.e("standard error=" + e.getMessage());
                            if(mView!=null)
                            {
                                mView.dismissLoadingDialog();
                            }
                        }

                        @Override
                        public void onNext(List<ModuleStandardItem> list) {
                            LogUtil.e("standardList = "+new GsonBuilder().create().toJson(list));
                            if(list!=null && list.size()>0){
                                if(mView!=null)
                                {
                                    mView.updateListView(list);
                                }
                            }
                            if(mView!=null)
                            {
                                mView.dismissLoadingDialog();
                            }
                        }
                    });
            mSubscription.add(sub);
        }else{
            ToastManager.showNetWorkToast();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void onDestroy() {
        if(mSubscription!=null){
            mSubscription.unsubscribe();
            mSubscription = null;
        }
        mView = null;
        mModel = null;
    }
}
