package com.glodon.bim.business.qualityManage.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.glodon.bim.R;
import com.glodon.bim.base.BaseActivity;
import com.glodon.bim.basic.listener.ThrottleClickEvents;

/**
 * 描述：图纸目录
 * 作者：zhourf on 2017/9/8
 * 邮箱：zhourf@glodon.com
 */
public class BluePrintActivity extends BaseActivity implements View.OnClickListener{

    private LinearLayout mStatusView;//状态栏
    //导航栏
    private RelativeLayout mNavBack, mNavSearch;

    private BluePrintView mBluePrintView;
    private View mParent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quality_blue_print_activity);

        initView();

        setListener();

        initData();

    }

    private void initView() {
        mStatusView = (LinearLayout) findViewById(R.id.blue_print_list_status);
        //导航栏
        mNavBack = (RelativeLayout) findViewById(R.id.blue_print_list_nav_back);
        mNavSearch = (RelativeLayout) findViewById(R.id.blue_print_list_nav_search);
        mParent = findViewById(R.id.blue_print_list_content_view);

        initStatusBar(mStatusView);
    }



    private void setListener() {
        //导航栏
        ThrottleClickEvents.throttleClick(mNavBack, this);
        ThrottleClickEvents.throttleClick(mNavSearch, this, 1);
    }

    private void initData() {
        mBluePrintView = new BluePrintView(this,mParent);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.blue_print_list_nav_back://返回按钮
                mActivity.finish();
                break;
            case R.id.blue_print_list_nav_search://搜索
                mBluePrintView.toSearch();
                break;

        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mBluePrintView!=null){
            mBluePrintView.onDestroy();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(mBluePrintView!=null){
            mBluePrintView.onActivityResult(requestCode, resultCode, data);
        }
    }

}
