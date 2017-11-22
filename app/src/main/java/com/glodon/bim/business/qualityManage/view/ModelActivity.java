package com.glodon.bim.business.qualityManage.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.glodon.bim.R;
import com.glodon.bim.base.BaseActivity;
import com.glodon.bim.basic.listener.ThrottleClickEvents;
import com.glodon.bim.business.qualityManage.adapter.ModelListAdapter;
import com.glodon.bim.business.qualityManage.adapter.ModelSingleListAdapter;
import com.glodon.bim.business.qualityManage.adapter.ModelSpecialListAdapter;
import com.glodon.bim.business.qualityManage.bean.ModelListBeanItem;
import com.glodon.bim.business.qualityManage.bean.ModelSingleListItem;
import com.glodon.bim.business.qualityManage.bean.ModelSpecialListItem;
import com.glodon.bim.business.qualityManage.contract.ModelContract;
import com.glodon.bim.business.qualityManage.presenter.ModelPresenter;

import java.util.List;

/**
 * 模型
 */
public class ModelActivity extends BaseActivity implements View.OnClickListener, ModelContract.View {

    private ModelContract.Presenter mPresenter;

    //导航栏
    private RelativeLayout mNavBack, mNavSearch;

    //专业单体
    private TextView mSpecialName, mSingleName;
    private ImageView mSpecialTrangle, mSingleTrangle;

    //初始默认的背景
    private LinearLayout mDefaultBg;

    //模型列表
    private RecyclerView mModelListView;

    //专业单体列表展示
    private RelativeLayout mHintBg;
    private RecyclerView mSpecialListView, mSingleListView;

    private ModelListAdapter mModelListAdapter;
    private ModelSpecialListAdapter mSpecialAdapter;
    private ModelSingleListAdapter mSingleAdapter;

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

        //专业单体
        mSpecialName = (TextView) findViewById(R.id.model_special_name);
        mSingleName = (TextView) findViewById(R.id.model_single_name);
        mSpecialTrangle = (ImageView) findViewById(R.id.model_special_trangle);
        mSingleTrangle = (ImageView) findViewById(R.id.model_single_trangle);

        //初始默认的背景
        mDefaultBg = (LinearLayout) findViewById(R.id.model_bg_default);

        //模型列表
        mModelListView = (RecyclerView) findViewById(R.id.model_bg_content_list);

        //专业单体列表展示
        mHintBg = (RelativeLayout) findViewById(R.id.model_bg_hint);
        mSpecialListView = (RecyclerView) findViewById(R.id.model_bg_hint_special_list);
        mSingleListView = (RecyclerView) findViewById(R.id.model_bg_hint_single_list);

        initStatusBar(mStatusView);

