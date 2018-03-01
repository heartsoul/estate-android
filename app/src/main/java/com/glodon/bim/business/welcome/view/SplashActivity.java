package com.glodon.bim.business.welcome.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

import com.glodon.bim.R;
import com.glodon.bim.base.BaseActivity;
import com.glodon.bim.basic.utils.SharedPreferencesUtil;
import com.glodon.bim.business.login.listener.OnLoginListener;
import com.glodon.bim.business.login.util.LoginManager;
import com.glodon.bim.business.login.view.LoginActivity;
import com.glodon.bim.business.main.view.MainActivity;

import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 描述：启动页
 * 作者：zhourf on 2017/9/11
 * 邮箱：zhourf@glodon.com
 */

public class SplashActivity extends BaseActivity {

//    private Handler handler = new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//        }
//    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        setContentView(R.layout.splash_main_activity);


//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Intent intent = new Intent(SplashActivity.this, WelcomeActivity.class);
//                startActivity(intent);
//                SplashActivity.this.finish();
//            }
//        },2500);

        if(SharedPreferencesUtil.getProjectInfo()!=null){
            LoginManager.updateLoginInfo(new OnLoginListener() {
                @Override
                public void onLoginSuccess(String cookie) {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onLoginFailed(Call<ResponseBody> call, Throwable t) {

                }
            });

        }else{
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
