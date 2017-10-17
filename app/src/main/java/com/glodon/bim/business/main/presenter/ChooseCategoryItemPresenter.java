package com.glodon.bim.business.main.presenter;

import android.content.Intent;

import com.glodon.bim.R;
import com.glodon.bim.business.main.bean.CategoryItem;
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
    private List<CategoryItem> mDataList;

    public ChooseCategoryItemPresenter(ChooseCategoryItemContract.View mView) {
        this.mView = mView;
        mDataList = new ArrayList<>();
    }

    @Override
    public void initData(Intent intent) {
        CategoryItem item0 = new CategoryItem();
        item0.resource = R.drawable.icon_category_item_zjqd;
        item0.name = "质检清单";
        mDataList.add(item0);
        CategoryItem item1 = new CategoryItem();
        item1.resource = R.drawable.icon_category_item_tj;
        item1.name = "图纸";
        mDataList.add(item1);
        CategoryItem item2 = new CategoryItem();
        item2.resource = R.drawable.icon_category_item_mx;
        item2.name = "模型";
        mDataList.add(item2);
        CategoryItem item3 = new CategoryItem();
        item3.resource = R.drawable.icon_category_item_zjxm;
        item3.name = "质检项目";
        mDataList.add(item3);
        CategoryItem item4 = new CategoryItem();
        item4.resource = R.drawable.icon_category_item_create;
        item4.name = "新建";
        mDataList.add(item4);

        mView.updateData(mDataList);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void onDestroy() {

    }
}
