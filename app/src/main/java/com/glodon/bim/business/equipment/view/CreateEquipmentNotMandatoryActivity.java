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
import com.glodon.bim.business.equipment.bean.CreateEquipmentMandatoryNotInfo;
import com.glodon.bim.business.equipment.contract.CreateEquipmentNotMandatoryContract;
import com.glodon.bim.business.equipment.presenter.CreateEquipmentNotMandatoryPresenter;

/**
 * 描述：创建材设进场记录-非必填项页面
 * 作者：zhourf on 2017/9/8
 * 邮箱：zhourf@glodon.com
 */
public class CreateEquipmentNotMandatoryActivity extends BaseActivity implements View.OnClickListener, CreateEquipmentNotMandatoryContract.View {

    private View mStatusView;
    private RelativeLayout mBackView;
    private TextView mTitleView;
    private EditText mNumEt, mUnitEt, mSpecEt, mModelNumEt, mFactoryEt, mMakeEt, mSupplierEt;
    private RelativeLayout mNumDelete, mUnitDelete, mSpecDelete, mModelNumDelete, mFactoryDelete, mMakeDelete, mSupplierDelete;
    private LinearLayout mModelParent;
    private TextView mModelTv, mSkipTv;
    private Button mNextBtn;
    private CreateEquipmentNotMandatoryContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.equipment_create_not_mandatory_activity);

        initView();
        initStatusBar(mStatusView);
        setListener();
        initData();
    }

    private void initView() {
        mStatusView = findViewById(R.id.create_equipment_not_mandatory_header_top);
        mBackView = (RelativeLayout) findViewById(R.id.create_equipment_not_mandatory_header_back);
        mTitleView = (TextView) findViewById(R.id.create_equipment_not_mandatory_header_title);
        mNumEt = (EditText) findViewById(R.id.create_equipment_not_mandatory_num);
        mUnitEt = (EditText) findViewById(R.id.create_equipment_not_mandatory_unit);
        mSpecEt = (EditText) findViewById(R.id.create_equipment_not_mandatory_spec);
        mModelNumEt = (EditText) findViewById(R.id.create_equipment_not_mandatory_modelnum);
        mFactoryEt = (EditText) findViewById(R.id.create_equipment_not_mandatory_factory);
        mMakeEt = (EditText) findViewById(R.id.create_equipment_not_mandatory_make);
        mSupplierEt = (EditText) findViewById(R.id.create_equipment_not_mandatory_supplier);

        mNumDelete = (RelativeLayout) findViewById(R.id.create_equipment_not_mandatory_num_delete);
        mUnitDelete = (RelativeLayout) findViewById(R.id.create_equipment_not_mandatory_unit_delete);
        mSpecDelete = (RelativeLayout) findViewById(R.id.create_equipment_not_mandatory_spec_delete);
        mModelNumDelete = (RelativeLayout) findViewById(R.id.create_equipment_not_mandatory_modelnum_delete);
        mFactoryDelete = (RelativeLayout) findViewById(R.id.create_equipment_not_mandatory_factory_delete);
        mMakeDelete = (RelativeLayout) findViewById(R.id.create_equipment_not_mandatory_make_delete);
        mSupplierDelete = (RelativeLayout) findViewById(R.id.create_equipment_not_mandatory_supplier_delete);

        mModelParent = (LinearLayout) findViewById(R.id.create_equipment_not_mandatory_model_parent);
        mModelTv = (TextView) findViewById(R.id.create_equipment_not_mandatory_model);
        mNextBtn = (Button) findViewById(R.id.create_equipment_not_mandatory_next);
        mSkipTv = (TextView) findViewById(R.id.create_equipment_not_mandatory_skip);
    }


    private void setListener() {
        mBackView.setOnClickListener(this);

        mNumDelete.setOnClickListener(this);
        mUnitDelete.setOnClickListener(this);
        mSpecDelete.setOnClickListener(this);
        mModelNumDelete.setOnClickListener(this);
        mFactoryDelete.setOnClickListener(this);
        mMakeDelete.setOnClickListener(this);
        mSupplierDelete.setOnClickListener(this);

        mModelParent.setOnClickListener(this);
        mNextBtn.setOnClickListener(this);
        mSkipTv.setOnClickListener(this);

        addTextWatcher(mNumEt,mNumDelete);
        addTextWatcher(mUnitEt,mUnitDelete);
        addTextWatcher(mSpecEt,mSpecDelete);
        addTextWatcher(mModelNumEt,mModelNumDelete);
        addTextWatcher(mFactoryEt,mFactoryDelete);
        addTextWatcher(mMakeEt,mMakeDelete);
        addTextWatcher(mSupplierEt,mSupplierDelete);
    }

    private void addTextWatcher(final EditText et, final RelativeLayout delete) {
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String content = et.getText().toString().trim();
                delete.setVisibility(TextUtils.isEmpty(content) ? View.GONE : View.VISIBLE);
            }
        });
    }

    private void initData() {
        mPresenter = new CreateEquipmentNotMandatoryPresenter(this);
        mPresenter.initData(getIntent());
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.create_equipment_not_mandatory_header_back:
                mActivity.finish();
                break;
            case R.id.create_equipment_not_mandatory_num_delete:
                mNumEt.setText("");
                break;
            case R.id.create_equipment_not_mandatory_unit_delete:
                mUnitEt.setText("");
                break;
            case R.id.create_equipment_not_mandatory_spec_delete:
                mSpecEt.setText("");
                break;
            case R.id.create_equipment_not_mandatory_modelnum_delete:
                mModelNumEt.setText("");
                break;
            case R.id.create_equipment_not_mandatory_factory_delete:
                mFactoryEt.setText("");
                break;
            case R.id.create_equipment_not_mandatory_make_delete:
                mMakeEt.setText("");
                break;
            case R.id.create_equipment_not_mandatory_supplier_delete:
                mSupplierEt.setText("");
                break;
            case R.id.create_equipment_not_mandatory_model_parent://构件位置
                mPresenter.toModel();
                break;
            case R.id.create_equipment_not_mandatory_next:
                toNext();
                break;
            case R.id.create_equipment_not_mandatory_skip:
                toSkip();
                break;
        }
    }

    private void toSkip() {
        mPresenter.toSkip();
    }


    private void toNext() {
        CreateEquipmentMandatoryNotInfo info = new CreateEquipmentMandatoryNotInfo();
        info.num = mNumEt.getText().toString().trim();
//        if(TextUtils.isEmpty(num)){
//            info.num = 0;
//        }else{
//            info.num = Long.parseLong(num);
//        }
        info.unit = mUnitEt.getText().toString().trim();
        info.spec = mSpecEt.getText().toString().trim();
        info.modelnum = mModelNumEt.getText().toString().trim();
        info.factory = mFactoryEt.getText().toString().trim();
        info.make = mMakeEt.getText().toString().trim();
        info.supplier = mSupplierEt.getText().toString().trim();
        mPresenter.toNext(info);

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
        if (mPresenter != null) {
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

    @Override
    public void showModelName(String elementName) {
        mModelTv.setText(elementName);
    }

    @Override
    public void showEditInfo(CreateEquipmentMandatoryNotInfo info) {
        mTitleView.setText("编辑材设详情");
        mNextBtn.setText("确定");
        mSkipTv.setVisibility(View.GONE);
        mNumEt.setText(info.num);
        mUnitEt.setText(info.unit);
        mSpecEt.setText(info.spec);
        mModelNumEt.setText(info.modelnum);
        if(info.model!=null && info.model.component!=null) {
            mModelTv.setText(info.model.component.elementName);
        }
        mFactoryEt.setText(info.factory);
        mMakeEt.setText(info.make);
        mSupplierEt.setText(info.supplier);
    }
}
