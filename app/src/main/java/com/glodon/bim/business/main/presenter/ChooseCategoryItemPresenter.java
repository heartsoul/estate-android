package com.glodon.bim.business.main.presenter;

import android.app.Activity;
import android.content.Intent;

import com.glodon.bim.basic.utils.CameraUtil;
import com.glodon.bim.business.main.bean.ChooseCategoryItem;
import com.glodon.bim.business.main.contract.ChooseCategoryItemContract;
import com.glodon.bim.business.qualityManage.view.BluePrintActivity;
import com.glodon.bim.business.qualityManage.view.ModelActivity;
import com.glodon.bim.business.qualityManage.view.PhotoEditActivity;
import com.glodon.bim.business.qualityManage.view.QualityMangeMainActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：项目功能列表
 * 作者：zhourf on 2017/10/17
 * 邮箱：zhourf@glodon.com
 */

public class ChooseCategoryItemPresenter implements ChooseCategoryItemContract.Presenter {

    private ChooseCategoryItemContract.View mView;
    private List<ChooseCategoryItem> mDataList;
    private final int REQUEST_CODE_TAKE_PHOTO = 0;
    private String mPhotoPath;

    public ChooseCategoryItemPresenter(ChooseCategoryItemContract.View mView) {
        this.mView = mView;
        mDataList = new ArrayList<>();
    }

    @Override
    public void initData(Intent intent) {
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
    public void toQualityChickList(int type) {
        Intent intent = new Intent(mView.getActivity(), QualityMangeMainActivity.class);
        intent.putExtra("type",type);
        mView.getActivity().startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_TAKE_PHOTO://拍照返回
                if (resultCode == Activity.RESULT_OK) {
                    //正常返回
                    Intent intent = new Intent(mView.getActivity(), PhotoEditActivity.class);
                    intent.putExtra("imagePath",mPhotoPath);
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
