package com.glodon.bim.business.qualityManage.view;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.glodon.bim.R;
import com.glodon.bim.base.BaseActivity;
import com.glodon.bim.customview.pullrefreshview.OnPullRefreshListener;
import com.glodon.bim.customview.pullrefreshview.PullRefreshView;

/**
 * Created by cwj on 2018/3/9.
 * Description:搜索页
 */

public abstract class BaseSearchActivity extends BaseActivity {

    private View mStatusView;
    private TextView mCancelView;
    private EditText mInputView;
    private PullRefreshView mPullRefreshView;
    private RecyclerView mContentRecyclerView, mHistoryRecyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_search);

    }

    private void initView() {
        mStatusView = findViewById(R.id.equipment_search_statusview);
        mCancelView = (TextView) findViewById(R.id.equipment_search_cancel);
        mInputView = (EditText) findViewById(R.id.equipment_search_input);

        mHistoryRecyclerView = (RecyclerView) findViewById(R.id.equipment_search_history);
        mPullRefreshView = (PullRefreshView) findViewById(R.id.equipment_search_pull_refresh_view);

        initPullRefreshView();
        initRecyclerView(mHistoryRecyclerView);
        initRecyclerView(mContentRecyclerView);

        mInputView.clearFocus();
    }

    private void initPullRefreshView() {
        mPullRefreshView.setPullDownEnable(true);
        mPullRefreshView.setPullUpEnable(true);
        mPullRefreshView.setOnPullRefreshListener(new OnPullRefreshListener() {
            @Override
            public void onPullDown() {
//                mPresenter.pullDown();
                mPullRefreshView.onPullDownComplete();
            }

            @Override
            public void onPullUp() {
//                mPresenter.pullUp();
                mPullRefreshView.onPullUpComplete();
            }
        });

        mContentRecyclerView = mPullRefreshView.getmRecyclerView();
    }

    private void initRecyclerView(RecyclerView recyclerView) {
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setVerticalScrollBarEnabled(true);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
    }

    abstract void pullDown();

    abstract void pullUp();

}
