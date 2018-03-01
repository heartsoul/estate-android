package com.glodon.bim.business.main.presenter;

import android.app.Activity;
import android.content.Intent;

import com.glodon.bim.basic.log.LogUtil;
import com.glodon.bim.basic.utils.NetWorkUtils;
import com.glodon.bim.basic.utils.SharedPreferencesUtil;
import com.glodon.bim.business.authority.AuthorityManager;
import com.glodon.bim.business.main.bean.ProjectListBean;
import com.glodon.bim.business.main.bean.ProjectListItem;
import com.glodon.bim.business.main.contract.ChooseProjectContract;
import com.glodon.bim.business.main.listener.OnGetAuthorityListener;
import com.glodon.bim.business.main.listener.OnProjectClickListener;
import com.glodon.bim.business.main.model.ChooseProjectModel;
import com.glodon.bim.business.main.view.MainActivity;
import com.glodon.bim.business.qualityManage.bean.ProjectVersionBean;
import com.glodon.bim.common.config.CommonConfig;
import com.glodon.bim.customview.ToastManager;
import com.google.gson.GsonBuilder;

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
    private int mCurrentPage = 0;
    private int mSize = 35;
    private boolean mIsChangeProject = false;//是否是切换项目
    private OnProjectClickListener mListener = new OnProjectClickListener() {
        @Override
        public void clickTenant(ProjectListItem item) {
            clickProject(item);
        }
    };


    public ChooseProjectPresenter(ChooseProjectContract.View view) {
        this.mView = view;
        mModel= new ChooseProjectModel();
        mDataList = new ArrayList<>();
        mSubscription = new CompositeSubscription();
    }

    @Override
    public OnProjectClickListener getListener(){
        return mListener;
    }

    /**
     * 点击项目
     */
    public void clickProject(final ProjectListItem item) {
        SharedPreferencesUtil.setProjectInfo(item);
        AuthorityManager.getAuthorities(new OnGetAuthorityListener() {
            @Override
            public void finish() {
                Intent intent = new Intent(mView.getActivity(), MainActivity.class);
//                Intent intent = new Intent(mView.getActivity(), ChooseCategoryItemActivity.class);
//                intent.putExtra(CommonConfig.PROJECT_LIST_ITEM, item);
                mView.getActivity().startActivity(intent);
                mView.getActivity().setResult(Activity.RESULT_OK);
                mView.getActivity().finish();
            }
        });

        getLatestVersion(item.deptId);
    }

    //获取最新版本
    private void getLatestVersion(final long projectId){
        Subscription sub = mModel.getLatestVersion(projectId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ProjectVersionBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(e.getMessage());
                    }

                    @Override
                    public void onNext(ProjectVersionBean projectVersionBean) {
                        if(projectVersionBean!=null && projectVersionBean.data!=null) {
                            SharedPreferencesUtil.setString(projectId + "", projectVersionBean.data.versionId);
                        }
                    }
                });
        mSubscription.add(sub);
    }

    @Override
    public void initData(Intent intent) {
        mIsChangeProject = intent.getBooleanExtra(CommonConfig.CHANGE_PROJECT,false);
        if(NetWorkUtils.isNetworkAvailable(mView.getActivity())) {
            if(mView!=null){
                mView.showLoadingDialog();
            }
            Subscription sub = mModel.getAvailableProjects(mCurrentPage, mSize)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<ProjectListBean>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            LogUtil.e(e.getMessage());
                            if(mView!=null){
                                mView.dismissLoadingDialog();
                            }
                        }

                        @Override
                        public void onNext(ProjectListBean bean) {
                            LogUtil.e("项目列表="+new GsonBuilder().create().toJson(bean));
                            if (bean != null && bean.content != null && bean.content.size() > 0) {
                                mDataList.addAll(bean.content);
                                mView.setStyle(mDataList.size());
                                mView.updateData(mDataList);
                                if (mCurrentPage < bean.totalPages) {
                                    mCurrentPage++;
                                }
                                //如果只有一个项目 直接进入
                                if (!mIsChangeProject && mDataList.size() == 1) {
                                    clickProject(mDataList.get(0));
                                }
                            }
                            if(mView!=null){
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
    public void pullUp() {
        if(NetWorkUtils.isNetworkAvailable(mView.getActivity())) {
            if(mView!=null){
                mView.showLoadingDialog();
            }
            Subscription sub = mModel.getAvailableProjects(mCurrentPage, mSize)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<ProjectListBean>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            LogUtil.e(e.getMessage());
                            if(mView!=null){
                                mView.dismissLoadingDialog();
                            }
                        }

                        @Override
                        public void onNext(ProjectListBean bean) {
                            if (bean != null && bean.content != null && bean.content.size() > 0) {
                                mDataList.addAll(bean.content);
                                mView.updateData(mDataList);
                                if (mCurrentPage < bean.totalPages) {
                                    mCurrentPage++;
                                }
                            }
                            if(mView!=null){
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
