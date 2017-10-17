package com.glodon.bim.business.main.presenter;

import android.content.Intent;

import com.glodon.bim.business.main.bean.ProjectItem;
import com.glodon.bim.business.main.contract.ChooseProjectContract;
import com.glodon.bim.business.main.model.ChooseProjectModel;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：选择项目列表
 * 作者：zhourf on 2017/10/17
 * 邮箱：zhourf@glodon.com
 */

public class ChooseProjectPresenter implements ChooseProjectContract.Presenter {

    private ChooseProjectContract.View mView;
    private ChooseProjectContract.Model mModel;
    private List<ProjectItem> mDataList;

    public ChooseProjectPresenter(ChooseProjectContract.View view) {
        this.mView = view;
        mModel= new ChooseProjectModel();
        mDataList = new ArrayList<>();
    }

    @Override
    public void initData(Intent intent) {
        for (int i = 0;i<4;i++){
            ProjectItem item = new ProjectItem();
            item.name = "项目 "+i;
            mDataList.add(item);
        }
        mView.setStyle(mDataList.size());
        mView.updateData(mDataList);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void onDestroy() {

    }
}
