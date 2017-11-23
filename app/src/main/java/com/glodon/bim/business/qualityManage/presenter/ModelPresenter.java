package com.glodon.bim.business.qualityManage.presenter;

import android.app.Activity;
import android.content.Intent;

import com.glodon.bim.business.qualityManage.bean.ModelListBeanItem;
import com.glodon.bim.business.qualityManage.bean.ModelSingleListItem;
import com.glodon.bim.business.qualityManage.bean.ModelSpecialListItem;
import com.glodon.bim.business.qualityManage.contract.ModelContract;
import com.glodon.bim.business.qualityManage.model.ModelModel;
import com.glodon.bim.business.qualityManage.model.OnModelSelectListener;
import com.glodon.bim.common.config.CommonConfig;

import java.util.ArrayList;
import java.util.List;

import rx.subscriptions.CompositeSubscription;

/**
 * 描述：模型
 * 作者：zhourf on 2017/11/22
 * 邮箱：zhourf@glodon.com
 */

public class ModelPresenter implements ModelContract.Presenter {
    private ModelContract.View mView;
    private ModelContract.Model mModel;
    private CompositeSubscription mSubscription;
    private List<ModelListBeanItem> mModelList;
    private List<ModelSpecialListItem> mSpecialList;
    private List<ModelSingleListItem> mSingleList;

    private long mSpecialSelectId = -1;
    private long mSingleSelectId = -1;

    private ModelSpecialListItem mCurrentSpecial;
    private ModelSingleListItem mCurrentSingle;

    private OnModelSelectListener mListener = new OnModelSelectListener() {
        @Override
        public void selectModel(ModelListBeanItem item) {
            if(mView!=null) {
                Intent data = new Intent();
                data.putExtra(CommonConfig.MODULE_LIST_NAME, item);
                mView.getActivity().setResult(Activity.RESULT_OK, data);
                mView.getActivity().finish();
            }
        }

        @Override
        public void selectSingle(ModelSingleListItem item) {
            mCurrentSingle = item;
            mSingleSelectId = item.id;
            //修改顶部显示的单体
            if(mView!=null){
                mView.showSingle(item);
            }
            //刷新模型列表
            mModelList = getModelList(mSpecialSelectId,mSingleSelectId);
            if(mView!=null) {
                mView.updateModelList(mModelList);
            }
        }

        @Override
        public void selectSpecial(ModelSpecialListItem item) {
            mCurrentSpecial = item;
            mSpecialSelectId = item.id;
            //修改顶部显示的专业
            if(mView!=null){
                mView.showSpecial(item);
            }
            //刷新模型列表
            mModelList = getModelList(mSpecialSelectId,mSingleSelectId);
            if(mView!=null) {
                mView.updateModelList(mModelList);
            }
        }
    };

    public OnModelSelectListener getListener()
    {
        return mListener;
    }

    public ModelPresenter(ModelContract.View mView) {
        this.mView = mView;
        mSubscription = new CompositeSubscription();
        mModel = new ModelModel();
        mModelList = new ArrayList<>();
        mSpecialList = new ArrayList<>();
        mSingleList = new ArrayList<>();
    }

    @Override
    public void initData(Intent intent) {

    }


    @Override
    public void showSpecialList() {
        if (mSpecialList.size() == 0) {
            mSpecialList = getSpecialList();
        }
        if(mCurrentSpecial!=null && mCurrentSpecial.id>=0){
            mSpecialSelectId = mCurrentSpecial.id;
        }
        mView.updateSpecialList(mSpecialList, mSpecialSelectId);
    }

    @Override
    public void showSingleList() {
        if (mSingleList.size() == 0) {
            mSingleList = getSingleList();
        }
        if(mCurrentSingle!=null && mCurrentSingle.id>=0){
            mSingleSelectId = mCurrentSingle.id;
        }
        mView.updateSingleList(mSingleList, mSingleSelectId);
    }

    private List<ModelListBeanItem> getModelList(long specialId,long singleId) {
        List<ModelListBeanItem> list = new ArrayList<>();
        for(int i = 0;i<20;i++){
            ModelListBeanItem item = new ModelListBeanItem();
            item.id = specialId*singleId;
            item.name = "special"+specialId+" single"+singleId;
            list.add(item);
        }
        return list;
    }


    private List<ModelSpecialListItem> getSpecialList() {
        List<ModelSpecialListItem> list = new ArrayList<>();
        for (long i = 0; i < 20; i++) {
            ModelSpecialListItem item = new ModelSpecialListItem();
            item.id = i;
            item.name = "专业 " + i;
            list.add(item);
        }
        return list;
    }

    private List<ModelSingleListItem> getSingleList() {
        List<ModelSingleListItem> list = new ArrayList<>();
        for (long i = 1; i <= 20; i++) {
            ModelSingleListItem item = new ModelSingleListItem();
            item.id = i;
            item.name = i + "栋";
            list.add(item);
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
        mView = null;
        mModel = null;
    }

}
