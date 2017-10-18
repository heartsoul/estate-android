package com.glodon.bim.business.main.presenter;

import android.content.Intent;

import com.glodon.bim.basic.log.LogUtil;
import com.glodon.bim.business.main.contract.ChooseTenantContract;
import com.glodon.bim.business.main.model.ChooseTenantModel;
import com.glodon.bim.business.main.view.ChooseProjectActivity;
import com.glodon.bim.common.login.User;
import com.glodon.bim.common.login.UserTenant;

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

    public ChooseTenantPresenter(ChooseTenantContract.View mView) {
        this.mView = mView;
        mModel = new ChooseTenantModel();
        mDataList = new ArrayList<>();
        mSubscriptions = new CompositeSubscription();
    }

    @Override
    public void initData(Intent intent) {
        User user = (User) intent.getSerializableExtra("user");
        if(user!=null && user.accountInfo!=null && user.accountInfo.userTenants!=null){
            mDataList.addAll(user.accountInfo.userTenants);
        }
        mView.updateData(mDataList);
    }


    @Override
    public void clickTenant(UserTenant tenant) {

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
                        Intent intent = new Intent(mView.getActivity(),ChooseProjectActivity.class);
                        mView.getActivity().startActivity(intent);
                    }
                });
        mSubscriptions.add(sub);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void onDestroy() {
        if(mSubscriptions!=null)
        {
            mSubscriptions.unsubscribe();
            mSubscriptions = null;
        }
    }

}
