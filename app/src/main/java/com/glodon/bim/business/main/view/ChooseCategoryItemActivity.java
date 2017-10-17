package com.glodon.bim.business.main.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Window;

import com.glodon.bim.R;
import com.glodon.bim.base.BaseActivity;
import com.glodon.bim.business.main.adapter.CategoryItemAdapter;
import com.glodon.bim.business.main.bean.CategoryItem;
import com.glodon.bim.business.main.contract.ChooseCategoryItemContract;
import com.glodon.bim.business.main.presenter.ChooseCategoryItemPresenter;

import java.util.List;

/**
 * 描述：选择目录的具体事项界面
 * 作者：zhourf on 2017/9/8
 * 邮箱：zhourf@glodon.com
 */
public class ChooseCategoryItemActivity extends BaseActivity implements ChooseCategoryItemContract.View{

    private ChooseCategoryItemContract.Presenter mPresenter;
    private RecyclerView mContentView;
    private CategoryItemAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main_choose_category_item_activity);
        initView();
        setListener();
        initDataForActivity();
    }

    private void initView() {
        mContentView = (RecyclerView) findViewById(R.id.main_choose_category_item_recyclerview);
        initRecyclerView();
    }

    private void initRecyclerView(){
        mContentView.setLayoutManager(new GridLayoutManager(this,3));
        mContentView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new CategoryItemAdapter(this);
        mContentView.setAdapter(mAdapter);
    }

    private void setListener() {

    }

    private void initDataForActivity() {
        mPresenter = new ChooseCategoryItemPresenter(this);
        mPresenter.initData(getIntent());
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
    public void updateData(List<CategoryItem> mDataList) {
        mAdapter.updateData(mDataList);
    }
}
