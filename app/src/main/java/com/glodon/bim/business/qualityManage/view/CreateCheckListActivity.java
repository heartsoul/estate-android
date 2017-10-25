package com.glodon.bim.business.qualityManage.view;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.glodon.bim.R;
import com.glodon.bim.base.BaseActivity;
import com.glodon.bim.basic.listener.ThrottleClickEvents;
import com.glodon.bim.common.config.CommonConfig;

public class CreateCheckListActivity extends BaseActivity implements View.OnClickListener {

    private String mImagePath;//前面传递过来的图片路径
    private LinearLayout mStatusView;//状态栏
    //导航栏
    private ImageView mNavBack;
    private TextView mNavSubmit,mNavCheckLeftTitle,mNavCheckRightTitle;
    private View mNavLeftLine,mNavRightLine;
    //内容
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quality_create_check_list_activity);

        initView();

        setListener();

        initData();

    }

    private void initView() {
        mStatusView = (LinearLayout) findViewById(R.id.create_check_list_status);
        //导航栏
        mNavBack = (ImageView) findViewById(R.id.create_check_list_nav_back);
        mNavSubmit = (TextView) findViewById(R.id.create_check_list_nav_submit);
        mNavCheckLeftTitle = (TextView) findViewById(R.id.create_check_list_nav_check_left_title);
        mNavCheckRightTitle = (TextView) findViewById(R.id.create_check_list_nav_check_right_title);
        mNavLeftLine = findViewById(R.id.create_check_list_nav_check_left_line);
        mNavRightLine = findViewById(R.id.create_check_list_nav_check_right_line);

    }

    private void setListener() {
        ThrottleClickEvents.throttleClick(mNavBack,this);
        ThrottleClickEvents.throttleClick(mNavSubmit,this);
        ThrottleClickEvents.throttleClick(mNavCheckLeftTitle,this,1);
        ThrottleClickEvents.throttleClick(mNavCheckRightTitle,this,1);
    }

    private void initData() {
        mImagePath = getIntent().getStringExtra(CommonConfig.IAMGE_SAVE_PATH);
        initStatusBar(mStatusView);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id)
        {
            case R.id.create_check_list_nav_back://返回按钮
                mActivity.finish();
                break;
            case R.id.create_check_list_nav_check_left_title://检查单
                mNavLeftLine.setVisibility(View.VISIBLE);
                mNavCheckLeftTitle.setTextSize(17);
                mNavRightLine.setVisibility(View.INVISIBLE);
                mNavCheckRightTitle.setTextSize(15);
                break;
            case R.id.create_check_list_nav_check_right_title://验收单
                mNavLeftLine.setVisibility(View.INVISIBLE);
                mNavCheckLeftTitle.setTextSize(15);
                mNavRightLine.setVisibility(View.VISIBLE);
                mNavCheckRightTitle.setTextSize(17);
                break;
            case R.id.create_check_list_nav_submit://提交
                //必填  施工单位   责任人  现场描述  质检项目
                break;
        }
    }
}
