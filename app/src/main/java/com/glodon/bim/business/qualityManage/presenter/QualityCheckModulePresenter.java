package com.glodon.bim.business.qualityManage.presenter;

import android.content.Intent;

import com.glodon.bim.basic.log.LogUtil;
import com.glodon.bim.basic.utils.NetWorkUtils;
import com.glodon.bim.basic.utils.SharedPreferencesUtil;
import com.glodon.bim.business.qualityManage.bean.ModuleListBeanItem;
import com.glodon.bim.business.qualityManage.contract.ChooseModuleContract;
import com.glodon.bim.business.qualityManage.contract.QulityCheckModuleContract;
import com.glodon.bim.business.qualityManage.model.ChooseModuleModel;
import com.glodon.bim.customview.ToastManager;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * 描述：
 * 作者：zhourf on 2017/11/24
 * 邮箱：zhourf@glodon.com
 */

public class QualityCheckModulePresenter implements QulityCheckModuleContract.Presenter {

    private ChooseModuleContract.Model mModel;
    private QulityCheckModuleContract.View mView;

    private List<ModuleListBeanItem> mModuleDataList;//质检项目列表
    private List<ModuleListBeanItem> mRootList;//最外层的质检项目列表
    private CompositeSubscription mSubscription;
    private long mDeptId;

    public QualityCheckModulePresenter(QulityCheckModuleContract.View mView) {
        this.mView = mView;
        mModel = new ChooseModuleModel();
        mModuleDataList = new ArrayList<>();
        mRootList = new ArrayList<>();
        mSubscription = new CompositeSubscription();
        mDeptId = SharedPreferencesUtil.getProjectId();
    }

    @Override
    public void initData(Intent intent) {
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
                                for(ModuleListBeanItem item:list) {
                                    LogUtil.e("item=" +item.toString());
                                }
//                                list = getList();
                                mModuleDataList.addAll(list);
                                mRootList = getListByParentId(0);//获取一级目录的item
                                if (mView != null) {
                                    mView.updateList(mRootList);
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
    public List<ModuleListBeanItem> getChildList(long id) {
        return getListByParentId(id);
    }


    private List<ModuleListBeanItem> getList(){
        List<ModuleListBeanItem> list = new ArrayList<>();
        long count = 1;
        int size = 10;
        for(long i = 1;i<=size;i++){
            ModuleListBeanItem item = new ModuleListBeanItem();
            item.id = count;
            item.parentId = 0;
            item.name = "id="+i+"  parentId=0";
            list.add(item);
            count++;
        }
        for(long i = 1;i<=size;i++){
            for(long j = 1;j<size;j++) {
                ModuleListBeanItem item = new ModuleListBeanItem();
                item.id = count;
                item.parentId = i;
                item.name = "id="+count+"  parentId="+i;
                count++;
                list.add(item);
            }
        }
        for(long i=size+1;i<=size*size;i++){
            for(int j=0;j<5;j++) {
                ModuleListBeanItem item = new ModuleListBeanItem();
                item.id = count;
                item.parentId = i;
                item.name = "id="+count+"  parentId="+i;
                count++;
                list.add(item);
            }
        }
        return list;
    }

    private List<ModuleListBeanItem> getListByParentId(long parentId){
        List<ModuleListBeanItem> list = new ArrayList<>();
        for(ModuleListBeanItem item: mModuleDataList){
            if(item.parentId == parentId){
                for(ModuleListBeanItem cItem: mModuleDataList){
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
        mModuleDataList = null;
        mView = null;
        mModel = null;
    }


}
