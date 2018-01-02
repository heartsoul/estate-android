package com.glodon.bim.business.main.presenter;

import android.app.Activity;
import android.content.Intent;

import com.glodon.bim.basic.utils.CameraUtil;
import com.glodon.bim.basic.utils.SharedPreferencesUtil;
import com.glodon.bim.business.main.bean.ProjectListItem;
import com.glodon.bim.business.main.contract.ChooseCategoryItemContract;
import com.glodon.bim.business.qualityManage.view.CreateCheckListActivity;
import com.glodon.bim.business.qualityManage.view.PhotoEditActivity;
import com.glodon.bim.business.qualityManage.view.QualityMangeMainActivity;
import com.glodon.bim.business.setting.view.SettingActivity;
import com.glodon.bim.common.config.CommonConfig;
import com.glodon.bim.common.config.RequestCodeConfig;
import com.glodon.bim.customview.album.AlbumEditActivity;

/**
 * 描述：项目功能列表
 * 作者：zhourf on 2017/10/17
 * 邮箱：zhourf@glodon.com
 */

public class ChooseCategoryItemPresenter implements ChooseCategoryItemContract.Presenter {

    private ChooseCategoryItemContract.View mView;
    private String mPhotoPath;
    private ProjectListItem mProjectInfo;

    public ChooseCategoryItemPresenter(ChooseCategoryItemContract.View mView) {
        this.mView = mView;
    }

    @Override
    public void initData(Intent intent) {
        mProjectInfo = (ProjectListItem) intent.getSerializableExtra(CommonConfig.PROJECT_LIST_ITEM);
        SharedPreferencesUtil.setProjectInfo(mProjectInfo);
    }

    @Override
    public void openPhoto() {
        mPhotoPath = CameraUtil.getFilePath();
        CameraUtil.openCamera(mPhotoPath, mView.getActivity(), RequestCodeConfig.REQUEST_CODE_TAKE_PHOTO);
    }

    @Override
    public void openAlbum() {
        Intent intent = new Intent(mView.getActivity(), AlbumEditActivity.class);
        intent.putExtra(CommonConfig.ALBUM_FROM_TYPE,0);
        intent.putExtra(CommonConfig.CREATE_TYPE,CommonConfig.CREATE_TYPE_CHECK);
        mView.getActivity().startActivity(intent);
    }

    @Override
    public void toCreate() {
        Intent intent = new Intent(mView.getActivity(), CreateCheckListActivity.class);
        intent.putExtra(CommonConfig.SHOW_PHOTO,false);
        mView.getActivity().startActivity(intent);
    }

    @Override
    public void toQualityChickList(int type) {
        Intent intent = new Intent(mView.getActivity(), QualityMangeMainActivity.class);
        intent.putExtra(CommonConfig.PROJECT_LIST_ITEM,mProjectInfo);
        intent.putExtra(CommonConfig.MAIN_FROM_TYPE,type);
        mView.getActivity().startActivityForResult(intent,RequestCodeConfig.REQUEST_CODE_CHANGE_PROJECT);
    }

    @Override
    public void toSetting() {
        Intent intent = new Intent(mView.getActivity(), SettingActivity.class);
        intent.putExtra(CommonConfig.PROJECT_LIST_ITEM,mProjectInfo);
        mView.getActivity().startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RequestCodeConfig.REQUEST_CODE_TAKE_PHOTO://拍照返回
                if (resultCode == Activity.RESULT_OK) {
                    //正常返回
                    Intent intent = new Intent(mView.getActivity(), PhotoEditActivity.class);
                    intent.putExtra(CommonConfig.IMAGE_PATH,mPhotoPath);
                    intent.putExtra(CommonConfig.CREATE_TYPE,CommonConfig.CREATE_TYPE_CHECK);//表示创建检查单
                    mView.getActivity().startActivityForResult(intent,RequestCodeConfig.REQUEST_CODE_CREATE_CHECK_LIST);

                } else if (resultCode == Activity.RESULT_CANCELED) {
                    //返回键返回
                }
                break;
            case RequestCodeConfig.REQUEST_CODE_CHANGE_PROJECT:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    boolean isChangeProject = data.getBooleanExtra(CommonConfig.CHANGE_PROJECT,false);
                    if(isChangeProject && mView!=null){
                        mView.getActivity().finish();
                    }
                }
                break;
            case RequestCodeConfig.REQUEST_CODE_CREATE_CHECK_LIST:

                break;
        }
    }

    @Override
    public void onDestroy() {
        mView = null;
        mProjectInfo = null;
        mPhotoPath = null;
    }
}
