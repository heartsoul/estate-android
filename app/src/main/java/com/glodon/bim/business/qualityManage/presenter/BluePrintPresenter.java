package com.glodon.bim.business.qualityManage.presenter;

import android.app.Activity;
import android.content.Intent;

import com.glodon.bim.basic.log.LogUtil;
import com.glodon.bim.basic.utils.NetWorkUtils;
import com.glodon.bim.basic.utils.SharedPreferencesUtil;
import com.glodon.bim.business.qualityManage.bean.BluePrintBean;
import com.glodon.bim.business.qualityManage.bean.BlueprintListBeanItem;
import com.glodon.bim.business.qualityManage.bean.ProjectVersionBean;
import com.glodon.bim.business.qualityManage.contract.BluePrintContract;
import com.glodon.bim.business.qualityManage.listener.OnBlueprintCatalogClickListener;
import com.glodon.bim.business.qualityManage.listener.OnBlueprintHintClickListener;
import com.glodon.bim.business.qualityManage.listener.OnChooseBlueprintCataListener;
import com.glodon.bim.business.qualityManage.listener.OnChooseBlueprintObjListener;
import com.glodon.bim.business.qualityManage.model.BluePrintModel;
import com.glodon.bim.business.qualityManage.model.ModelModel;
import com.glodon.bim.business.qualityManage.view.RelevantBluePrintActivity;
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
 * 描述：选择图纸
 * 作者：zhourf on 2017/10/27
 * 邮箱：zhourf@glodon.com
 */

public class BluePrintPresenter implements BluePrintContract.Presenter {
    private BluePrintContract.View mView;
    private BluePrintContract.Model mModel;
    private List<BlueprintListBeanItem> mDataList;
    private List<BlueprintListBeanItem> mContentList;
    private List<BlueprintListBeanItem> mCatalogList;
    private List<BlueprintListBeanItem> mHintList;
    private CompositeSubscription mSubscription;
    private String mSelectId;
    private long mDeptId;//项目id
    private BlueprintListBeanItem mClickedItem;
    private ProjectVersionBean mLatestVersionInfo;//最新版本信息
    private String fileId = "";
    private int pageIndex = 0;

    private OnBlueprintHintClickListener mHintClickListener = new OnBlueprintHintClickListener() {
        @Override
        public void onSelect(BlueprintListBeanItem item) {
            //删除点击的item后面的
            int size = mCatalogList.size();
            for (int i = size - 1; i >= 0; i--) {
                if (mClickedItem.fileId.equals(mCatalogList.get(i).fileId)) {
                    mCatalogList.remove(i);
                    break;
                } else {
                    mCatalogList.remove(i);
                }
            }
            mCatalogList.add(item);
            if (mView != null) {
                mView.updateCataListView(mCatalogList);
            }
            //刷新内容列表
//            mContentList = getListByParentId(item.fileId);//获取一级目录的item
//            if (mView != null) {
//                mView.updateContentListView(mContentList, mSelectId);
//            }
            fileId = item.fileId;
            getBluePrintData();
        }
    };


    private OnBlueprintCatalogClickListener mCataClickListener = new OnBlueprintCatalogClickListener() {

        @Override
        public void onSelect(BlueprintListBeanItem item, boolean isShow) {
            if (isShow) {
                mClickedItem = item;
                getBluePrintHintData(item);
            } else {
                if (mView != null) {
                    mView.closeHint();
                }
            }
        }
    };


    private OnChooseBlueprintCataListener mCataListener = new OnChooseBlueprintCataListener() {
        @Override
        public void onSelect(BlueprintListBeanItem item) {
            //选中了一个目录
            //更新顶部目录
            mCatalogList.add(item);
            if (mView != null) {
                mView.updateCataListView(mCatalogList);
            }
            //更新目录列表
            fileId = item.fileId;
            getBluePrintData();
        }
    };
    private OnChooseBlueprintObjListener mObjListener = new OnChooseBlueprintObjListener() {
        @Override
        public void onSelect(BlueprintListBeanItem item, String position) {
//            Intent data = new Intent();
//            data.putExtra(CommonConfig.MODULE_LIST_NAME, item);
//            mView.getActivity().setResult(Activity.RESULT_OK, data);
//            mView.getActivity().finish();
            Intent intent = new Intent(mView.getActivity(), RelevantBluePrintActivity.class);
            intent.putExtra(CommonConfig.BLUE_PRINT_FILE_NAME,item.name);
            intent.putExtra(CommonConfig.BLUE_PRINT_FILE_ID,item.fileId);
            mView.getActivity().startActivity(intent);
        }
    };

    public BluePrintPresenter(BluePrintContract.View mView) {
        this.mView = mView;
        mModel = new BluePrintModel();
        mDataList = new ArrayList<>();
        mContentList = new ArrayList<>();
        mCatalogList = new ArrayList<>();
        mHintList = new ArrayList<>();
        mSubscription = new CompositeSubscription();
        mDeptId = SharedPreferencesUtil.getProjectId();
    }

