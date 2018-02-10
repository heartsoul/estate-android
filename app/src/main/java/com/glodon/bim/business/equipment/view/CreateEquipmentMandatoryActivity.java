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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.glodon.bim.R;
import com.glodon.bim.base.BaseActivity;
import com.glodon.bim.basic.listener.ThrottleClickEvents;
import com.glodon.bim.basic.utils.DateUtil;
import com.glodon.bim.basic.utils.ResourceUtil;
import com.glodon.bim.business.equipment.bean.CreateEquipmentMandatoryInfo;
import com.glodon.bim.business.equipment.contract.CreateEquipmentMandatoryContract;
import com.glodon.bim.business.equipment.presenter.CreateEquipmentMandatoryPresenter;
import com.glodon.bim.business.qualityManage.bean.InspectionCompanyItem;
import com.glodon.bim.business.qualityManage.listener.OnChooseListListener;
import com.glodon.bim.business.qualityManage.view.ChooseListDialog;
import com.glodon.bim.customview.datepicker.CustomDatePickerUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 描述：创建材设进场记录-必填项页面
 * 作者：zhourf on 2017/9/8
 * 邮箱：zhourf@glodon.com
 */
public class CreateEquipmentMandatoryActivity extends BaseActivity implements View.OnClickListener ,CreateEquipmentMandatoryContract.View{

    private View mStatusView;
    private RelativeLayout mBackView;
    private TextView mTitleView;
    private LinearLayout mInspectionParent;
    private TextView mInspectionText;
    private EditText mIndexEt,mCodeEt,mNameEt;
    private RelativeLayout mIndexDelete,mCodeDelete,mNameDelete;
    private LinearLayout mDateParent;
    private TextView mDateTv;
    private Button mNextBtn;
    private CreateEquipmentMandatoryContract.Presenter mPresenter;
    private String mDate;
    private InspectionCompanyItem mSelectAcceptionItem;

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
        mTitleView = (TextView) findViewById(R.id.create_equipment_mandatory_header_title);
        mInspectionParent = (LinearLayout) findViewById(R.id.create_equipment_mandatory_inspection);
        mInspectionText = (TextView) findViewById(R.id.create_equipment_mandatory_inspection_text);
        mIndexEt = (EditText) findViewById(R.id.create_equipment_mandatory_index);
        mCodeEt = (EditText) findViewById(R.id.create_equipment_mandatory_code);
        mNameEt = (EditText) findViewById(R.id.create_equipment_mandatory_name);
        mIndexDelete = (RelativeLayout) findViewById(R.id.create_equipment_mandatory_index_delete);
        mCodeDelete = (RelativeLayout) findViewById(R.id.create_equipment_mandatory_code_delete);
        mNameDelete = (RelativeLayout) findViewById(R.id.create_equipment_mandatory_name_delete);
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
        ThrottleClickEvents.throttleClick(mInspectionParent,this);

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
        String company = mInspectionText.getText().toString().trim();
        String index = mIndexEt.getText().toString().trim();
        String date = mDateTv.getText().toString().trim();
        String code = mCodeEt.getText().toString().trim();
        String name = mNameEt.getText().toString().trim();
        mIndexDelete.setVisibility(TextUtils.isEmpty(index)?View.GONE:View.VISIBLE);
        mCodeDelete.setVisibility(TextUtils.isEmpty(code)?View.GONE:View.VISIBLE);
        mNameDelete.setVisibility(TextUtils.isEmpty(name)?View.GONE:View.VISIBLE);
        if(!TextUtils.isEmpty(index)&&!TextUtils.isEmpty(date)&&!TextUtils.isEmpty(code)&&!TextUtils.isEmpty(name)&&!TextUtils.isEmpty(company)){
            mNextBtn.setOnClickListener(this);
            mNextBtn.setBackgroundResource(R.drawable.corner_radius_33_blue_bg);
        }else{
            mNextBtn.setOnClickListener(null);
            mNextBtn.setBackgroundResource(R.drawable.corner_radius_33_gray_bg);
        }
    }

    private void initData() {
        mPresenter = new CreateEquipmentMandatoryPresenter(this);
        mPresenter.initData(getIntent());
    }

    @Override
    public void showMandatoryInfo(CreateEquipmentMandatoryInfo info) {
        mSelectAcceptionItem = new InspectionCompanyItem();
        mSelectAcceptionItem.id = info.acceptanceCompanyId;
        mSelectAcceptionItem.name = info.acceptanceCompanyName;

        mInspectionText.setText(info.acceptanceCompanyName);
        mIndexEt.setText(info.batchCode);
        mDate = info.approachDate;
        mDateTv.setText(DateUtil.getShowDate(mDate));
        mCodeEt.setText(info.facilityCode);
        mNameEt.setText(info.facilityName);
        mNextBtn.setText(ResourceUtil.getResourceString(R.string.str_sure));
        mTitleView.setText(ResourceUtil.getResourceString(R.string.str_equipment_edit_title));
    }

    @Override
    public void showAccpecionCompany(InspectionCompanyItem item) {
        mSelectAcceptionItem = item;
        mInspectionText.setText(item.name);
    }

    @Override
    public void showCompanyList(final List<InspectionCompanyItem> mInspectionCompanyItems, int position) {
        ChooseListDialog mInspectionCompanyListDialog = new ChooseListDialog(getActivity(), position, ResourceUtil.getResourceString(R.string.str_equipment_edit_choose_company));
        final List<String> mInspectionCompanyNameList = new ArrayList<>();
        for(InspectionCompanyItem item:mInspectionCompanyItems){
            mInspectionCompanyNameList.add(item.name);
        }
        mInspectionCompanyListDialog.builder(new OnChooseListListener() {
            @Override
            public void onSelect(int position) {
                mSelectAcceptionItem = mInspectionCompanyItems.get(position);
                mInspectionText.setText(mInspectionCompanyNameList.get(position));

            }
        }, mInspectionCompanyNameList);
        mInspectionCompanyListDialog.show();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id)
        {
            case R.id.create_equipment_mandatory_inspection:
                mPresenter.showAcceptionCompany(mSelectAcceptionItem);
                break;
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
                calendar.set(map.get("year"), map.get("month") - 1, map.get("approachDate"));
                Date date = calendar.getTime();
                String time = (new SimpleDateFormat("yyyy-MM-dd")).format(date);
                mDateTv.setText(time);
                mDate = calendar.getTimeInMillis() + "";
                showNext();
            }
        });
    }

    private void toNext() {
        CreateEquipmentMandatoryInfo info = new CreateEquipmentMandatoryInfo();
        if(mSelectAcceptionItem!=null){
            info.acceptanceCompanyId = mSelectAcceptionItem.id;
            info.acceptanceCompanyName = mSelectAcceptionItem.name;
        }
        info.batchCode = mIndexEt.getText().toString().trim();
        info.approachDate = mDate;
        info.facilityCode = mCodeEt.getText().toString().trim();
        info.facilityName = mNameEt.getText().toString().trim();
        mPresenter.toNotMandatory(info);
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
