package com.glodon.bim.business.qualityManage.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.glodon.bim.R;
import com.glodon.bim.base.BaseActivity;
import com.glodon.bim.basic.listener.ThrottleClickEvents;
import com.glodon.bim.business.qualityManage.util.IntentManager;

/**
 * 模型
 */
public class ModelActivity extends BaseActivity implements View.OnClickListener{


    //导航栏
    private RelativeLayout mNavBack, mNavSearch;
    private View mParent;
    private ModelView mModelView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quality_model_activity);

        initView();

        setListener();

        initData();

    }

    private void initView() {
        LinearLayout mStatusView = (LinearLayout) findViewById(R.id.model_list_status);//状态栏
        //导航栏
        mNavBack = (RelativeLayout) findViewById(R.id.model_list_nav_back);
        mNavSearch = (RelativeLayout) findViewById(R.id.model_list_nav_search);

        mParent = findViewById(R.id.model_content_parent);

        initStatusBar(mStatusView);

    }



    private void setListener() {
        //导航栏
        ThrottleClickEvents.throttleClick(mNavBack, this);
        ThrottleClickEvents.throttleClick(mNavSearch, this, 1);
    }

    private void initData() {
        mModelView = new ModelView(this,mParent);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.model_list_nav_back://返回按钮
                mActivity.finish();
                break;
            case R.id.model_list_nav_search://搜索
                mModelView.toSearch();
                break;


        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mModelView != null) {
            mModelView.onDestroy();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mModelView != null) {
            mModelView.onActivityResult(requestCode, resultCode, data);
        }
    }


}
