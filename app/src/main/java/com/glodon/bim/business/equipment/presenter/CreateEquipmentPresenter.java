package com.glodon.bim.business.equipment.presenter;

import android.app.Activity;
import android.content.Intent;

import com.glodon.bim.business.equipment.bean.CreateEquipmentMandatoryInfo;
import com.glodon.bim.business.equipment.bean.CreateEquipmentMandatoryNotInfo;
import com.glodon.bim.business.equipment.bean.CreateEquipmentPictureInfo;
import com.glodon.bim.business.equipment.contract.CreateEquipmentContract;
import com.glodon.bim.business.equipment.view.CreateEquipmentMandatoryActivity;
import com.glodon.bim.business.equipment.view.CreateEquipmentNotMandatoryActivity;
import com.glodon.bim.business.equipment.view.CreateEquipmentPictureActivity;
import com.glodon.bim.business.qualityManage.view.RelevantModelActivity;
import com.glodon.bim.common.config.CommonConfig;
import com.glodon.bim.common.config.RequestCodeConfig;
import com.glodon.bim.customview.album.AlbumData;
import com.glodon.bim.customview.photopreview.PhotoPreviewActivity;

/**
 * 描述：创建材设进场记录-提交页面
 * 作者：zhourf on 2018/1/9
 * 邮箱：zhourf@glodon.com
 */

public class CreateEquipmentPresenter implements CreateEquipmentContract.Presenter {
    private CreateEquipmentContract.View mView;
    private CreateEquipmentContract.Model mModel;

    private CreateEquipmentMandatoryInfo mMandatoryInfo;
    private CreateEquipmentMandatoryNotInfo mMandatoryNotInfo;
    private CreateEquipmentPictureInfo mPictureInfo;

    public CreateEquipmentPresenter(CreateEquipmentContract.View mView) {
        this.mView = mView;
    }

    @Override
    public void initData(Intent intent) {
        mMandatoryInfo = (CreateEquipmentMandatoryInfo) intent.getSerializableExtra(CommonConfig.EQUIPMENT_MANDATORYINFO);
        mMandatoryNotInfo = (CreateEquipmentMandatoryNotInfo) intent.getSerializableExtra(CommonConfig.EQUIPMENT_NOT_MANDATORYINFO);
        mPictureInfo = (CreateEquipmentPictureInfo) intent.getSerializableExtra(CommonConfig.EQUIPMENT_PICTURE_INFO);

        if(mView!=null){
            if(mMandatoryInfo!=null) {
                mView.showBasicInfo(mMandatoryInfo);
            }
            if(mMandatoryNotInfo!=null) {
                mView.showOtherInfo(mMandatoryNotInfo);
            }
            if(mPictureInfo!=null) {
                mView.showPictureInfo(mPictureInfo);
            }

        }
    }




    @Override
    public void submit() {

    }

    @Override
    public void save() {

    }

    @Override
    public void delete() {

    }

    @Override
    public void toBasic() {
        Intent intent = new Intent(mView.getActivity(), CreateEquipmentMandatoryActivity.class);
        intent.putExtra(CommonConfig.EQUIPMENT_EDIT_MANDATORY_INFO,mMandatoryInfo);
        mView.getActivity().startActivityForResult(intent,RequestCodeConfig.REQUEST_CODE_EQUIPMENT_EDIT_MANDATORY_INFO);
    }

    @Override
    public void toOther() {
        Intent intent = new Intent(mView.getActivity(), CreateEquipmentNotMandatoryActivity.class);
        intent.putExtra(CommonConfig.EQUIPMENT_EDIT_NOT_MANDATORY_INFO,mMandatoryNotInfo);
        mView.getActivity().startActivityForResult(intent,RequestCodeConfig.REQUEST_CODE_EQUIPMENT_EDIT_NOT_MANDATORY_INFO);
    }

    @Override
    public void toPicture() {
        Intent intent = new Intent(mView.getActivity(), CreateEquipmentPictureActivity.class);
        intent.putExtra(CommonConfig.EQUIPMENT_EDIT_PICTURE_INFO,mPictureInfo);
        mView.getActivity().startActivityForResult(intent,RequestCodeConfig.REQUEST_CODE_EQUIPMENT_EDIT_PICTURE_INFO);
    }

    @Override
    public void toPreview(int position) {
        Intent intent = new Intent(mView.getActivity(), PhotoPreviewActivity.class);
        intent.putExtra(CommonConfig.ALBUM_DATA, new AlbumData(mPictureInfo.mSelectedMap));
        intent.putExtra(CommonConfig.ALBUM_POSITION, position);
        intent.putExtra(CommonConfig.ALBUM_SHOW_DELETE, false);
        mView.getActivity().startActivityForResult(intent, RequestCodeConfig.REQUEST_CODE_PHOTO_PREVIEW);
    }

    @Override
    public void toModel() {
        //跳转到模型
        if(mMandatoryNotInfo!=null && mMandatoryNotInfo.model!=null && mMandatoryNotInfo.model.component!=null) {
            Intent intent = new Intent(mView.getActivity(), RelevantModelActivity.class);
            intent.putExtra(CommonConfig.RELEVANT_TYPE, 2);

            intent.putExtra(CommonConfig.MODEL_SELECT_INFO, mMandatoryNotInfo.model);
            intent.putExtra(CommonConfig.BLUE_PRINT_FILE_ID, mMandatoryNotInfo.model.fileId);
            mView.getActivity().startActivity(intent);
        }
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode)
        {
            case RequestCodeConfig.REQUEST_CODE_EQUIPMENT_EDIT_MANDATORY_INFO:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    mMandatoryInfo = (CreateEquipmentMandatoryInfo) data.getSerializableExtra(CommonConfig.EQUIPMENT_EDIT_MANDATORY_INFO);
                    if(mMandatoryInfo!=null && mView!=null){
                        mView.showBasicInfo(mMandatoryInfo);
                    }
                }
                break;
            case RequestCodeConfig.REQUEST_CODE_EQUIPMENT_EDIT_NOT_MANDATORY_INFO:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    mMandatoryNotInfo = (CreateEquipmentMandatoryNotInfo) data.getSerializableExtra(CommonConfig.EQUIPMENT_EDIT_NOT_MANDATORY_INFO);
                    if(mMandatoryNotInfo!=null && mView!=null){
                        mView.showOtherInfo(mMandatoryNotInfo);
                    }
                }
                break;
            case RequestCodeConfig.REQUEST_CODE_EQUIPMENT_EDIT_PICTURE_INFO:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    mPictureInfo = (CreateEquipmentPictureInfo) data.getSerializableExtra(CommonConfig.EQUIPMENT_EDIT_PICTURE_INFO);
                    if(mPictureInfo!=null && mView!=null){
                        mView.showPictureInfo(mPictureInfo);
                    }
                }
                break;

        }
    }

    @Override
    public void onDestroy() {

    }
}
