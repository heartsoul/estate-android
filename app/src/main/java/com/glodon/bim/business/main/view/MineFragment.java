package com.glodon.bim.business.main.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.glodon.bim.R;
import com.glodon.bim.base.BaseFragment;
import com.glodon.bim.basic.listener.ThrottleClickEvents;
import com.glodon.bim.basic.utils.SharedPreferencesUtil;
import com.glodon.bim.business.setting.view.SettingActivity;

/**
 * 描述：质量下模型
 * 作者：zhourf on 2017/9/29
 * 邮箱：zhourf@glodon.com
 */

public class MineFragment extends BaseFragment implements View.OnClickListener {
    private TextView mUsernameView;
    private RelativeLayout mPermissionView,mPlanView,mSetView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflate(R.layout.quality_main_fragment_mine);
        initView(view);
        setListener();
        initData();
        return view;
    }

    private void initView(View view) {
        mUsernameView = view.findViewById(R.id.mine_fragment_username);
        mPermissionView = view.findViewById(R.id.mine_fragment_pemission);
        mPlanView = view.findViewById(R.id.mine_fragment_plan);
        mSetView = view.findViewById(R.id.mine_fragment_set);
    }

    private void setListener() {
        ThrottleClickEvents.throttleClick(mPermissionView,this);
        ThrottleClickEvents.throttleClick(mPlanView,this);
        ThrottleClickEvents.throttleClick(mSetView,this);
    }

    private void initData() {
        mUsernameView.setText(SharedPreferencesUtil.getUserName());
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id)
        {
            case R.id.mine_fragment_pemission:

                break;
            case R.id.mine_fragment_plan:

                break;
            case R.id.mine_fragment_set:
                Intent intent = new Intent(getActivity(), SettingActivity.class);
                startActivity(intent);
                break;
        }
    }
}
