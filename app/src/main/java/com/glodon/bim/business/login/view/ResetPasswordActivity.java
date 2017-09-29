package com.glodon.bim.business.login.view;

import android.os.Bundle;
import android.view.View;

import com.glodon.bim.R;
import com.glodon.bim.base.BaseActivity;

/**
 * 描述：忘记密码-短信验证码界面
 * 作者：zhourf on 2017/9/8
 * 邮箱：zhourf@glodon.com
 */
public class ResetPasswordActivity extends BaseActivity {

    @Override
    protected void onCreateHeader() {
        super.onCreateHeader();
        mHeader.setTitle("忘记密码")
                .setLeftOneIcon(R.drawable.ic_launcher_round)
                .setLeftOneClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mActivity.finish();
                    }
                })
                .setVisible(View.VISIBLE);
    }

    @Override
    protected View onCreateView() {
        View view = inflate(R.layout.login_reset_password_activity);
        return view;
    }

    @Override
    protected void initDataForActivity() {
        super.initDataForActivity();
    }
}
