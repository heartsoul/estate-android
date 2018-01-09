package com.glodon.bim.business.equipment.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.glodon.bim.R;
import com.glodon.bim.base.BaseActivity;
import com.glodon.bim.business.equipment.bean.MandatoryInfo;
import com.glodon.bim.business.equipment.contract.CreateEquipmentMandatoryContract;
import com.glodon.bim.business.equipment.presenter.CreateEquipmentMandatoryPresenter;
import com.glodon.bim.customview.datepicker.CustomDatePickerUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * 描述：创建材设进场记录-必填项页面
 * 作者：zhourf on 2017/9/8
 * 邮箱：zhourf@glodon.com
 */
public class CreateEquipmentMandatoryActivity extends BaseActivity implements View.OnClickListener ,CreateEquipmentMandatoryContract.View{

    private View mStatusView;
    private RelativeLayout mBackView;
    private EditText mIndexEt,mCodeEt,mNameEt;
    private ImageView mIndexDelete,mCodeDelete,mNameDelete;
    private LinearLayout mDateParent;
    private TextView mDateTv;
    private Button mNextBtn;
    private CreateEquipmentMandatoryContract.Presenter mPresenter;
    private String mDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.equipment_create_mandatory_activity);

        initView();
        initStatusBar(mStatusView);
        setListener();
        initData();
    }

    private void initView() {
        mStatusView = findViewById(R.id.create_equipment_mandatory_header_top);
        mBackView = (RelativeLayout) findViewById(R.id.create_equipment_mandatory_header_back);
        mIndexEt = (EditText) findViewById(R.id.create_equipment_mandatory_index);
        mCodeEt = (EditText) findViewById(R.id.create_equipment_mandatory_code);
        mNameEt = (EditText) findViewById(R.id.create_equipment_mandatory_name);
        mIndexDelete = (ImageView) findViewById(R.id.create_equipment_mandatory_index_delete);
        mCodeDelete = (ImageView) findViewById(R.id.create_equipment_mandatory_code_delete);
        mNameDelete = (ImageView) findViewById(R.id.create_equipment_mandatory_name_delete);
        mDateParent = (LinearLayout) findViewById(R.id.create_equipment_mandatory_date_parent);
        mDateTv = (TextView) findViewById(R.id.create_equipment_mandatory_date);
        mNextBtn = (Button) findViewById(R.id.create_equipment_mandatory_next);
    }


    private void setListener() {
        mBackView.setOnClickListener(this);
        mIndexDelete.setOnClickListener(this);
        mCodeDelete.setOnClickListener(this);
        mNameDelete.setOnClickListener(this);
        mDateParent.setOnClickListener(this);

        TextWatcher mTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                showNext();
            }
        };

        mIndexEt.addTextChangedListener(mTextWatcher);
        mCodeEt.addTextChangedListener(mTextWatcher);
        mNameEt.addTextChangedListener(mTextWatcher);
    }

    //控制是否显示下一步按钮
    private void showNext() {
        String index = mIndexEt.getText().toString().trim();
        String date = mDateTv.getText().toString().trim();
        String code = mCodeEt.getText().toString().trim();
        String name = mNameEt.getText().toString().trim();
        if(!TextUtils.isEmpty(index)&&!TextUtils.isEmpty(date)&&!TextUtils.isEmpty(code)&&!TextUtils.isEmpty(name)){
            mNextBtn.setOnClickListener(this);
        }else{
            mNextBtn.setOnClickListener(null);
        }
    }

    private void initData() {
        mPresenter = new CreateEquipmentMandatoryPresenter(this);
        mPresenter.initData(getIntent());
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id)
        {
            case R.id.create_equipment_mandatory_header_back:
                mActivity.finish();
                break;
            case R.id.create_equipment_mandatory_index_delete:
                mIndexEt.setText("");
                break;
            case R.id.create_equipment_mandatory_code_delete:
                mCodeEt.setText("");
                break;
            case R.id.create_equipment_mandatory_name_delete:
                mNameEt.setText("");
                break;
            case R.id.create_equipment_mandatory_date_parent:
                chooseDate();
                break;
            case R.id.create_equipment_mandatory_next:
                toNext();
                break;
        }
    }

    private void chooseDate() {
        CustomDatePickerUtils.showDayDialog(getActivity(), new CustomDatePickerUtils.OnDateSelectedListener() {
            @Override
            public void onDateSelected(Map<String, Integer> map) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(map.get("year"), map.get("month") - 1, map.get("date"));
                Date date = calendar.getTime();
                String time = (new SimpleDateFormat("yyyy-MM-dd")).format(date);
                mDateTv.setText(time);
                mDate = calendar.getTimeInMillis() + "";
            }
        });
    }

    private void toNext() {
        MandatoryInfo info = new MandatoryInfo();
        info.index = mIndexEt.getText().toString().trim();
        info.date = mDate;
        info.code = mCodeEt.getText().toString().trim();
        info.name = mNameEt.getText().toString().trim();
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
        if(mPresenter!=null) {
            mPresenter.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.onDestroy();
            mPresenter = null;
        }
    }
}
