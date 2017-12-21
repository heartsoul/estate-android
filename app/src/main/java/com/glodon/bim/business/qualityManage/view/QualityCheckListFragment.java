package com.glodon.bim.business.qualityManage.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.glodon.bim.R;
import com.glodon.bim.base.BaseFragment;
import com.glodon.bim.basic.listener.ThrottleClickEvents;
import com.glodon.bim.basic.log.LogUtil;
import com.glodon.bim.business.main.bean.ProjectListItem;
import com.glodon.bim.business.qualityManage.OnClassifyItemClickListener;
import com.glodon.bim.business.qualityManage.adapter.QualityCheckListAdapter;
import com.glodon.bim.business.qualityManage.adapter.QualityCheckListClassifyAdapter;
import com.glodon.bim.business.qualityManage.bean.ClassifyItem;
import com.glodon.bim.business.qualityManage.bean.ClassifyNum;
import com.glodon.bim.business.qualityManage.bean.QualityCheckListBeanItem;
import com.glodon.bim.business.qualityManage.contract.QualityCheckListContract;
import com.glodon.bim.business.qualityManage.presenter.QualityCheckListPresenter;
import com.glodon.bim.business.qualityManage.util.IntentManager;
import com.glodon.bim.common.config.CommonConfig;
import com.glodon.bim.customview.dialog.PhotoAlbumDialog;
import com.glodon.bim.customview.pullrefreshview.OnPullRefreshListener;
import com.glodon.bim.customview.pullrefreshview.PullRefreshView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述：质检清单
 * 作者：zhourf on 2017/9/29
 * 邮箱：zhourf@glodon.com
 */

public class QualityCheckListFragment extends BaseFragment{
    private ProjectListItem mProjectInfo;
    private QualityCheckListView mQualityCheckListView;
    public void setProjectInfo(ProjectListItem info) {
        this.mProjectInfo = info;
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
