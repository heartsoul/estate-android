package com.glodon.bim.business.qualityManage.presenter;

import android.content.Intent;

import com.glodon.bim.basic.log.LogUtil;
import com.glodon.bim.basic.utils.NetWorkUtils;
import com.glodon.bim.business.qualityManage.bean.ModuleStandardItem;
import com.glodon.bim.business.qualityManage.contract.ModuleStandardContract;
import com.glodon.bim.business.qualityManage.model.ModuleStandardModel;
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
 * 描述：质检标准
 * 作者：zhourf on 2017/11/20
 * 邮箱：zhourf@glodon.com
 */

public class ModuleStandardPresenter implements ModuleStandardContract.Presenter {
    private ModuleStandardContract.View mView;
    private ModuleStandardContract.Model mModel;
    private CompositeSubscription mSubscription;
    private List<ModuleStandardItem> mDataList;

    public ModuleStandardPresenter(ModuleStandardContract.View mView) {
        this.mView = mView;
        mModel = new ModuleStandardModel();
        mSubscription = new CompositeSubscription();
    }

    @Override
    public void initData(Intent intent) {
        if(NetWorkUtils.isNetworkAvailable(mView.getActivity())) {
            mView.showLoadingDialog();
            long templateId = intent.getLongExtra(CommonConfig.MODULE_TEMPLATEID, -1);
            LogUtil.e("templateId="+templateId);
            Subscription sub = mModel.getModuleStandardList(templateId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<List<ModuleStandardItem>>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            LogUtil.e("standard error=" + e.getMessage());
                            if(mView!=null)
                            {
                                mView.dismissLoadingDialog();
                            }
                        }

                        @Override
                        public void onNext(List<ModuleStandardItem> list) {
//                            list =getList();
//                            LogUtil.e("standardList = "+new GsonBuilder().create().toJson(list));
                            if(list!=null && list.size()>0){
                                mDataList = list;
                                handleDataList();
                                if(mView!=null)
                                {
                                    mView.updateListView(list);
                                }
                            }
                            if(mView!=null)
                            {
                                mView.dismissLoadingDialog();
                            }
                        }
                    });
            mSubscription.add(sub);
        }else{
            ToastManager.showNetWorkToast();
        }
    }

    private void handleDataList(){
        List<ModuleStandardItem> list = getRootList();
        for(ModuleStandardItem item : list){
           handleLevel(item);
        }
//        LogUtil.e("standardList = "+new GsonBuilder().create().toJson(mDataList));
    }

    private void handleLevel(ModuleStandardItem item){
        List<ModuleStandardItem> list = getListByParentId(item.id);
        if(list.size()>0){
            for(ModuleStandardItem i : list){
                handleLevel(i);
            }
        }else{
            for(int i = 0;i<mDataList.size();i++){
                if(item.id == mDataList.get(i).id){
                    if(item.parentId!=null && getItemById(item.parentId)!=null) {
                        mDataList.get(i).level = getItemById(item.parentId).level + 1;
                    }
                    mDataList.get(i).hasChild = false;
                }
            }
        }
    }

    private ModuleStandardItem getItemById(long id){
        for(ModuleStandardItem item :mDataList){
            if(id == item.id){
                return item;
            }
        }
        return null;
    }

    public List<ModuleStandardItem> getRootList(){
        List<ModuleStandardItem> list = new ArrayList<>();
        for(ModuleStandardItem item : mDataList){
            if(item.parentId == null || item.parentId.longValue() == 0){
                list.add(item);
            }
        }
        return list;
    }

    public List<ModuleStandardItem> getListByParentId(long parentId){
        List<ModuleStandardItem> list = new ArrayList<>();
        for(ModuleStandardItem item : mDataList){
            if(item.parentId != null && item.parentId.longValue() == parentId){
                list.add(item);
            }
        }
        return list;
    }


    private List<ModuleStandardItem> getList(){
        List<ModuleStandardItem> list = new ArrayList<>();

        for(long i = 100 ;i<106;i++){
            ModuleStandardItem item = new ModuleStandardItem();
            item.parentId = null;
            item.level = 0;
            item.id = i;
            item.name = "根部"+i;
            item.content =  " 内容内容内容内容内容内容内容内容内容内容内容内容内容内容 "+i;
            list.add(item);
        }
        int count = 106;
        for(long j = 100;j<106;j++){
            for(long i =106;i<111;i++){
                ModuleStandardItem item = new ModuleStandardItem();
                item.parentId = j;
                item.id = count;
                item.name = "名字名字名字名字名字="+i;
                item.content =  item.name+" 内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容 "+i;
                list.add(item);
                count++;
            }
        }

        int num = 200;
        for(long j = 106;j<120;j++){
            for(int i=0;i<5;i++){
                ModuleStandardItem item = new ModuleStandardItem();
                item.parentId = j;
                item.id = num;
                item.name = "名字名字名字名字名字="+i;
                item.content =  item.name+" 内容内容内容内容内容内容内容内容内容内容内容内容内容内容 "+i;
                list.add(item);
                num++;
            }
        }
        for(long i = 95 ;i<100;i++){
            ModuleStandardItem item = new ModuleStandardItem();
            item.parentId = null;
            item.level = 0;
            item.id = i;
            item.name = "根部"+i;
            item.content =  " 内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容 "+i;
            list.add(item);
        }
        return list;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void onDestroy() {
        if(mSubscription!=null){
            mSubscription.unsubscribe();
            mSubscription = null;
        }
        mView = null;
        mModel = null;
    }
}
