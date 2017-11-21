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
import com.glodon.bim.business.qualityManage.adapter.ModuleCatalogAdapter;
import com.glodon.bim.business.qualityManage.adapter.ModuleContentAdapter;
import com.glodon.bim.business.qualityManage.adapter.ModuleHintAdapter;
import com.glodon.bim.business.qualityManage.bean.ModuleListBeanItem;
import com.glodon.bim.business.qualityManage.contract.ChooseModuleContract;
import com.glodon.bim.business.qualityManage.listener.OnModuleHintClickListener;
import com.glodon.bim.business.qualityManage.presenter.ChooseModulePresenter;

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
    private RelativeLayout mNavBack,mNavSearch;

    //横向目录的view   展示目录的View   切换目录的view
    private RecyclerView mCatalogView,mContentView,mHintView;
    private LinearLayout mHintParent;

    private ModuleContentAdapter mContentAdapter;
    private ModuleCatalogAdapter mCatalogAdapter;
    private ModuleHintAdapter mHintAdapter;

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
        mNavBack = (RelativeLayout) findViewById(R.id.choose_module_list_nav_back);
        mNavSearch = (RelativeLayout) findViewById(R.id.choose_module_list_nav_search);
        mCatalogView = (RecyclerView) findViewById(R.id.catalog_module_catalog_recyclerview);
        mContentView = (RecyclerView) findViewById(R.id.catalog_module_content_recyclerview);
        mHintView = (RecyclerView) findViewById(R.id.catalog_module_hint_recyclerview);
        mHintParent = (LinearLayout) findViewById(R.id.catalog_module_hint);

        initStatusBar(mStatusView);

        mHintParent.setVisibility(View.GONE);
        mCatalogView.setVisibility(View.GONE);
    }

    private void initCatalog(){
        mCatalogView.setItemAnimator(new DefaultItemAnimator());
         LinearLayoutManager manager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        mCatalogView.setLayoutManager(manager);
        mCatalogAdapter = new ModuleCatalogAdapter(this);
        mCatalogAdapter.setListener(mPresenter.getmCataClickListener());
        mCatalogView.setAdapter(mCatalogAdapter);
    }
    private void initContent(){
        mContentView.setItemAnimator(new DefaultItemAnimator());
         LinearLayoutManager manager = new LinearLayoutManager(this);
        mContentView.setLayoutManager(manager);
        mContentAdapter = new ModuleContentAdapter(this);
        mContentAdapter.setListener(mPresenter.getmObjListener(),mPresenter.getmCataListener());
        mContentView.setAdapter(mContentAdapter);
    }
    private void initHint(){
        mHintView.setItemAnimator(new DefaultItemAnimator());
         LinearLayoutManager manager = new LinearLayoutManager(this);
        mHintView.setLayoutManager(manager);
        mHintAdapter = new ModuleHintAdapter(this);
        mHintAdapter.setmListener(new OnModuleHintClickListener() {
            @Override
            public void onSelect(ModuleListBeanItem item) {
                mHintParent.setVisibility(View.GONE);
                mPresenter.getmHintClickListener().onSelect(item);
            }
        });
        mHintView.setAdapter(mHintAdapter);
    }


    @Override
    public void updateContentListView(List<ModuleListBeanItem> list, Long selectId) {
        mContentAdapter.updateList(list,selectId);
    }

    @Override
    public void updateCataListView(List<ModuleListBeanItem> mCatalogList) {
        mCatalogView.setVisibility(View.VISIBLE);
        mCatalogAdapter.updateList(mCatalogList);
    }

    @Override
    public void updateHintListView(List<ModuleListBeanItem> mHintList, ModuleListBeanItem item) {
        mHintParent.setVisibility(View.VISIBLE);
        mHintAdapter.updateList(mHintList,item.id.longValue());
    }

    @Override
    public void closeHint() {
        mHintParent.setVisibility(View.GONE);
    }

    private void setListener() {
        //导航栏
        ThrottleClickEvents.throttleClick(mNavBack, this);
        ThrottleClickEvents.throttleClick(mNavSearch, this,1);
    }

    private void initData() {
        mPresenter = new ChooseModulePresenter(this);
        initCatalog();
        initContent();
        initHint();
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
