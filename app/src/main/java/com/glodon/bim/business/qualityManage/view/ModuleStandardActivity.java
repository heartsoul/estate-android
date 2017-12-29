package com.glodon.bim.business.qualityManage.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.glodon.bim.R;
import com.glodon.bim.base.BaseActivity;
import com.glodon.bim.basic.listener.ThrottleClickEvents;
import com.glodon.bim.basic.utils.ScreenUtil;
import com.glodon.bim.business.qualityManage.adapter.ModuleStandardAdapter;
import com.glodon.bim.business.qualityManage.bean.ModuleStandardItem;
import com.glodon.bim.business.qualityManage.contract.ModuleStandardContract;
import com.glodon.bim.business.qualityManage.presenter.ModuleStandardPresenter;
import com.glodon.bim.common.config.CommonConfig;
import com.glodon.bim.customview.pullrefreshview.PullRefreshView;

import java.util.List;

/**
 * 描述：质检标准
 * 作者：zhourf on 2017/9/8
 * 邮箱：zhourf@glodon.com
 */
public class ModuleStandardActivity extends BaseActivity implements View.OnClickListener, ModuleStandardContract.View {

    private ModuleStandardContract.Presenter mPresenter;

    private LinearLayout mStatusView;//状态栏
    private ImageView mCancelView;
    private TextView mTitleView;
    private LinearLayout mContentParent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quality_module_standard_activity);

        initView();

        setListener();

        initData();

    }

    private void initView() {
        mStatusView = (LinearLayout) findViewById(R.id.module_standard_status);
        mCancelView = (ImageView) findViewById(R.id.module_standard_cancel);
        mTitleView = (TextView) findViewById(R.id.module_standard_title);
        mContentParent = (LinearLayout) findViewById(R.id.module_standard_content_parent);
        initStatusBar(mStatusView);
    }



    @Override
    public void updateListView(List<ModuleStandardItem> mDataList) {

        List<ModuleStandardItem> rootList = mPresenter.getRootList();
        for(ModuleStandardItem item : rootList){
            addCata(mContentParent,item);
        }
    }

    private void addCata(LinearLayout parent,ModuleStandardItem item){
        View view = LayoutInflater.from(mActivity).inflate(R.layout.quality_module_standard_cata_view,null);
        final ImageView mFlagView = view.findViewById(R.id.module_standard_cata_flag);
        TextView mTitleView = view.findViewById(R.id.module_standard_cata_title);
        TextView mTextView = view.findViewById(R.id.module_standard_cata_text);
        final LinearLayout mContentParent = view.findViewById(R.id.module_standard_cata_content_parent);
        mTitleView.setText(item.name);
        mTitleView.setTextSize(18-item.level*2);
        mTextView.setTextSize(18-item.level*2);
        if(!TextUtils.isEmpty(item.content)){
            mTextView.setVisibility(View.VISIBLE);
            mTextView.setText(Html.fromHtml(item.content));
        }else{
            mTextView.setVisibility(View.GONE);
        }

        mFlagView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mContentParent.getVisibility() == View.GONE){
                    mContentParent.setVisibility(View.VISIBLE);
                    mFlagView.setBackgroundResource(R.drawable.icon_module_standard_close);
                }else{
                    mContentParent.setVisibility(View.GONE);
                    mFlagView.setBackgroundResource(R.drawable.icon_module_standard_open);
                }
            }
        });
        mTitleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mContentParent.getVisibility() == View.GONE){
                    mContentParent.setVisibility(View.VISIBLE);
                    mFlagView.setBackgroundResource(R.drawable.icon_module_standard_close);
                }else{
                    mContentParent.setVisibility(View.GONE);
                    mFlagView.setBackgroundResource(R.drawable.icon_module_standard_open);
                }
            }
        });
        //添加子类
        List<ModuleStandardItem> list = mPresenter.getListByParentId(item.id);
        if(list.size()>0){
            for(ModuleStandardItem i : list){
                addCata(mContentParent,i);
            }
        }else{
//            addObj(mContentParent,item);
        }
        parent.addView(view);
    }

    private void addObj(LinearLayout parent,ModuleStandardItem item){
        View view = LayoutInflater.from(mActivity).inflate(R.layout.quality_module_standard_obj_view,null);
        TextView mTitleView = view.findViewById(R.id.module_standard_obj_title);
        TextView mTextView = view.findViewById(R.id.module_standard_obj_text);
        mTitleView.setText(item.name);
        if(!TextUtils.isEmpty(item.content)){
            mTextView.setText(Html.fromHtml(item.content));
            mTextView.setVisibility(View.VISIBLE);
        }else{
            mTextView.setVisibility(View.GONE);
        }
        mTitleView.setTextSize(18-item.level*2);
        mTextView.setTextSize(18-item.level*2);
        parent.addView(view);
    }

    private void setListener() {
        //导航栏
        ThrottleClickEvents.throttleClick(mCancelView, this);
    }

    private void initData() {
        mTitleView.setText(getIntent().getStringExtra(CommonConfig.MODULE_TITLE)+"质检标准");
        mPresenter = new ModuleStandardPresenter(this);
        mPresenter.initData(getIntent());
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.module_standard_cancel://返回按钮
                mActivity.finish();
                break;

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.onDestroy();
            mPresenter = null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mPresenter != null) {
            mPresenter.onActivityResult(requestCode, resultCode, data);
        }
    }


}
