package com.glodon.bim.business.equipment.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.glodon.bim.R;
import com.glodon.bim.base.BaseActivity;
import com.glodon.bim.basic.image.ImageLoader;
import com.glodon.bim.basic.listener.ThrottleClickEvents;
import com.glodon.bim.basic.utils.CameraUtil;
import com.glodon.bim.basic.utils.DateUtil;
import com.glodon.bim.basic.utils.LinkedHashList;
import com.glodon.bim.business.equipment.bean.CreateEquipmentMandatoryInfo;
import com.glodon.bim.business.equipment.bean.CreateEquipmentMandatoryNotInfo;
import com.glodon.bim.business.equipment.bean.CreateEquipmentPictureInfo;
import com.glodon.bim.business.equipment.contract.CreateEquipmentContract;
import com.glodon.bim.business.equipment.presenter.CreateEquipmentPresenter;
import com.glodon.bim.business.qualityManage.view.SaveDeleteDialog;
import com.glodon.bim.customview.album.ImageItem;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：创建材设进场记录-提交页面
 * 作者：zhourf on 2017/9/8
 * 邮箱：zhourf@glodon.com
 */
public class CreateEquipmentActivity extends BaseActivity implements View.OnClickListener ,CreateEquipmentContract.View{

    //导航栏
    private View mStatusView;
    private RelativeLayout mBackView;
    private TextView mSubmitView;
    //基本信息
    private LinearLayout mBasicParent;
    private TextView mAccepitonCompany,mIndexView,mDateView,mCodeView,mNameView;
    private ImageView mStandardView;

    //其他信息
    private LinearLayout mOtherParent;
    private TextView mNumView,mUnitView,mSpecView,mModelNumView,mModelView,mFactoryView,mMakeView,mSupplierView;
    //照片信息
    private LinearLayout mPhotoParent;
    private ImageView mPhoto0,mPhoto1,mPhoto2;
    //保存删除
    private Button mSaveBtn,mDeleteBtn;

    //三个右边箭头
    private ImageView mBasicArrow,mOtherArrow,mPhotoArrow;

