package com.glodon.bim.business.main.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.glodon.bim.R;
import com.glodon.bim.base.BaseActivity;
import com.glodon.bim.base.BaseFragment;
import com.glodon.bim.business.main.adapter.MainAdapter;
import com.glodon.bim.business.main.contract.MainContract;
import com.glodon.bim.business.main.presenter.MainPresenter;
import com.glodon.bim.customview.ToastManager;

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

    private ImageView mCreateTab;

    private List<BaseFragment> mFragmentList ;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    private long mStartTime = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        initView();
        setStatusBar();
        setListener();
        initData();
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
