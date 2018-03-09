package com.glodon.bim.business.qualityManage.view;

import android.app.Activity;

import com.glodon.bim.base.BaseSearchActivity;
import com.glodon.bim.business.qualityManage.adapter.QualityCheckListAdapter;
import com.glodon.bim.business.qualityManage.bean.ClassifyNum;
import com.glodon.bim.business.qualityManage.bean.QualityCheckListBeanItem;
import com.glodon.bim.business.qualityManage.contract.QualityCheckListContract;
import com.glodon.bim.business.qualityManage.presenter.QualityCheckListPresenter;

import java.util.List;

/**
 * 质检清单搜索
 */
public class QualityCheckListSearchActivity extends BaseSearchActivity implements QualityCheckListContract.View{

    private QualityCheckListAdapter mQualityResultAdapter;//质检清单结果
    private QualityCheckListContract.Presenter mPresenter;

    @Override
    public void initPresenterAndContentAdapter() {
        mPresenter = new QualityCheckListPresenter(this);
        mQualityResultAdapter = new QualityCheckListAdapter(this, mPresenter.getListener());
        mContentRecyclerView.setAdapter(mQualityResultAdapter);
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
        // TODO: 2018/3/9 从质检项目进入时，要区分质检项目和构件

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
    public void updateData(List<QualityCheckListBeanItem> mDataList) {
        mQualityResultAdapter.updateList(mDataList);
        clearInputViewFocus();
    }

    @Override
    public void create() {

    }

    @Override
    public void updateClassifyCount(List<ClassifyNum> list) {

    }
}
