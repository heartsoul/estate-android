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
import com.glodon.bim.business.qualityManage.contract.CreateCheckListContract;
import com.glodon.bim.business.qualityManage.model.CreateCheckListModel;
import com.glodon.bim.business.qualityManage.view.ChooseModuleActivity;
import com.glodon.bim.common.config.CommonConfig;

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
    public void submit(CreateCheckListParams createCheckListParams) {
        //现场描述
        mInput.description = "测试description";
        //项目名称
        mInput.projectName = SharedPreferencesUtil.getProjectName();
        //新建的时间
        mInput.inspectionDate  = SystemClock.currentThreadTimeMillis()+"";
        //施工单位
        mInput.constructionCompanyId = mCompanyList.get(mCompanySelectPosition).id;
        //是否整改
        mInput.isNeedRectification = true;
        mInput.lastRectificationDate = (SystemClock.currentThreadTimeMillis()+1000000)+"";
        //质检项目
        mInput.inspectionProjectName = mModuleSelectInfo.name;
        mInput.inspectionProjectId = mModuleSelectInfo.id;
        //责任人
        mInput.responsibleUserId = mPersonList.get(mPersonSelectPosition).userId;
        mInput.responsibleUserName = mPersonList.get(mPersonSelectPosition).name;

        Subscription sub = mModel.createSubmit(mProjectId,mInput)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e("---response",e.getMessage());
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        LogUtil.e("---response",responseBody.toString());
                    }
                });
        mSubscritption.add(sub);
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
