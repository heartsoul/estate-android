package com.glodon.bim.business.main.view;

import android.view.View;

import com.glodon.bim.R;
import com.glodon.bim.base.BaseTitleActivity;
/**
 * 描述：选择项目界面
 * 作者：zhourf on 2017/9/8
 * 邮箱：zhourf@glodon.com
 */
public class ChooseProjectActivity extends BaseTitleActivity {

    @Override
    protected void onCreateHeader() {
        mHeader.setTitle("选择项目")
                .setLeftOneIcon(R.drawable.ic_launcher)
                .setLeftOneClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                }).setVisible(View.VISIBLE);
    }

    @Override
    protected View onCreateView() {
        View view = inflate(R.layout.main_choose_project_activity);
        return view;
    }

    @Override
    protected void initDataForActivity() {
        super.initDataForActivity();
    }
}
