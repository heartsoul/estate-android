package com.glodon.bim.business.login.view;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.glodon.bim.R;
import com.glodon.bim.base.BaseActivity;
import com.glodon.bim.basic.listener.ThrottleClickEvents;
import com.glodon.bim.basic.utils.ScreenUtil;
import com.glodon.bim.business.login.contract.LoginContract;
import com.glodon.bim.business.login.presenter.LoginPresenter;
import com.glodon.bim.customview.ToastManager;

/**
 * 描述：登录界面
 * 作者：zhourf on 2017/9/8
 * 邮箱：zhourf@glodon.com
 */
public class LoginActivity extends BaseActivity implements LoginContract.View, View.OnClickListener {

    private LoginContract.Presenter mPresenter;
    private EditText mUsernameEt, mPasswordEt;
    private TextView mUsernameTop, mPasswordTop, mForgetPassword;
    private ImageView mUsernameDelete, mPasswordDelete, mPasswordHide;
    private Button mLoginBtn;
    private View mUsernameLine, mPasswordLine;//用户名密码下方的蓝线
    private boolean mIsHidePassword = true; //密码框右侧  显示隐藏密码
    private boolean mIsHasUsername = false;//是否输入了用户名
    private boolean mIsHasPassword = false;//是否输入了密码   用于控制登录按钮状态
    private ErrorMessageDialog mErrorDialog;//错误密码>3次的提示框

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login_main_activity);
        initView();
        setListener();
        initDataForActivity();
    }

    private void initView() {
        mUsernameTop = (TextView) findViewById(R.id.login_username_top_text);
        mUsernameEt = (EditText) findViewById(R.id.login_username);
        mUsernameDelete = (ImageView) findViewById(R.id.login_username_delete);
        mUsernameLine = findViewById(R.id.login_username_bottom_line);

        mPasswordTop = (TextView) findViewById(R.id.login_password_top_text);
        mPasswordEt = (EditText) findViewById(R.id.login_password);
        mPasswordDelete = (ImageView) findViewById(R.id.login_password_delete);
        mPasswordHide = (ImageView) findViewById(R.id.login_password_showhide);
        mPasswordLine = findViewById(R.id.login_password_bottom_line);

        mLoginBtn = (Button) findViewById(R.id.login_button);
        mForgetPassword = (TextView) findViewById(R.id.login_forget_password);
    }

    private void setListener() {
        mUsernameEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                mUsernameEt.setBackgroundColor(getResources().getColor(R.color.white));
                String temp = mUsernameEt.getText().toString().trim();
                LinearLayout.LayoutParams lineParams = (LinearLayout.LayoutParams) mUsernameLine.getLayoutParams();
                if (hasFocus) {
                    mUsernameLine.setBackgroundColor(getResources().getColor(R.color.c_00baf3));
                    lineParams.height = ScreenUtil.dp2px(2);
                    mUsernameEt.setHint("");
                    mUsernameTop.setVisibility(View.VISIBLE);
                    mUsernameDelete.setVisibility(View.VISIBLE);
                } else {
                    mUsernameLine.setBackgroundColor(getResources().getColor(R.color.c_f3f3f2));
                    lineParams.height = ScreenUtil.dp2px(1);
                    mUsernameDelete.setVisibility(View.INVISIBLE);
                    if (TextUtils.isEmpty(temp)) {
                        mUsernameTop.setVisibility(View.INVISIBLE);
                        mUsernameEt.setHint("账户名");
                    } else {
                        mUsernameTop.setVisibility(View.VISIBLE);
                    }
                }
                mUsernameLine.setLayoutParams(lineParams);
            }
        });
        mUsernameDelete.setOnClickListener(this);
        mPasswordEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                mPasswordEt.setBackgroundColor(getResources().getColor(R.color.white));
                String temp = mPasswordEt.getText().toString().trim();
                LinearLayout.LayoutParams lineParams = (LinearLayout.LayoutParams) mPasswordLine.getLayoutParams();
                if (hasFocus) {
                    mPasswordLine.setBackgroundColor(getResources().getColor(R.color.c_00baf3));
                    lineParams.height = ScreenUtil.dp2px(2);
                    mPasswordEt.setHint("");
                    mPasswordTop.setVisibility(View.VISIBLE);
                    mPasswordDelete.setVisibility(View.VISIBLE);
                } else {
                    mPasswordLine.setBackgroundColor(getResources().getColor(R.color.c_f3f3f2));
                    lineParams.height = ScreenUtil.dp2px(1);
                    mPasswordDelete.setVisibility(View.INVISIBLE);
                    if (TextUtils.isEmpty(temp)) {
                        mPasswordTop.setVisibility(View.INVISIBLE);
                        mPasswordEt.setHint("密码");
                    } else {
                        mPasswordTop.setVisibility(View.VISIBLE);
                    }
                }
                mPasswordLine.setLayoutParams(lineParams);
            }
        });
        mUsernameEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String temp = mUsernameEt.getText().toString().trim();
                mIsHasUsername = !TextUtils.isEmpty(temp);
                changeLoginButton();
            }
        });
        mPasswordEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String temp = mPasswordEt.getText().toString().trim();
                mIsHasPassword = !TextUtils.isEmpty(temp);
                changeLoginButton();
            }
        });
        mPasswordDelete.setOnClickListener(this);
        mPasswordHide.setOnClickListener(this);
        ThrottleClickEvents.throttleClick(mLoginBtn, this);
        ThrottleClickEvents.throttleClick(mForgetPassword, this);

