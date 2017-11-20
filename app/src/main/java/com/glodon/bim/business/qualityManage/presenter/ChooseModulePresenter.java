package com.glodon.bim.business.qualityManage.presenter;

import android.app.Activity;
import android.content.Intent;

import com.glodon.bim.basic.log.LogUtil;
import com.glodon.bim.basic.utils.NetWorkUtils;
import com.glodon.bim.basic.utils.SharedPreferencesUtil;
import com.glodon.bim.business.qualityManage.bean.ModuleListBeanItem;
import com.glodon.bim.business.qualityManage.contract.ChooseModuleContract;
import com.glodon.bim.business.qualityManage.listener.OnChooseModuleListener;
import com.glodon.bim.business.qualityManage.model.ChooseModuleModel;
import com.glodon.bim.common.config.CommonConfig;
import com.glodon.bim.customview.ToastManager;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * 描述：选择质检项目
 * 作者：zhourf on 2017/10/27
 * 邮箱：zhourf@glodon.com
 */

public class ChooseModulePresenter implements ChooseModuleContract.Presenter {
    private ChooseModuleContract.View mView;
    private ChooseModuleContract.Model mModel;
    private List<ModuleListBeanItem> mDataList;
    private CompositeSubscription mSubscription;
    private Long mSelectId;
    private long mDeptId;//项目id
    private OnChooseModuleListener mListener = new OnChooseModuleListener() {
        @Override
        public void onSelect(ModuleListBeanItem item, long position) {
            Intent data = new Intent();
            data.putExtra(CommonConfig.MODULE_LIST_NAME, item);
            mView.getActivity().setResult(Activity.RESULT_OK, data);
            mView.getActivity().finish();
        }
    };

    @Override
    public OnChooseModuleListener getmListener() {
        return mListener;
    }

    public ChooseModulePresenter(ChooseModuleContract.View mView) {
        this.mView = mView;
        mModel = new ChooseModuleModel();
        mDataList = new ArrayList<>();
        mSubscription = new CompositeSubscription();
        mDeptId = SharedPreferencesUtil.getProjectId();
    }

    @Override
    public void initData(Intent intent) {
        mSelectId = intent.getLongExtra(CommonConfig.MODULE_LIST_POSITION, -1);
        if (NetWorkUtils.isNetworkAvailable(mView.getActivity())) {
            if (mView != null) {
                mView.showLoadingDialog();
            }
            Subscription sub = mModel.getModuleList(mDeptId,mDeptId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<List<ModuleListBeanItem>>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            LogUtil.e("----", e.getMessage());
                            if (mView != null) {
                                mView.dismissLoadingDialog();
                            }
                        }

                        @Override
                        public void onNext(List<ModuleListBeanItem> list) {
                            if (list != null && list.size()>0) {
                                mDataList.addAll(list);
                                if (mView != null) {
                                    mView.initListView(mDataList, mSelectId);
                                }

                            }
                            if (mView != null) {
                                mView.dismissLoadingDialog();
                            }

                        }
                    });
            mSubscription.add(sub);
        } else {
            ToastManager.showNetWorkToast();
        }
    }


    @Override
    public void pullUp() {

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
        mDataList = null;
        mView = null;
        mModel = null;
    }

    @Override
    public void pullDown() {

    }

}
