package com.glodon.bim.business.qualityManage.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.glodon.bim.R;
import com.glodon.bim.base.BaseFragment;

/**
 * 描述：图纸
 * 作者：zhourf on 2017/9/29
 * 邮箱：zhourf@glodon.com
 */

public class BluePrintFragment extends BaseFragment {
    private BluePrintView mBluePrintView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflate(R.layout.quality_catalog_module_view);
        mBluePrintView = new BluePrintView(getActivity(),view);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mBluePrintView!=null)
        {
            mBluePrintView.onDestroy();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(mBluePrintView!=null)
        {
            mBluePrintView.onActivityResult(requestCode, resultCode, data);
        }
    }
}
