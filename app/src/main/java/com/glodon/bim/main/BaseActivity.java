package com.glodon.bim.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.glodon.bim.R;

/**
 * 描述：Activity基类
 * 作者：zhourf on 2017/9/11
 * 邮箱：zhourf@glodon.com
 */

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
    }
}
