package com.glodon.bim.business.qualityManage.presenter;

import android.app.Activity;
import android.content.Intent;

import com.glodon.bim.basic.log.LogUtil;
import com.glodon.bim.basic.utils.SharedPreferencesUtil;
import com.glodon.bim.business.qualityManage.bean.ModelListBean;
import com.glodon.bim.business.qualityManage.bean.ModelListBeanItem;
import com.glodon.bim.business.qualityManage.bean.ModelSingleListItem;
import com.glodon.bim.business.qualityManage.bean.ModelSpecialListItem;
import com.glodon.bim.business.qualityManage.bean.ProjectVersionBean;
import com.glodon.bim.business.qualityManage.contract.ModelContract;
import com.glodon.bim.business.qualityManage.model.ModelModel;
import com.glodon.bim.business.qualityManage.model.OnModelSelectListener;
import com.glodon.bim.business.qualityManage.view.RelevantModelActivity;
import com.glodon.bim.common.config.CommonConfig;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * 描述：模型
 * 作者：zhourf on 2017/11/22
 * 邮箱：zhourf@glodon.com
 */

public class ModelPresenter implements ModelContract.Presenter {
    private static final int REQUEST_CODE_OPEN_MODEL = 0;
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

    private long projectId;

    private ProjectVersionBean mLatestVersionInfo;//最新版本信息

    private ModelListBeanItem mModelSelectInfo;//编辑时有过这个item
    private int type = 0;//0新建检查单 1检查单编辑状态 2详情查看  3模型模式
    private boolean mIsFragment = false;
    private OnModelSelectListener mListener = new OnModelSelectListener() {
        @Override
        public void selectModel(ModelListBeanItem item) {
            if(mView!=null) {
                Intent intent = new Intent(mView.getActivity(), RelevantModelActivity.class);
                intent.putExtra(CommonConfig.BLUE_PRINT_FILE_ID,item.fileId);
                intent.putExtra(CommonConfig.BLUE_PRINT_FILE_NAME,item.fileName);
                if(mModelSelectInfo==null){
                    mModelSelectInfo = new ModelListBeanItem();
                    if(mIsFragment){
                        type = 3;
                    }else {
                        type = 0;
                    }
                }else{
                    if(item.fileId.equals(mModelSelectInfo.fileId)){
                        //同一个模型
                        type = 1;
                    }else{
                        //不同的模型
                        type = 0;
                    }
                }
                intent.putExtra(CommonConfig.RELEVANT_TYPE, type);
                mModelSelectInfo.fileId = item.fileId;
                mModelSelectInfo.fileName = item.fileName;
                if(mCurrentSingle!=null) {
                    mModelSelectInfo.buildingId = mCurrentSingle.id;
                    mModelSelectInfo.buildingName = mCurrentSingle.name;
                }
                intent.putExtra(CommonConfig.MODEL_SELECT_INFO, mModelSelectInfo);

                mView.getActivity().startActivityForResult(intent,REQUEST_CODE_OPEN_MODEL);
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
//            mModelList = getModelList(mSpecialSelectId,mSingleSelectId);
//            if(mView!=null) {
//                mView.updateModelList(mModelList);
//            }
            getModelData();
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
//            mModelList = getModelList(mSpecialSelectId,mSingleSelectId);
//            if(mView!=null) {
//                mView.updateModelList(mModelList);
//            }
            getModelData();
        }
    };

    public OnModelSelectListener getListener()
    {
        return mListener;
    }

    @Override
    public void setIsFragment() {
        mIsFragment = true;
    }

    public ModelPresenter(ModelContract.View mView) {
        this.mView = mView;
        mSubscription = new CompositeSubscription();
        mModel = new ModelModel();
        mModelList = new ArrayList<>();
        mSpecialList = new ArrayList<>();
        mSingleList = new ArrayList<>();
        projectId = SharedPreferencesUtil.getProjectId();
        mCurrentSingle = new ModelSingleListItem();
        mCurrentSpecial = new ModelSpecialListItem();
    }

    @Override
    public void initData(Intent intent) {
        mModelSelectInfo = (ModelListBeanItem) intent.getSerializableExtra(CommonConfig.MODEL_SELECT_INFO);
        type = intent.getIntExtra(CommonConfig.RELEVANT_TYPE,0);
        //编辑状态直接进入预览
        if(type==1){
            toModelPreview();
        }
        getLatestVersion();
        getSpecialData();
        getSingleData();
    }

    private void toModelPreview(){
        if(mModelSelectInfo!=null) {
            Intent intent = new Intent(mView.getActivity(), RelevantModelActivity.class);
            intent.putExtra(CommonConfig.BLUE_PRINT_FILE_ID, mModelSelectInfo.fileId);
            intent.putExtra(CommonConfig.BLUE_PRINT_FILE_NAME, mModelSelectInfo.fileName);
            intent.putExtra(CommonConfig.RELEVANT_TYPE, type);
            intent.putExtra(CommonConfig.MODEL_SELECT_INFO, mModelSelectInfo);
            mView.getActivity().startActivityForResult(intent, REQUEST_CODE_OPEN_MODEL);
        }
    }

    //获取最新版本
    private void getLatestVersion(){
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
                        mLatestVersionInfo = projectVersionBean;
                    }
                });
        mSubscription.add(sub);
    }

    //专业列表
    private void getSpecialData(){
        Subscription sub = mModel.getSpecialList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<ModelSpecialListItem>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(e.getMessage());
                    }

                    @Override
                    public void onNext(List<ModelSpecialListItem> list) {
                        mSpecialList = list;
                    }
                });
        mSubscription.add(sub);
    }

    //单体列表
    private void getSingleData(){
        Subscription sub = mModel.getSingleList(projectId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<ModelSingleListItem>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(e.getMessage());
                    }

                    @Override
                    public void onNext(List<ModelSingleListItem> list) {
                        mSingleList = list;
                    }
                });
        mSubscription.add(sub);
    }

    //模型列表
    private void getModelData(){
        Subscription sub = mModel.getModelList(projectId,mLatestVersionInfo.data.versionId,mCurrentSingle.id,mCurrentSpecial.code)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ModelListBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(e.getMessage());
                    }

                    @Override
                    public void onNext(ModelListBean bean) {
                        mModelList.clear();
                        LogUtil.e("bean==null?"+(bean==null));
                        if(bean!=null){
                            LogUtil.e("bean.tostring="+bean.toString());
                            if(bean.data!=null) {
                                LogUtil.e("bean.size=" + bean.data.size());
                            }
                            if(bean.data!=null && bean.data.size()>0){
                                mModelList = bean.data;
                            }
                        }
                        if(mView!=null){
                            mView.updateModelList(mModelList);
                        }
                    }
                });
        mSubscription.add(sub);
    }

    @Override
    public void showSpecialList() {
        if(mCurrentSpecial!=null && mCurrentSpecial.id>=0){
            mSpecialSelectId = mCurrentSpecial.id;
        }
        mView.updateSpecialList(mSpecialList, mSpecialSelectId);
    }

    @Override
    public void showSingleList() {
        if(mCurrentSingle!=null && mCurrentSingle.id>=0){
            mSingleSelectId = mCurrentSingle.id;
        }
        mView.updateSingleList(mSingleList, mSingleSelectId);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case REQUEST_CODE_OPEN_MODEL:
                if(resultCode == Activity.RESULT_OK && mView!=null) {
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
        mView = null;
        mModel = null;
    }

}
