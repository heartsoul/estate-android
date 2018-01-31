package com.glodon.bim.business.qualityManage.presenter;

import android.app.Activity;
import android.content.Intent;

import com.glodon.bim.basic.log.LogUtil;
import com.glodon.bim.basic.utils.NetWorkUtils;
import com.glodon.bim.basic.utils.SharedPreferencesUtil;
import com.glodon.bim.business.qualityManage.bean.BluePrintModelSearchBean;
import com.glodon.bim.business.qualityManage.bean.BluePrintModelSearchBeanItem;
import com.glodon.bim.business.qualityManage.bean.BlueprintListBeanItem;
import com.glodon.bim.business.qualityManage.bean.ModelListBeanItem;
import com.glodon.bim.business.qualityManage.contract.BluePrintModelSearchContract;
import com.glodon.bim.business.qualityManage.listener.OnBluePrintModelSearchResultClickListener;
import com.glodon.bim.business.qualityManage.model.BluePrintModelSearchModel;
import com.glodon.bim.business.qualityManage.view.RelevantBluePrintActivity;
import com.glodon.bim.business.qualityManage.view.RelevantModelActivity;
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
 * 描述：模型图纸搜索
 * 作者：zhourf on 2017/12/21
 * 邮箱：zhourf@glodon.com
 */

public class BluePrintModelSearchPresenter implements BluePrintModelSearchContract.Presenter{

    private BluePrintModelSearchContract.View mView;
    private BluePrintModelSearchContract.Model mModel;
    private CompositeSubscription mSubscription;
    private int mSearchType = 0;//0图纸  1模型
    private List<BluePrintModelSearchBeanItem> mDataList;
    //图纸相关
    private String mSelectId;//编辑状态选中的图纸
    private String mSelectFileName;//编辑状态选中的图纸
    private String drawingPositionX;//编辑状态  位置的x信息
    private String drawingPositionY;//编辑状态  位置的y信息
    private int blueprintType = 0;//0新建检查单 1检查单编辑状态 2详情查看  3图纸模式
    //模型
    private ModelListBeanItem mModelSelectInfo;//编辑时有过这个item
    private int modelType = 0;//0新建检查单 1检查单编辑状态 2详情查看  3模型模式
    private boolean mIsChangeModel = false;//是否是切换模型

    private OnBluePrintModelSearchResultClickListener mListener = new OnBluePrintModelSearchResultClickListener() {
        @Override
        public void onSelectBluePrint(BluePrintModelSearchBeanItem item) {

            if(mIsChangeModel){
                Intent data = new Intent();
                BlueprintListBeanItem result = new BlueprintListBeanItem();
                result.fileId = item.fileId;
                result.name = item.name;
//                result.buildingId = item.buildingId;
//                result.buildingName = item.buildingName;
                data.putExtra(CommonConfig.CHANGE_BLUEPRINT_RESULT,result);
                mView.getActivity().setResult(Activity.RESULT_OK,data);
                mView.getActivity().finish();
            }else {
                Intent intent = new Intent(mView.getActivity(), RelevantBluePrintActivity.class);

                intent.putExtra(CommonConfig.BLUE_PRINT_FILE_NAME, item.name);
                intent.putExtra(CommonConfig.BLUE_PRINT_FILE_ID, item.fileId);
                switch (blueprintType) {
                    case 0:
                        intent.putExtra(CommonConfig.RELEVANT_TYPE, blueprintType);
                        break;
                    case 1:
                        if (item.fileId.equals(mSelectId)) {
                            intent.putExtra(CommonConfig.BLUE_PRINT_POSITION_X, drawingPositionX);
                            intent.putExtra(CommonConfig.BLUE_PRINT_POSITION_Y, drawingPositionY);
                            intent.putExtra(CommonConfig.RELEVANT_TYPE, 1);
                        } else {
                            intent.putExtra(CommonConfig.RELEVANT_TYPE, 0);
                        }
                        break;
                    case 2:

                        break;
                    case 3:
                        intent.putExtra(CommonConfig.RELEVANT_TYPE, blueprintType);
                        break;
                }

                mView.getActivity().startActivityForResult(intent, RequestCodeConfig.REQUEST_CODE_TO_RELEVANT_BLUEPRINT);
            }
        }

        @Override
        public void onSelectModel(BluePrintModelSearchBeanItem item) {
            if(mIsChangeModel){
                Intent data = new Intent();
                ModelListBeanItem modelInfo = new ModelListBeanItem();
                modelInfo.fileId = item.fileId;
                modelInfo.fileName = item.name;
                modelInfo.buildingId = item.buildingId;
                modelInfo.buildingName = item.buildingName;
                data.putExtra(CommonConfig.CHANGE_MODEL_RESULT,modelInfo);
                mView.getActivity().setResult(Activity.RESULT_OK,data);
                mView.getActivity().finish();
            }else {
                Intent intent = new Intent(mView.getActivity(), RelevantModelActivity.class);
                intent.putExtra(CommonConfig.BLUE_PRINT_FILE_ID, item.fileId);
                intent.putExtra(CommonConfig.BLUE_PRINT_FILE_NAME, item.name);

                ModelListBeanItem modelInfo = new ModelListBeanItem();
                modelInfo.fileId = item.fileId;
                modelInfo.fileName = item.name;
                modelInfo.buildingId = item.buildingId;
                modelInfo.buildingName = item.buildingName;
                switch (modelType) {
                    case 0:

                        intent.putExtra(CommonConfig.RELEVANT_TYPE, modelType);
                        break;
                    case 1:
                        if (item.fileId.equals(mModelSelectInfo.fileId)) {
                            //同一个模型
                            intent.putExtra(CommonConfig.RELEVANT_TYPE, 1);
//                            modelInfo.buildingId = mModelSelectInfo.buildingId;
//                            modelInfo.buildingName = mModelSelectInfo.buildingName;
                            modelInfo.component = mModelSelectInfo.component;
                        } else {
                            //不同的模型
                            intent.putExtra(CommonConfig.RELEVANT_TYPE, 0);
                        }
                        break;
                    case 2:

                        break;
                    case 3:
                        intent.putExtra(CommonConfig.RELEVANT_TYPE, modelType);
                        break;
                }

                intent.putExtra(CommonConfig.MODEL_SELECT_INFO, modelInfo);

                mView.getActivity().startActivityForResult(intent, RequestCodeConfig.REQUEST_CODE_OPEN_MODEL);
            }
        }
    };

