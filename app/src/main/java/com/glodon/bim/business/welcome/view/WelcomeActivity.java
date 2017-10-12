package com.glodon.bim.business.welcome.view;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.glodon.bim.R;
import com.glodon.bim.basic.utils.ScreenUtil;
import com.glodon.bim.business.login.view.LoginActivity;
import com.glodon.bim.business.welcome.adapter.WelcomeAdapter;
import com.glodon.bim.business.welcome.bean.WelcomeItem;
import com.glodon.bim.base.BaseTitleActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：ViewPager滑动页
 * 作者：zhourf on 2017/9/11
 * 邮箱：zhourf@glodon.com
 */
public class WelcomeActivity extends BaseTitleActivity implements View.OnClickListener {

    private ViewPager mViewPager;
    private LinearLayout mPointParent;//点的布局
    private WelcomeAdapter mAdapter;
    private List<WelcomeItem> mDataList;
    private TextView mGoOver, mGoTry;
    private int mCurrentItemPosition = 0;

    @Override
    protected View onCreateView() {
        View view = LayoutInflater.from(this).inflate(R.layout.welcome_main_activity, null);
        mViewPager = view.findViewById(R.id.welcome_viewpager);
        mPointParent = view.findViewById(R.id.welcome_dots);
        mGoOver = view.findViewById(R.id.welcome_go_over);
        mGoTry = view.findViewById(R.id.welcome_go_try);
        return view;
    }

    @Override
    protected void initDataForActivity() {
        super.initDataForActivity();
        initViewPager();
        setListener();
    }

    @Override
    protected void setListener() {
        mGoOver.setOnClickListener(this);
        mGoTry.setOnClickListener(this);
    }

    private void initViewPager() {
        mDataList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            WelcomeItem item = new WelcomeItem();
            item.text = "text" + i;
            mDataList.add(item);
        }
        mAdapter = new WelcomeAdapter(this, mDataList);
        mViewPager.setAdapter(mAdapter);

        mPointParent.removeAllViews();
        final ImageView[] dotViews = getDotsView(mDataList.size());
        setImageBackground(dotViews, mCurrentItemPosition);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setImageBackground(dotViews, position);
                mCurrentItemPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //滑动到最后一页时  再次滑动则进入主页
        mViewPager.setOnTouchListener(new View.OnTouchListener() {
            float startX;
            float startY;//没有用到
            float endX;
            float endY;//没有用到

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = event.getX();
                        startY = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        endX = event.getX();
                        endY = event.getY();
                        int width = ScreenUtil.getScreenInfo()[0];
                        //首先要确定的是，是否到了最后一页，然后判断是否向左滑动，并且滑动距离是否符合，我这里的判断距离是屏幕宽度的4分之一（这里可以适当控制）
                        if (mCurrentItemPosition == (mDataList.size() - 1) && startX - endX >= (width / 6)) {
                            toLogin();
                            overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);//这部分代码是切换Activity时的动画，看起来就不会很生硬
                        }
                        break;
                }
                return false;
            }
        });
    }

    /**
     * 获取点的viewlist
     */
    protected ImageView[] getDotsView(int size) {
        int dotSize = ScreenUtil.dp2px(10);//点的大小
        final ImageView[] dotViews = new ImageView[size];
        for (int i = 0; i < size; i++) {
            //创建dots
            ImageView imageView = new ImageView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dotSize, dotSize);
            params.rightMargin = ScreenUtil.dp2px(5);
            dotViews[i] = imageView;
            mPointParent.addView(imageView, params);
        }
        return dotViews;
    }

    /**
     * 设置选中的dots的背景
     */
    protected void setImageBackground(ImageView[] dotViews, int selectItems) {
        for (int i = 0; i < dotViews.length; i++) {
            if (i == selectItems) {
                dotViews[i].setImageResource(R.drawable.welcome_item_white_dot);
            } else {
                dotViews[i].setImageResource(R.drawable.welcome_item_gray_dot);
            }
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.welcome_go_over:
                toLogin();
                break;
            case R.id.welcome_go_try:
                toLogin();
                break;
        }
    }

    private void toLogin() {
        Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
        startActivity(intent);
        WelcomeActivity.this.finish();
    }
}
