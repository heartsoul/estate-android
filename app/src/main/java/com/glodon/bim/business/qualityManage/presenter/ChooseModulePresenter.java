package com.glodon.bim.business.qualityManage.presenter;

import android.app.Activity;
import android.content.Intent;

import com.glodon.bim.basic.log.LogUtil;
import com.glodon.bim.basic.utils.NetWorkUtils;
import com.glodon.bim.basic.utils.SharedPreferencesUtil;
import com.glodon.bim.business.qualityManage.bean.ModuleListBeanItem;
import com.glodon.bim.business.qualityManage.contract.ChooseModuleContract;
import com.glodon.bim.business.qualityManage.listener.OnChooseModuleCataListener;
import com.glodon.bim.business.qualityManage.listener.OnChooseModuleObjListener;
import com.glodon.bim.business.qualityManage.listener.OnModuleCatalogClickListener;
import com.glodon.bim.business.qualityManage.listener.OnModuleHintClickListener;
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
    private List<ModuleListBeanItem> mContentList;
    private List<ModuleListBeanItem> mCatalogList;
    private List<ModuleListBeanItem> mHintList;
    private CompositeSubscription mSubscription;
    private Long mSelectId;
    private long mDeptId;//项目id
    private ModuleListBeanItem mClickedItem ;

    private OnModuleHintClickListener mHintClickListener = new OnModuleHintClickListener() {
        @Override
        public void onSelect(ModuleListBeanItem item) {
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



    private OnModuleCatalogClickListener mCataClickListener = new OnModuleCatalogClickListener(){

        @Override
        public void onSelect(ModuleListBeanItem item,boolean isShow) {
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


    private OnChooseModuleCataListener mCataListener = new OnChooseModuleCataListener() {
        @Override
        public void onSelect(ModuleListBeanItem item) {
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
    private OnChooseModuleObjListener mObjListener = new OnChooseModuleObjListener() {
        @Override
        public void onSelect(ModuleListBeanItem item, long position) {
            Intent data = new Intent();
            data.putExtra(CommonConfig.MODULE_LIST_NAME, item);
            mView.getActivity().setResult(Activity.RESULT_OK, data);
            mView.getActivity().finish();
        }
    };

    public ChooseModulePresenter(ChooseModuleContract.View mView) {
        this.mView = mView;
        mModel = new ChooseModuleModel();
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
            Subscription sub = mModel.getModuleList(mDeptId)
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
//                                for(ModuleListBeanItem item:list) {
//                                    LogUtil.e("item=" +item.toString());
//                                }
//                                list = getList();
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

    private List<ModuleListBeanItem> getList(){
        List<ModuleListBeanItem> list = new ArrayList<>();
        long count = 1;
        int size = 10;
        for(long i = 1;i<=size;i++){
            ModuleListBeanItem item = new ModuleListBeanItem();
            item.id = count;
            item.parentId = 0;
            item.inspectItem = "fileId="+i+"  parentId=0";
            list.add(item);
            count++;
        }
        for(long i = 1;i<=size;i++){
            for(long j = 1;j<size;j++) {
                ModuleListBeanItem item = new ModuleListBeanItem();
                item.id = count;
                item.parentId = i;
                item.inspectItem = "fileId="+count+"  parentId="+i;
                count++;
                list.add(item);
            }
        }
        for(long i=size+1;i<=size*size;i++){
            for(int j=0;j<5;j++) {
                ModuleListBeanItem item = new ModuleListBeanItem();
                item.id = count;
                item.parentId = i;
                item.inspectItem = "fileId="+count+"  parentId="+i;
                count++;
                list.add(item);
            }
        }
        return list;
    }

    private List<ModuleListBeanItem> getListByParentId(long parentId){
        List<ModuleListBeanItem> list = new ArrayList<>();
        for(ModuleListBeanItem item:mDataList){
            if(item.parentId == parentId){
                for(ModuleListBeanItem cItem:mDataList){
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

    public OnChooseModuleObjListener getmObjListener() {
        return mObjListener;
    }

    public OnChooseModuleCataListener getmCataListener() {
        return mCataListener;
    }

    public OnModuleCatalogClickListener getmCataClickListener() {
        return mCataClickListener;
    }
    public OnModuleHintClickListener getmHintClickListener() {
        return mHintClickListener;
    }
}