        mHintBg.setVisibility(View.GONE);
        mModelListView.setVisibility(View.GONE);
    }

    private void initModelList() {
        mModelListView.setItemAnimator(new DefaultItemAnimator());
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mModelListView.setLayoutManager(manager);
        mModelListAdapter = new ModelListAdapter(this);
        mModelListAdapter.setListener(mPresenter.getListener());
        mModelListView.setAdapter(mModelListAdapter);
    }

    private void initSpecialList() {
        mSpecialListView.setItemAnimator(new DefaultItemAnimator());
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mSpecialListView.setLayoutManager(manager);
        mSpecialAdapter = new ModelSpecialListAdapter(this);
        mSpecialAdapter.setListener(mPresenter.getListener());
        mSpecialListView.setAdapter(mSpecialAdapter);
    }


    private void initSingleList() {
        mSingleListView.setItemAnimator(new DefaultItemAnimator());
        GridLayoutManager manager = new GridLayoutManager(this, 3);
        mSingleListView.setLayoutManager(manager);
        mSingleAdapter = new ModelSingleListAdapter(this);
        mSingleAdapter.setListener(mPresenter.getListener());
        mSingleListView.setAdapter(mSingleAdapter);
    }

    private void setListener() {
        //导航栏
        ThrottleClickEvents.throttleClick(mNavBack, this);
        ThrottleClickEvents.throttleClick(mNavSearch, this, 1);
        mSpecialName.setOnClickListener(this);
        mSingleName.setOnClickListener(this);
    }

    private void initData() {
        mPresenter = new ModelPresenter(this);
        initModelList();
        initSpecialList();
        initSingleList();
        mPresenter.initData(getIntent());
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.model_list_nav_back://返回按钮
                mActivity.finish();
                break;
            case R.id.model_list_nav_search://搜索

                break;
            case R.id.model_special_name://专业
                if (mHintBg.getVisibility() == View.GONE) {
                    //关闭着的
                    mSpecialTrangle.setBackgroundResource(R.drawable.icon_blue_trangle_up);
                    mSpecialName.setTextColor(getResources().getColor(R.color.c_00b5f2));

                    mSpecialListView.setVisibility(View.VISIBLE);
                    mSingleListView.setVisibility(View.GONE);
                    mHintBg.setVisibility(View.VISIBLE);
                    mPresenter.showSpecialList();
                } else {
                    //打开着的
                    if(mSpecialListView.getVisibility()==View.GONE){
                        //打开着的是单体
                        mSpecialTrangle.setBackgroundResource(R.drawable.icon_blue_trangle_up);
                        mSpecialName.setTextColor(getResources().getColor(R.color.c_00b5f2));

                        mSpecialListView.setVisibility(View.VISIBLE);

                        mSingleListView.setVisibility(View.GONE);
                        mSingleName.setTextColor(getResources().getColor(R.color.black));
                        mSingleTrangle.setBackgroundResource(R.drawable.icon_gray_trangle_down);

                        mPresenter.showSpecialList();
                    }else{
                        //打开着的是专业
                        mHintBg.setVisibility(View.GONE);
                        mSpecialListView.setVisibility(View.GONE);

                        mSpecialTrangle.setBackgroundResource(R.drawable.icon_gray_trangle_down);
                        mSpecialName.setTextColor(getResources().getColor(R.color.black));
                    }

                }
                break;
            case R.id.model_single_name://单体
                if (mHintBg.getVisibility() == View.GONE) {
                    mSingleTrangle.setBackgroundResource(R.drawable.icon_blue_trangle_up);
                    mSingleName.setTextColor(getResources().getColor(R.color.c_00b5f2));

                    mHintBg.setVisibility(View.VISIBLE);
                    mSingleListView.setVisibility(View.VISIBLE);
                    mSpecialListView.setVisibility(View.GONE);
                    mPresenter.showSingleList();
                } else {
                    if(mSingleListView.getVisibility()==View.GONE){
                        //打开着的是专业
                        mSingleTrangle.setBackgroundResource(R.drawable.icon_blue_trangle_up);
                        mSingleName.setTextColor(getResources().getColor(R.color.c_00b5f2));

                        mSingleListView.setVisibility(View.VISIBLE);

                        mSpecialListView.setVisibility(View.GONE);
                        mSpecialName.setTextColor(getResources().getColor(R.color.black));
                        mSpecialTrangle.setBackgroundResource(R.drawable.icon_gray_trangle_down);

                        mPresenter.showSingleList();
                    }else{
                        //打开着的是单体
                        mHintBg.setVisibility(View.GONE);
                        mSingleListView.setVisibility(View.GONE);

                        mSingleTrangle.setBackgroundResource(R.drawable.icon_gray_trangle_down);
                        mSingleName.setTextColor(getResources().getColor(R.color.black));
                    }

                }
                break;

        }
    }

    @Override
    public void updateSpecialList(List<ModelSpecialListItem> mSpecialList, long mSpecialSelectId) {
        mSpecialAdapter.updateList(mSpecialList, mSpecialSelectId);
        mHintBg.setVisibility(View.VISIBLE);
        mSpecialListView.setVisibility(View.VISIBLE);
    }

    @Override
    public void updateSingleList(List<ModelSingleListItem> mSingleList, long mSingleSelectId) {
        mSingleAdapter.updateList(mSingleList, mSingleSelectId);
        mHintBg.setVisibility(View.VISIBLE);
        mSingleListView.setVisibility(View.VISIBLE);
    }

    @Override
    public void updateModelList(List<ModelListBeanItem> mModelList) {
        mModelListAdapter.updateList(mModelList, 0);
        if(mModelList!=null && mModelList.size()>0) {
            mDefaultBg.setVisibility(View.GONE);
            mModelListView.setVisibility(View.VISIBLE);
        }else{
            mDefaultBg.setVisibility(View.VISIBLE);
            mModelListView.setVisibility(View.GONE);
        }
    }

    @Override
    public void showSingle(ModelSingleListItem item) {
        mSingleName.setText(item.name);
        mHintBg.setVisibility(View.GONE);
        mSingleTrangle.setBackgroundResource(R.drawable.icon_gray_trangle_down);
        mSingleName.setTextColor(getResources().getColor(R.color.black));
    }

    @Override
    public void showSpecial(ModelSpecialListItem item) {
        mSpecialName.setText(item.name);
        mHintBg.setVisibility(View.GONE);
        mSpecialTrangle.setBackgroundResource(R.drawable.icon_gray_trangle_down);
        mSpecialName.setTextColor(getResources().getColor(R.color.black));
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
