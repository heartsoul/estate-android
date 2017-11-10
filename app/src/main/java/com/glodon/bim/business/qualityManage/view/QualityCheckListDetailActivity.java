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
import com.glodon.bim.basic.utils.SharedPreferencesUtil;
import com.glodon.bim.business.authority.AuthorityManager;
import com.glodon.bim.business.qualityManage.bean.QualityCheckListDetailBean;
import com.glodon.bim.business.qualityManage.bean.QualityCheckListDetailInspectionInfo;
import com.glodon.bim.business.qualityManage.contract.QualityCheckListDetailContract;
import com.glodon.bim.business.qualityManage.listener.OnShowQualityCheckDetailListener;
import com.glodon.bim.business.qualityManage.presenter.QualityCheckListDetailPresenter;
import com.glodon.bim.common.config.CommonConfig;

/**
 * 描述：检查单详情
 * 作者：zhourf on 2017/9/8
 * 邮箱：zhourf@glodon.com
 */
public class QualityCheckListDetailActivity extends BaseActivity implements View.OnClickListener,QualityCheckListDetailContract.View{

    private static final int REQUESTCODE_CREATE_TYPE_REPAIR = 20;
    private static final int REQUESTCODE_CREATE_TYPE_REVIEW = 21;
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
    }



    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id)
        {
            case R.id.quality_check_list_detail_back:
                mActivity.finish();
                break;
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

                                        startActivityForResult(intent,REQUESTCODE_CREATE_TYPE_REPAIR);
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

                                        startActivityForResult(intent,REQUESTCODE_CREATE_TYPE_REVIEW);
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
            case REQUESTCODE_CREATE_TYPE_REPAIR:
                if(resultCode == RESULT_OK){
                    mDetailView.updateInfo(deptId,id);
                }
                break;
            case REQUESTCODE_CREATE_TYPE_REVIEW:
                if(resultCode == RESULT_OK){
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
