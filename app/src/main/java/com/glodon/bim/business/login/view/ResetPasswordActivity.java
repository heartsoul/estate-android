package com.glodon.bim.business.login.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.glodon.bim.R;
import com.glodon.bim.base.BaseActivity;
import com.glodon.bim.business.login.contract.ResetPasswordContract;
import com.glodon.bim.business.login.presenter.ResetPasswordPresenter;

import java.lang.reflect.Field;

/**
 * 描述：忘记密码-短信验证码界面
 * 作者：zhourf on 2017/9/8
 * 邮箱：zhourf@glodon.com
 */
public class ResetPasswordActivity extends BaseActivity implements View.OnClickListener,ResetPasswordContract.View{

    private View mStatusView;
    private RelativeLayout mBackView;
    private TextView mNewTop;
    private EditText mNewText;
    private RelativeLayout mNewDelete;
    private ImageView mNewEye;
    private View mNewLineGray,mNewLineBlue;

    private TextView mConfirmTop;
    private EditText mConfirmText;
    private RelativeLayout mConfirmDelete;
    private ImageView mConfirmEye;
    private View mConfirmLineGray,mConfirmLineBlue,mConfirmLineRed;
    private TextView mErrorView;

    private Button mSubmitView;

    private ResetPasswordContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_reset_password_activity);

        initView();
        initStatusBar(mStatusView);
        setListener();
        initData();
    }

    private void initView() {
        mStatusView = findViewById(R.id.reset_password_statusview);
        mBackView = (RelativeLayout) findViewById(R.id.reset_password_back);

        mNewTop = (TextView) findViewById(R.id.reset_password_new_top);
        mNewText = (EditText) findViewById(R.id.reset_password_new_text);
        mNewDelete = (RelativeLayout) findViewById(R.id.reset_password_new_delete);
        mNewEye = (ImageView) findViewById(R.id.reset_password_new_eye);
        mNewLineBlue = findViewById(R.id.reset_password_new_line_blue);
        mNewLineGray = findViewById(R.id.reset_password_new_line_gray);

        mConfirmTop = (TextView) findViewById(R.id.reset_password_confirm_top);
        mConfirmText = (EditText) findViewById(R.id.reset_password_confirm_text);
        mConfirmDelete = (RelativeLayout) findViewById(R.id.reset_password_confirm_delete);
        mConfirmEye = (ImageView) findViewById(R.id.reset_password_confirm_eye);
        mConfirmLineBlue = findViewById(R.id.reset_password_confirm_line_blue);
        mConfirmLineGray = findViewById(R.id.reset_password_confirm_line_gray);
        mConfirmLineRed = findViewById(R.id.reset_password_confirm_line_red);
        mErrorView = (TextView) findViewById(R.id.reset_password_error);

        mSubmitView = (Button) findViewById(R.id.reset_password_finish);
    }


    private void setListener() {
        mBackView.setOnClickListener(this);

        mNewDelete.setOnClickListener(this);
        mNewEye.setOnClickListener(this);
        mConfirmDelete.setOnClickListener(this);
        mConfirmEye.setOnClickListener(this);

        mNewText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = mNewText.getText().toString().toString();
                if (!TextUtils.isEmpty(text)) {
                    mNewDelete.setVisibility(View.VISIBLE);
                    mNewTop.setVisibility(View.VISIBLE);
                } else {
                    mNewDelete.setVisibility(View.GONE);
                }
                showNextButton();
            }
        });
        mNewText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                String text = mNewText.getText().toString().trim();
                if (hasFocus) {
                    mNewLineGray.setVisibility(View.GONE);
                    mNewLineBlue.setVisibility(View.VISIBLE);
                    mNewTop.setVisibility(View.VISIBLE);
                    if(TextUtils.isEmpty(text)){
                        mNewText.setHint("6-20位");
                        mNewDelete.setVisibility(View.GONE);
                    }else{
                        mNewDelete.setVisibility(View.VISIBLE);
                    }

                }else{
                    mNewLineGray.setVisibility(View.VISIBLE);
                    mNewLineBlue.setVisibility(View.GONE);
                    mNewDelete.setVisibility(View.GONE);
                    if(TextUtils.isEmpty(text)){
                        mNewText.setHint("新密码");
                        mNewTop.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });

        mConfirmText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = mConfirmText.getText().toString().toString();
                if (!TextUtils.isEmpty(text)) {
                    mConfirmDelete.setVisibility(View.VISIBLE);
                    mConfirmTop.setVisibility(View.VISIBLE);
                } else {
                    mConfirmDelete.setVisibility(View.GONE);
                }
                showNextButton();
            }
        });
        mConfirmText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                String text = mConfirmText.getText().toString().trim();
                if (hasFocus) {
                    mConfirmLineGray.setVisibility(View.GONE);
                    mConfirmLineBlue.setVisibility(View.VISIBLE);
                    mConfirmTop.setVisibility(View.VISIBLE);
                    if(TextUtils.isEmpty(text)){
                        mConfirmDelete.setVisibility(View.GONE);
                    }else{
                        mConfirmDelete.setVisibility(View.VISIBLE);
                    }
                }else{
                    hideError();
                    mConfirmLineGray.setVisibility(View.VISIBLE);
                    mConfirmLineBlue.setVisibility(View.GONE);
                    mConfirmDelete.setVisibility(View.GONE);
                    if(TextUtils.isEmpty(text)){
                        mConfirmTop.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });
    }

    private void initData() {
        mPresenter = new ResetPasswordPresenter(this);
        mPresenter.initData(getIntent());
    }



    private boolean mIsNewHide = true;
    private boolean mIsConfirmHide = true;
    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.reset_password_back://返回键
                mActivity.finish();
                break;
            case R.id.reset_password_new_delete://新密码删除
                mNewText.setText("");
                break;
            case R.id.reset_password_new_eye:
                if (mIsNewHide) {
                    //如果选中，显示密码
                    mNewText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    mNewEye.setBackgroundResource(R.drawable.icon_login_password_show);
                } else {
                    //否则隐藏密码
                    mNewText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    mNewEye.setBackgroundResource(R.drawable.icon_login_password_hide);
                }
                //更改光标位置
                String psd = mNewText.getText().toString();
                if (!TextUtils.isEmpty(psd)) {
                    mNewText.setSelection(mNewText.getText().toString().length());
                }
                mIsNewHide = !mIsNewHide;
                break;
            case R.id.reset_password_confirm_delete://确认密码删除
                mConfirmText.setText("");
                break;
            case R.id.reset_password_confirm_eye:
                if (mIsConfirmHide) {
                    //如果选中，显示密码
                    mConfirmText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    mConfirmEye.setBackgroundResource(R.drawable.icon_login_password_show);
                } else {
                    //否则隐藏密码
                    mConfirmText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    mConfirmEye.setBackgroundResource(R.drawable.icon_login_password_hide);
                }
                //更改光标位置
                String psdd = mConfirmText.getText().toString();
                if (!TextUtils.isEmpty(psdd)) {
                    mConfirmText.setSelection(mConfirmText.getText().toString().length());
                }
                mIsConfirmHide = !mIsConfirmHide;
                break;
            case R.id.reset_password_finish://下一步
                toNext();

                break;
        }
    }

    private void showNextButton() {
        String newPsd = mNewText.getText().toString().trim();
        String confirmPsd = mConfirmText.getText().toString().trim();
        if (!TextUtils.isEmpty(newPsd) && !TextUtils.isEmpty(confirmPsd) && newPsd.length()>=6 && confirmPsd.length()>=6 && newPsd.length() == confirmPsd.length()) {
            mSubmitView.setBackgroundResource(R.drawable.corner_radius_33_blue_bg);
            mSubmitView.setOnClickListener(this);
        } else {
            mSubmitView.setBackgroundResource(R.drawable.corner_radius_33_gray_bg);
            mSubmitView.setOnClickListener(null);
        }
    }

    private void toNext() {
        String newPsd = mNewText.getText().toString().trim();
        String confirmPsd = mConfirmText.getText().toString().trim();
        if(newPsd.equals(confirmPsd)){
            hideError();
            mPresenter.resetPwd(newPsd);
        }else{
            showError();
        }

    }

    private void showError(){
        //让编辑框弹出来，并显示对谁进行评论
        mConfirmText.setFocusable(true);
        mConfirmText.setFocusableInTouchMode(true);
        mConfirmText.requestFocus();

        mConfirmLineBlue.setVisibility(View.GONE);
        mConfirmLineGray.setVisibility(View.GONE);
        mConfirmLineRed.setVisibility(View.VISIBLE);
        mErrorView.setVisibility(View.VISIBLE);
        try {//修改光标的颜色（反射）
            Field f = TextView.class.getDeclaredField("mCursorDrawableRes");
            f.setAccessible(true);
            f.set(mConfirmText, R.drawable.edit_cursor_color_red);
        } catch (Exception ignored) {
        }
    }

    private void hideError(){
        mConfirmLineRed.setVisibility(View.GONE);
        mConfirmLineBlue.setVisibility(View.VISIBLE);
        mErrorView.setVisibility(View.INVISIBLE);
        try {//修改光标的颜色（反射）
            Field f = TextView.class.getDeclaredField("mCursorDrawableRes");
            f.setAccessible(true);
            f.set(mConfirmText, R.drawable.edit_cursor_color);
        } catch (Exception ignored) {
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
        return mActivity;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(mPresenter!=null){
            mPresenter.onActivityResult(requestCode, resultCode, data);
        }
    }
}
