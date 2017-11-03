package com.glodon.bim.business.qualityManage.presenter;

import android.app.Activity;
import android.content.Intent;

import com.glodon.bim.basic.utils.CameraUtil;
import com.glodon.bim.business.main.bean.ProjectListItem;
import com.glodon.bim.business.qualityManage.contract.QualityMangeMainContract;
import com.glodon.bim.business.qualityManage.view.BluePrintActivity;
import com.glodon.bim.business.qualityManage.view.CreateCheckListActivity;
import com.glodon.bim.business.qualityManage.view.ModelActivity;
import com.glodon.bim.business.qualityManage.view.PhotoEditActivity;
import com.glodon.bim.business.setting.view.SettingActivity;
import com.glodon.bim.common.config.CommonConfig;
import com.glodon.bim.customview.album.AlbumEditActivity;

/**
 * 描述：质量管理
 * 作者：zhourf on 2017/10/9
 * 邮箱：zhourf@glodon.com
 */

public class QualityMangeMainPresenter implements QualityMangeMainContract.Presenter {

    private final int REQUEST_CODE_TAKE_PHOTO = 0;
    private final int REQUEST_CODE_OPEN_ALBUM = 1;
    private final int REQUEST_CODE_CREATE_CHECK_LIST = 2;

    private QualityMangeMainContract.View mView;
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
        Intent intent = new Intent(mView.getActivity(), AlbumEditActivity.class);
        intent.putExtra(CommonConfig.ALBUM_FROM_TYPE,0);
        intent.putExtra(CommonConfig.CREATE_TYPE,CommonConfig.CREATE_TYPE_CHECK);//表示创建检查单
        mView.getActivity().startActivityForResult(intent,REQUEST_CODE_OPEN_ALBUM);
    }

    @Override
    public void toCreate() {
        Intent intent = new Intent(mView.getActivity(), CreateCheckListActivity.class);
        intent.putExtra(CommonConfig.SHOW_PHOTO,false);
        mView.getActivity().startActivity(intent);
    }

    @Override
    public void toSetting(ProjectListItem mProjectInfo) {
        Intent intent = new Intent(mView.getActivity(), SettingActivity.class);
        intent.putExtra(CommonConfig.PROJECT_LIST_ITEM,mProjectInfo);
        mView.getActivity().startActivity(intent);
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
                    intent.putExtra(CommonConfig.CREATE_TYPE,CommonConfig.CREATE_TYPE_CHECK);//表示创建检查单
                    mView.getActivity().startActivityForResult(intent,REQUEST_CODE_CREATE_CHECK_LIST);

                } else if (resultCode == Activity.RESULT_CANCELED) {
                    //返回键返回
                }
                break;
            case REQUEST_CODE_OPEN_ALBUM://打开相册

                break;
            case REQUEST_CODE_CREATE_CHECK_LIST:

                break;
        }
    }

    @Override
    public void onDestroy() {

    }
}
