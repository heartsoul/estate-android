package com.glodon.bim.business.welcome.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;

import com.glodon.bim.R;

/**
 * 描述：启动页
 * 作者：zhourf on 2017/9/11
 * 邮箱：zhourf@glodon.com
 */

public class SplashActivity extends Activity {

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.splash_main_activity);


        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, WelcomeActivity.class);
                startActivity(intent);
                SplashActivity.this.finish();
            }
        },2500);
    }
}
