package com.glodon.bim.business.main.view;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.glodon.bim.R;
import com.glodon.bim.base.BaseActivity;
import com.glodon.bim.base.BaseFragment;
import com.glodon.bim.basic.listener.ThrottleClickEvents;
import com.glodon.bim.basic.utils.ScreenUtil;
import com.glodon.bim.business.authority.AuthorityManager;
import com.glodon.bim.business.equipment.view.CreateEquipmentMandatoryActivity;
import com.glodon.bim.business.main.adapter.MainAdapter;
import com.glodon.bim.business.main.contract.MainContract;
import com.glodon.bim.business.main.listener.OnGetAuthorityListener;
import com.glodon.bim.business.main.presenter.MainPresenter;
import com.glodon.bim.customview.ToastManager;
import com.glodon.bim.customview.dialog.PhotoAlbumDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：主界面
 * 作者：zhourf on 2017/9/8
 * 邮箱：zhourf@glodon.com
 */
public class MainActivity extends BaseActivity implements View.OnClickListener,MainContract.View{

    private View mStatusView;
    private MainContract.Presenter mPresenter;
    private ViewPager mViewPager;
    private MainAdapter mAdapter;
    //底部tab标签
    private LinearLayout mMainPageTab,mSubscribeTab,mMessageTab,mMineTab;
    private ImageView mMainPageTabIcon,mSubscribeTabIcon,mMessageTabIcon,mMineTabIcon;