    @Override
    public void initData(Intent intent) {
        mSelectId = intent.getStringExtra(CommonConfig.MODULE_LIST_POSITION);
        if (NetWorkUtils.isNetworkAvailable(mView.getActivity())) {

//            Subscription sub = mModel.getBluePrintList(mDeptId, mDeptId)
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new Subscriber<List<BlueprintListBeanItem>>() {
//                        @Override
//                        public void onCompleted() {
//
//                        }
//
//                        @Override
//                        public void onError(Throwable e) {
//                            LogUtil.e("----", e.getMessage());
//                            if (mView != null) {
//                                mView.dismissLoadingDialog();
//                            }
//                        }
//
//                        @Override
//                        public void onNext(List<BlueprintListBeanItem> list) {
//                            if (list != null && list.size() > 0) {
////                                for(BlueprintListBeanItem item:list) {
////                                    LogUtil.e("item=" +item.toString());
////                                }
////                                list = getList();
//                                mDataList.addAll(list);
//                                mContentList = getListByParentId(0);//获取一级目录的item
//                                if (mView != null) {
//                                    mView.updateContentListView(mContentList, mSelectId);
//                                }
//
//                            }
//                            if (mView != null) {
//                                mView.dismissLoadingDialog();
//                            }
//
//                        }
//                    });
//            mSubscription.add(sub);
            getLatestVersion();
        } else {
            ToastManager.showNetWorkToast();
        }
    }

    //获取最新版本
    private void getLatestVersion() {
        if (mView != null) {
            mView.showLoadingDialog();
        }
        Subscription sub = new ModelModel().getLatestVersion(mDeptId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ProjectVersionBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(e.getMessage());
                        if (mView != null) {
                            mView.dismissLoadingDialog();
                        }
                    }

                    @Override
                    public void onNext(ProjectVersionBean projectVersionBean) {
                        mLatestVersionInfo = projectVersionBean;
                        getBluePrintData();
                    }
                });
        mSubscription.add(sub);
    }

    private void getBluePrintData() {
        Subscription sub = mModel.getBluePrint(mDeptId, mLatestVersionInfo.data.versionId, fileId, pageIndex)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BluePrintBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(e.getMessage());
                        if (mView != null) {
                            mView.dismissLoadingDialog();
                        }
                    }

                    @Override
                    public void onNext(BluePrintBean bean) {
                        mDataList.clear();
                        if (bean != null) {
                            if (bean.data != null && bean.data.items != null && bean.data.items.size() > 0) {
                                for (BlueprintListBeanItem item : bean.data.items) {
                                    LogUtil.e("item=" + item.toString());
                                }
                            }
//                            mDataList.addAll(bean.data.items);
//                            mContentList = getListByParentId(0);//获取一级目录的item
                            mContentList = bean.data.items;
                            if (mView != null) {
                                mView.updateContentListView(mContentList, mSelectId);
                            }

                        }
                        if (mView != null) {
                            mView.dismissLoadingDialog();
                        }
                    }
                });
        mSubscription.add(sub);
    }

    private void getBluePrintHintData(final BlueprintListBeanItem item) {
        if(mView!=null) {
            mView.showLoadingDialog();
        }
        Subscription sub = mModel.getBluePrint(mDeptId, mLatestVersionInfo.data.versionId, item.parentId, pageIndex)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BluePrintBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(e.getMessage());
                        if (mView != null) {
                            mView.dismissLoadingDialog();
                        }
                    }

                    @Override
                    public void onNext(BluePrintBean bean) {
                        mDataList.clear();
                        if (bean != null) {
                            if (bean.data != null && bean.data.items != null && bean.data.items.size() > 0) {
                                for (BlueprintListBeanItem item : bean.data.items) {
                                    LogUtil.e("item=" + item.toString());
                                }
                            }
                            mHintList.clear();
                            for(BlueprintListBeanItem item:bean.data.items){
                                if(item.folder){
                                    mHintList.add(item);
                                }
                            }
                            if (mView != null) {
                                mView.updateHintListView(mHintList, item);
                            }

                        }
                        if (mView != null) {
                            mView.dismissLoadingDialog();
                        }
                    }
                });
        mSubscription.add(sub);
    }

//    private List<BlueprintListBeanItem> getList(){
//        List<BlueprintListBeanItem> list = new ArrayList<>();
//        long count = 1;
//        int size = 10;
//        for(long i = 1;i<=size;i++){
//            BlueprintListBeanItem item = new BlueprintListBeanItem();
//            item.fileId = count;
//            item.parentId = 0;
//            item.fileName = "fileId="+i+"  parentId=0";
//            list.add(item);
//            count++;
//        }
//        for(long i = 1;i<=size;i++){
//            for(long j = 1;j<size;j++) {
//                BlueprintListBeanItem item = new BlueprintListBeanItem();
//                item.fileId = count;
//                item.parentId = i;
//                item.fileName = "fileId="+count+"  parentId="+i;
//                count++;
//                list.add(item);
//            }
//        }
//        for(long i=size+1;i<=size*size;i++){
//            for(int j=0;j<5;j++) {
//                BlueprintListBeanItem item = new BlueprintListBeanItem();
//                item.fileId = count;
//                item.parentId = i;
//                item.fileName = "fileId="+count+"  parentId="+i+".jpg";
//                count++;
//                list.add(item);
//            }
//        }
//        return list;
//    }
//
//    private List<BlueprintListBeanItem> getListByParentId(long parentId){
//        List<BlueprintListBeanItem> list = new ArrayList<>();
//        for(BlueprintListBeanItem item:mDataList){
//            if(item.parentId == parentId){
//                for(BlueprintListBeanItem cItem:mDataList){
//                    if(cItem.parentId == item.fileId.longValue()){
//                        item.viewType = 0;
//                        break;
//                    }
//                }
//                list.add(item);
//            }
//        }
//
//        return list;
//    }
//


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

    public OnChooseBlueprintObjListener getmObjListener() {
        return mObjListener;
    }

    public OnChooseBlueprintCataListener getmCataListener() {
        return mCataListener;
    }

    public OnBlueprintCatalogClickListener getmCataClickListener() {
        return mCataClickListener;
    }

    public OnBlueprintHintClickListener getmHintClickListener() {
        return mHintClickListener;
    }
}
