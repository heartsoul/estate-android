package com.glodon.bim.business.main.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.glodon.bim.R;
import com.glodon.bim.base.BaseFragment;
import com.glodon.bim.business.main.adapter.MainPagerQualityEquipmentAdapter;
import com.glodon.bim.business.main.bean.MainPagerQualityEquipmentItem;
import com.glodon.bim.business.main.contract.MainPagerQualityContract;
import com.glodon.bim.business.main.listener.OnPagerItemClickListener;
import com.glodon.bim.business.main.presenter.MainPagerQualityPresenter;

import java.util.List;

/**
 * 描述：首页质量
 * 作者：zhourf on 2017/9/29
 * 邮箱：zhourf@glodon.com
 */

public class MainPagerQualityFragment extends BaseFragment implements MainPagerQualityContract.View{
    private RecyclerView mRecyclerView;
    private MainPagerQualityEquipmentAdapter mAdapter;
    private MainPagerQualityContract.Presenter mPresenter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflate(R.layout.quality_main_fragment_main_page_quality);
        initView(view);
        setListener();
        initData();
        return view;
    }


    private void initView(View view) {
        mRecyclerView = view.findViewById(R.id.main_pager_quality_recyclerview);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),4));

    }

    private void setListener() {

    }

    private void initData() {
        mPresenter = new MainPagerQualityPresenter(this);
        mPresenter.initData(getActivity().getIntent());
    }

    @Override
    public void updateList(List<MainPagerQualityEquipmentItem> mDataList, OnPagerItemClickListener mListener) {
        mAdapter = new MainPagerQualityEquipmentAdapter(getActivity(),mDataList,mListener);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(mPresenter!=null){
            mPresenter.onDestroy();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(mPresenter!=null){
            mPresenter.onActivityResult(requestCode, resultCode, data);
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


}
