package com.glodon.bim.test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.glodon.bim.R;
import com.glodon.bim.customview.pullrefreshview.OnPullRefreshListener;
import com.glodon.bim.customview.pullrefreshview.PullRefreshView;

import java.util.ArrayList;
import java.util.List;

public class TestRecyclerViewActivity extends AppCompatActivity {

    private PullRefreshView msv;
    private RecyclerView mRecyclerView;
    private List<String> list;
    private MyAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);
        list = new ArrayList<>();
        for(int i = 0;i<3;i++){
            list.add("item="+i);
        }
        msv = (PullRefreshView) findViewById(R.id.msv);
//        msv.setPullDownEnable(false);
//        msv.setPullUpEnable(false);
        msv.setOnPullRefreshListener(new OnPullRefreshListener() {
            @Override
            public void onPullDown() {
                list.add(0,"pulldown");
                list.add(0,"pulldown");
                list.add(0,"pulldown");
                mAdapter.notifyDataSetChanged();
                msv.onPullDownComplete();
            }

            @Override
            public void onPullUp() {
                list.add("pullup");
                list.add("pullup");
                list.add("pullup");
                mAdapter.notifyDataSetChanged();
                msv.onPullUpComplete();
            }
        });
        mRecyclerView = msv.getmRecyclerView();

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setVerticalScrollBarEnabled(true);
        final LinearLayoutManager manager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(manager);
        mAdapter = new MyAdapter(this,list );
        mRecyclerView.setAdapter(mAdapter);

    }
}
