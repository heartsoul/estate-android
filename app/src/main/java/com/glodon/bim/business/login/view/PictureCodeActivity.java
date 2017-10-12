package com.glodon.bim.business.login.view;

import android.view.View;

import com.glodon.bim.R;
import com.glodon.bim.base.BaseTitleActivity;
/**
 * 描述：忘记密码-图片验证码界面
 * 作者：zhourf on 2017/9/8
 * 邮箱：zhourf@glodon.com
 */
public class PictureCodeActivity extends BaseTitleActivity {

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
        View view = inflate(R.layout.login_picture_code_activity);
        return view;
    }

    @Override
    protected void initDataForActivity() {
        super.initDataForActivity();
    }
}
