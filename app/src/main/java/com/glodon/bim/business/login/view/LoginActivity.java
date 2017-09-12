package com.glodon.bim.business.login.view;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

import com.glodon.bim.R;
import com.glodon.bim.business.login.contract.LoginContract;
import com.glodon.bim.business.login.model.ServiceGenerator;
import com.glodon.bim.business.login.presenter.LoginPresenter;
import com.glodon.bim.main.BaseActivity;
import com.jakewharton.rxbinding.view.RxView;

import java.util.concurrent.TimeUnit;

import rx.functions.Action1;

/**
 * 描述：登录界面
 * 作者：zhourf on 2017/9/8
 * 邮箱：zhourf@glodon.com
 */
public class LoginActivity extends BaseActivity implements LoginContract.View{

    private LoginContract.Presenter mPresenter;
    private EditText mUsernameEt,mPasswordEt;
    private Button mLoginBtn;
    private String redirectUri = "http://192.168.81.41/login";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initData();
    }

    private void initData(){
        mPresenter = new LoginPresenter(this);
        mUsernameEt = (EditText) findViewById(R.id.login_username);
        mPasswordEt = (EditText) findViewById(R.id.login_password);
        mLoginBtn = (Button) findViewById(R.id.login_button);

        RxView.clicks(mLoginBtn).throttleFirst(1, TimeUnit.SECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                mPresenter.clickLoginBtn(mUsernameEt.getText().toString(),mPasswordEt.getText().toString());

//                Intent intent = new Intent(
//                        Intent.ACTION_VIEW,
//                        Uri.parse(ServiceGenerator.API_BASE_URL + "/uaa/login" + "?client_id=" + mUsernameEt.getText().toString()+"&client_secret="+mPasswordEt.getText().toString() + "&redirect_uri=" + redirectUri));
//                startActivity(intent);

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // the intent filter defined in AndroidManifest will handle the return from ACTION_VIEW intent
        Uri uri = getIntent().getData();
        if (uri != null && uri.toString().startsWith(redirectUri)) {
            // use the parameter your API exposes for the code (mostly it's "code")
            String code = uri.getQueryParameter("code");
            System.out.println("code="+code);
            if (code != null) {
                // get access token
                // we'll do that in a minute
            } else if (uri.getQueryParameter("error") != null) {
                // show an error message here
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPresenter.onActivityResult(requestCode,resultCode,data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }

    @Override
    public void showLoadingDialog() {

    }

    @Override
    public void dismissLoadingDialog() {

    }

    @Override
    public Activity getActivity() {
        return this;
    }
}

