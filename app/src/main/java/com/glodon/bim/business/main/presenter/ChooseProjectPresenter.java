package com.glodon.bim.business.main.presenter;

import android.content.Intent;

import com.glodon.bim.basic.log.LogUtil;
import com.glodon.bim.business.main.bean.ProjectListBean;
import com.glodon.bim.business.main.bean.ProjectListItem;
import com.glodon.bim.business.main.contract.ChooseProjectContract;
import com.glodon.bim.business.main.model.ChooseProjectModel;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * 描述：选择项目列表
 * 作者：zhourf on 2017/10/17
 * 邮箱：zhourf@glodon.com
 */

public class ChooseProjectPresenter implements ChooseProjectContract.Presenter {

    private ChooseProjectContract.View mView;
    private ChooseProjectContract.Model mModel;
    private List<ProjectListItem> mDataList;
    private CompositeSubscription mSubscription;

    public ChooseProjectPresenter(ChooseProjectContract.View view) {
        this.mView = view;
        mModel= new ChooseProjectModel();
        mDataList = new ArrayList<>();
        mSubscription = new CompositeSubscription();
    }

    @Override
    public void initData(Intent intent) {
        Subscription sub = mModel.getAvailableProjects(0,200)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ProjectListBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(e.getMessage());
                    }

                    @Override
                    public void onNext(ProjectListBean bean) {
                        if(bean!=null && bean.content!=null && bean.content.size()>0){
                            mDataList.addAll(bean.content);
                            mView.setStyle(mDataList.size());
                            mView.updateData(mDataList);
                        }
                    }
                })
                ;

        mSubscription.add(sub);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void onDestroy() {
        if(mSubscription!=null)
        {
            mSubscription.unsubscribe();
            mSubscription = null;
        }
    }
}