    private CreateEquipmentContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.equipment_create_activity);

        initView();
        initStatusBar(mStatusView);
        setListener();
        initData();
    }

    private void initView() {
        mStatusView = findViewById(R.id.create_equipment_header_top);
        mBackView = (RelativeLayout) findViewById(R.id.create_equipment_header_back);
        mSubmitView = (TextView) findViewById(R.id.create_equipment_header_submit);
        //基本信息
        mBasicParent = (LinearLayout) findViewById(R.id.create_equipment_basic);
        mAccepitonCompany = (TextView) findViewById(R.id.create_equipment_basic_acception);
        mIndexView = (TextView) findViewById(R.id.create_equipment_basic_index);
        mDateView = (TextView) findViewById(R.id.create_equipment_basic_date);
        mCodeView = (TextView) findViewById(R.id.create_equipment_basic_code);
        mNameView = (TextView) findViewById(R.id.create_equipment_basic_name);
        mStandardView = (ImageView) findViewById(R.id.create_equipment_basic_standard);

        //其他信息
        mOtherParent = (LinearLayout) findViewById(R.id.create_equipment_other);
        mNumView = (TextView) findViewById(R.id.create_equipment_other_num);
        mUnitView = (TextView) findViewById(R.id.create_equipment_other_unit);
        mSpecView = (TextView) findViewById(R.id.create_equipment_other_spec);
        mModelNumView = (TextView) findViewById(R.id.create_equipment_other_modelnum);
        mModelView = (TextView) findViewById(R.id.create_equipment_other_model);
        mModelView.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG );
        mFactoryView = (TextView) findViewById(R.id.create_equipment_other_factory);
        mMakeView = (TextView) findViewById(R.id.create_equipment_other_make);
        mSupplierView = (TextView) findViewById(R.id.create_equipment_other_supplier);
        //照片信息
        mPhotoParent = (LinearLayout) findViewById(R.id.create_equipment_picture);
        mPhoto0 = (ImageView) findViewById(R.id.create_equipment_picture_0);
        mPhoto1 = (ImageView) findViewById(R.id.create_equipment_picture_1);
        mPhoto2 = (ImageView) findViewById(R.id.create_equipment_picture_2);
        //保存删除
        mSaveBtn = (Button) findViewById(R.id.create_equipment_save);
        mDeleteBtn = (Button) findViewById(R.id.create_equipment_delete);

        //三个箭头

        mBasicArrow = (ImageView) findViewById(R.id.create_equipment_basic_arrow);
        mOtherArrow = (ImageView) findViewById(R.id.create_equipment_other_arrow);
        mPhotoArrow = (ImageView) findViewById(R.id.create_equipment_picture_arrow);
    }


    private void setListener() {
        mBackView.setOnClickListener(this);
        ThrottleClickEvents.throttleClick(mSubmitView, this);

        ThrottleClickEvents.throttleClick(mBasicParent, this);
        ThrottleClickEvents.throttleClick(mOtherParent, this);
        ThrottleClickEvents.throttleClick(mPhotoParent, this);

        ThrottleClickEvents.throttleClick(mPhoto0, this, 1);
        ThrottleClickEvents.throttleClick(mPhoto1, this, 1);
        ThrottleClickEvents.throttleClick(mPhoto2, this, 1);

        ThrottleClickEvents.throttleClick(mModelView, this, 1);
        ThrottleClickEvents.throttleClick(mSaveBtn, this);
        ThrottleClickEvents.throttleClick(mDeleteBtn, this);

    }


    private void initData() {
        mPresenter = new CreateEquipmentPresenter(this);
        mPresenter.initData(getIntent());
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id)
        {
            case R.id.create_equipment_header_back:
                back();
                break;

            case R.id.create_equipment_header_submit:
                mPresenter.submit();
                break;
            case R.id.create_equipment_basic:
                mPresenter.toBasic();
                break;
            case R.id.create_equipment_other:
                mPresenter.toOther();
                break;
            case R.id.create_equipment_picture:
                mPresenter.toPicture();
                break;
            case R.id.create_equipment_picture_0:
                mPresenter.toPreview(0);
                break;
            case R.id.create_equipment_picture_1:
                mPresenter.toPreview(1);
                break;
            case R.id.create_equipment_picture_2:
                mPresenter.toPreview(2);
                break;
            case R.id.create_equipment_other_model:
                mPresenter.toModel();
                break;
            case R.id.create_equipment_save:
                mPresenter.save();
                break;
            case R.id.create_equipment_delete:
                SaveDeleteDialog mDeleteDialog = new SaveDeleteDialog(getActivity());
                mDeleteDialog.getDeleteDialog(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //调用接口删除
                        mPresenter.delete();
                    }
                });
                mDeleteDialog.show();

                break;
        }
    }

    @Override
    public void onBackPressed() {
        back();
    }

    private void back(){
        mPresenter.back();
    }

    @Override
    public void showBasicInfo(CreateEquipmentMandatoryInfo mMandatoryInfo) {
        mAccepitonCompany.setText(mMandatoryInfo.acceptanceCompanyName);
        mIndexView.setText(mMandatoryInfo.batchCode);
        mDateView.setText(DateUtil.getShowDate(mMandatoryInfo.approachDate));
        mCodeView.setText(mMandatoryInfo.facilityCode);
        mNameView.setText(mMandatoryInfo.facilityName);
    }

    @Override
    public void showOtherInfo(CreateEquipmentMandatoryNotInfo mMandatoryNotInfo) {
        if(!TextUtils.isEmpty(mMandatoryNotInfo.quantity)) {
            mNumView.setText(mMandatoryNotInfo.quantity + "");
        }
        mUnitView.setText(mMandatoryNotInfo.unit);
        mSpecView.setText(mMandatoryNotInfo.specification);
        mModelNumView.setText(mMandatoryNotInfo.modelNum);
        if(mMandatoryNotInfo.model!=null && mMandatoryNotInfo.model.component!=null) {
            mModelView.setText(mMandatoryNotInfo.model.component.elementName);
        }
        mFactoryView.setText(mMandatoryNotInfo.manufacturer);
        mMakeView.setText(mMandatoryNotInfo.brand);
        mSupplierView.setText(mMandatoryNotInfo.supplier);
    }

    @Override
    public void showPictureInfo(CreateEquipmentPictureInfo mPictureInfo) {
        if(mPictureInfo.mSelectedMap!=null){
            showImages(mPictureInfo.mSelectedMap);
        }
        mStandardView.setBackgroundResource(mPictureInfo.qualified ?R.drawable.icon_up_to_standard:R.drawable.icon_not_up_to_standard);
    }

    @Override
    public void showEdit() {
        mSaveBtn.setVisibility(View.VISIBLE);
        mDeleteBtn.setVisibility(View.VISIBLE);
    }

    @Override
    public void showDetail() {
        mSaveBtn.setVisibility(View.GONE);
        mDeleteBtn.setVisibility(View.GONE);
        mSubmitView.setVisibility(View.GONE);
        mBasicParent.setOnClickListener(null);
        mOtherParent.setOnClickListener(null);
        mPhotoParent.setOnClickListener(null);
        mBasicArrow.setVisibility(View.GONE);
        mOtherArrow.setVisibility(View.GONE);
        mPhotoArrow.setVisibility(View.GONE);
    }

    @Override
    public void showDelete() {
        mDeleteBtn.setVisibility(View.VISIBLE);
    }


    public void showImages(LinkedHashList<String, ImageItem> mSelectedMap) {
        int size = mSelectedMap.size();
        List<ImageView> list = new ArrayList<>();
        list.add(mPhoto0);
        list.add(mPhoto1);
        list.add(mPhoto2);
        int position = 0;
        for (ImageItem entry : mSelectedMap.getValueList()) {
            ImageItem item = entry;
            String url = item.thumbnailPath;
            if (TextUtils.isEmpty(url)) {
                url = item.imagePath;
            }
            ImageLoader.showImageCenterCrop(getActivity(), url, list.get(position), R.drawable.icon_default_image);
            position++;
            CameraUtil.frushStyemDCIM(getActivity(), item.imagePath);
        }
        if (size == 0) {
            mPhoto0.setVisibility(View.GONE);
            mPhoto1.setVisibility(View.GONE);
            mPhoto2.setVisibility(View.GONE);
        }
        if (size == 1) {
            mPhoto0.setVisibility(View.VISIBLE);
            mPhoto1.setVisibility(View.GONE);
            mPhoto2.setVisibility(View.GONE);
        }
        if (size == 2) {
            mPhoto0.setVisibility(View.VISIBLE);
            mPhoto1.setVisibility(View.VISIBLE);
            mPhoto2.setVisibility(View.GONE);
        }
        if (size == 3) {
            mPhoto0.setVisibility(View.VISIBLE);
            mPhoto1.setVisibility(View.VISIBLE);
            mPhoto2.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showBackDialog() {
        SaveDeleteDialog mBackDialog = new SaveDeleteDialog(getActivity());
        mBackDialog.getBackDialog(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (mPresenter.isChange()) {
//                    getActivity().setResult(Activity.RESULT_OK);
//                }
                getActivity().finish();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.save();
            }
        });
        mBackDialog.show();
    }

    @Override
    public void showModelName(String elementName) {
        mModelView.setText(elementName);
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
