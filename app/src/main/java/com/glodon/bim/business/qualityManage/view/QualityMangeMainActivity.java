package com.glodon.bim.business.qualityManage.view;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.glodon.bim.R;
import com.glodon.bim.base.BaseFragment;
import com.glodon.bim.basic.log.LogUtil;
import com.glodon.bim.basic.utils.ScreenUtil;
import com.glodon.bim.business.greendao.provider.DaoProvider;
import com.glodon.bim.business.qualityManage.contract.QualityMangeMainContract;
import com.glodon.bim.business.qualityManage.presenter.QualityMangeMainPresenter;
import com.glodon.bim.customview.PhotoAlbumDialog;

import java.util.HashMap;
import java.util.Map;

/**
 * 描述：质量管理
 * 作者：zhourf on 2017/9/8
 * 邮箱：zhourf@glodon.com
 */
public class QualityMangeMainActivity extends AppCompatActivity implements View.OnClickListener,QualityMangeMainContract.View {

    private Activity mActivity;

    private QualityMangeMainContract.Presenter mPresenter;

    private final int REQUEST_CAMERA = 1;

    private LinearLayout mDrawerView;
    private LinearLayout mContentView;
    private ImageView mBackView, mMenuView, mSearchView, mCreateView;

    private boolean mIsDrawerOpen = false;

    private TextView mQualityCheckListTv, mBluePrintTv, mModelTv, mQualityCheckModuleTv;

    private int mCurrentFragmentId = -1;

    private final int mQualityCheckListFragmentId = 0;
    private final int mBluePrintFragmentId = 1;
    private final int mModelFragmentId = 2;
    private final int mQualityCheckModuleFragmentId = 3;

    private QualityCheckListFragment mQualityCheckListFragment;
    private BluePrintFragment mBluePrintFragment;
    private ModelFragment mModelFragment;
    private QualityCheckModuleFragment mQualityCheckModuleFragment;

    private Map<Integer, BaseFragment> mFragmentMap;

    private LinearLayout mStatusLeft, mStatusRight;

