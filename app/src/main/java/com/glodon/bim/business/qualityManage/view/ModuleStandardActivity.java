package com.glodon.bim.business.qualityManage.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.glodon.bim.R;
import com.glodon.bim.base.BaseActivity;
import com.glodon.bim.basic.listener.ThrottleClickEvents;
import com.glodon.bim.business.qualityManage.adapter.ModuleStandardAdapter;
import com.glodon.bim.business.qualityManage.bean.ModuleStandardItem;
import com.glodon.bim.business.qualityManage.contract.ModuleStandardContract;
import com.glodon.bim.business.qualityManage.presenter.ModuleStandardPresenter;
import com.glodon.bim.customview.pullrefreshview.PullRefreshView;

import java.util.List;

/**
 * 描述：质检标准
 * 作者：zhourf on 2017/9/8
 * 邮箱：zhourf@glodon.com
 */
public class ModuleStandardActivity extends BaseActivity implements View.OnClickListener, ModuleStandardContract.View {

    private ModuleStandardContract.Presenter mPresenter;

    private LinearLayout mStatusView;//状态栏
    //导航栏
    private RelativeLayout mNavBack;

    private PullRefreshView mPullRefreshView;
    private RecyclerView mRecyclerView;

    private ModuleStandardAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quality_module_standard_activity);

        initView();

        setListener();

        initData();

    }

    private void initView() {
        mStatusView = (LinearLayout) findViewById(R.id.module_standard_status);
        //导航栏
        mNavBack = (RelativeLayout) findViewById(R.id.module_standard_nav_back);
        mPullRefreshView = (PullRefreshView) findViewById(R.id.module_standard_recyclerview);
        initStatusBar(mStatusView);
        initRecyclerView();
    }

    private void initRecyclerView(){
        mPullRefreshView.setPullDownEnable(false);
        mPullRefreshView.setPullUpEnable(false);
        mRecyclerView = mPullRefreshView.getmRecyclerView();

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setVerticalScrollBarEnabled(true);
        final LinearLayoutManager manager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(manager);
        mAdapter = new ModuleStandardAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
    }


    @Override
    public void updateListView(List<ModuleStandardItem> mDataList) {
        mAdapter.updateList(mDataList);

    }

    private void setListener() {
        //导航栏
        ThrottleClickEvents.throttleClick(mNavBack, this);
    }

    private void initData() {
        mPresenter = new ModuleStandardPresenter(this);
        mPresenter.initData(getIntent());
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.choose_module_list_nav_back://返回按钮
                mActivity.finish();
                break;

        }
    }

    @Override
    public void showLoadingDialog() {
        showLoadDialog(true);
    }

    @Override
    public void dismissLoadingDialog() {
        dismissLoadDialog();
    }

    @Override
    public Activity getActivity() {
        return mActivity;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.onDestroy();
            mPresenter = null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mPresenter != null) {
            mPresenter.onActivityResult(requestCode, resultCode, data);
        }
    }


}
