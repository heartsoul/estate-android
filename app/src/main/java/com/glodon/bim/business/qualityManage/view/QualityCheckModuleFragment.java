package com.glodon.bim.business.qualityManage.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.glodon.bim.R;
import com.glodon.bim.base.BaseFragment;
import com.glodon.bim.business.main.bean.ProjectListItem;

/**
 * 描述：质检项目
 * 作者：zhourf on 2017/9/29
 * 邮箱：zhourf@glodon.com
 */

public class QualityCheckModuleFragment extends BaseFragment {
    private ProjectListItem mProjectInfo;

    public void setProjectInfo(ProjectListItem info)
    {
        this.mProjectInfo = info;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflate(R.layout.quality_check_module_fragment);
        return view;
    }
}
