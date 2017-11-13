package com.glodon.bim.customview;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

import com.glodon.bim.R;
import com.glodon.bim.basic.utils.ScreenUtil;

/**
 * 描述：自定义阴影
 * 作者：zhourf on 2017/9/26
 * 邮箱：zhourf@glodon.com
 */

public class CustomShadow extends View {
    private Paint mPaintShadow;
    private Paint mPaintCenter;
    private int left, top;
    private int padding;//边距
    private int radius = 6;//圆角

    public CustomShadow(Context context) {
        super(context);
        init();
    }

    public CustomShadow(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        padding =ScreenUtil.dp2px(7);
        mPaintShadow = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mPaintShadow.setStyle(Paint.Style.FILL);
        mPaintShadow.setColor(getResources().getColor(R.color.c_1a000000));
        mPaintCenter = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mPaintCenter.setStyle(Paint.Style.FILL);
        mPaintCenter.setColor(Color.WHITE);
        mPaintShadow.setMaskFilter(new BlurMaskFilter(padding,BlurMaskFilter.Blur.SOLID));
        left = padding;
        top = padding;
    }




    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.WHITE);
        canvas.drawRoundRect(left,top,getWidth()-padding,getHeight()-padding,radius,radius, mPaintShadow);
        canvas.drawRoundRect(left,top,getWidth()-padding,getHeight()-padding,radius,radius, mPaintCenter);
    }

}
