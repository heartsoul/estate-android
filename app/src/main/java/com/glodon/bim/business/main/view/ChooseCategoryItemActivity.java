package com.glodon.bim.business.main.view;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import com.glodon.bim.R;
import com.glodon.bim.base.BaseActivity;
import com.glodon.bim.basic.listener.ThrottleClickEvents;
import com.glodon.bim.basic.utils.ScreenUtil;
import com.glodon.bim.business.authority.AuthorityManager;
import com.glodon.bim.business.main.contract.ChooseCategoryItemContract;
import com.glodon.bim.business.main.presenter.ChooseCategoryItemPresenter;
import com.glodon.bim.common.config.CommonConfig;
import com.glodon.bim.customview.dialog.PhotoAlbumDialog;

/**
 * 描述：选择目录的具体事项界面
 * 作者：zhourf on 2017/9/8
 * 邮箱：zhourf@glodon.com
 */
public class ChooseCategoryItemActivity extends BaseActivity implements ChooseCategoryItemContract.View, View.OnClickListener {

    private ChooseCategoryItemContract.Presenter mPresenter;
    private LinearLayout mQualityCheckListView, mModelView, mBluePrintView, mQualityCheckModuleVIew, mCreateView;
    private PhotoAlbumDialog mPhotoAlbumDialog;//拍照相册弹出框
    private final int REQUEST_CAMERA = 1;
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            checkAuthority();
        }
    };

    /**
     * 判断权限  控制是否显示新增按钮
     */
    private void checkAuthority(){
        if(mCreateView!=null) {
            if (AuthorityManager.isShowCreateButton()) {
                mCreateView.setVisibility(View.VISIBLE);
            } else {
                mCreateView.setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main_choose_category_item_activity);
        initView();
        setListener();
        initDataForActivity();

    }


    private void initView() {
        mQualityCheckListView = (LinearLayout) findViewById(R.id.choose_category_item_item_zjqd);
        mModelView = (LinearLayout) findViewById(R.id.choose_category_item_item_mx);
        mBluePrintView = (LinearLayout) findViewById(R.id.choose_category_item_item_tz);
        mQualityCheckModuleVIew = (LinearLayout) findViewById(R.id.choose_category_item_item_zjxm);
        mCreateView = (LinearLayout) findViewById(R.id.choose_category_item_item_create);

        //计算每个方形的宽度
        int width = (ScreenUtil.getScreenInfo()[0] - ScreenUtil.dp2px(44)) / 3;
        mQualityCheckListView.getLayoutParams().height = width;
        mQualityCheckListView.getLayoutParams().width = width;
        mModelView.getLayoutParams().height = width;
        mModelView.getLayoutParams().width = width;
        mBluePrintView.getLayoutParams().height = width;
        mBluePrintView.getLayoutParams().width = width;
        mQualityCheckModuleVIew.getLayoutParams().height = width;
        mQualityCheckModuleVIew.getLayoutParams().width = width;
        mCreateView.getLayoutParams().height = width;
        mCreateView.getLayoutParams().width = width;
    }

    private void setListener() {
        ThrottleClickEvents.throttleClick(mQualityCheckListView, this);
        ThrottleClickEvents.throttleClick(mModelView, this);
        ThrottleClickEvents.throttleClick(mBluePrintView, this);
        ThrottleClickEvents.throttleClick(mQualityCheckModuleVIew, this);
        ThrottleClickEvents.throttleClick(mCreateView, this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.choose_category_item_item_zjqd://点击质检清单
                mPresenter.toQualityChickList(0);
                break;
            case R.id.choose_category_item_item_mx://点击模型
                mPresenter.toModel();
                break;
            case R.id.choose_category_item_item_tz://点击图纸
                mPresenter.toBluePrint();
                break;
            case R.id.choose_category_item_item_zjxm://点击质检项目
                mPresenter.toQualityChickList(1);
                break;
            case R.id.choose_category_item_item_create://点击新建
                create();
                break;
        }
    }


    private void initDataForActivity() {
        //注册广播  监听   获取权限的变化  控制新增按钮的显示
        registerReceiver(mReceiver,new IntentFilter(CommonConfig.ACTION_GET_AUTHORITY_CHECK));
        mPresenter = new ChooseCategoryItemPresenter(this);
        mPresenter.initData(getIntent());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] PERMISSIONS_STORAGE = {
                    Manifest.permission.CAMERA,
            };
            requestPermission(PERMISSIONS_STORAGE, REQUEST_CAMERA);
        }

        checkAuthority();
    }

    //弹出照片选择框
    private void create() {
        if (mPhotoAlbumDialog == null) {
            mPhotoAlbumDialog = new PhotoAlbumDialog(this).builder(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mPresenter.openPhoto();
                }
            }, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mPresenter.openAlbum();
                }
            }, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mPresenter.toCreate();
                }
            });
        }
        mPhotoAlbumDialog.show();
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
        mPresenter.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.onDestroy();
            mPresenter = null;
        }
        if(mReceiver!=null) {
            unregisterReceiver(mReceiver);
        }
    }
}
