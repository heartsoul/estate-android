package com.glodon.bim.business.qualityManage.presenter;

import android.app.Activity;
import android.content.Intent;

import com.glodon.bim.basic.utils.CameraUtil;
import com.glodon.bim.business.qualityManage.contract.QualityMangeMainContract;
import com.glodon.bim.business.qualityManage.view.BluePrintActivity;
import com.glodon.bim.business.qualityManage.view.ModelActivity;
import com.glodon.bim.business.qualityManage.view.PhotoEditActivity;
import com.glodon.bim.common.config.CommonConfig;

import java.io.File;

/**
 * 描述：质量管理
 * 作者：zhourf on 2017/10/9
 * 邮箱：zhourf@glodon.com
 */

public class QualityMangeMainPresenter implements QualityMangeMainContract.Presenter {

    private QualityMangeMainContract.View mView;

    private final int REQUEST_CODE_TAKE_PHOTO = 0;
    private String mPhotoPath;

    public QualityMangeMainPresenter(QualityMangeMainContract.View mView) {
        this.mView = mView;

    }

    //跳转到图纸
    public void toBluePrint() {
        Intent intent = new Intent(mView.getActivity(), BluePrintActivity.class);
        mView.getActivity().startActivity(intent);
    }

    //跳转到模型
    public void toModel() {
        Intent intent = new Intent(mView.getActivity(), ModelActivity.class);
        mView.getActivity().startActivity(intent);
    }

    @Override
    public void openPhoto() {
        mPhotoPath = CameraUtil.getFilePath();
        CameraUtil.openCamera(mPhotoPath, mView.getActivity(), REQUEST_CODE_TAKE_PHOTO);
    }

    @Override
    public void openAlbum() {

    }

    @Override
    public void toCreate() {

    }

    @Override
    public void initData(Intent intent) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_TAKE_PHOTO://拍照返回
                if (resultCode == Activity.RESULT_OK) {
                    //正常返回
                    Intent intent = new Intent(mView.getActivity(), PhotoEditActivity.class);
                    intent.putExtra(CommonConfig.IMAGE_PATH,mPhotoPath);
                    mView.getActivity().startActivity(intent);

                } else if (resultCode == Activity.RESULT_CANCELED) {
                    //返回键返回
                }
                break;
        }
    }

    @Override
    public void onDestroy() {

    }
}
