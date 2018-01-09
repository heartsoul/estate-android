package com.glodon.bim.business.equipment.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.glodon.bim.R;
import com.glodon.bim.base.BaseFragment;
import com.glodon.bim.business.main.bean.ProjectListItem;
import com.glodon.bim.business.qualityManage.listener.OnTitleChangerListener;
import com.glodon.bim.business.qualityManage.view.QualityCheckListView;

/**
 * 描述：材设清单
 * 作者：zhourf on 2017/9/29
 * 邮箱：zhourf@glodon.com
 */

public class EquipmentListFragment extends BaseFragment{
    private QualityCheckListView mQualityCheckListView;
    private ProjectListItem mProjectInfo;
    public void setProjectInfo(ProjectListItem info) {
        this.mProjectInfo = info;
    }
    private String mCurrentTitle = "材设清单";
    private OnTitleChangerListener mTitleListener;

    public void setTitleChangeListener(OnTitleChangerListener listner){
        mTitleListener = listner;
    }
    public void changeTitle(){
        if(mTitleListener!=null)
        {
            mTitleListener.onTitleChange(mCurrentTitle);
        }
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflate(R.layout.quality_check_list_fragment);
        mQualityCheckListView = new QualityCheckListView(getActivity(),view,mProjectInfo);
        mQualityCheckListView.initData();
        return view;
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(mQualityCheckListView!=null) {
            mQualityCheckListView.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(mQualityCheckListView!=null) {
            mQualityCheckListView.onDestroyView();
        }
    }

    @Override
    public void clickSearch(){

    }
}
