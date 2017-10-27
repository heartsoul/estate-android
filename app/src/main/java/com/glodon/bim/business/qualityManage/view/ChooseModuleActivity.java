package com.glodon.bim.business.qualityManage.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.glodon.bim.R;
import com.glodon.bim.base.BaseActivity;
import com.glodon.bim.basic.listener.ThrottleClickEvents;
import com.glodon.bim.business.qualityManage.adapter.ChooseModuleAdapter;
import com.glodon.bim.business.qualityManage.bean.ModuleListBeanItem;
import com.glodon.bim.business.qualityManage.contract.ChooseModuleContract;
import com.glodon.bim.business.qualityManage.presenter.ChooseModulePresenter;
import com.glodon.bim.customview.pullrefreshview.OnPullRefreshListener;
import com.glodon.bim.customview.pullrefreshview.PullRefreshView;

import java.util.List;

/**
 * 描述：选择质检项目
 * 作者：zhourf on 2017/9/8
 * 邮箱：zhourf@glodon.com
 */
public class ChooseModuleActivity extends BaseActivity implements View.OnClickListener, ChooseModuleContract.View {

    private ChooseModuleContract.Presenter mPresenter;

    private LinearLayout mStatusView;//状态栏
    //导航栏
    private ImageView mNavBack,mNavSearch;

    private PullRefreshView mPullRefreshView;
    private RecyclerView mRecyclerView;

    private ChooseModuleAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quality_choose_module_activity);

        initView();

        setListener();

        initData();

    }

    private void initView() {
        mStatusView = (LinearLayout) findViewById(R.id.choose_module_list_status);
        //导航栏
        mNavBack = (ImageView) findViewById(R.id.choose_module_list_nav_back);
        mNavSearch = (ImageView) findViewById(R.id.choose_module_list_nav_search);
        mPullRefreshView = (PullRefreshView) findViewById(R.id.choose_module_list_recyclerview);
        initStatusBar(mStatusView);
        initRecyclerView();
    }

    private void initRecyclerView(){
        mPullRefreshView.setPullDownEnable(false);
        mPullRefreshView.setPullUpEnable(true);
        mPullRefreshView.setOnPullRefreshListener(new OnPullRefreshListener() {
            @Override
            public void onPullDown() {
                mPresenter.pullDown();
                mPullRefreshView.onPullDownComplete();
            }

            @Override
            public void onPullUp() {
                mPresenter.pullUp();
                mPullRefreshView.onPullUpComplete();
            }
        });
        mRecyclerView = mPullRefreshView.getmRecyclerView();

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setVerticalScrollBarEnabled(true);
        final LinearLayoutManager manager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(manager);

    }


    @Override
    public void initListView(List<ModuleListBeanItem> list, int selectPosition) {

        mAdapter = new ChooseModuleAdapter(mActivity,list,selectPosition);
        mAdapter.setmListener(mPresenter.getmListener());
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void updateListView(List<ModuleListBeanItem> mDataList) {
        mAdapter.updateList(mDataList);
    }

    private void setListener() {
        //导航栏
        ThrottleClickEvents.throttleClick(mNavBack, this);
        ThrottleClickEvents.throttleClick(mNavSearch, this,1);
    }

    private void initData() {
        mPresenter = new ChooseModulePresenter(this);
        mPresenter.initData(getIntent());
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.choose_module_list_nav_back://返回按钮
                mActivity.finish();
                break;
            case R.id.choose_module_list_nav_search://搜索

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
