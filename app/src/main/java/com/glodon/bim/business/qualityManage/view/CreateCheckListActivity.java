package com.glodon.bim.business.qualityManage.view;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.glodon.bim.R;
import com.glodon.bim.base.BaseActivity;
import com.glodon.bim.basic.listener.ThrottleClickEvents;
import com.glodon.bim.business.qualityManage.bean.CreateCheckListParams;
import com.glodon.bim.common.config.CommonConfig;
import com.glodon.bim.common.config.RequestCodeConfig;

/**
 * 描述：新建检查单
 * 作者：zhourf on 2017/9/8
 * 邮箱：zhourf@glodon.com
 */
public class CreateCheckListActivity extends BaseActivity implements View.OnClickListener {



    private LinearLayout mStatusView;//状态栏
    //导航栏
    private RelativeLayout mNavBack;
    private TextView mNavSubmit, mNavCheckLeftTitle, mNavCheckRightTitle;
    private View mNavLeftLine, mNavRightLine;

    //输入法变化
    private LinearLayout rootLayout;//跟布局

    private CreateInspectionAcceptionFragment mInspectionFragment;
    private CreateInspectionAcceptionFragment mAcceptionFragment;
    private CreateInspectionAcceptionFragment mCurrentFragment;

    //前面传递过来的数据
    private Intent data;
    private CreateCheckListParams  mParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quality_create_check_list_activity);

        initView();

        setListener();

        initData();

        getInputMethodHeight();
    }

    private void initView() {
        data = getIntent();

        mStatusView = (LinearLayout) findViewById(R.id.create_check_list_status);
        rootLayout = (LinearLayout) findViewById(R.id.create_check_list_root);
        //导航栏
        mNavBack = (RelativeLayout) findViewById(R.id.create_check_list_nav_back);
        mNavSubmit = (TextView) findViewById(R.id.create_check_list_nav_submit);
        mNavCheckLeftTitle = (TextView) findViewById(R.id.create_check_list_nav_check_left_title);
        mNavCheckRightTitle = (TextView) findViewById(R.id.create_check_list_nav_check_right_title);
        mNavLeftLine = findViewById(R.id.create_check_list_nav_check_left_line);
        mNavRightLine = findViewById(R.id.create_check_list_nav_check_right_line);

    }


    //展示检查单
    private void showInspectionFragment(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if(mInspectionFragment == null){
            mInspectionFragment = new CreateInspectionAcceptionFragment();
            mInspectionFragment.setInspectionCompanyTitle("检查单位");
            if(mParams!=null && !CommonConfig.TYPE_INSPECTION.equals(mParams.inspectionType)){
                data.removeExtra(CommonConfig.CREATE_CHECK_LIST_PROPS);
            }
            mInspectionFragment.setIntentData(data);
            mInspectionFragment.setInspectionType(CommonConfig.TYPE_INSPECTION);
            transaction.add(R.id.create_check_list_content,mInspectionFragment);
        }
        transaction.show(mInspectionFragment);
        if(mAcceptionFragment!=null){
            transaction.hide(mAcceptionFragment);
        }
        transaction.commit();
        mCurrentFragment = mInspectionFragment;
    }

    //展示验收单
    private void showAcceptionFragment(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if(mAcceptionFragment == null){
            mAcceptionFragment = new CreateInspectionAcceptionFragment();
            mAcceptionFragment.setInspectionCompanyTitle("验收单位");
            if(mParams!=null && !CommonConfig.TYPE_ACCEPTANCE.equals(mParams.inspectionType)){
                data.removeExtra(CommonConfig.CREATE_CHECK_LIST_PROPS);
            }
            mAcceptionFragment.setIntentData(data);
            mAcceptionFragment.setInspectionType(CommonConfig.TYPE_ACCEPTANCE);
            transaction.add(R.id.create_check_list_content,mAcceptionFragment);
        }
        transaction.show(mAcceptionFragment);
        if(mInspectionFragment!=null){
            transaction.hide(mInspectionFragment);
        }
        transaction.commit();
        mCurrentFragment = mAcceptionFragment;
    }

    private void setListener() {
        //导航栏
        ThrottleClickEvents.throttleClick(mNavBack, this);
        ThrottleClickEvents.throttleClick(mNavSubmit, this);
        ThrottleClickEvents.throttleClick(mNavCheckLeftTitle, this, 1);
        ThrottleClickEvents.throttleClick(mNavCheckRightTitle, this, 1);


    }

    private void initData() {
        //状态栏
        initStatusBar(mStatusView);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] PERMISSIONS_STORAGE = {
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
            };
            requestPermission(PERMISSIONS_STORAGE, RequestCodeConfig.REQUEST_CODE_CAMERA);
        }

        //如果是编辑，则params不为空  此时就要设置编辑数据
        mParams = (CreateCheckListParams) getIntent().getSerializableExtra(CommonConfig.CREATE_CHECK_LIST_PROPS);
        if(mParams!=null && !TextUtils.isEmpty(mParams.inspectionType)){
            switch (mParams.inspectionType){
                case CommonConfig.TYPE_INSPECTION:
                    clickLeftTitle();
                    break;
                case CommonConfig.TYPE_ACCEPTANCE:
                    clickRightTitle();
                    break;
                default:
                    clickLeftTitle();
                    break;
            }
        }else{
            //为空表示新建  默认检查单
            clickLeftTitle();
        }
    }

    /**
     * 输入法高度
     */
    private void getInputMethodHeight() {
        rootLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                if(mCurrentFragment!=null){
                    mCurrentFragment.onGlobalLayout(rootLayout);
                }
            }
        });

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.create_check_list_nav_back://返回按钮
                back();
                break;
            case R.id.create_check_list_nav_check_left_title://检查单
                clickLeftTitle();
                break;
            case R.id.create_check_list_nav_check_right_title://验收单
                clickRightTitle();
                break;
            case R.id.create_check_list_nav_submit://提交
                //必填  施工单位   责任人  现场描述  质检项目
                submit();
                break;

        }
    }

    private void clickRightTitle() {
        mNavLeftLine.setVisibility(View.INVISIBLE);
        mNavCheckLeftTitle.setTextSize(15);
        mNavRightLine.setVisibility(View.VISIBLE);
        mNavCheckRightTitle.setTextSize(17);
        showAcceptionFragment();
    }

    private void clickLeftTitle() {
        mNavLeftLine.setVisibility(View.VISIBLE);
        mNavCheckLeftTitle.setTextSize(17);
        mNavRightLine.setVisibility(View.INVISIBLE);
        mNavCheckRightTitle.setTextSize(15);
        showInspectionFragment();
    }

    @Override
    public void onBackPressed() {
        if(mCurrentFragment!=null) {
            mCurrentFragment.back();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(mCurrentFragment!=null){
            mCurrentFragment.onActivityResult(requestCode, resultCode, data);
        }
    }




    //点击返回按钮
    private void back() {
        if(mCurrentFragment!=null) {
            mCurrentFragment.back();
        }
    }


    //点击提交
    private void submit() {
        if(mCurrentFragment!=null) {
            mCurrentFragment.submit();
        }
    }


}
