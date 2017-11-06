package com.glodon.bim.business.qualityManage.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.glodon.bim.R;
import com.glodon.bim.base.BaseActivity;
import com.glodon.bim.basic.listener.ThrottleClickEvents;
import com.glodon.bim.business.qualityManage.contract.QualityCheckListDetailContract;
import com.glodon.bim.business.qualityManage.presenter.QualityCheckListDetailPresenter;
import com.glodon.bim.common.config.CommonConfig;

/**
 * 描述：检查单详情
 * 作者：zhourf on 2017/9/8
 * 邮箱：zhourf@glodon.com
 */
public class QualityCheckListDetailActivity extends BaseActivity implements View.OnClickListener,QualityCheckListDetailContract.View{

    //状态
    private View mStatusView;
    //导航
    private ImageView mBackView;
    private TextView mRepairView;
    //检查单问题
    private LinearLayout mParent;

    private QualityCheckListDetailContract.Presenter mPresenter;

    private QualityCheckListDetailView mDetailView;

    private long deptId,id;//项目id 和检查单id

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.quality_check_list_detail_activity);

        initView();
        initStatusBar(mStatusView);
        setListener();
        initData();
    }

    private void initView() {
        mStatusView = findViewById(R.id.quality_check_list_detail_status);
        mBackView = (ImageView) findViewById(R.id.quality_check_list_detail_back);
        mParent = (LinearLayout) findViewById(R.id.quality_check_list_detail_content);
        mRepairView = (TextView) findViewById(R.id.quality_check_list_detail_repair);

    }


    private void setListener() {
        mBackView.setOnClickListener(this);
        ThrottleClickEvents.throttleClick(mRepairView,this);
    }



    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id)
        {
            case R.id.quality_check_list_detail_back:
                mActivity.finish();
                break;
            case R.id.quality_check_list_detail_repair:

                break;
        }
    }

    private void initData() {
        mPresenter = new QualityCheckListDetailPresenter(this);
        deptId = getIntent().getLongExtra(CommonConfig.QUALITY_CHECK_LIST_DEPTID,0);
        id = getIntent().getLongExtra(CommonConfig.QUALITY_CHECK_LIST_ID,0);
        mPresenter.initData(getIntent());
        mDetailView = new QualityCheckListDetailView(mActivity,mParent);
        mDetailView.getInfo(deptId,id);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mPresenter!=null)
        {
            mPresenter.onDestroy();
            mPresenter = null;
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


}
