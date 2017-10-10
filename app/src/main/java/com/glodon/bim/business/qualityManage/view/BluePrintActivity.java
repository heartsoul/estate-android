package com.glodon.bim.business.qualityManage.view;

import android.view.View;

import com.glodon.bim.R;
import com.glodon.bim.base.BaseActivity;

public class BluePrintActivity extends BaseActivity {


    @Override
    protected void onCreateHeader() {
        mHeader.setTitle("图纸")
                .setLeftOneIcon(R.drawable.ic_launcher)
                .setLeftOneClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mActivity.finish();
                    }
                })
                .setRightOneIcon(R.drawable.ic_launcher)
                .setRightOneClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                })
                .setVisible(View.VISIBLE);

    }

    @Override
    protected View onCreateView() {
        View view = inflate(R.layout.quality_blue_print_activity);
        return view;
    }
}
