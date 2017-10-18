package com.glodon.bim.business.main.presenter;

import android.content.Intent;

import com.glodon.bim.business.main.bean.ChooseCategoryItem;
import com.glodon.bim.business.main.contract.ChooseCategoryItemContract;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：项目功能列表
 * 作者：zhourf on 2017/10/17
 * 邮箱：zhourf@glodon.com
 */

public class ChooseCategoryItemPresenter implements ChooseCategoryItemContract.Presenter {

    private ChooseCategoryItemContract.View mView;
    private List<ChooseCategoryItem> mDataList;

    public ChooseCategoryItemPresenter(ChooseCategoryItemContract.View mView) {
        this.mView = mView;
        mDataList = new ArrayList<>();
    }

    @Override
    public void initData(Intent intent) {
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void onDestroy() {

    }
}
