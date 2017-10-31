package com.glodon.bim.business.qualityManage.presenter;

import android.app.Activity;
import android.content.Intent;
import android.os.SystemClock;

import com.glodon.bim.basic.log.LogUtil;
import com.glodon.bim.basic.utils.SharedPreferencesUtil;
import com.glodon.bim.business.qualityManage.bean.CompanyItem;
import com.glodon.bim.business.qualityManage.bean.CreateCheckListParams;
import com.glodon.bim.business.qualityManage.bean.ModuleListBeanItem;
import com.glodon.bim.business.qualityManage.bean.PersonItem;
import com.glodon.bim.business.qualityManage.bean.SaveBean;
import com.glodon.bim.business.qualityManage.contract.CreateCheckListContract;
import com.glodon.bim.business.qualityManage.model.CreateCheckListModel;
import com.glodon.bim.business.qualityManage.view.ChooseModuleActivity;
import com.glodon.bim.common.config.CommonConfig;
import com.glodon.bim.customview.ToastManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * 描述：新建检查单
 * 作者：zhourf on 2017/10/26
 * 邮箱：zhourf@glodon.com
 */

public class CreateCheckListPresenter implements CreateCheckListContract.Presenter {
    private static final int REQUEST_CODE_CHOOSE_MODULE = 0;//跳转到选择质检项目
    private CreateCheckListContract.Model mModel;
    private CreateCheckListContract.View mView;
    private CompositeSubscription mSubscritption;

    private long mProjectId;//项目id
    private long mInspectId = -1;//检查单id，新增时没有   编辑时有值
    //施工单位列表
    private List<CompanyItem> mCompanyList;
    private List<String> mCompanyNameList;
    private int mCompanySelectPosition = 0;
    //责任人列表
    private List<PersonItem> mPersonList;
    private List<String> mPersonNameList;
    private int mPersonSelectPosition = -1;
    //质检项目
    private int mModuleSelectPosition = -1;
    private ModuleListBeanItem mModuleSelectInfo;

    //新建检查单参数
    private CreateCheckListParams mInput;

    public CreateCheckListPresenter(CreateCheckListContract.View mView) {
        this.mView = mView;
        mModel = new CreateCheckListModel();
        mSubscritption = new CompositeSubscription();
        mCompanyNameList = new ArrayList<>();
        mPersonNameList = new ArrayList<>();
        mProjectId = SharedPreferencesUtil.getProjectId();
        mInput = new CreateCheckListParams();
        mInput.projectId = mProjectId;
        mInput.code = SystemClock.currentThreadTimeMillis()+"";
    }

    @Override
    public void initData(Intent intent) {

        getCompanyList();
    }

    //获取施工单位列表
    private void getCompanyList(){
        List<String> list = new ArrayList<>();
        list.add("SGDW");
        Subscription sub = mModel.getCompaniesList(mProjectId,list)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<CompanyItem>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e("---",e.getMessage());
                    }