    //创建
    private ImageView mCreateTab;
    private RelativeLayout mCreateBg;
    private LinearLayout mCreateBgContent;
    private List<BaseFragment> mFragmentList ;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    private long mStartTime = -1;
    private boolean mIsChange = false;//创建按钮是否旋转了
    private PhotoAlbumDialog mPhotoAlbumDialog;//拍照相册弹出框
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        initView();
        setStatusBar();
        setListener();
        AuthorityManager.getAuthorities(new OnGetAuthorityListener() {
            @Override
            public void finish() {
                initData();
            }
        });

    }

    private void setStatusBar() {
        initStatusBar(mStatusView);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //>=6时，修改状态栏字体和图标为深色
            getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    private void initView() {
        mStatusView = findViewById(R.id.main_activity_status);
        mViewPager = (ViewPager) findViewById(R.id.main_activity_content);
        mMainPageTab = (LinearLayout) findViewById(R.id.main_activity_main_page);
        mSubscribeTab = (LinearLayout) findViewById(R.id.main_activity_subscribe);
        mMessageTab = (LinearLayout) findViewById(R.id.main_activity_message);
        mMineTab = (LinearLayout) findViewById(R.id.main_activity_mine);
        mMainPageTabIcon = (ImageView) findViewById(R.id.main_activity_main_page_icon);
        mSubscribeTabIcon = (ImageView) findViewById(R.id.main_activity_subscribe_icon);
        mMessageTabIcon = (ImageView) findViewById(R.id.main_activity_message_icon);
        mMineTabIcon = (ImageView) findViewById(R.id.main_activity_mine_icon);
        mCreateBg = (RelativeLayout) findViewById(R.id.main_activity_create_bg);
        mCreateBgContent = (LinearLayout) findViewById(R.id.main_activity_create_bg_content);
        mCreateTab = (ImageView) findViewById(R.id.main_activity_create);
    }


    private void setListener() {
        mMainPageTab.setOnClickListener(this);
        mSubscribeTab.setOnClickListener(this);
        mMessageTab.setOnClickListener(this);
        mMineTab.setOnClickListener(this);
        mCreateTab.setOnClickListener(this);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //改变状态栏颜色
                switch (position)
                {
                    case 0:
                        mStatusView.setBackgroundResource(R.color.white);
                        break;
                    case 1:
                        mStatusView.setBackgroundResource(R.color.white);
                        break;
                    case 2:
                        mStatusView.setBackgroundResource(R.color.white);
                        break;
                    case 3:
                        mStatusView.setBackgroundResource(R.color.c_00baf3);
                        break;
                }

                setSelectedIcon(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    //设定选中的fragment的底部icon的选中状态
    private void setSelectedIcon(int position){
        switch (position){
            case 0:
                mMainPageTabIcon.setBackgroundResource(R.drawable.icon_main_page_selected);
                mSubscribeTabIcon.setBackgroundResource(R.drawable.icon_main_subscribe);
                mMessageTabIcon.setBackgroundResource(R.drawable.icon_main_message);
                mMineTabIcon.setBackgroundResource(R.drawable.icon_main_mine);
                break;
            case 1:
                mMainPageTabIcon.setBackgroundResource(R.drawable.icon_main_main_page);
                mSubscribeTabIcon.setBackgroundResource(R.drawable.icon_main_subscribe_selected);
                mMessageTabIcon.setBackgroundResource(R.drawable.icon_main_message);
                mMineTabIcon.setBackgroundResource(R.drawable.icon_main_mine);
                break;
            case 2:
                mMainPageTabIcon.setBackgroundResource(R.drawable.icon_main_main_page);
                mSubscribeTabIcon.setBackgroundResource(R.drawable.icon_main_subscribe);
                mMessageTabIcon.setBackgroundResource(R.drawable.icon_main_message_selected);
                mMineTabIcon.setBackgroundResource(R.drawable.icon_main_mine);
                break;
            case 3:
                mMainPageTabIcon.setBackgroundResource(R.drawable.icon_main_main_page);
                mSubscribeTabIcon.setBackgroundResource(R.drawable.icon_main_subscribe);
                mMessageTabIcon.setBackgroundResource(R.drawable.icon_main_message);
                mMineTabIcon.setBackgroundResource(R.drawable.icon_main_mine_selected);
                break;

        }
    }

    private void initData() {
        mFragmentList = new ArrayList<>();
        mFragmentList.add(new MainPageFragment());
        mFragmentList.add(new SubscribeFragment());
        mFragmentList.add(new MessageFragment());
        mFragmentList.add(new MineFragment());
        mPresenter = new MainPresenter(this);
        mPresenter.initData(getIntent());
        mAdapter = new MainAdapter(getSupportFragmentManager(),mFragmentList);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(4);
        mViewPager.setCurrentItem(0);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id)
        {
            case R.id.main_activity_create:
                create();
                break;
            case R.id.main_activity_main_page:
                mViewPager.setCurrentItem(0);

                break;
            case R.id.main_activity_subscribe:
                mViewPager.setCurrentItem(1);
                break;
            case R.id.main_activity_message:
                mViewPager.setCurrentItem(2);
                break;
            case R.id.main_activity_mine:
                mViewPager.setCurrentItem(3);

                break;
        }
    }


    private void create(){
        if(mIsChange){
            hideCreate();
        }else {
            showCreate();
        }
    }

    private void showCreate(){
        if (mCreateBgContent.getChildCount() == 0) {
            if (AuthorityManager.isShowCreateButton() && AuthorityManager.isEquipmentModify()) {
                mCreateBgContent.addView(getCreateQuality());
                LinearLayout.LayoutParams pamras = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                pamras.leftMargin = ScreenUtil.dp2px(55);
                mCreateBgContent.addView(getCreateEquipment(), pamras);
            } else if (AuthorityManager.isShowCreateButton()) {
                mCreateBgContent.addView(getCreateQuality());
            } else if (AuthorityManager.isEquipmentModify()) {
                mCreateBgContent.addView(getCreateEquipment());
            }
        }

        //显示创建按钮
        if (mCreateBgContent.getChildCount() > 0) {
            mCreateBg.setVisibility(View.VISIBLE);
            mStatusView.setBackgroundResource(R.color.c_b3000000);
            showCreateContent();
            rotateTo45();
        }
    }
    private void hideCreate(){
        rotateToNormal();
        hideCreateContent();
    }
    private void showCreateContent(){
        int height = ScreenUtil.heightPixels;
        ObjectAnimator.ofFloat(mCreateBgContent,"translationY",height,height-ScreenUtil.dp2px(206))
                .setDuration(400)
                .start();
    }
    private void hideCreateContent(){
        int height = ScreenUtil.heightPixels;
        ObjectAnimator oa = ObjectAnimator.ofFloat(mCreateBgContent,"translationY",height-ScreenUtil.dp2px(206),height)
                .setDuration(400);
        oa.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                mCreateBg.setVisibility(View.GONE);
                mStatusView.setBackgroundResource(R.color.transparent);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        oa.start();
    }

    private void rotateTo45(){
        mIsChange = true;
        ObjectAnimator animator = ObjectAnimator.ofFloat(mCreateTab,"rotation",0,-45);
        animator.setDuration(600);
        animator.start();
    }
    private void rotateToNormal(){
        mIsChange = false;
        ObjectAnimator animator = ObjectAnimator.ofFloat(mCreateTab,"rotation",-45,0);
        animator.setDuration(600);
        animator.start();
    }
    private LinearLayout getCreateQuality(){
        LinearLayout view = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.main_activity_create_item,null);
        ImageView icon = view.findViewById(R.id.main_activity_create_item_icon);
        TextView text = view.findViewById(R.id.main_activity_create_item_text);
        icon.setBackgroundResource(R.drawable.icon_main_quality_create);
        text.setText("新建质检单");
        ThrottleClickEvents.throttleClick(view, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideCreate();
                createQuality();
            }
        });
        return view;
    }

    //弹出照片选择框
    private void createQuality() {
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

    private LinearLayout getCreateEquipment(){
        LinearLayout view = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.main_activity_create_item,null);
        ImageView icon = view.findViewById(R.id.main_activity_create_item_icon);
        TextView text = view.findViewById(R.id.main_activity_create_item_text);
        icon.setBackgroundResource(R.drawable.icon_main_equipment_create);
        text.setText("新建材设单");
        ThrottleClickEvents.throttleClick(view, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mActivity, CreateEquipmentMandatoryActivity.class);
                startActivity(intent);
                hideCreate();
            }
        });
        return view;
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
        if (mPresenter != null) {
            mPresenter.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onBackPressed() {
        back();
    }

    public void back(){
        if (mStartTime == -1) {
            mStartTime = SystemClock.currentThreadTimeMillis();
            ToastManager.show("再按一次退出BIM协同！");
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mStartTime = -1;
                }
            }, 2000);
        } else {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.onDestroy();
            mPresenter = null;
        }
    }
}
