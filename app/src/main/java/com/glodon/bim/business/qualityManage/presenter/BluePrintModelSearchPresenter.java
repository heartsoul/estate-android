package com.glodon.bim.business.qualityManage.presenter;

import android.content.Intent;

import com.glodon.bim.basic.config.AppConfig;
import com.glodon.bim.basic.log.LogUtil;
import com.glodon.bim.basic.network.NetRequest;
import com.glodon.bim.basic.utils.NetWorkUtils;
import com.glodon.bim.basic.utils.SharedPreferencesUtil;
import com.glodon.bim.business.greendao.provider.DaoProvider;
import com.glodon.bim.business.qualityManage.bean.BluePrintModelSearchBean;
import com.glodon.bim.business.qualityManage.bean.BluePrintModelSearchBeanItem;
import com.glodon.bim.business.qualityManage.contract.BluePrintModelSearchContract;
import com.glodon.bim.business.qualityManage.model.BluePrintModelSearchApi;
import com.glodon.bim.business.qualityManage.model.BluePrintModelSearchModel;
import com.glodon.bim.common.config.CommonConfig;
import com.glodon.bim.customview.ToastManager;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * 描述：模型图纸搜索
 * 作者：zhourf on 2017/12/21
 * 邮箱：zhourf@glodon.com
 */

public class BluePrintModelSearchPresenter implements BluePrintModelSearchContract.Presenter{
    private BluePrintModelSearchContract.View mView;
    private BluePrintModelSearchContract.Model mModel;
    private CompositeSubscription mSubscription;
    private int mSearchType = 0;//0图纸  1模型
    private List<BluePrintModelSearchBeanItem> mDataList;

    public BluePrintModelSearchPresenter(BluePrintModelSearchContract.View mView) {
        this.mView = mView;
        mModel = new BluePrintModelSearchModel();
        mSubscription = new CompositeSubscription();
        mDataList = new ArrayList<>();
    }

    @Override
    public void initData(Intent intent) {
        mSearchType = intent.getIntExtra(CommonConfig.SEARCH_TYPE,0);
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
    }

    @Override
    public void pullUp() {

    }

    @Override
    public void search(String key) {
        if(NetWorkUtils.isNetworkAvailable(mView.getActivity())) {
            if(mView!=null){
                mView.showLoadingDialog();
            }
            String suffix = mSearchType == 0 ? "dwg" : "rvt";
            long projectId = SharedPreferencesUtil.getProjectId();
            String projectVersionId = SharedPreferencesUtil.getProjectVersionId(projectId);
            LogUtil.e("search,suffix=" + suffix + " key=" + key + " type=" + mSearchType);
            LogUtil.e("search,projectId=" + projectId + " projectVersionId=" + projectVersionId);
            Subscription sub = mModel.search(key, suffix)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<BluePrintModelSearchBean>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            LogUtil.e(e.getMessage());
                            if(mView!=null){
                                mView.dismissLoadingDialog();
                                mView.hideHistory();
                            }
                        }

                        @Override
                        public void onNext(BluePrintModelSearchBean bean) {
                            LogUtil.e("result="+new GsonBuilder().create().toJson(bean));
                            if(mView!=null){
                                mView.dismissLoadingDialog();
                                mView.hideHistory();
                            }
                            if(bean!=null){
                                mDataList = bean.data;
                                if(mDataList==null){
                                    mDataList = new ArrayList<>();
                                }
                                mView.showResult(mDataList);
                            }
                        }
                    });
            mSubscription.add(sub);
        }else{
            ToastManager.showNetWorkToast();
        }
    }
}
