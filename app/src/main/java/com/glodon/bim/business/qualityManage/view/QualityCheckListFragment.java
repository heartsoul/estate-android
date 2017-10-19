package com.glodon.bim.business.qualityManage.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.glodon.bim.R;
import com.glodon.bim.base.BaseFragment;
import com.glodon.bim.basic.listener.ThrottleClickEvents;
import com.glodon.bim.business.qualityManage.adapter.QualityCheckListAdapter;
import com.glodon.bim.business.qualityManage.bean.SheetItem;
import com.glodon.bim.customview.pullrefreshview.OnPullRefreshListener;
import com.glodon.bim.customview.pullrefreshview.PullRefreshView;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：质检清单
 * 作者：zhourf on 2017/9/29
 * 邮箱：zhourf@glodon.com
 */

public class QualityCheckListFragment extends BaseFragment {
    private PullRefreshView mPullRefreshView;
    private RecyclerView mRecyclerView;
    private ImageView mToTopView;
    private QualityCheckListAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflate(R.layout.quality_check_list_fragment);
        initView(view);
        return view;
    }


    private void initView(View view) {
        mPullRefreshView =view.findViewById(R.id.quality_check_list_recyclerview);
        mToTopView = view.findViewById(R.id.quality_check_list_to_top);

        setListener();
        initRecyclerView();
    }

    private void setListener() {
        ThrottleClickEvents.throttleClick(mToTopView, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRecyclerView.smoothScrollToPosition(0);
            }
        });
    }

    private void initRecyclerView(){
        mPullRefreshView.setPullDownEnable(false);
        mPullRefreshView.setPullUpEnable(false);
        mPullRefreshView.setOnPullRefreshListener(new OnPullRefreshListener() {
            @Override
            public void onPullDown() {

                mPullRefreshView.onPullDownComplete();
            }

            @Override
            public void onPullUp() {

                mPullRefreshView.onPullUpComplete();
            }
        });
        mRecyclerView = mPullRefreshView.getmRecyclerView();

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setVerticalScrollBarEnabled(true);
        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(manager);
        mAdapter = new QualityCheckListAdapter(getContext(),null);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.updateList(getDataList());
    }

    private List<SheetItem> getDataList(){
        List<SheetItem> list = new ArrayList<>();
        for(int i = 0;i<22;i++){
            SheetItem item = new SheetItem();
            item.showType = i%2;
            if(i>0){
                item.timeType= 1;
            }
            item.sheetStatus = i%7;
            list.add(item);
        }
        return list;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }
}