//        mUsernameEt.setText("13810457782");
//        mPasswordEt.setText("123456789.");
        mUsernameEt.setText("15822320523");
        mPasswordEt.setText("123qwe");
    }

    //更改登录按钮的显示
    private void changeLoginButton() {
        if (mIsHasPassword || mIsHasUsername) {
            mLoginBtn.setClickable(true);
            mLoginBtn.setBackgroundResource(R.drawable.corner_login_bg_blue);
        } else {
            mLoginBtn.setClickable(false);
            mLoginBtn.setBackgroundResource(R.drawable.corner_login_bg_gray);
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.login_username_delete:
                mUsernameEt.setText("");
                break;
            case R.id.login_password_delete:
                mPasswordEt.setText("");
                break;
            case R.id.login_password_showhide:
                if (mIsHidePassword) {
                    //如果选中，显示密码
                    mPasswordEt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    mPasswordHide.setBackgroundResource(R.drawable.icon_login_password_show);
                } else {
                    //否则隐藏密码
                    mPasswordEt.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    mPasswordHide.setBackgroundResource(R.drawable.icon_login_password_hide);
                }
                //更改光标位置
                String psd = mPasswordEt.getText().toString();
                if (!TextUtils.isEmpty(psd)) {
                    mPasswordEt.setSelection(mPasswordEt.getText().toString().length());
                }
                mIsHidePassword = !mIsHidePassword;
                break;
            case R.id.login_button:
                mPresenter.clickLoginBtn(mUsernameEt.getText().toString(), mPasswordEt.getText().toString());
//                Intent intent = new Intent(LoginActivity.this, QualityMangeMainActivity.class);
//                startActivity(intent);
                break;
            case R.id.login_forget_password:
                mPresenter.forgetPassword();
                break;
        }
    }

    private void initDataForActivity() {

        initData();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] PERMISSIONS_STORAGE = {
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            };
            requestPermission(PERMISSIONS_STORAGE, 0);
        }
    }

    private void initData() {
        mPresenter = new LoginPresenter(this);
        mPresenter.initData(getIntent());
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPresenter.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.onDestroy();
            mPresenter = null;
        }
    }

    @Override
    public void showLoadingDialog() {
        showLoadDialog(true);
    }

    @Override
    public void dismissLoadingDialog() {
        dismissLoadDialog();
    }


    @Override
    public Activity getActivity() {
        return this;
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    private long mStartTime = -1;


    @Override
    public void onBackPressed() {
        if (mStartTime == -1) {
            mStartTime = SystemClock.currentThreadTimeMillis();
            ToastManager.show("再按一次退出BIM协同！");
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mStartTime = -1;
                }
            }, 2000);
        } else {
            LoginActivity.this.finish();
        }
    }

    @Override
    public void showErrorDialog() {
        if(mErrorDialog == null){
            mErrorDialog = new ErrorMessageDialog(this);
            mErrorDialog.builder(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    hideErrorDialog();
                    mPresenter.forgetPassword();
                }
            });
        }
        mErrorDialog.show();
    }

    private void hideErrorDialog(){
        if(mErrorDialog!=null){
            mErrorDialog.dismiss();
        }
    }
}

