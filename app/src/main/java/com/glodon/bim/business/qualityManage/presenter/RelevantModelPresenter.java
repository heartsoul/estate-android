package com.glodon.bim.business.qualityManage.presenter;

import android.content.Intent;
import android.os.SystemClock;
import android.text.TextUtils;

import com.glodon.bim.basic.config.AppConfig;
import com.glodon.bim.basic.log.LogUtil;
import com.glodon.bim.basic.network.NetRequest;
import com.glodon.bim.basic.utils.NetWorkUtils;
import com.glodon.bim.basic.utils.SharedPreferencesUtil;
import com.glodon.bim.business.greendao.provider.DaoProvider;
import com.glodon.bim.business.qualityManage.bean.ModelComponentWorldPosition;
import com.glodon.bim.business.qualityManage.bean.ModelElementHistory;
import com.glodon.bim.business.qualityManage.bean.ModelElementInfo;
import com.glodon.bim.business.qualityManage.bean.ProjectVersionBean;
import com.glodon.bim.business.qualityManage.bean.ProjectVersionData;
import com.glodon.bim.business.qualityManage.bean.RelevantBluePrintToken;
import com.glodon.bim.business.qualityManage.contract.RelevantBluePrintContract;
import com.glodon.bim.business.qualityManage.contract.RelevantModelContract;
import com.glodon.bim.business.qualityManage.model.RelevantBluePrintModel;
import com.glodon.bim.business.qualityManage.model.RelevantModelApi;
import com.glodon.bim.business.qualityManage.model.RelevantModelModel;
import com.glodon.bim.common.config.CommonConfig;
import com.glodon.bim.customview.ToastManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * 描述：具体模型展示
 * 作者：zhourf on 2017/12/11
 * 邮箱：zhourf@glodon.com
 */

public class RelevantModelPresenter implements RelevantModelContract.Presenter {
    private RelevantModelContract.View mView;
    private RelevantModelContract.Model mModel;
    private CompositeSubscription mSubscription;
    private long mProjectId;
    private ProjectVersionData mPersionData;
    private String mProjectVersionId;
    private String mFileId;
    private List<ModelElementHistory> mElementHistories;

    public RelevantModelPresenter(RelevantModelContract.View mView) {
        this.mView = mView;
        mModel = new RelevantModelModel();
        mSubscription = new CompositeSubscription();
        mPositionMap = new LinkedHashMap<>();
    }

    @Override
    public void initData(Intent intent) {
        mProjectId = SharedPreferencesUtil.getProjectId();
        mFileId = intent.getStringExtra(CommonConfig.BLUE_PRINT_FILE_ID);
        getLatestVersion();
    }

    private void getLatestVersion() {
        if (NetWorkUtils.isNetworkAvailable(mView.getActivity())) {
            if (mView != null) {
                mView.showLoadingDialog();
            }
            Subscription sub = mModel.getLatestVersion(SharedPreferencesUtil.getProjectId())
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
                            if (projectVersionBean != null && projectVersionBean.data != null) {
                                mProjectVersionId = projectVersionBean.data.versionId;
                                getTokey(mProjectId, mProjectVersionId, mFileId);
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

    private void getTokey(long mProjectId, String mProjectVersionId, String mFileId) {
        LogUtil.e("projectId="+mProjectId);
        LogUtil.e("mProjectVersionId="+mProjectVersionId);
        LogUtil.e("mFileId="+mFileId);
        Subscription sub = mModel.getToken(mProjectId, mProjectVersionId, mFileId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<RelevantBluePrintToken>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(e.getMessage());
                    }

                    @Override
                    public void onNext(RelevantBluePrintToken bean) {
                        if(bean!=null){
                            LogUtil.e("info="+bean.toString());
                        }
                        if(bean!=null && !TextUtils.isEmpty(bean.data)){

                            if(mView!=null){
                                mView.sendBasicInfo(bean.data);
                            }
                        }
                    }
                });
        mSubscription.add(sub);
//        NetRequest.getInstance().getCall(AppConfig.BASE_URL,RelevantModelApi.class).getToken2(mProjectId,mProjectVersionId,mFileId,new DaoProvider().getCookie())
//                .enqueue(new Callback<ResponseBody>() {
//                    @Override
//                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                        if(response.body()!=null)
//                        {
//                            try {
//                                LogUtil.e("body="+response.body().string());
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                        }
//
//                        if(response.errorBody()!=null)
//                        {
//                            try {
//                                LogUtil.e("errorbody="+response.errorBody().string());
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<ResponseBody> call, Throwable t) {
//
//                    }
//                });
    }

    public void getElements(){
        Subscription sub = mModel.getElements(mProjectId,mFileId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<ModelElementHistory>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(e.getMessage());
                    }

                    @Override
                    public void onNext(List<ModelElementHistory> modelElementHistories) {
                        LogUtil.e("size="+(modelElementHistories==null));
                        if(modelElementHistories!=null){
                            LogUtil.e("size="+modelElementHistories.size());
                        }
                        if(modelElementHistories!=null && modelElementHistories.size()>0){
                            mElementHistories = modelElementHistories;
                            for(ModelElementHistory element:modelElementHistories)
                            {
                                getElementName(element,element.gdocFileId,element.elementId);
                            }
                        }
                    }
                });
        mSubscription.add(sub);
    }

    private LinkedHashMap<String,ModelElementHistory> mPositionMap;
    //获取构件名称
    private void getElementName(final ModelElementHistory element, String fileId, final String elementId) {
        Subscription sub = mModel.getElementProperty(mProjectId, SharedPreferencesUtil.getString(mProjectId + "", ""), fileId, elementId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ModelElementInfo>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(e.getMessage());
                    }

                    @Override
                    public void onNext(ModelElementInfo modelElementInfo) {
                        if (modelElementInfo != null && modelElementInfo.data != null && modelElementInfo.data.boundingBox!=null) {
                            ModelComponentWorldPosition min = modelElementInfo.data.boundingBox.min;
                            ModelComponentWorldPosition max = modelElementInfo.data.boundingBox.max;
                            if(max!=null && min!=null) {
                                element.drawingPositionX = (max.x + min.x) / 2;
                                element.drawingPositionY = (max.y + min.y) / 2;
                                element.drawingPositionZ = max.z > min.z ? max.z : min.z;
                                mPositionMap.put(elementId, element);
                                if (mPositionMap.size() == mElementHistories.size()) {
                                    List<ModelElementHistory> list = new ArrayList<>();
                                    for(Map.Entry<String,ModelElementHistory> entry:mPositionMap.entrySet()){
                                        list.add(entry.getValue());
                                    }
                                    if(mView!=null)
                                    {
                                        mView.showModelHistory(list);
                                    }
                                }
                            }
                        }
                    }
                });
        mSubscription.add(sub);
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
    }
}
