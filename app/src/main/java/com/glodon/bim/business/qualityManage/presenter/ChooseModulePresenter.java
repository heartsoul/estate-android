package com.glodon.bim.business.qualityManage.presenter;

import android.app.Activity;
import android.content.Intent;

import com.glodon.bim.basic.log.LogUtil;
import com.glodon.bim.basic.utils.SharedPreferencesUtil;
import com.glodon.bim.business.qualityManage.bean.ModuleListBean;
import com.glodon.bim.business.qualityManage.bean.ModuleListBeanItem;
import com.glodon.bim.business.qualityManage.contract.ChooseModuleContract;
import com.glodon.bim.business.qualityManage.listener.OnChooseModuleListener;
import com.glodon.bim.business.qualityManage.model.ChooseModuleModel;
import com.glodon.bim.common.config.CommonConfig;

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
    private long mSelectId = -1;
    private int mCurrentPage = 0;
    private int mSize = 35;
    private OnChooseModuleListener mListener = new OnChooseModuleListener() {
        @Override
        public void onSelect(ModuleListBeanItem item, long position) {
            Intent data = new Intent();
            data.putExtra(CommonConfig.MODULE_LIST_NAME,item);
//            data.putExtra(CommonConfig.MODULE_LIST_POSITION,position);
            mView.getActivity().setResult(Activity.RESULT_OK,data);
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
    }

    @Override
    public void initData(Intent intent) {
        mSelectId = intent.getLongExtra(CommonConfig.MODULE_LIST_POSITION,-1);
        Subscription sub = mModel.getModuleList(SharedPreferencesUtil.getProjectTypeCode(),mCurrentPage,mSize)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ModuleListBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e("----",e.getMessage());
                    }

                    @Override
                    public void onNext(ModuleListBean bean) {
                        if(bean!=null && bean.content!=null && bean.content.size()>0)
                        {
                            mDataList.clear();
                            mDataList.addAll(bean.content);
                            if(mView!=null){
                                mView.initListView(mDataList, mSelectId);
                            }
                            if(mCurrentPage<bean.totalPages){
                                mCurrentPage++;
                            }
                        }

                    }
                })
                ;
        mSubscription.add(sub);

    }


    @Override
    public void pullUp() {
        Subscription sub = mModel.getModuleList(SharedPreferencesUtil.getProjectTypeCode(),mCurrentPage,mSize)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ModuleListBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e("----",e.getMessage());
                    }

                    @Override
                    public void onNext(ModuleListBean bean) {
                        if(bean!=null && bean.content!=null && bean.content.size()>0)
                        {
                            mDataList.addAll(bean.content);
                            if(mView!=null){
                                mView.updateListView(mDataList);
                            }
                            if(mCurrentPage<bean.totalPages){
                                mCurrentPage++;
                            }
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
        mDataList = null;
        mView = null;
        mModel = null;
    }

    @Override
    public void pullDown() {

    }

}
