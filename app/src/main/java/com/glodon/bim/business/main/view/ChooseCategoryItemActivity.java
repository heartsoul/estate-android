package com.glodon.bim.business.main.view;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.glodon.bim.R;
import com.glodon.bim.base.BaseActivity;
import com.glodon.bim.basic.listener.ThrottleClickEvents;
import com.glodon.bim.basic.utils.BitmapUtil;
import com.glodon.bim.basic.utils.ScreenUtil;
import com.glodon.bim.business.authority.AuthorityManager;
import com.glodon.bim.business.equipment.view.CreateEquipmentMandatoryActivity;
import com.glodon.bim.business.main.adapter.ChooseCategoryItemAdapter;
import com.glodon.bim.business.main.bean.ChooseCategoryItem;
import com.glodon.bim.business.main.contract.ChooseCategoryItemContract;
import com.glodon.bim.business.main.presenter.ChooseCategoryItemPresenter;
import com.glodon.bim.common.config.CommonConfig;
import com.glodon.bim.customview.dialog.PhotoAlbumDialog;

import java.util.List;

/**
 * 描述：选择目录的具体事项界面
 * 作者：zhourf on 2017/9/8
 * 邮箱：zhourf@glodon.com
 */
public class ChooseCategoryItemActivity extends BaseActivity implements ChooseCategoryItemContract.View, View.OnClickListener {

    private ChooseCategoryItemContract.Presenter mPresenter;
    private RecyclerView mRecyclerView;
    private ImageView mCreateView;
    private PhotoAlbumDialog mPhotoAlbumDialog;//拍照相册弹出框
    private ImageView mSetView;
    private final int REQUEST_CAMERA = 1;
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            checkAuthority();
        }
    };
    private ChooseCategoryItemAdapter mAdapter;
    private RelativeLayout mBlurParent;
    private ImageView mBlurClose,mBlurQuality,mBlurEquipment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main_choose_category_item_activity);
        initView();
        setListener();
        initDataForActivity();

    }


    @Override
    protected void onResume() {
        super.onResume();
        checkAuthority();
    }

    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.choose_category_item_recyclerview);
        mCreateView = (ImageView) findViewById(R.id.choose_category_item_create);
        mSetView = (ImageView) findViewById(R.id.choose_category_item_setting);

        mBlurParent = (RelativeLayout) findViewById(R.id.choose_category_item_blur);
        mBlurClose = (ImageView) findViewById(R.id.choose_category_item_blur_close);
        mBlurQuality = (ImageView) findViewById(R.id.choose_category_item_blur_quality);
        mBlurEquipment = (ImageView) findViewById(R.id.choose_category_item_blur_equipment);

        initSetView();

        initRecyclerView();

        initBlur();
    }

    private void initBlur() {

        Bitmap bm = Bitmap.createBitmap(ScreenUtil.getScreenInfo()[0]/5,ScreenUtil.getScreenInfo()[1]/5, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bm);
        canvas.drawColor(getResources().getColor(R.color.c_f1ffffff));
        mBlurParent.setBackground(new BitmapDrawable(getResources(), BitmapUtil.fastblur(bm,2)));

    }


    private void initRecyclerView() {
        GridLayoutManager manager = new GridLayoutManager(this,3);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(manager);
        mAdapter = new ChooseCategoryItemAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
    }

    protected void initSetView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,  //设置StatusBar透明
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            int id = 0;
            id = getResources().getIdentifier("status_bar_height", "dimen", //获取状态栏的高度
                    "android");
            if (id > 0) {
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mSetView.getLayoutParams();
                int height = getResources() //设置状态栏的高度
                        .getDimensionPixelOffset(id);
                lp.topMargin = height + ScreenUtil.dp2px(10);
            }
        }
    }


    private void setListener() {
        mBlurClose.setOnClickListener(this);
        ThrottleClickEvents.throttleClick(mSetView, this);
        ThrottleClickEvents.throttleClick(mBlurQuality, this);
        ThrottleClickEvents.throttleClick(mBlurEquipment, this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {

            case R.id.choose_category_item_blur_close://点击关闭浮层
                mBlurParent.setVisibility(View.GONE);
                break;
            case R.id.choose_category_item_blur_quality://点击创建质量
                mBlurParent.setVisibility(View.GONE);
                create();
                break;
            case R.id.choose_category_item_blur_equipment://点击创建材设
                mBlurParent.setVisibility(View.GONE);
                Intent intent = new Intent(mActivity, CreateEquipmentMandatoryActivity.class);
                startActivity(intent);
                break;
            case R.id.choose_category_item_setting://点击账户设置
                mPresenter.toSetting();
                break;
        }
    }


    private void initDataForActivity() {
        //注册广播  监听   获取权限的变化  控制新增按钮的显示
        registerReceiver(mReceiver, new IntentFilter(CommonConfig.ACTION_GET_AUTHORITY_CHECK));
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

    /**
     * 判断权限  控制是否显示新增按钮
     */
    private void checkAuthority() {
        mPresenter.checkAuthority();
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
        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
        }
    }

    @Override
    public void updateList(List<ChooseCategoryItem> mDataList) {
        mAdapter.updateList(mDataList);
    }

    @Override
    public void createBoth() {
        mCreateView.setVisibility(View.VISIBLE);
        mCreateView.setBackgroundResource(R.drawable.icon_category_create);
        ThrottleClickEvents.throttleClick(mCreateView, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBlurParent.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void createQuality() {
        mCreateView.setVisibility(View.VISIBLE);
        mCreateView.setBackgroundResource(R.drawable.icon_category_create_single_quality);
        ThrottleClickEvents.throttleClick(mCreateView, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                create();
            }
        });
    }

    @Override
    public void createEquipment() {
        mCreateView.setVisibility(View.VISIBLE);
        mCreateView.setBackgroundResource(R.drawable.icon_category_create_single_equipment);
        ThrottleClickEvents.throttleClick(mCreateView, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mActivity, CreateEquipmentMandatoryActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void createNone() {
        mCreateView.setVisibility(View.GONE);
    }
}
