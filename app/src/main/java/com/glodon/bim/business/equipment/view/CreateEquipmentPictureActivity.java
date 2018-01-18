package com.glodon.bim.business.equipment.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.glodon.bim.R;
import com.glodon.bim.base.BaseActivity;
import com.glodon.bim.basic.image.ImageLoader;
import com.glodon.bim.basic.listener.ThrottleClickEvents;
import com.glodon.bim.basic.utils.CameraUtil;
import com.glodon.bim.basic.utils.LinkedHashList;
import com.glodon.bim.business.equipment.bean.CreateEquipmentPictureInfo;
import com.glodon.bim.business.equipment.contract.CreateEquipmentPictureContract;
import com.glodon.bim.business.equipment.presenter.CreateEquipmentPicturePresenter;
import com.glodon.bim.customview.album.ImageItem;
import com.glodon.bim.customview.dialog.PhotoAlbumDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：创建材设进场记录-拍照页面
 * 作者：zhourf on 2017/9/8
 * 邮箱：zhourf@glodon.com
 */
public class CreateEquipmentPictureActivity extends BaseActivity implements View.OnClickListener ,CreateEquipmentPictureContract.View{

    private View mStatusView;
    private RelativeLayout mBackView;
    private TextView mTitleView;
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
        mTitleView = (TextView) findViewById(R.id.create_equipment_picture_header_title);
        mPhoto0 = (ImageView) findViewById(R.id.create_equipment_picture_photo_0);
        mPhoto1 = (ImageView) findViewById(R.id.create_equipment_picture_photo_1);
        mPhoto2 = (ImageView) findViewById(R.id.create_equipment_picture_photo_2);
        mPhoto3 = (ImageView) findViewById(R.id.create_equipment_picture_photo_3);
        mFlag = (ImageView) findViewById(R.id.create_equipment_picture_flag);
        mNextBtn = (Button) findViewById(R.id.create_equipment_picture_next);
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
            case R.id.create_equipment_picture_photo_0:
                mPresenter.toPreview(0);
                break;
            case R.id.create_equipment_picture_photo_1:
                mPresenter.toPreview(1);
                break;
            case R.id.create_equipment_picture_photo_2:
                mPresenter.toPreview(2);
                break;
            case R.id.create_equipment_picture_photo_3://添加图片
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
            case R.id.create_equipment_picture_flag://验收合格开关
                clickRemain();
                break;
            case R.id.create_equipment_picture_next:
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
        mPresenter.next(mState);
    }

    @Override
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
            mPhoto3.setVisibility(View.VISIBLE);
        }
        if (size == 1) {
            mPhoto0.setVisibility(View.VISIBLE);
            mPhoto1.setVisibility(View.GONE);
            mPhoto2.setVisibility(View.GONE);
            mPhoto3.setVisibility(View.VISIBLE);
        }
        if (size == 2) {
            mPhoto0.setVisibility(View.VISIBLE);
            mPhoto1.setVisibility(View.VISIBLE);
            mPhoto2.setVisibility(View.GONE);
            mPhoto3.setVisibility(View.VISIBLE);
        }
        if (size == 3) {
            mPhoto0.setVisibility(View.VISIBLE);
            mPhoto1.setVisibility(View.VISIBLE);
            mPhoto2.setVisibility(View.VISIBLE);
            mPhoto3.setVisibility(View.GONE);
        }
    }

    @Override
    public void showPhotoInfo(CreateEquipmentPictureInfo info) {
        mFlag.setBackgroundResource(info.qualified ?R.drawable.icon_flag_open:R.drawable.icon_flag_close);
        mState = info.qualified;
        mNextBtn.setText("确定");
        mTitleView.setText("编辑现场照片");
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
