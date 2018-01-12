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
import com.glodon.bim.basic.listener.ThrottleClickEvents;
import com.glodon.bim.business.equipment.bean.MandatoryInfo;
import com.glodon.bim.business.equipment.contract.CreateEquipmentMandatoryContract;
import com.glodon.bim.business.equipment.contract.CreateEquipmentPictureContract;
import com.glodon.bim.business.equipment.presenter.CreateEquipmentMandatoryPresenter;
import com.glodon.bim.business.equipment.presenter.CreateEquipmentPicturePresenter;
import com.glodon.bim.customview.datepicker.CustomDatePickerUtils;
import com.glodon.bim.customview.dialog.PhotoAlbumDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * 描述：创建材设进场记录-拍照页面
 * 作者：zhourf on 2017/9/8
 * 邮箱：zhourf@glodon.com
 */
public class CreateEquipmentPictureActivity extends BaseActivity implements View.OnClickListener ,CreateEquipmentPictureContract.View{

    private View mStatusView;
    private RelativeLayout mBackView;
    private ImageView mPhoto0,mPhoto2,mPhoto3,mPhoto1,mFlag;
    private Button mNextBtn;
    private CreateEquipmentPictureContract.Presenter mPresenter;
    private PhotoAlbumDialog mPhotoAlbumDialog;
    private boolean  mState = true;//验收合格 true合格

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.equipment_create_picture_activity);

        initView();
        initStatusBar(mStatusView);
        setListener();
        initData();
    }

    private void initView() {
        mStatusView = findViewById(R.id.create_equipment_picture_header_top);
        mBackView = (RelativeLayout) findViewById(R.id.create_equipment_picture_header_back);
        mPhoto0 = (ImageView) findViewById(R.id.create_equipment_picture_photo_0);
        mPhoto1 = (ImageView) findViewById(R.id.create_equipment_picture_photo_1);
        mPhoto2 = (ImageView) findViewById(R.id.create_equipment_picture_photo_2);
        mPhoto3 = (ImageView) findViewById(R.id.create_equipment_picture_photo_3);
        mFlag = (ImageView) findViewById(R.id.create_equipment_picture_flag);
        mNextBtn = (Button) findViewById(R.id.create_equipment_mandatory_next);
    }


    private void setListener() {
        mBackView.setOnClickListener(this);
        mNextBtn.setOnClickListener(this);
        ThrottleClickEvents.throttleClick(mPhoto0, this, 1);
        ThrottleClickEvents.throttleClick(mPhoto1, this, 1);
        ThrottleClickEvents.throttleClick(mPhoto2, this, 1);
        ThrottleClickEvents.throttleClick(mPhoto3, this, 1);
        ThrottleClickEvents.throttleClick(mFlag, this, 1);


    }


    private void initData() {
        mPresenter = new CreateEquipmentPicturePresenter(this);
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
            case R.id.create_check_list_photo_0:
                mPresenter.toPreview(0);
                break;
            case R.id.create_check_list_photo_1:
                mPresenter.toPreview(1);
                break;
            case R.id.create_check_list_photo_2:
                mPresenter.toPreview(2);
                break;
            case R.id.create_check_list_photo_3://添加图片
                if (mPhotoAlbumDialog == null) {
                    mPhotoAlbumDialog = new PhotoAlbumDialog(getActivity());
                    mPhotoAlbumDialog.builder(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mPresenter.takePhoto();
                        }
                    }, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mPresenter.openAlbum();
                        }
                    });
                }
                mPhotoAlbumDialog.show();
                break;
            case R.id.create_check_list_remain_flag://验收合格开关
                clickRemain();
                break;
            case R.id.create_equipment_mandatory_next:
                toNext();
                break;
        }
    }

    private void clickRemain() {
        if (mState) {
            mFlag.setBackgroundResource(R.drawable.icon_flag_close);
        } else {
            mFlag.setBackgroundResource(R.drawable.icon_flag_open);
        }
        mState = !mState;
    }


    private void toNext() {

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
