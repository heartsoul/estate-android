package com.glodon.bim.main;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.glodon.bim.R;
import com.glodon.bim.basic.listener.ThrottleClickEvents;
import com.glodon.bim.basic.log.LogUtil;
import com.glodon.bim.business.greendao.provider.DaoProvider;
import com.glodon.bim.test.TestRecyclerViewActivity;

public class MainActivity extends BaseActivity {

    private Activity context;

    private int REQUEST_EXTERNAL_STORAGE = 1;

    @Override
    protected View onCreateView() {
        context = this;
        View view = LayoutInflater.from(this).inflate(R.layout.activity_main,null);
        return view;
    }

    @Override
    protected void initDataForActivity() {
        super.initDataForActivity();
        String[] PERMISSIONS_STORAGE = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        requestPermission(PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);


        LogUtil.d("====",new DaoProvider().getCookie());
    }



}
