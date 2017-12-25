package com.glodon.bim.business.qualityManage.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.glodon.bim.R;
import com.glodon.bim.base.BaseActivity;
import com.glodon.bim.basic.utils.SharedPreferencesUtil;
import com.glodon.bim.business.authority.AuthorityManager;
import com.glodon.bim.business.qualityManage.bean.QualityCheckListDetailBean;
import com.glodon.bim.business.qualityManage.bean.QualityCheckListDetailInspectionInfo;
import com.glodon.bim.business.qualityManage.contract.QualityCheckListDetailContract;
import com.glodon.bim.business.qualityManage.listener.OnShowQualityCheckDetailListener;
import com.glodon.bim.business.qualityManage.presenter.QualityCheckListDetailPresenter;
import com.glodon.bim.common.config.CommonConfig;
import com.glodon.bim.common.config.RequestCodeConfig;

/**
 * 描述：检查单详情
 * 作者：zhourf on 2017/9/8
 * 邮箱：zhourf@glodon.com
 */
public class QualityCheckListDetailActivity extends BaseActivity implements View.OnClickListener,QualityCheckListDetailContract.View{

    //状态
    private View mStatusView;
    //导航
    private RelativeLayout mBackView;
    private TextView mTitleView, mRepairView;
    //检查单问题
    private LinearLayout mParent;

    private QualityCheckListDetailContract.Presenter mPresenter;

    private QualityCheckListDetailView mDetailView;

    private long deptId,id;//项目id 和检查单id

    private boolean mIsChange = false;//判断当前页面是否发生了变化

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.quality_check_list_detail_activity);

        initView();
        initStatusBar(mStatusView);
        setListener();
        initData();
    }

    private void initView() {
        mStatusView = findViewById(R.id.quality_check_list_detail_status);
        mBackView = (RelativeLayout) findViewById(R.id.quality_check_list_detail_back);
        mParent = (LinearLayout) findViewById(R.id.quality_check_list_detail_content);
        mRepairView = (TextView) findViewById(R.id.quality_check_list_detail_repair);
        mTitleView = (TextView) findViewById(R.id.quality_check_list_detail_title);
    }


    private void setListener() {
        mBackView.setOnClickListener(this);
    }



    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id)
        {
            case R.id.quality_check_list_detail_back:
                back();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        back();
    }

    private void back(){
        if(mIsChange){
            setResult(RESULT_OK);
            mActivity.finish();
        }else {
            mActivity.finish();
        }
    }

    private void initData() {
        mPresenter = new QualityCheckListDetailPresenter(this);
        deptId = getIntent().getLongExtra(CommonConfig.QUALITY_CHECK_LIST_DEPTID,0);
        id = getIntent().getLongExtra(CommonConfig.QUALITY_CHECK_LIST_ID,0);
        mPresenter.initData(getIntent());
        mDetailView = new QualityCheckListDetailView(mActivity,mParent);
        mDetailView.setmListener(new OnShowQualityCheckDetailListener() {
            @Override
            public void onDetailShow(QualityCheckListDetailBean bean) {

                mRepairView.setVisibility(View.GONE);
                if(bean!=null && bean.inspectionInfo!=null){
                    QualityCheckListDetailInspectionInfo info = bean.inspectionInfo;
                    //设置title
                    switch (info.inspectionType){
                        case CommonConfig.TYPE_INSPECTION:
                            mTitleView.setText("检查单");
                            break;
                        case CommonConfig.TYPE_ACCEPTANCE:
                            mTitleView.setText("验收单");
                            break;
                    }
                    //设置整改单还是复查单按钮
                    switch (info.qcState){
                        case CommonConfig.QC_STATE_UNRECTIFIED://待整改
                            if(AuthorityManager.isCreateRepair()&& AuthorityManager.isMe(info.responsibleUserId)) {
                                mRepairView.setVisibility(View.VISIBLE);
                                mRepairView.setText("+整改单");
                                mRepairView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(mActivity, CreateReviewActivity.class);
                                        intent.putExtra(CommonConfig.CREATE_TYPE,CommonConfig.CREATE_TYPE_REPAIR);
                                        intent.putExtra(CommonConfig.SHOW_PHOTO,true);
                                        intent.putExtra(CommonConfig.QUALITY_CHECK_LIST_DEPTID, SharedPreferencesUtil.getProjectId());
                                        intent.putExtra(CommonConfig.QUALITY_CHECK_LIST_ID,id);

                                        startActivityForResult(intent, RequestCodeConfig.REQUEST_CODE_CREATE_TYPE_REPAIR);
                                    }
                                });
                            }else{
                                mRepairView.setVisibility(View.GONE);
                            }
                            break;
                        case CommonConfig.QC_STATE_UNREVIEWED://待复查
                            if(AuthorityManager.isCreateReview()&& AuthorityManager.isMe(info.creatorId)) {
                                mRepairView.setVisibility(View.VISIBLE);
                                mRepairView.setText("+复查单");
                                mRepairView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(mActivity, CreateReviewActivity.class);
                                        intent.putExtra(CommonConfig.CREATE_TYPE,CommonConfig.CREATE_TYPE_REVIEW);
                                        intent.putExtra(CommonConfig.SHOW_PHOTO,true);
                                        intent.putExtra(CommonConfig.QUALITY_CHECK_LIST_DEPTID, SharedPreferencesUtil.getProjectId());
                                        intent.putExtra(CommonConfig.QUALITY_CHECK_LIST_ID,id);

                                        startActivityForResult(intent, RequestCodeConfig.REQUEST_CODE_CREATE_TYPE_REVIEW);
                                    }
                                });
                            }else{
                                mRepairView.setVisibility(View.GONE);
                            }
                            break;
                    }
                }
            }
        });
        mDetailView.getInfo(deptId,id);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
            case RequestCodeConfig.REQUEST_CODE_CREATE_TYPE_REPAIR:
                if(resultCode == RESULT_OK){
                    mIsChange = true;
                    mDetailView.updateInfo(deptId,id);
                }
                break;
            case RequestCodeConfig.REQUEST_CODE_CREATE_TYPE_REVIEW:
                if(resultCode == RESULT_OK){
                    mIsChange = true;
                    mDetailView.updateInfo(deptId,id);
                }
                break;
        }
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
