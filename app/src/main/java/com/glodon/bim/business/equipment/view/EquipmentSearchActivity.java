package com.glodon.bim.business.equipment.view;

import android.app.Activity;

import com.glodon.bim.business.equipment.adapter.EquipmentListAdapter;
import com.glodon.bim.business.equipment.bean.EquipmentListBeanItem;
import com.glodon.bim.business.equipment.contract.EquipmentListContract;
import com.glodon.bim.business.equipment.presenter.EquipmentListPresenter;
import com.glodon.bim.business.qualityManage.bean.ClassifyNum;
import com.glodon.bim.base.BaseSearchActivity;

import java.util.List;

/**
 * Created by cwj on 2018/3/8.
 * Description:材设清单搜索
 */
public class EquipmentSearchActivity extends BaseSearchActivity implements EquipmentListContract.View {


    private EquipmentListAdapter mAdapter;
    private EquipmentListContract.Presenter mPresenter;

    @Override
    public void initPresenterAndContentAdapter() {
        mPresenter = new EquipmentListPresenter(this);
        mAdapter = new EquipmentListAdapter(this, mPresenter.getListener());
        mContentRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void pullDown() {
        mPresenter.pullDown();
    }

    @Override
    public void pullUp() {
        mPresenter.pullUp();
    }

    @Override
    public void presenterSearch(String key) {
        mAdapter.setSearch(key);
        mPresenter.search(key);
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
    public void updateData(List<EquipmentListBeanItem> mDataList) {
        mAdapter.updateList(mDataList);
        clearInputViewFocus();
    }

    @Override
    public void updateClassifyCount(List<ClassifyNum> list) {

    }

    @Override
    public void create() {

    }


}
