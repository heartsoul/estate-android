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
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.glodon.bim.R;
import com.glodon.bim.base.BaseActivity;
import com.glodon.bim.base.BaseFragment;
import com.glodon.bim.basic.log.LogUtil;
import com.glodon.bim.basic.utils.ScreenUtil;
import com.glodon.bim.business.authority.AuthorityManager;
import com.glodon.bim.business.greendao.provider.DaoProvider;
import com.glodon.bim.business.main.bean.ProjectListItem;
import com.glodon.bim.business.qualityManage.contract.QualityMangeMainContract;
import com.glodon.bim.business.qualityManage.presenter.QualityMangeMainPresenter;
import com.glodon.bim.common.config.CommonConfig;
import com.glodon.bim.customview.dialog.PhotoAlbumDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述：质量管理
 * 作者：zhourf on 2017/9/8
 * 邮箱：zhourf@glodon.com
 */
public class QualityMangeMainActivity extends BaseActivity implements View.OnClickListener, QualityMangeMainContract.View {

    private Activity mActivity;

    private QualityMangeMainContract.Presenter mPresenter;

    private final int REQUEST_CAMERA = 1;

    //drawer
    private LinearLayout mDrawerView;
    private TextView mProjectNameView;
    private TextView mQualityCheckListTv, mBluePrintTv, mModelTv, mQualityCheckModuleTv;
    private RelativeLayout mQualityManagerView,mSettingView;
    private LinearLayout mQualityContentView;
    //content
    private LinearLayout mContentView;

    private RelativeLayout  mBackView,mMenuView;
    private ImageView  mSearchView, mCreateView;

    private boolean mIsDrawerOpen = false;

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

    private ProjectListItem mProjectInfo;//项目信息
    private List<TextView> mContentList;

    private int mFromType = 0;//0质检清单  1质检项目

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        mActivity = this;
        mPresenter = new QualityMangeMainPresenter(this);
        mProjectInfo = (ProjectListItem) getIntent().getSerializableExtra(CommonConfig.PROJECT_LIST_ITEM);

