package com.glodon.bim.business.qualityManage.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.glodon.bim.business.qualityManage.listener.OnPhotoEditChangeListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：照片编辑页
 * 作者：zhourf on 2017/9/26
 * 邮箱：zhourf@glodon.com
 */

public class PhotoEditView extends ImageView {
    private Paint mPaint;
    private Path mPath;
    private List<Path> list;
    private boolean isAdd = false;
    private OnPhotoEditChangeListener mListener;

    public void setmListener(OnPhotoEditChangeListener mListener) {
        this.mListener = mListener;
    }

    private float preX, preY;// 记录上一个触摸事件的位置坐标
    private static final int MIN_MOVE_DIS = 5;// 最小的移动距离：如果我们手指在屏幕上的移动距离小于此值则不会绘制

    public PhotoEditView(Context context) {
        super(context);
        init();
    }

    public PhotoEditView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        list = new ArrayList<>();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPath = new Path();
        mPaint.setXfermode(getPdf());
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(20);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawPath(mPath,mPaint);

    }

    private PorterDuffXfermode getPdf(){
        PorterDuff.Mode mode = PorterDuff.Mode.SRC_IN;
        return  new PorterDuffXfermode(mode);
    }

    /**
     * View的事件将会在7/12详解
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        /*
         * 获取当前事件位置坐标
         */
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:// 手指接触屏幕重置路径

                mPath.moveTo(x, y);
                preX = x;
                preY = y;
                break;
            case MotionEvent.ACTION_MOVE:// 手指移动时连接路径
                float dx = Math.abs(x - preX);
                float dy = Math.abs(y - preY);
                if (dx >= MIN_MOVE_DIS || dy >= MIN_MOVE_DIS) {
                    mPath.quadTo(preX, preY, (x + preX) / 2, (y + preY) / 2);
                    preX = x;
                    preY = y;
                    isAdd = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                if(isAdd) {
                    Path path = new Path();
                    path.addPath(mPath);
                    list.add(path);
                    mListener.change(list.size()>0);
                }
                isAdd = false;
                break;
        }

        // 重绘视图
        invalidate();
        return true;
    }



    public void playBack() {
        int size = list.size();
        if(size>0){
            list.remove(size-1);
            if(list.size()>0) {
                mPath = list.get(list.size() - 1);
            }else{
                mPath.reset();
            }
        }else{
            mPath.reset();
        }
        invalidate();
        mListener.change(list.size()>0);
    }

    public void cancel(){
        list.clear();
        mPath.reset();
        invalidate();
        mListener.change(list.size()>0);
    }

    public boolean isShowPlayBack(){
        return list.size()>0;
    }
}
