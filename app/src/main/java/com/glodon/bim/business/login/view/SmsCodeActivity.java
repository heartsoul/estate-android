package com.glodon.bim.business.login.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.glodon.bim.R;
import com.glodon.bim.base.BaseActivity;
import com.glodon.bim.customview.ToastManager;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Func1;

/**
 * 描述：忘记密码-短信验证码界面
 * 作者：zhourf on 2017/9/8
 * 邮箱：zhourf@glodon.com
 */
public class SmsCodeActivity extends BaseActivity implements View.OnClickListener {

    private View mStatusView;
    private RelativeLayout mBackView;
    private TextView mHintView;

    private TextView mCodeTop;
    private EditText mCodeText;
    private ImageView mCodeDelete;
    private TextView mCodeView;

    private Button mSubmitView;

    private String mCurrentCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_sms_code_activity);

        initView();
        initStatusBar(mStatusView);
        setListener();
        initData();
    }

    private void initView() {
        mStatusView = findViewById(R.id.sms_code_statusview);
        mBackView = (RelativeLayout) findViewById(R.id.sms_code_back);

        mHintView = (TextView) findViewById(R.id.sms_code_hint);
        mCodeTop = (TextView) findViewById(R.id.sms_code_top);

        mCodeText = (EditText) findViewById(R.id.sms_code_text);
        mCodeDelete = (ImageView) findViewById(R.id.sms_code_delete);
        mCodeView = (TextView) findViewById(R.id.sms_code_view);

        mSubmitView = (Button) findViewById(R.id.sms_code_next);
    }


    private void setListener() {
        mBackView.setOnClickListener(this);

        mCodeDelete.setOnClickListener(this);
        mCodeView.setOnClickListener(this);

        mCodeText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = mCodeText.getText().toString().toString();
                if (!TextUtils.isEmpty(text)) {
                    mCodeDelete.setVisibility(View.VISIBLE);
                } else {
                    mCodeDelete.setVisibility(View.GONE);
                }
                showNextButton();
            }
        });
        mCodeText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    mCodeTop.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void initData() {
        sendSms();
    }

    private void showNextButton() {
        String code = mCodeText.getText().toString().trim();
        if (!TextUtils.isEmpty(code)) {
            mSubmitView.setBackgroundResource(R.drawable.corner_radius_33_blue_bg);
            mSubmitView.setOnClickListener(this);
        } else {
            mSubmitView.setBackgroundResource(R.drawable.corner_radius_33_gray_bg);
            mSubmitView.setOnClickListener(null);
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.sms_code_back://返回键
                mActivity.finish();
                break;
            case R.id.sms_code_delete://删除
                mCodeText.setText("");
                break;
            case R.id.sms_code_view://获取验证码
                sendSms();
                break;
            case R.id.sms_code_next://下一步
                toNext();

                break;
        }
    }

    private void sendSms(){
        changeTime();
    }

    private void changeTime(){
        countdown(5)
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
//                        appendLog("开始计时");
                        mCodeView.setOnClickListener(null);
                    }
                })
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        mCodeView.setText("获取验证码");
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        mCodeView.setText("重新发送" + integer.intValue()+"(s)");
                        timeFinish();
                    }
                });

    }

    private void timeFinish(){
        mCodeView.setOnClickListener(this);
    }

    private Observable<Integer> countdown(int time) {
        if (time < 0) time = 0;
        final int countTime = time;
        return Observable.interval(0, 1, TimeUnit.SECONDS)
//                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<Long, Integer>() {
                    @Override
                    public Integer call(Long increaseTime) {
                        return countTime - increaseTime.intValue();
                    }
                })
                .take(countTime + 1);

    }


    private void toNext() {
        String code = mCodeText.getText().toString().trim();
        if (!TextUtils.isEmpty(code) && code.equals(mCurrentCode)) {
            Intent intent = new Intent(mActivity, ResetPasswordActivity.class);
            startActivity(intent);
        } else {
            ToastManager.show("验证码错误，请重试！");
            mCodeText.setText("");
        }
    }
}
