package com.glodon.bim.business.setting.view;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.glodon.bim.R;
import com.glodon.bim.base.BaseActivity;
import com.glodon.bim.business.setting.contract.SettingContract;
import com.glodon.bim.business.setting.presenter.SettingPresenter;

/**
 * 描述：设置界面
 * 作者：zhourf on 2017/10/24
 * 邮箱：zhourf@glodon.com
 */

public class SettingActivity extends BaseActivity implements SettingContract.View, View.OnClickListener {

    private SettingContract.Presenter mPresenter;
    private Button mSignOutBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_activity);

        initView();

        setListener();

        initData();
    }



    private void initView() {
        mSignOutBtn = (Button) findViewById(R.id.setting_signout_btn);
    }
    private void setListener() {
        mSignOutBtn.setOnClickListener(this);
    }

    private void initData() {
        mPresenter = new SettingPresenter(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id)
        {
            case R.id.setting_signout_btn:
                mPresenter.signOut();
                break;
        }
    }

    @Override
    public void showLoadingDialog() {

    }

    @Override
    public void dismissLoadingDialog() {

    }

    @Override
    public Activity getActivity() {
        return mActivity;
    }

}
