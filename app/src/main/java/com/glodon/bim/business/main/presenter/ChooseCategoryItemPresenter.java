package com.glodon.bim.business.main.presenter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.glodon.bim.R;
import com.glodon.bim.basic.log.LogUtil;
import com.glodon.bim.basic.utils.CameraUtil;
import com.glodon.bim.basic.utils.SharedPreferencesUtil;
import com.glodon.bim.business.authority.AuthorityManager;
import com.glodon.bim.business.main.bean.ChooseCategoryItem;
import com.glodon.bim.business.main.bean.ProjectListItem;
import com.glodon.bim.business.main.contract.ChooseCategoryItemContract;
import com.glodon.bim.business.qualityManage.view.CreateCheckListActivity;
import com.glodon.bim.business.qualityManage.view.PhotoEditActivity;
import com.glodon.bim.business.qualityManage.view.QualityMangeMainActivity;
import com.glodon.bim.business.setting.view.SettingActivity;
import com.glodon.bim.common.config.CommonConfig;
import com.glodon.bim.common.config.RequestCodeConfig;
import com.glodon.bim.customview.album.AlbumEditActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：项目功能列表
 * 作者：zhourf on 2017/10/17
 * 邮箱：zhourf@glodon.com
 */

public class ChooseCategoryItemPresenter implements ChooseCategoryItemContract.Presenter {

    private ChooseCategoryItemContract.View mView;
    private String mPhotoPath;
    private ProjectListItem mProjectInfo;
    private List<ChooseCategoryItem> mDataList;

    public ChooseCategoryItemPresenter(ChooseCategoryItemContract.View mView) {
        this.mView = mView;
        mDataList = new ArrayList<>();

    }

    private void assembleData() {
        ChooseCategoryItem item;
        if(AuthorityManager.isQualityBrowser()) {
            item = new ChooseCategoryItem(R.drawable.icon_category_item_zjqd, "质检清单", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    toQualityChickList(0);
                }
            });
            mDataList.add(item);
            item = new ChooseCategoryItem(R.drawable.icon_category_item_tj, "图纸", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    toQualityChickList(1);
                }
            });
            mDataList.add(item);
            item = new ChooseCategoryItem(R.drawable.icon_category_item_mx, "模型", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    toQualityChickList(2);
                }
            });
            mDataList.add(item);
            item = new ChooseCategoryItem(R.drawable.icon_category_item_zjxm, "质检项目", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    toQualityChickList(3);
                }
            });
            mDataList.add(item);
        }
        if(AuthorityManager.isEquipmentBrowser()) {
            item = new ChooseCategoryItem(R.drawable.icon_category_item_csqd, "材设清单", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    toQualityChickList(4);
                }
            });
            mDataList.add(item);
            item = new ChooseCategoryItem(R.drawable.icon_category_item_mxyl, "模型预览", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    toQualityChickList(5);
                }
            });
            mDataList.add(item);
        }
        mView.updateList(mDataList);
    }

    @Override
    public void initData(Intent intent) {
        mProjectInfo = (ProjectListItem) intent.getSerializableExtra(CommonConfig.PROJECT_LIST_ITEM);
        SharedPreferencesUtil.setProjectInfo(mProjectInfo);
        assembleData();
        handleCreate();
    }

    private void handleCreate() {
        if(AuthorityManager.isShowCreateButton() && AuthorityManager.isEquipmentModify()){
            //材设和质量都可以创建
            mView.createBoth();
        }else if(AuthorityManager.isShowCreateButton()){
            //可创建质量
            mView.createQuality();
        }else if(AuthorityManager.isEquipmentModify()){
            //可创建材设
            mView.createEquipment();
        }else{
            //都不可创建
            mView.createNone();
        }
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
        LogUtil.e("type="+type);
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

    private boolean isReceive = true;
    @Override
    public void checkAuthority() {
//        if(isReceive){
//            isReceive= false;
//            assembleData();
//        }
//        handleCreate();
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
