package com.glodon.bim.business.main.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.glodon.bim.R;
import com.glodon.bim.base.BaseActivity;
import com.glodon.bim.business.main.adapter.ChooseTenantAdapter;
import com.glodon.bim.business.main.contract.ChooseTenantContract;
import com.glodon.bim.business.main.listener.OnTenantClickListener;
import com.glodon.bim.business.main.presenter.ChooseTenantPresenter;
import com.glodon.bim.common.login.UserTenant;

import java.util.List;

/**
 * 描述：选择租户界面
 * 作者：zhourf on 2017/9/8
 * 邮箱：zhourf@glodon.com
 */
public class ChooseTenantActivity extends BaseActivity implements View.OnClickListener ,ChooseTenantContract.View{

    private View mStatusView;
    private ImageView mBackView,mSearchView;

    private RecyclerView mRecyclerView;
    private ChooseTenantAdapter mAdapter;

    private ChooseTenantContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main_choose_tenant_activity);

        initView();
        initStatusBar(mStatusView);
        setListener();
        initData();
    }

    private void initView() {
        mStatusView = findViewById(R.id.choose_tenant_header_top);
        mBackView = (ImageView) findViewById(R.id.choose_tenant_header_back);
        mSearchView = (ImageView) findViewById(R.id.choose_tenant_header_search);
        mRecyclerView = (RecyclerView) findViewById(R.id.choose_tenant_recyclerview);
        initRecyclerView();
    }

    private void initRecyclerView(){
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setVerticalScrollBarEnabled(true);
        final LinearLayoutManager manager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(manager);
        mAdapter = new ChooseTenantAdapter(this, new OnTenantClickListener() {
            @Override
            public void clickTenant(UserTenant tenant) {
                mPresenter.clickTenant(tenant);
            }
        });
        mRecyclerView.setAdapter(mAdapter);
    }

    private void setListener() {
        mBackView.setOnClickListener(this);
        mSearchView.setOnClickListener(this);
    }

    private void initData() {
        mPresenter = new ChooseTenantPresenter(this);
        mPresenter.initData(getIntent());
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id)
        {
            case R.id.choose_tenant_header_back:
                mActivity.finish();
                break;
            case R.id.choose_tenant_header_search:

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
    public void updateData(List<UserTenant> mDataList) {
        mAdapter.updateData(mDataList);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPresenter.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.onDestroy();
            mPresenter = null;
        }
    }
}
