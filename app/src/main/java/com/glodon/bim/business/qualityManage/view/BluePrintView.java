package com.glodon.bim.business.qualityManage.view;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.glodon.bim.R;
import com.glodon.bim.business.qualityManage.adapter.BluePrintCatalogAdapter;
import com.glodon.bim.business.qualityManage.adapter.BluePrintContentAdapter;
import com.glodon.bim.business.qualityManage.adapter.BluePrintHintAdapter;
import com.glodon.bim.business.qualityManage.bean.BlueprintListBeanItem;
import com.glodon.bim.business.qualityManage.contract.BluePrintContract;
import com.glodon.bim.business.qualityManage.listener.OnBlueprintHintClickListener;
import com.glodon.bim.business.qualityManage.presenter.BluePrintPresenter;
import com.glodon.bim.customview.dialog.LoadingDialogManager;

import java.util.List;

/**
 * 描述：图纸目录
 * 作者：zhourf on 2017/9/8
 * 邮箱：zhourf@glodon.com
 */
public class BluePrintView implements BluePrintContract.View {

    private BluePrintContract.Presenter mPresenter;


    //横向目录的view   展示目录的View   切换目录的view
    private RecyclerView mCatalogView, mContentView, mHintView;
    private LinearLayout mHintParent;

    private BluePrintContentAdapter mContentAdapter;
    private BluePrintCatalogAdapter mCatalogAdapter;
    private BluePrintHintAdapter mHintAdapter;

    private Activity mActivity;
    private View mParent;

    private LoadingDialogManager mLoadingDialog;

    public BluePrintView(Activity mActivity, View mParent) {
        this.mActivity = mActivity;
        this.mParent = mParent;

        initView();

        setListener();

        initData();
    }


    public void setIsFragment(){
        mPresenter.setIsFragment();
    }

    private void initView() {
        mCatalogView = mParent.findViewById(R.id.catalog_module_catalog_recyclerview);
        mContentView = mParent.findViewById(R.id.catalog_module_content_recyclerview);
        mHintView = mParent.findViewById(R.id.catalog_module_hint_recyclerview);
        mHintParent = mParent.findViewById(R.id.catalog_module_hint);

        mHintParent.setVisibility(View.GONE);
        mCatalogView.setVisibility(View.GONE);
    }

    private void initCatalog() {
        mCatalogView.setItemAnimator(new DefaultItemAnimator());
        LinearLayoutManager manager = new LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false);
        mCatalogView.setLayoutManager(manager);
        mCatalogAdapter = new BluePrintCatalogAdapter(mActivity);
        mCatalogAdapter.setListener(mPresenter.getmCataClickListener());
        mCatalogView.setAdapter(mCatalogAdapter);
    }

    private void initContent() {
        mContentView.setItemAnimator(new DefaultItemAnimator());
        LinearLayoutManager manager = new LinearLayoutManager(mActivity);
        mContentView.setLayoutManager(manager);
        mContentAdapter = new BluePrintContentAdapter(mActivity);
        mContentAdapter.setListener(mPresenter.getmObjListener(), mPresenter.getmCataListener());
        mContentView.setAdapter(mContentAdapter);
    }

    private void initHint() {
        mHintView.setItemAnimator(new DefaultItemAnimator());
        LinearLayoutManager manager = new LinearLayoutManager(mActivity);
        mHintView.setLayoutManager(manager);
        mHintAdapter = new BluePrintHintAdapter(mActivity);
        mHintAdapter.setmListener(new OnBlueprintHintClickListener() {
            @Override
            public void onSelect(BlueprintListBeanItem item) {
                mHintParent.setVisibility(View.GONE);
                mPresenter.getmHintClickListener().onSelect(item);
            }
        });
        mHintView.setAdapter(mHintAdapter);
    }


    @Override
    public void updateContentListView(List<BlueprintListBeanItem> list, String selectId) {
        mContentAdapter.updateList(list, selectId);
    }

    @Override
    public void updateCataListView(List<BlueprintListBeanItem> mCatalogList) {
        mCatalogView.setVisibility(View.VISIBLE);
        mCatalogAdapter.updateList(mCatalogList);
    }

    @Override
    public void updateHintListView(List<BlueprintListBeanItem> mHintList, BlueprintListBeanItem item) {
        mHintParent.setVisibility(View.VISIBLE);
        mHintAdapter.updateList(mHintList, item.fileId);
    }

    @Override
    public void closeHint() {
        mHintParent.setVisibility(View.GONE);
    }

    private void setListener() {
    }

    private void initData() {
        mPresenter = new BluePrintPresenter(this);
        initCatalog();
        initContent();
        initHint();
        mPresenter.initData(mActivity.getIntent());
    }


    @Override
    public void showLoadingDialog() {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialogManager(getActivity());
        }
        mLoadingDialog.show();
    }

    @Override
    public void dismissLoadingDialog() {
        mLoadingDialog.dismiss();
    }

    @Override
    public Activity getActivity() {
        return mActivity;
    }

    public void onDestroy() {
        if (mPresenter != null) {
            mPresenter.onDestroy();
            mPresenter = null;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mPresenter != null) {
            mPresenter.onActivityResult(requestCode, resultCode, data);
        }
    }

    //调整到搜索
    public void toSearch() {
        mPresenter.toSearch();
    }
}