    private PhotoAlbumDialog mPhotoAlbumDialog;//拍照相册弹出框

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main_activity);
        mActivity = this;
        mPresenter = new QualityMangeMainPresenter(this);
        initView();
        setListener();
        initDataForActivity();
    }

    private void initView() {
        mDrawerView = (LinearLayout) findViewById(R.id.main_drawer);
        mContentView = (LinearLayout) findViewById(R.id.main_content);
        mStatusLeft = (LinearLayout) findViewById(R.id.main_drawer_status_left);
        mStatusRight = (LinearLayout) findViewById(R.id.main_drawer_status_right);

        LinearLayout.LayoutParams contentParams = (LinearLayout.LayoutParams) mContentView.getLayoutParams();
        contentParams.width = ScreenUtil.getScreenInfo()[0];
        mContentView.setLayoutParams(contentParams);

        mBackView = (ImageView) findViewById(R.id.main_header_back_icon);
        mMenuView = (ImageView) findViewById(R.id.main_header_menu_icon);
        mSearchView = (ImageView) findViewById(R.id.main_header_search_icon);
        mCreateView = (ImageView) findViewById(R.id.main_header_new_icon);

        mQualityCheckListTv = (TextView) findViewById(R.id.main_drawer_quality_check_list);
        mBluePrintTv = (TextView) findViewById(R.id.main_drawer_quality_blueprint);
        mModelTv = (TextView) findViewById(R.id.main_drawer_quality_model);
        mQualityCheckModuleTv = (TextView) findViewById(R.id.main_drawer_quality_module);

        initStatusBar();

        hideDrawer(1);
    }

    private void initStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,  //设置StatusBar透明
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            int id = 0;
            id = getResources().getIdentifier("status_bar_height", "dimen", //获取状态栏的高度
                    "android");
            if (id > 0) {
                mStatusRight.getLayoutParams().height = getResources() //设置状态栏的高度
                        .getDimensionPixelOffset(id);
                mStatusLeft.getLayoutParams().height = getResources() //设置状态栏的高度
                        .getDimensionPixelOffset(id);
            }
        }
    }

    private void setListener() {
        mBackView.setOnClickListener(this);
        mMenuView.setOnClickListener(this);
        mSearchView.setOnClickListener(this);
        mCreateView.setOnClickListener(this);

        mQualityCheckListTv.setOnClickListener(this);
        mBluePrintTv.setOnClickListener(this);
        mModelTv.setOnClickListener(this);
        mQualityCheckModuleTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.main_header_back_icon://点击返回键
                mActivity.finish();
                break;
            case R.id.main_header_menu_icon://点击菜单键
                if (mIsDrawerOpen) {
                    hideDrawer(300);
                } else {
                    showDrawer(300);
                }
                mIsDrawerOpen = !mIsDrawerOpen;
                break;
            case R.id.main_header_search_icon://点击搜索按钮

                break;
            case R.id.main_header_new_icon://点击创建
                create();
                break;
            case R.id.main_drawer_quality_check_list://点击质检清单
                showFragmentById(mQualityCheckListFragmentId);
                break;
            case R.id.main_drawer_quality_blueprint://点击图纸
//                showFragmentById(mBluePrintFragmentId);
                mPresenter.toBluePrint();
                break;
            case R.id.main_drawer_quality_model://点击模型
//                showFragmentById(mModelFragmentId);
                mPresenter.toModel();
                break;
            case R.id.main_drawer_quality_module://点击质检项目
                showFragmentById(mQualityCheckModuleFragmentId);
                break;

        }
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



    protected void initDataForActivity() {
        mFragmentMap = new HashMap<>();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] PERMISSIONS_STORAGE = {
                    Manifest.permission.CAMERA,
            };
            requestPermission(PERMISSIONS_STORAGE, REQUEST_CAMERA);
        }

        LogUtil.d("====", new DaoProvider().getCookie());

        showFragmentById(mQualityCheckListFragmentId);
        mCurrentFragmentId = mQualityCheckListFragmentId;
    }

    //显示fragment
    private void showFragmentById(int id) {
        if (mCurrentFragmentId == id) {
            return;
        }
        //开启事务，fragment的控制是由事务来实现的
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        BaseFragment currentFragment = null;
        switch (id) {
            case mQualityCheckListFragmentId:
                //第一种方式（add），初始化fragment并添加到事务中，如果为null就new一个
                if (mQualityCheckListFragment == null) {
                    mQualityCheckListFragment = new QualityCheckListFragment();
                    transaction.add(R.id.main_fragment_content, mQualityCheckListFragment);
                }
                currentFragment = mQualityCheckListFragment;
                break;
            case mBluePrintFragmentId:
                if (mBluePrintFragment == null) {
                    mBluePrintFragment = new BluePrintFragment();
                    transaction.add(R.id.main_fragment_content, mBluePrintFragment);
                }
                currentFragment = mBluePrintFragment;
                break;
            case mModelFragmentId:
                if (mModelFragment == null) {
                    mModelFragment = new ModelFragment();
                    transaction.add(R.id.main_fragment_content, mModelFragment);
                }
                currentFragment = mModelFragment;
                break;
            case mQualityCheckModuleFragmentId:
                if (mQualityCheckModuleFragment == null) {
                    mQualityCheckModuleFragment = new QualityCheckModuleFragment();
                    transaction.add(R.id.main_fragment_content, mQualityCheckModuleFragment);
                }
                currentFragment = mQualityCheckModuleFragment;
                break;
        }

        //隐藏所有fragment
        hideFragment(transaction);
        //显示需要显示的fragment
        transaction.show(currentFragment);
        if (mFragmentMap.get(new Integer(id)) == null) {
            mFragmentMap.put(id, currentFragment);
        }
        //提交事务
        transaction.commit();
        mCurrentFragmentId = id;
    }


    //隐藏所有的fragment
    private void hideFragment(FragmentTransaction transaction) {
        for (Map.Entry<Integer, BaseFragment> entry : mFragmentMap.entrySet()) {
            transaction.hide(entry.getValue());
        }
    }

    //隐藏侧边栏
    private void hideDrawer(int duraion) {

        final int margin = ScreenUtil.dp2px(150);
        ObjectAnimator animator = ObjectAnimator.ofFloat(mDrawerView, "translationX", 0, -margin);
        animator.setDuration(duraion);
        animator.start();

        ObjectAnimator animator2 = ObjectAnimator.ofFloat(mContentView, "translationX", 0, -margin);
        animator2.setDuration(duraion);
        animator2.start();
    }

    //展示侧边栏
    private void showDrawer(int duraion) {

        final int margin = ScreenUtil.dp2px(150);
        ObjectAnimator animator = ObjectAnimator.ofFloat(mDrawerView, "translationX", -margin, 0);
        animator.setDuration(duraion);
        animator.start();

        ObjectAnimator animator2 = ObjectAnimator.ofFloat(mContentView, "translationX", -margin, 0);
        animator2.setDuration(duraion);
        animator2.start();
    }


    @Override
    public void showLoadingDialog() {

    }

    @Override
    public void dismissLoadingDialog() {

    }

    @Override
    public Activity getActivity() {
        return mActivity;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPresenter.onActivityResult(requestCode,resultCode,data);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mPresenter!=null)
        {
            mPresenter.onDestroy();
            mPresenter = null;
        }
    }

    private void requestPermission(String[] permissions, int requestCode) {

        int permission = ActivityCompat.checkSelfPermission(this, permissions[0]);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    this,
                    permissions,
                    requestCode
            );
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode)
        {
            case REQUEST_CAMERA:
                if(grantResults!=null && grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    //授权允许
                }else{
                    //授权拒绝
                }
                break;
        }
    }
}