                    @Override
                    public void onNext(List<CompanyItem> companyItems) {

                        mCompanyList = companyItems;
                        mCompanyNameList.clear();
                        if(mCompanyList!=null && mCompanyList.size()>0) {
                            for(CompanyItem item:mCompanyList)
                            {
                                mCompanyNameList.add(item.name);
                            }
                            mView.showCompany(mCompanyList.get(0));
                        }
                    }
                });
        mSubscritption.add(sub);
    }

    //获取责任人列表
    @Override
    public void getPersonList(){
        Subscription sub = mModel.gePersonList(mProjectId,mCompanyList.get(mCompanySelectPosition).coperationId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<PersonItem>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<PersonItem> personItems) {
                        mPersonList = personItems;
                        mPersonNameList.clear();
                        if(mPersonList!=null && mPersonList.size()>0){
                            for(PersonItem item:mPersonList)
                            {
                                mPersonNameList.add(item.name);
                            }
                            if(mView!=null) {
                                mView.showPersonList(mPersonNameList, mPersonSelectPosition);
                            }
                        }
                    }
                });
        mSubscritption.add(sub);
    }

    @Override
    public void setPersonSelectedPosition(int position) {
        this.mPersonSelectPosition = position;
    }


    @Override
    public void submit(CreateCheckListParams params) {
        assembleParams(params);

        if(mInspectId==-1) {
            //新增
            Subscription sub = mModel.createSubmit(mProjectId, mInput)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<SaveBean>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            LogUtil.e("submit---response", e.getMessage());
                        }

                        @Override
                        public void onNext(SaveBean responseBody) {
                            LogUtil.e("submit---response", responseBody.id+"");
                            ToastManager.showSubmitToast();
                            if (mView != null) {
                                mView.getActivity().finish();
                            }
                        }
                    });
            mSubscritption.add(sub);
        }else{
            //编辑
            Subscription sub = mModel.editSubmit(mProjectId,mInspectId, mInput)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<ResponseBody>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            LogUtil.e("edit submit error---response", e.getMessage());
                        }

                        @Override
                        public void onNext(ResponseBody responseBody) {
                            try {
                                LogUtil.e("edit submit---response", responseBody.string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            ToastManager.showSubmitToast();
                            if (mView != null) {
                                mView.getActivity().finish();
                            }
                        }
                    });
            mSubscritption.add(sub);
        }
    }



    @Override
    public void save(CreateCheckListParams params) {
        assembleParams(params);
        if(mInspectId==-1) {
            //新增
            Subscription sub = mModel.createSave(mProjectId, mInput)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<SaveBean>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            LogUtil.e("save error---response", e.getMessage());
                        }

                        @Override
                        public void onNext(SaveBean responseBody) {
                            LogUtil.e("save---response", responseBody.id + "");
                            mInspectId = responseBody.id;
                            ToastManager.showSaveToast();
                            if (mView != null) {
                                mView.showDeleteButton();
                            }
                        }
                    });
            mSubscritption.add(sub);
        }else{
            //编辑
            Subscription sub = mModel.editSave(mProjectId, mInspectId,mInput)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<ResponseBody>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            LogUtil.e("edit save error---response", e.getMessage());
                        }

                        @Override
                        public void onNext(ResponseBody responseBody) {
                            try {
                                LogUtil.e("edit save---response", responseBody.string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
//                            mInspectId = responseBody.id;
                            ToastManager.showSaveToast();
                            if (mView != null) {
                                mView.showDeleteButton();
                            }
                        }
                    });
            mSubscritption.add(sub);
        }
    }

    @Override
    public void deleteCheckList() {
        Subscription sub = mModel.createDelete(mProjectId,mInspectId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e("delete---response",e.getMessage());
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        try {
                            LogUtil.e("delete---response",responseBody.string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if(mView!=null)
                        {
                            mView.getActivity().finish();
                        }
                    }
                });
        mSubscritption.add(sub);
    }

    //组织保存和提交的数据
    private void assembleParams(CreateCheckListParams params){

        //施工单位
        mInput.constructionCompanyId = mCompanyList.get(mCompanySelectPosition).id;
        //责任人
        mInput.responsibleUserId = mPersonList.get(mPersonSelectPosition).userId;
        mInput.responsibleUserName = mPersonList.get(mPersonSelectPosition).name;
        //现场描述
        mInput.description = params.description;
        //图片描述
        mInput.files = params.files;
        //项目名称
        mInput.projectName = SharedPreferencesUtil.getProjectName();
        //新建的时间
        mInput.inspectionDate  = SystemClock.currentThreadTimeMillis()+"";
        //是否整改
        mInput.isNeedRectification = params.isNeedRectification;
        mInput.lastRectificationDate = params.lastRectificationDate;
        //质检项目
        mInput.inspectionProjectName = mModuleSelectInfo.name;
        mInput.inspectionProjectId = mModuleSelectInfo.id;
    }

    @Override
    public void toModuleList() {
        Intent intent = new Intent(mView.getActivity(), ChooseModuleActivity.class);
        intent.putExtra(CommonConfig.MODULE_LIST_POSITION,mModuleSelectPosition);
        mView.getActivity().startActivityForResult(intent,REQUEST_CODE_CHOOSE_MODULE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case REQUEST_CODE_CHOOSE_MODULE:
                if(resultCode == Activity.RESULT_OK && data!=null){
                    mModuleSelectPosition = data.getIntExtra(CommonConfig.MODULE_LIST_POSITION,-1);
                    mModuleSelectInfo = (ModuleListBeanItem) data.getSerializableExtra(CommonConfig.MODULE_LIST_NAME);
                    if(mView!=null && mModuleSelectInfo!=null){
                        mView.showModuleName(mModuleSelectInfo.name);
                    }
                }
                break;
        }
    }

    @Override
    public void onDestroy() {
        if(mSubscritption!=null)
        {
            mSubscritption.unsubscribe();
            mSubscritption = null;
        }
    }

    @Override
    public void showCompanyList() {
        if(mView!=null){
            mView.showCompanyList(mCompanyNameList,mCompanySelectPosition);
        }
    }

    @Override
    public void setCompanySelectedPosition(int position) {
        this.mCompanySelectPosition = position;
    }
}
