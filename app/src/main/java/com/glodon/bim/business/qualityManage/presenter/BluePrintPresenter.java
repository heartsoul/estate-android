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
import com.glodon.bim.business.qualityManage.view.BluePrintModelSearchActivity;
import com.glodon.bim.business.qualityManage.view.RelevantBluePrintActivity;
import com.glodon.bim.common.config.CommonConfig;
import com.glodon.bim.common.config.RequestCodeConfig;
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
    private String mSelectFileName;
    private String drawingPositionX;//位置的x信息
    private String drawingPositionY;//位置的y信息
    private int type = 0;//0新建检查单 1检查单编辑状态 2详情查看  3图纸模式
    private long mDeptId;//项目id
    private BlueprintListBeanItem mClickedItem;
    private ProjectVersionBean mLatestVersionInfo;//最新版本信息
    private String fileId = "";
    private int pageIndex = 0;
    private boolean mIsFragment = false;
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
            //新建检查单时
            Intent intent = new Intent(mView.getActivity(), RelevantBluePrintActivity.class);
            intent.putExtra(CommonConfig.BLUE_PRINT_FILE_NAME, item.name);
            intent.putExtra(CommonConfig.BLUE_PRINT_FILE_ID, item.fileId);
            intent.putExtra(CommonConfig.RELEVANT_TYPE, type);
            if (item.fileId.equals(mSelectId)) {
                intent.putExtra(CommonConfig.BLUE_PRINT_POSITION_X, drawingPositionX);
                intent.putExtra(CommonConfig.BLUE_PRINT_POSITION_Y, drawingPositionY);
            }
            mView.getActivity().startActivityForResult(intent, RequestCodeConfig.REQUEST_CODE_TO_RELEVANT);
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
        mSelectFileName = intent.getStringExtra(CommonConfig.MODULE_LIST_NAME);
        drawingPositionX = intent.getStringExtra(CommonConfig.BLUE_PRINT_POSITION_X);
        drawingPositionY = intent.getStringExtra(CommonConfig.BLUE_PRINT_POSITION_Y);
        type = intent.getIntExtra(CommonConfig.RELEVANT_TYPE, 0);
        //编辑状态直接进入预览
        if (type == 1) {
            toBluePrintPreview();
        }
        if (NetWorkUtils.isNetworkAvailable(mView.getActivity())) {
            getLatestVersion();
        } else {
            ToastManager.showNetWorkToast();
        }
    }

    private void toBluePrintPreview() {
        //编辑检查单时
        Intent intent = new Intent(mView.getActivity(), RelevantBluePrintActivity.class);
        intent.putExtra(CommonConfig.RELEVANT_TYPE, type);
        intent.putExtra(CommonConfig.BLUE_PRINT_FILE_NAME, mSelectFileName);
        intent.putExtra(CommonConfig.BLUE_PRINT_FILE_ID, mSelectId);
        intent.putExtra(CommonConfig.BLUE_PRINT_POSITION_X, drawingPositionX);
        intent.putExtra(CommonConfig.BLUE_PRINT_POSITION_Y, drawingPositionY);
        mView.getActivity().startActivityForResult(intent, RequestCodeConfig.REQUEST_CODE_TO_RELEVANT);
    }

    @Override
    public void toSearch() {
        Intent intent = new Intent(mView.getActivity(),BluePrintModelSearchActivity.class);
        intent.putExtra(CommonConfig.SEARCH_TYPE,0);//表示图纸
        //0新建检查单 1检查单编辑状态 2详情查看  3图纸模式
        if(type==1){
            //编辑状态 传递其他数据
            intent.putExtra(CommonConfig.BLUE_PRINT_FILE_NAME, mSelectFileName);
            intent.putExtra(CommonConfig.BLUE_PRINT_FILE_ID, mSelectId);
            intent.putExtra(CommonConfig.BLUE_PRINT_POSITION_X, drawingPositionX);
            intent.putExtra(CommonConfig.BLUE_PRINT_POSITION_Y, drawingPositionY);
        }
        intent.putExtra(CommonConfig.RELEVANT_TYPE,type);
        mView.getActivity().startActivityForResult(intent,RequestCodeConfig.REQUEST_CODE_TO_SEARCH);
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

//    private void getBluePrintRoot() {
//        Subscription sub = mModel.getBluePrint(mDeptId, mLatestVersionInfo.data.versionId, fileId, pageIndex)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<BluePrintBean>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        LogUtil.e(e.getMessage());
//                        if (mView != null) {
//                            mView.dismissLoadingDialog();
//                        }
//                    }
//
//                    @Override
//                    public void onNext(BluePrintBean bean) {
//                        mDataList.clear();
//                        if (bean != null && bean.data != null && bean.data.items != null && bean.data.items.size() > 0) {
//
//                            for (BlueprintListBeanItem item : bean.data.items) {
//                                if ("图纸文件".equals(item.inspectItem)) {
//                                    fileId = item.fileId;
//                                    getBluePrintData();
//                                    break;
//                                }
//                            }
//
//                        }
//
//                    }
//                });
//        mSubscription.add(sub);
//    }

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
                            LogUtil.e("bean=" + new GsonBuilder().create().toJson(bean));
                            mContentList = bean.data.items;
                            if(mContentList!=null && mContentList.size()>0){
                                //删除模型文件
                                int i = 0;
                                boolean isRemove = false;
                                for(BlueprintListBeanItem item:mContentList){
                                    if("模型文件".equals(item.name)){
                                        isRemove = true;
                                        break;
                                    }
                                    i++;
                                }
                                if(isRemove) {
                                    mContentList.remove(i);
                                }
                            }
                            if (mContentList == null) {
                                mContentList = new ArrayList<>();
                            }
                            mContentList = getSortedList(mContentList);
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

    private List<BlueprintListBeanItem> getSortedList(List<BlueprintListBeanItem> contentlist){
        List<BlueprintListBeanItem> cataList = new ArrayList<>();
        List<BlueprintListBeanItem> objList = new ArrayList<>();
        if(contentlist!=null&&contentlist.size()>0){
            for(BlueprintListBeanItem item:contentlist){
                if(item.folder){
                    cataList.add(item);
                }else{
                    objList.add(item);
                }
            }
        }
        if(objList.size()>0) {
            cataList.addAll(objList);
        }
        return cataList;
    }

    private void getBluePrintHintData(final BlueprintListBeanItem item) {
        if (mView != null) {
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
                            for (BlueprintListBeanItem item : bean.data.items) {
                                if (item.folder && !"模型文件".equals(item.name)) {
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RequestCodeConfig.REQUEST_CODE_TO_RELEVANT:
                if (resultCode == Activity.RESULT_OK) {
                    mView.getActivity().setResult(Activity.RESULT_OK, data);
                    mView.getActivity().finish();
                }
                break;
            case RequestCodeConfig.REQUEST_CODE_TO_SEARCH:
                if (resultCode == Activity.RESULT_OK) {
                    mView.getActivity().setResult(Activity.RESULT_OK, data);
                    mView.getActivity().finish();
                }
                break;
        }
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

    @Override
    public void setIsFragment() {
        mIsFragment = true;
        type = 3;
    }


}
