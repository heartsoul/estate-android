package com.glodon.bim.business.qualityManage.presenter;

import android.app.Activity;
import android.content.Intent;

import com.glodon.bim.basic.log.LogUtil;
import com.glodon.bim.basic.utils.NetWorkUtils;
import com.glodon.bim.basic.utils.SharedPreferencesUtil;
import com.glodon.bim.business.qualityManage.bean.BlueprintListBeanItem;
import com.glodon.bim.business.qualityManage.contract.BluePrintContract;
import com.glodon.bim.business.qualityManage.listener.OnBlueprintCatalogClickListener;
import com.glodon.bim.business.qualityManage.listener.OnBlueprintHintClickListener;
import com.glodon.bim.business.qualityManage.listener.OnChooseBlueprintCataListener;
import com.glodon.bim.business.qualityManage.listener.OnChooseBlueprintObjListener;
import com.glodon.bim.business.qualityManage.model.BluePrintModel;
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
    private Long mSelectId;
    private long mDeptId;//项目id
    private BlueprintListBeanItem mClickedItem ;

    private OnBlueprintHintClickListener mHintClickListener = new OnBlueprintHintClickListener() {
        @Override
        public void onSelect(BlueprintListBeanItem item) {
            //删除点击的item后面的
            int size = mCatalogList.size();
            for(int i=size-1;i>=0;i--){
                if(mClickedItem.id.longValue() == mCatalogList.get(i).id.longValue()){
                    mCatalogList.remove(i);
                    break;
                }else{
                    mCatalogList.remove(i);
                }
            }
            mCatalogList.add(item);
            if(mView!=null)
            {
                mView.updateCataListView(mCatalogList);
            }
            //刷新内容列表
            mContentList = getListByParentId(item.id);//获取一级目录的item
            if (mView != null) {
                mView.updateContentListView(mContentList, mSelectId);
            }
        }
    };



    private OnBlueprintCatalogClickListener mCataClickListener = new OnBlueprintCatalogClickListener(){

        @Override
        public void onSelect(BlueprintListBeanItem item,boolean isShow) {
            if(isShow) {
                mClickedItem = item;
                mHintList = getListByParentId(item.parentId);
                if (mView != null) {
                    mView.updateHintListView(mHintList, item);
                }
            }else{
                if(mView!=null){
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
            if(mView!=null)
            {
                mView.updateCataListView(mCatalogList);
            }
            //更新目录列表
            mContentList = getListByParentId(item.id);//获取一级目录的item
            if (mView != null) {
                mView.updateContentListView(mContentList, mSelectId);
            }
        }
    };
    private OnChooseBlueprintObjListener mObjListener = new OnChooseBlueprintObjListener() {
        @Override
        public void onSelect(BlueprintListBeanItem item, long position) {
            Intent data = new Intent();
            data.putExtra(CommonConfig.MODULE_LIST_NAME, item);
            mView.getActivity().setResult(Activity.RESULT_OK, data);
            mView.getActivity().finish();
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
        mSelectId = intent.getLongExtra(CommonConfig.MODULE_LIST_POSITION, -1);
        if (NetWorkUtils.isNetworkAvailable(mView.getActivity())) {
            if (mView != null) {
                mView.showLoadingDialog();
            }
            Subscription sub = mModel.getBluePrintList(mDeptId,mDeptId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<List<BlueprintListBeanItem>>() {
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
                        public void onNext(List<BlueprintListBeanItem> list) {
                            if (list != null && list.size()>0) {
//                                for(BlueprintListBeanItem item:list) {
//                                    LogUtil.e("item=" +item.toString());
//                                }
                                list = getList();
                                mDataList.addAll(list);
                                mContentList = getListByParentId(0);//获取一级目录的item
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
        } else {
            ToastManager.showNetWorkToast();
        }
    }

    private List<BlueprintListBeanItem> getList(){
        List<BlueprintListBeanItem> list = new ArrayList<>();
        long count = 1;
        int size = 10;
        for(long i = 1;i<=size;i++){
            BlueprintListBeanItem item = new BlueprintListBeanItem();
            item.id = count;
            item.parentId = 0;
            item.name = "id="+i+"  parentId=0";
            list.add(item);
            count++;
        }
        for(long i = 1;i<=size;i++){
            for(long j = 1;j<size;j++) {
                BlueprintListBeanItem item = new BlueprintListBeanItem();
                item.id = count;
                item.parentId = i;
                item.name = "id="+count+"  parentId="+i;
                count++;
                list.add(item);
            }
        }
        for(long i=size+1;i<=size*size;i++){
            for(int j=0;j<5;j++) {
                BlueprintListBeanItem item = new BlueprintListBeanItem();
                item.id = count;
                item.parentId = i;
                item.name = "id="+count+"  parentId="+i+".jpg";
                count++;
                list.add(item);
            }
        }
        return list;
    }

    private List<BlueprintListBeanItem> getListByParentId(long parentId){
        List<BlueprintListBeanItem> list = new ArrayList<>();
        for(BlueprintListBeanItem item:mDataList){
            if(item.parentId == parentId){
                for(BlueprintListBeanItem cItem:mDataList){
                    if(cItem.parentId == item.id.longValue()){
                        item.viewType = 0;
                        break;
                    }
                }
                list.add(item);
            }
        }

        return list;
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
