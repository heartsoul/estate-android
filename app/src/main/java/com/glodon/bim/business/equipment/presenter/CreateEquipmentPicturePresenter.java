package com.glodon.bim.business.equipment.presenter;

import android.app.Activity;
import android.content.Intent;

import com.glodon.bim.basic.utils.CameraUtil;
import com.glodon.bim.basic.utils.LinkedHashList;
import com.glodon.bim.business.equipment.bean.CreateEquipmentMandatoryInfo;
import com.glodon.bim.business.equipment.bean.CreateEquipmentMandatoryNotInfo;
import com.glodon.bim.business.equipment.bean.CreateEquipmentPictureInfo;
import com.glodon.bim.business.equipment.contract.CreateEquipmentPictureContract;
import com.glodon.bim.business.equipment.view.CreateEquipmentActivity;
import com.glodon.bim.business.qualityManage.view.PhotoEditActivity;
import com.glodon.bim.common.config.CommonConfig;
import com.glodon.bim.common.config.RequestCodeConfig;
import com.glodon.bim.customview.album.AlbumData;
import com.glodon.bim.customview.album.AlbumEditActivity;
import com.glodon.bim.customview.album.ImageItem;
import com.glodon.bim.customview.photopreview.PhotoPreviewActivity;

/**
 * 描述：创建材设进场记录-拍照页面
 * 作者：zhourf on 2018/1/9
 * 邮箱：zhourf@glodon.com
 */

public class CreateEquipmentPicturePresenter implements CreateEquipmentPictureContract.Presenter {
    private CreateEquipmentPictureContract.View mView;
    //图片
    private LinkedHashList<String, ImageItem> mSelectedMap;
    private String mPhotoPath;//拍照的路径
    private CreateEquipmentMandatoryInfo mMandatoryInfo;
    private CreateEquipmentMandatoryNotInfo mMandatoryNotInfo;

    private boolean mIsEdit = false;

    public CreateEquipmentPicturePresenter(CreateEquipmentPictureContract.View mView) {
        this.mView = mView;
        mSelectedMap = new LinkedHashList<>();
    }

    @Override
    public void initData(Intent intent) {
        mMandatoryInfo = (CreateEquipmentMandatoryInfo) intent.getSerializableExtra(CommonConfig.EQUIPMENT_MANDATORYINFO);
        mMandatoryNotInfo = (CreateEquipmentMandatoryNotInfo) intent.getSerializableExtra(CommonConfig.EQUIPMENT_NOT_MANDATORYINFO);

        CreateEquipmentPictureInfo info = (CreateEquipmentPictureInfo) intent.getSerializableExtra(CommonConfig.EQUIPMENT_EDIT_PICTURE_INFO);
        if(info!=null){
            mIsEdit = true;
            mView.showPhotoInfo(info);
            if(info.mSelectedMap!=null){
                mSelectedMap = info.mSelectedMap;
            }
            mView.showImages(mSelectedMap);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RequestCodeConfig.REQUEST_CODE_OPEN_ALBUM:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    AlbumData album = (AlbumData) data.getSerializableExtra(CommonConfig.ALBUM_DATA);
                    if (album != null) {
                        mSelectedMap = album.map;
                        if (mView != null) {
                            mView.showImages(mSelectedMap);
                        }
                    }
                }
                break;
            case RequestCodeConfig.REQUEST_CODE_TAKE_PHOTO:
                if (resultCode == Activity.RESULT_OK) {
                    //正常返回
                    Intent intent = new Intent(mView.getActivity(), PhotoEditActivity.class);
                    intent.putExtra(CommonConfig.IMAGE_PATH, mPhotoPath);
                    intent.putExtra(CommonConfig.FROM_CREATE_CHECK_LIST, true);
                    mView.getActivity().startActivityForResult(intent, RequestCodeConfig.REQUEST_CODE_PHOTO_EDIT);

                }
                break;
            case RequestCodeConfig.REQUEST_CODE_PHOTO_EDIT:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    mPhotoPath = data.getStringExtra(CommonConfig.IAMGE_SAVE_PATH);
                    ImageItem item = new ImageItem();
                    item.imagePath = mPhotoPath;
                    mSelectedMap.put(mPhotoPath, item);
                    if (mView != null) {
                        mView.showImages(mSelectedMap);
                    }
                }
                break;
            case RequestCodeConfig.REQUEST_CODE_PHOTO_PREVIEW:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    AlbumData album = (AlbumData) data.getSerializableExtra(CommonConfig.ALBUM_DATA);
                    if (album != null) {
                        mSelectedMap = album.map;
                        if (mView != null) {
                            mView.showImages(mSelectedMap);
                        }
                    }
                }
                break;
        }
    }

    @Override
    public void onDestroy() {

    }


    @Override
    public void openAlbum() {
        Intent intent = new Intent(mView.getActivity(), AlbumEditActivity.class);
        intent.putExtra(CommonConfig.ALBUM_FROM_TYPE, 1);
        intent.putExtra(CommonConfig.ALBUM_DATA, new AlbumData(mSelectedMap));
        mView.getActivity().startActivityForResult(intent, RequestCodeConfig.REQUEST_CODE_OPEN_ALBUM);
    }

    @Override
    public void next(boolean isUpToStandard) {
        CreateEquipmentPictureInfo info = new CreateEquipmentPictureInfo();
        info.qualified = isUpToStandard;
        info.mSelectedMap = mSelectedMap;
        if(mIsEdit){
            Intent data = new Intent();
            data.putExtra(CommonConfig.EQUIPMENT_EDIT_PICTURE_INFO,info);
            mView.getActivity().setResult(Activity.RESULT_OK,data);
            mView.getActivity().finish();
        }else {
            Intent intent = new Intent(mView.getActivity(), CreateEquipmentActivity.class);
            intent.putExtra(CommonConfig.EQUIPMENT_PICTURE_INFO, info);
            if (mMandatoryInfo != null) {
                intent.putExtra(CommonConfig.EQUIPMENT_MANDATORYINFO, mMandatoryInfo);
            }
            if (mMandatoryNotInfo != null) {
                intent.putExtra(CommonConfig.EQUIPMENT_NOT_MANDATORYINFO, mMandatoryNotInfo);
            }
            mView.getActivity().startActivityForResult(intent, RequestCodeConfig.REQUEST_CODE_EQUIPMENT_PICTURE_INFO);
            mView.getActivity().setResult(Activity.RESULT_OK);
            mView.getActivity().finish();
        }
    }


    /**
     * 跳转到图片预览
     *
     * @param position 点击的图片的位置
     */
    @Override
    public void toPreview(int position) {
        Intent intent = new Intent(mView.getActivity(), PhotoPreviewActivity.class);
        intent.putExtra(CommonConfig.ALBUM_DATA, new AlbumData(mSelectedMap));
        intent.putExtra(CommonConfig.ALBUM_POSITION, position);
        mView.getActivity().startActivityForResult(intent, RequestCodeConfig.REQUEST_CODE_PHOTO_PREVIEW);
    }


    @Override
    public void takePhoto() {
        mPhotoPath = CameraUtil.getFilePath();
        CameraUtil.openCamera(mPhotoPath, mView.getActivity(), RequestCodeConfig.REQUEST_CODE_TAKE_PHOTO);
    }
}