    public BluePrintModelSearchPresenter(BluePrintModelSearchContract.View mView) {
        this.mView = mView;
        mModel = new BluePrintModelSearchModel();
        mSubscription = new CompositeSubscription();
        mDataList = new ArrayList<>();
    }

    @Override
    public void initData(Intent intent) {
        mSearchType = intent.getIntExtra(CommonConfig.SEARCH_TYPE,0);
        mSelectId = intent.getStringExtra(CommonConfig.MODULE_LIST_POSITION);
        mSelectFileName = intent.getStringExtra(CommonConfig.MODULE_LIST_NAME);
        drawingPositionX = intent.getStringExtra(CommonConfig.BLUE_PRINT_POSITION_X);
        drawingPositionY = intent.getStringExtra(CommonConfig.BLUE_PRINT_POSITION_Y);
        blueprintType = intent.getIntExtra(CommonConfig.RELEVANT_TYPE, 0);
        mIsChangeModel = intent.getBooleanExtra(CommonConfig.CHANGE_MODEL,false);
        //模型
        mModelSelectInfo = (ModelListBeanItem) intent.getSerializableExtra(CommonConfig.MODEL_SELECT_INFO);
        modelType = intent.getIntExtra(CommonConfig.RELEVANT_TYPE_MODEL,0);
    }

    public OnBluePrintModelSearchResultClickListener getmListener() {
        return mListener;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode)
        {
            case RequestCodeConfig.REQUEST_CODE_TO_RELEVANT_BLUEPRINT://到图纸
                if (resultCode == Activity.RESULT_OK) {
                    mView.getActivity().setResult(Activity.RESULT_OK, data);
                    mView.getActivity().finish();
                }
                break;
            case RequestCodeConfig.REQUEST_CODE_OPEN_MODEL:
                if (resultCode == Activity.RESULT_OK) {
                    mView.getActivity().setResult(Activity.RESULT_OK, data);
                    mView.getActivity().finish();
                }
                break;
        }
    }

    @Override
    public void onDestroy() {
        if(mSubscription!=null){
            mSubscription.unsubscribe();
            mSubscription = null;
        }
    }

    @Override
    public void pullUp() {

    }

    @Override
    public void search(String key) {
        if(NetWorkUtils.isNetworkAvailable(mView.getActivity())) {
            if(mView!=null){
                mView.showLoadingDialog();
            }
            String suffix = mSearchType == 0 ? "dwg" : "rvt";
            long projectId = SharedPreferencesUtil.getProjectId();
            String projectVersionId = SharedPreferencesUtil.getProjectVersionId(projectId);
            LogUtil.e("search,suffix=" + suffix + " key=" + key + " blueprintType=" + mSearchType);
            LogUtil.e("search,projectId=" + projectId + " projectVersionId=" + projectVersionId);
            Subscription sub = mModel.search(key, suffix)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<BluePrintModelSearchBean>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            LogUtil.e(e.getMessage());
                            if(mView!=null){
                                mView.dismissLoadingDialog();
                                mView.hideHistory();
                            }
                        }

                        @Override
                        public void onNext(BluePrintModelSearchBean bean) {
                            LogUtil.e("result="+new GsonBuilder().create().toJson(bean));
                            if(mView!=null){
                                mView.dismissLoadingDialog();
                                mView.hideHistory();
                            }
                            if(bean!=null){
                                mDataList = bean.data;
                                if(mDataList==null){
                                    mDataList = new ArrayList<>();
                                }
                                mView.showResult(mDataList);
                            }
                        }
                    });
            mSubscription.add(sub);
        }else{
            ToastManager.showNetWorkToast();
        }
    }
}
