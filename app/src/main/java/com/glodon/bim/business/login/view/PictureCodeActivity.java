package com.glodon.bim.business.login.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.glodon.bim.R;
import com.glodon.bim.base.BaseActivity;
import com.glodon.bim.customview.ToastManager;

import java.util.Random;

/**
 * 描述：忘记密码-图片验证码界面
 * 作者：zhourf on 2017/9/8
 * 邮箱：zhourf@glodon.com
 */
public class PictureCodeActivity extends BaseActivity implements View.OnClickListener{
    private View mStatusView;
    private RelativeLayout mBackView;
    private EditText mNameView;
    private View mNameLineGray,mNameLineBlue;
    private RelativeLayout mNameDelete;

    private TextView mCodeName;
    private EditText mCodeText;
    private RelativeLayout mCodeDelete;
    private TextView mCodeView;
    private View mCodeLineGray,mCodeLineBlue;

    private Button mSubmitView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_picture_code_activity);

        initView();
        initStatusBar(mStatusView);
        setListener();
        initData();
    }

    private void initView() {
        mStatusView = findViewById(R.id.picture_code_statusview);
        mBackView = (RelativeLayout) findViewById(R.id.picture_code_back);
        mNameView = (EditText) findViewById(R.id.picture_code_name);
        mNameLineGray = findViewById(R.id.picture_code_name_line_gray);
        mNameLineBlue = findViewById(R.id.picture_code_name_line_blue);
        mNameDelete = (RelativeLayout) findViewById(R.id.picture_code_name_delete);

        mCodeName = (TextView) findViewById(R.id.picture_code_code_name);
        mCodeText= (EditText) findViewById(R.id.picture_code_code_text);
        mCodeDelete= (RelativeLayout) findViewById(R.id.picture_code_code_text_delete);
        mCodeView= (TextView) findViewById(R.id.picture_code_code_view);
        mCodeLineGray = findViewById(R.id.picture_code_code_line_gray);
        mCodeLineBlue = findViewById(R.id.picture_code_code_line_blue);

        mSubmitView= (Button) findViewById(R.id.picture_code_next);
    }


    private void setListener() {
        mBackView.setOnClickListener(this);

        mNameDelete.setOnClickListener(this);
        mCodeDelete.setOnClickListener(this);
        mCodeView.setOnClickListener(this);

        mNameView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = mNameView.getText().toString().toString();
                if(!TextUtils.isEmpty(text)){
                    mNameDelete.setVisibility(View.VISIBLE);
                }else{
                    mNameDelete.setVisibility(View.GONE);
                }
                showNextButton();
            }
        });
        mNameView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus){
                    mNameLineGray.setVisibility(View.GONE);
                    mNameLineBlue.setVisibility(View.VISIBLE);
                    String text = mNameView.getText().toString().toString();
                    if(TextUtils.isEmpty(text)){
                        mNameDelete.setVisibility(View.GONE);
                    }else{
                        mNameDelete.setVisibility(View.VISIBLE);
                    }
                }else{
                    mNameLineGray.setVisibility(View.VISIBLE);
                    mNameLineBlue.setVisibility(View.GONE);
                    mNameDelete.setVisibility(View.GONE);
                }
            }
        });
        mCodeText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                String text = mCodeText.getText().toString().toString();
                if(hasFocus){
                    mCodeLineGray.setVisibility(View.GONE);
                    mCodeLineBlue.setVisibility(View.VISIBLE);
                    mCodeName.setVisibility(View.VISIBLE);
                    if(TextUtils.isEmpty(text)){
                        mCodeDelete.setVisibility(View.GONE);
                    }else{
                        mCodeDelete.setVisibility(View.VISIBLE);
                    }
                }else{
                    mCodeLineGray.setVisibility(View.VISIBLE);
                    mCodeLineBlue.setVisibility(View.GONE);
                    if(!TextUtils.isEmpty(text)){
                        mCodeName.setVisibility(View.VISIBLE);
                    }else{
                        mCodeName.setVisibility(View.INVISIBLE);
                    }
                    mCodeDelete.setVisibility(View.GONE);
                }
            }
        });
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
                if(!TextUtils.isEmpty(text)){
                    mCodeDelete.setVisibility(View.VISIBLE);
                    mCodeName.setVisibility(View.VISIBLE);
                }else{
                    mCodeDelete.setVisibility(View.GONE);
                }
                showNextButton();
            }
        });
    }

    private void initData() {
        changeCode();
    }

    private void showNextButton(){
        String name = mNameView.getText().toString().trim();
        String code = mCodeText.getText().toString().trim();
        if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(code)){
            mSubmitView.setBackgroundResource(R.drawable.corner_radius_33_blue_bg);
            mSubmitView.setOnClickListener(this);
        }else{
            mSubmitView.setBackgroundResource(R.drawable.corner_radius_33_gray_bg);
            mSubmitView.setOnClickListener(null);
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.picture_code_back://返回键
                mActivity.finish();
                break;
            case R.id.picture_code_name_delete://账户删除
                mNameView.setText("");
                break;
            case R.id.picture_code_code_text_delete://图片验证码删除
                mCodeText.setText("");
                break;
            case R.id.picture_code_code_view://图片验证码
                changeCode();
                break;
            case R.id.picture_code_next://下一步
                toNext();

                break;
        }
    }

    private void toNext() {
       String code = mCodeText.getText().toString().trim();
        if(!TextUtils.isEmpty(code) && code.equals(mCurrentCode)){
            Intent intent = new Intent(mActivity,SmsCodeActivity.class);
            startActivity(intent);
        }else{
            ToastManager.show("图片验证错误，请重试！");
            mCodeText.setText("");
        }
    }

    private String[] letters = {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"};
    private String mCurrentCode = "";
    //改变验证码
    private void changeCode() {
        int size = letters.length;
        StringBuilder code =new StringBuilder();
        StringBuilder codeText =new StringBuilder();
        for(int i = 0;i<4;i++){
            int random = new Random().nextInt(size);
            code.append(letters[random]);
            codeText.append(letters[random]+" ");
        }
        mCurrentCode = code.toString();
        mCodeView.setText(codeText.toString().trim());
    }


}
