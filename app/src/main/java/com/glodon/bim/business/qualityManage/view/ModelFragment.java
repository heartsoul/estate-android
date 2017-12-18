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
 * 描述：模型
 * 作者：zhourf on 2017/9/29
 * 邮箱：zhourf@glodon.com
 */

public class ModelFragment extends BaseFragment {
    private ModelView mModelView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflate(R.layout.quality_model_content_view);
        mModelView = new ModelView(getActivity(),view);
        mModelView.setIsFragment();
        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(mModelView!=null)
        {
            mModelView.onDestroy();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(mModelView!=null)
        {
            mModelView.onActivityResult(requestCode, resultCode, data);
        }
    }
}
