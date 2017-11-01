package com.glodon.bim.customview.album;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.glodon.bim.R;
import com.glodon.bim.base.BaseActivity;
import com.glodon.bim.basic.listener.ThrottleClickEvents;
import com.glodon.bim.business.qualityManage.view.CreateCheckListActivity;
import com.glodon.bim.common.config.CommonConfig;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * 描述：相册选择
 * 作者：zhourf on 2017/9/8
 * 邮箱：zhourf@glodon.com
 */
public class AlbumEditActivity extends BaseActivity implements View.OnClickListener {

    private TextView mNavCancel,mNavFinish;
    private View mStatusView;
    private RecyclerView mRecyclerView;
    private TextView mPreviewView;
    private AlbumEditAdapter mAdapter;
    private List<TNBImageItem> mDataList;
    private int fromType = 0;//0 从选择目录页跳转 或 从质检清单页跳转   1 从检查单页跳转

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quality_album_edit_activity);

        initView();

        setListener();

        initData();

    }

    private void initData() {
        fromType = getIntent().getIntExtra(CommonConfig.ALBUM_FROM_TYPE,0);
        new AsyncTask<Void, Void, List<TNBImageItem>>() {
            @Override
            protected List<TNBImageItem> doInBackground(Void... voids) {
                return new TNBAlbumUtils(mActivity).getImageList();
            }

            @Override
            protected void onPostExecute(List<TNBImageItem> list) {
                super.onPostExecute(list);
                mDataList = list;
                mAdapter.upateData(mDataList);
            }
        }.execute();
    }


    private void initView() {
        mNavCancel = (TextView) findViewById(R.id.album_edit_nav_cancel);
        mNavFinish = (TextView) findViewById(R.id.album_edit_nav_finish);
        mStatusView = findViewById(R.id.album_edit_status_view);
        mRecyclerView = (RecyclerView) findViewById(R.id.album_edit_recyclerview);
        mPreviewView = (TextView) findViewById(R.id.album_edit_preview);

        initStatusBar(mStatusView);
        initRecyclerView();
    }

    private void initRecyclerView(){
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setVerticalScrollBarEnabled(true);
        GridLayoutManager manager = new GridLayoutManager(this,4);
        mRecyclerView.setLayoutManager(manager);
        mAdapter = new AlbumEditAdapter(this, new OnAlbumChangeListener() {
            @Override
            public void onChange(LinkedHashMap<String, TNBImageItem> map) {
                if(map.size()==0){
                    mNavFinish.setText("完成");
                }else{
                    mNavFinish.setText("完成("+map.size()+")");
                }
            }
        });
        //设置选中后的数量
        AlbumData data = (AlbumData) getIntent().getSerializableExtra(CommonConfig.ALBUM_DATA);
        if(data!=null) {
            LinkedHashMap<String, TNBImageItem> map = data.map;
            if(map.size()==0){
                mNavFinish.setText("完成");
            }else{
                mNavFinish.setText("完成("+map.size()+")");
            }
        }
        mAdapter.setSelectedMap(data);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void setListener() {
        ThrottleClickEvents.throttleClick(mNavCancel,this);
        ThrottleClickEvents.throttleClick(mNavFinish,this);
        ThrottleClickEvents.throttleClick(mPreviewView,this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.album_edit_nav_cancel:
                finish();
                break;
            case R.id.album_edit_nav_finish:
                if(fromType == 0){
                    Intent intent = new Intent(mActivity, CreateCheckListActivity.class);
                    intent.putExtra(CommonConfig.ALBUM_DATA,mAdapter.getSelectedImages());
                    startActivity(intent);
                    finish();
                }
                if(fromType == 1){
                    Intent data = new Intent();
                    data.putExtra(CommonConfig.ALBUM_DATA,mAdapter.getSelectedImages());
                    setResult(RESULT_OK,data);
                    finish();
                }

                break;
            case R.id.album_edit_preview:

                break;
        }
    }
}
