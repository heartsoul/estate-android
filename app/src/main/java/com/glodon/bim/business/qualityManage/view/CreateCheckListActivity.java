package com.glodon.bim.business.qualityManage.view;

import android.os.Bundle;

import com.glodon.bim.R;
import com.glodon.bim.base.BaseActivity;
import com.glodon.bim.common.config.CommonConfig;

public class CreateCheckListActivity extends BaseActivity {

    private String mImagePath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quality_create_check_list_activity);

        mImagePath = getIntent().getStringExtra(CommonConfig.IAMGE_SAVE_PATH);
    }
}
