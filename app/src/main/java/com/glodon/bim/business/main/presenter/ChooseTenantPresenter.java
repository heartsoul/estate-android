package com.glodon.bim.business.main.presenter;

import android.app.Activity;
import android.content.Intent;

import com.glodon.bim.basic.log.LogUtil;
import com.glodon.bim.basic.utils.NetWorkUtils;
import com.glodon.bim.basic.utils.SharedPreferencesUtil;
import com.glodon.bim.business.main.contract.ChooseTenantContract;
import com.glodon.bim.business.main.model.ChooseTenantModel;
import com.glodon.bim.business.main.view.ChooseProjectActivity;
import com.glodon.bim.common.config.CommonConfig;
import com.glodon.bim.common.config.RequestCodeConfig;
import com.glodon.bim.common.login.User;
import com.glodon.bim.common.login.UserTenant;
import com.glodon.bim.customview.ToastManager;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * 描述：选择租户列表
 * 作者：zhourf on 2017/10/17
 * 邮箱：zhourf@glodon.com
 */

public class ChooseTenantPresenter implements ChooseTenantContract.Presenter {

    private ChooseTenantContract.View mView;
    private ChooseTenantContract.Model mModel;
    private List<UserTenant> mDataList;
    private CompositeSubscription mSubscriptions;
    private boolean mIsChangeProject = false;//是否是切换项目

    public ChooseTenantPresenter(ChooseTenantContract.View mView) {
        this.mView = mView;
        mModel = new ChooseTenantModel();
        mDataList = new ArrayList<>();
        mSubscriptions = new CompositeSubscription();
    }

    @Override
    public void initData(Intent intent) {
        User user = SharedPreferencesUtil.getUserInfo();
        mIsChangeProject = intent.getBooleanExtra(CommonConfig.CHANGE_PROJECT,false);
        if(user!=null && user.accountInfo!=null && user.accountInfo.userTenants!=null){
            mDataList.addAll(user.accountInfo.userTenants);
        }
        mView.updateData(mDataList);
        //如果是一个公司 直接进入
        if(mDataList.size()==1){
            clickTenant(mDataList.get(0));
        }
    }


    @Override
    public void clickTenant(UserTenant tenant) {
        LogUtil.toJson(tenant);
        SharedPreferencesUtil.setTenantInfo(tenant);
        if(NetWorkUtils.isNetworkAvailable(mView.getActivity())) {
            //保存当前的租户下的用户id
            SharedPreferencesUtil.setUserId(tenant.id);
            Subscription sub = mModel.setCurrentTenant(tenant.tenantId)
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
                        public void onNext(ResponseBody response) {
                            Intent intent = new Intent(mView.getActivity(), ChooseProjectActivity.class);
                            intent.putExtra(CommonConfig.CHANGE_PROJECT,mIsChangeProject);
                            mView.getActivity().startActivityForResult(intent, RequestCodeConfig.REQUEST_CODE_CLOSE_TENANT);
                        }
                    });
            mSubscriptions.add(sub);
        }else{
            ToastManager.showNetWorkToast();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode)
        {
            case RequestCodeConfig.REQUEST_CODE_CLOSE_TENANT:
                if(mView!=null && resultCode == Activity.RESULT_OK){
                    mView.getActivity().setResult(Activity.RESULT_OK);
                    mView.getActivity().finish();
                }
                break;
        }
    }

    @Override
    public void onDestroy() {
        if(mSubscriptions!=null)
        {
            mSubscriptions.unsubscribe();
            mSubscriptions = null;
        }
        mDataList = null;
        mView = null;
        mModel = null;
    }

}
