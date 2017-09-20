package com.glodon.bim.customview.pullrefreshview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Scroller;

/**
 * 描述：上下拉刷新
 * 作者：zhourf on 2017/9/19
 * 邮箱：zhourf@glodon.com
 */

public class PullRefreshView extends LinearLayout {

    private int CRISIS_VALUE = 120;//滑动过程的临界值，过值刷新  不过值直接返回
    private int STATE_IDLE = 0;//空闲状态，可上下拉刷新
    private int STATE_REFRESHING = 1;//刷新过程中
    private int state = STATE_IDLE;
    private boolean isPullDownEnable = true;//是否开启下拉刷新功能
    private boolean isPullUpEnable = true;//是否开启上拉加载功能
    private Scroller mScroller;
    private float mLastY;
    private RecyclerView mRecyclerView;
    private LinearLayout mHeader, mFooter;
    private OnPullRefreshListener mOnPullRefreshListener;
    private int endy = 0;

    //是否启用下拉刷新功能
    public void setPullDownEnable(boolean pullDownEnable) {
        isPullDownEnable = pullDownEnable;
        mHeader.setVisibility(pullDownEnable ? View.VISIBLE : View.GONE);
    }

    //是否启用上拉加载功能
    public void setPullUpEnable(boolean pullUpEnable) {
        isPullUpEnable = pullUpEnable;
        mFooter.setVisibility(pullUpEnable ? View.VISIBLE : View.GONE);
    }

    //设置上下拉接口回调
    public void setOnPullRefreshListener(OnPullRefreshListener mOnPullRefreshListener) {
        this.mOnPullRefreshListener = mOnPullRefreshListener;
    }

    public PullRefreshView(Context context) {
        super(context);
    }

    public PullRefreshView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mScroller = new Scroller(context);


    }

    public PullRefreshView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mRecyclerView = (RecyclerView) getChildAt(1);
        mHeader = (LinearLayout) getChildAt(0);
        mFooter = (LinearLayout) getChildAt(2);

    }

    /**
     * true  自己消费
     * 其他  交给父View的ontouchevent
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //在刷新过程中，拦截事件 不做处理
        if (state == STATE_REFRESHING) {
            return true;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                float cury = event.getY();
//                System.out.println("cury=" + cury);
                int dy = (int) ((mLastY - cury) * 0.3f);
//                System.out.println("dy=" + dy);
                if ((istop && (dy < 0)) || (isbottom && dy > 0)) {
                    scrollTo(0, dy);
                } else {
                    scrollTo(0, 0);
                }
                endy = (int) cury;
                break;
            case MotionEvent.ACTION_UP:
                endy = getScrollY();
                int scrolldy = endy - 0;
//                System.out.println("scrolldy=" + scrolldy);
                if (scrolldy > CRISIS_VALUE) {
                    //上拉加载更多
                    if (mOnPullRefreshListener != null && isPullUpEnable) {
                        state = STATE_REFRESHING;//改变状态
                        mOnPullRefreshListener.onPullUp();
                    }
                } else if (scrolldy < -CRISIS_VALUE) {
                    //下拉刷新
                    if (mOnPullRefreshListener != null && isPullDownEnable) {
                        state = STATE_REFRESHING;//改变状态
                        mOnPullRefreshListener.onPullDown();
                    }
                }
                mScroller.startScroll(0, endy, 0, -scrolldy, 500);

                break;
        }
        postInvalidate();
        return true;
    }

    private boolean istop;
    private boolean isbottom;

    /**
     * true：  交给自己的ontouchevent
     * 其他：  交给ziview的dipatch
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //在刷新过程中，拦截事件 不做处理
        if (state == STATE_REFRESHING) {
            return true;
        }
        istop = isTop();
        isbottom = isBottom();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastY = ev.getY();
                istop = isTop();
                isbottom = isBottom();
                break;
            case MotionEvent.ACTION_MOVE:
                float ds = ev.getY() - mLastY;
                if (istop) {
                    if (ds > 0) {//表示下拉
                        return true;
                    }

                }
                if (isbottom) {
                    if (ds < 0) {//上拉
                        return true;
                    }
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            scrollTo(0, mScroller.getCurrY());
            postInvalidate();
        }
    }

    //判断RecyclerView是否在顶部
    private boolean isTop() {
//        RecyclerView.canScrollVertically(1)的值表示是否能向上滚动，false表示已经滚动到底部
//        RecyclerView.canScrollVertically(-1)的值表示是否能向下滚动，false表示已经滚动到顶部
        return !mRecyclerView.canScrollVertically(-1);
    }

    //判断RecyclerView是否在底部
    private boolean isBottom() {
//        RecyclerView.canScrollVertically(1)的值表示是否能向上滚动，false表示已经滚动到底部
//        RecyclerView.canScrollVertically(-1)的值表示是否能向下滚动，false表示已经滚动到顶部
        return !mRecyclerView.canScrollVertically(1);
    }

    public RecyclerView getmRecyclerView() {
        return mRecyclerView;
    }

    //上拉加载完成
    public void onPullUpComplete() {
        state = STATE_IDLE;
    }

    //下拉刷新完成
    public void onPullDownComplete() {
        state = STATE_IDLE;
    }
}
