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
import android.widget.RelativeLayout;

import com.glodon.bim.R;
import com.glodon.bim.base.BaseActivity;
import com.glodon.bim.basic.utils.ScreenUtil;
import com.glodon.bim.business.main.adapter.ChooseProjectAdapter;
import com.glodon.bim.business.main.bean.ProjectListItem;
import com.glodon.bim.business.main.contract.ChooseProjectContract;
import com.glodon.bim.business.main.presenter.ChooseProjectPresenter;
import com.glodon.bim.customview.pullrefreshview.OnPullRefreshListener;
import com.glodon.bim.customview.pullrefreshview.PullRefreshView;

import java.util.List;

/**
 * 描述：选择项目界面
 * 作者：zhourf on 2017/9/8
 * 邮箱：zhourf@glodon.com
 */
public class ChooseProjectActivity extends BaseActivity implements View.OnClickListener ,ChooseProjectContract.View{

    private View mStatusView;
    private ImageView mBackView,mSearchView;

    private RelativeLayout mContentParent;
    private PullRefreshView mPullRefreshView;
    private RecyclerView mRecyclerView;
    private ImageView mBgView;
    private ChooseProjectAdapter mAdapter;

    private ChooseProjectContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main_choose_project_activity);

        initView();
        initStatusBar(mStatusView);
        setListener();
        initData();
    }

    private void initView() {
        mStatusView = findViewById(R.id.choose_project_header_top);
        mBackView = (ImageView) findViewById(R.id.choose_project_header_back);
        mSearchView = (ImageView) findViewById(R.id.choose_project_header_search);
        mContentParent = (RelativeLayout) findViewById(R.id.choose_project_content);
        mPullRefreshView = (PullRefreshView) findViewById(R.id.choose_project_recyclerview);
        mBgView = (ImageView) findViewById(R.id.choose_project_content_bg);
        initRecyclerView();
    }

    private void initRecyclerView(){
        mPullRefreshView.setPullDownEnable(false);
        mPullRefreshView.setPullUpEnable(false);
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

    private void setListener() {
        mBackView.setOnClickListener(this);
        mSearchView.setOnClickListener(this);
    }

    private void initData() {
        mPresenter = new ChooseProjectPresenter(this);
        mPresenter.initData(getIntent());
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id)
        {
            case R.id.choose_project_header_back:
                mActivity.finish();
                break;
            case R.id.choose_project_header_search:

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
    public void setStyle(int size) {
        if(size<=4){
            mContentParent.setPadding(ScreenUtil.dp2px(38),ScreenUtil.dp2px(11),ScreenUtil.dp2px(38),0);
            mBgView.setVisibility(View.VISIBLE);
        }else{
            mContentParent.setPadding(0,ScreenUtil.dp2px(5),0,0);
            mBgView.setVisibility(View.INVISIBLE);
            mPullRefreshView.setPullUpEnable(true);
        }
        mAdapter = new ChooseProjectAdapter(this,size);
        mAdapter.setListener(mPresenter.getListener());
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void updateData(List<ProjectListItem> mDataList) {
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