        mFromType = getIntent().getIntExtra(CommonConfig.MAIN_FROM_TYPE,0);
        initView();
        setListener();
        initDataForActivity();
    }

    private void initView() {
        mDrawerView = (LinearLayout) findViewById(R.id.main_drawer);
        mProjectNameView = (TextView) findViewById(R.id.main_drawer_project_name);
        mQualityManagerView = (RelativeLayout) findViewById(R.id.main_drawer_quality);
        mSettingView = (RelativeLayout) findViewById(R.id.main_drawer_setting);
        mQualityContentView = (LinearLayout) findViewById(R.id.main_drawer_quality_content);

        mContentView = (LinearLayout) findViewById(R.id.main_content);
        mStatusLeft = (LinearLayout) findViewById(R.id.main_drawer_status_left);
        mStatusRight = (LinearLayout) findViewById(R.id.main_drawer_status_right);


        LinearLayout.LayoutParams contentParams = (LinearLayout.LayoutParams) mContentView.getLayoutParams();
        contentParams.width = ScreenUtil.getScreenInfo()[0];
        mContentView.setLayoutParams(contentParams);

        mBackView = (RelativeLayout) findViewById(R.id.main_header_back_icon);
        mMenuView = (RelativeLayout) findViewById(R.id.main_header_menu_icon);
        mSearchView = (ImageView) findViewById(R.id.main_header_search_icon);
        mCreateView = (ImageView) findViewById(R.id.main_header_new_icon);

        mQualityCheckListTv = (TextView) findViewById(R.id.main_drawer_quality_check_list);
        mBluePrintTv = (TextView) findViewById(R.id.main_drawer_quality_blueprint);
        mModelTv = (TextView) findViewById(R.id.main_drawer_quality_model);
        mQualityCheckModuleTv = (TextView) findViewById(R.id.main_drawer_quality_module);

        initStatusBar(mStatusRight);
        initStatusBar(mStatusLeft);

        hideDrawer(1);

        //权限控制  是否显示新检检查单按钮
        if(AuthorityManager.isShowCreateButton()){
            mCreateView.setVisibility(View.VISIBLE);
        }else{
            mCreateView.setVisibility(View.GONE);
        }

    }



    private void setListener() {
        mBackView.setOnClickListener(this);
        mMenuView.setOnClickListener(this);
        mSearchView.setOnClickListener(this);
        mCreateView.setOnClickListener(this);

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
            case R.id.main_drawer_project_name://点击项目名称切换项目
                Intent data = new Intent();
                data.putExtra(CommonConfig.CHANGE_PROJECT,true);
                setResult(RESULT_OK,data);
                finish();
                break;
            case R.id.main_drawer_quality://点击质量管理
                if(mQualityContentView.getVisibility() == View.VISIBLE){
                    mQualityContentView.setVisibility(View.GONE);
                }else{
                    mQualityContentView.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.main_drawer_quality_check_list://点击质检清单
                setSelect(0);
                showFragmentById(mQualityCheckListFragmentId);
                hideDrawer(300);
                mIsDrawerOpen = false;
                break;
            case R.id.main_drawer_quality_blueprint://点击图纸
//                setSelect(1);
//                showFragmentById(mBluePrintFragmentId);
//                hideDrawer(300);
//                mIsDrawerOpen = false;
                break;
            case R.id.main_drawer_quality_model://点击模型
//                setSelect(2);
//                showFragmentById(mModelFragmentId);
//                hideDrawer(300);
//                mIsDrawerOpen = false;
                break;
            case R.id.main_drawer_quality_module://点击质检项目
//                setSelect(3);
//                showFragmentById(mQualityCheckModuleFragmentId);
//                hideDrawer(300);
//                mIsDrawerOpen = false;
                break;
            case R.id.main_drawer_setting:
//                mPresenter.toSetting(mProjectInfo);
                break;

        }
    }

    //弹出照片选择框
    @Override
    public void create() {
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] PERMISSIONS_STORAGE = {
                    Manifest.permission.CAMERA,
            };
            requestPermission(PERMISSIONS_STORAGE, REQUEST_CAMERA);
        }

        LogUtil.d("====", new DaoProvider().getCookie());

        showFragmentById(mQualityCheckListFragmentId);
        mCurrentFragmentId = mQualityCheckListFragmentId;

        initDrawer();
    }

    //初始化drawer数据
    private void initDrawer() {
        if(mProjectInfo!=null) {
            mProjectNameView.setText(mProjectInfo.name);
        }

        mProjectNameView.setOnClickListener(this);
        mQualityManagerView.setOnClickListener(this);

        mQualityCheckListTv.setOnClickListener(this);
        mBluePrintTv.setOnClickListener(this);
        mModelTv.setOnClickListener(this);
        mQualityCheckModuleTv.setOnClickListener(this);

        mSettingView.setOnClickListener(this);

        mContentList = new ArrayList<>();
        mContentList.add(mQualityCheckListTv);
        mContentList.add(mBluePrintTv);
        mContentList.add(mModelTv);
        mContentList.add(mQualityCheckModuleTv);

        //设置选中颜色
        if(mFromType == 0){
            setSelect(0);//质检清单
        }else if(mFromType == 1){
            setSelect(3);//质检项目
        }
    }

    private void setSelect(int position){
        for(int i = 0;i<mContentList.size();i++){
            TextView tv = mContentList.get(i);
            if(i == position){
                tv.setTextColor(getResources().getColor(R.color.c_00b5f2));
            }else{
                tv.setTextColor(getResources().getColor(R.color.c_d4d4d4));
            }
        }
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
                mQualityCheckListFragment.setProjectInfo(mProjectInfo);
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
                mQualityCheckModuleFragment.setProjectInfo(mProjectInfo);
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

        final int margin = ScreenUtil.dp2px(240);
        ObjectAnimator animator = ObjectAnimator.ofFloat(mDrawerView, "translationX", 0, -margin);
        animator.setDuration(duraion);
        animator.start();

        ObjectAnimator animator2 = ObjectAnimator.ofFloat(mContentView, "translationX", 0, -margin);
        animator2.setDuration(duraion);
        animator2.start();
    }

    //展示侧边栏
    private void showDrawer(int duraion) {

        final int margin = ScreenUtil.dp2px(240);
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
        if(mPresenter!=null) {
            mPresenter.onActivityResult(requestCode, resultCode, data);
        }
        mFragmentMap.get(mCurrentFragmentId).onActivityResult(requestCode, resultCode, data);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.onDestroy();
            mPresenter = null;
        }
//        mFragmentMap.get(mCurrentFragmentId).onDestroy();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CAMERA:
                if (grantResults != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //授权允许
                } else {
                    //授权拒绝
                }
                break;
        }
    }
}
