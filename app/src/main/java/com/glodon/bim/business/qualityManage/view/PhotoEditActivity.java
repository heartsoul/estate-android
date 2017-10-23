package com.glodon.bim.business.qualityManage.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.glodon.bim.R;
import com.glodon.bim.base.BaseActivity;
import com.glodon.bim.basic.image.ImageLoader;
import com.glodon.bim.basic.image.OnImageLoadListener;
import com.glodon.bim.basic.log.LogUtil;
import com.glodon.bim.basic.utils.CameraUtil;
import com.glodon.bim.basic.utils.InputMethodutil;
import com.glodon.bim.business.qualityManage.listener.OnDragTextListener;
import com.glodon.bim.business.qualityManage.listener.OnPhotoEditChangeListener;
import com.glodon.bim.common.config.CommonConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 描述：拍照后编辑
 * 作者：zhourf on 2017/9/8
 * 邮箱：zhourf@glodon.com
 */
public class PhotoEditActivity extends BaseActivity {

    private String mImagePath;
    private String mSavePath;

    private PhotoEditView mPhotoEditView;
    private EditText mEditText;
    private DragTextView mShowText;

    private TextView mTopCancel, mTopFinish;
    private LinearLayout mBottomContent;
    private LinearLayout mBottomDelete;
    private LinearLayout mBottomDrawFunction;
    private ImageView mDrawLine, mDrawText;
    private View mLineView;
    private LinearLayout mBottomCancelFinishParent, mBottomCancel, mBottomFinish;
    private LinearLayout mColorParent;
    private ImageView mColor0, mColor1, mColor2, mColor3, mColor4, mColor5, mColor6, mColor7, mColorBack;
    private int softHeight=0;//输入法高度
    private RelativeLayout rootLayout;//跟布局
    private View mColorBottomView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.quality_photo_edit_activity);

        initView();

        setListener();

        getInputMethodHeight();
    }

    private void initView() {
        rootLayout = (RelativeLayout) findViewById(R.id.photo_edit_root_layout);
        mImagePath = getIntent().getStringExtra(CommonConfig.IMAGE_PATH);


        mEditText = (EditText) findViewById(R.id.photo_edit_et);
        mShowText = (DragTextView) findViewById(R.id.photo_edit_show_text);

        mTopCancel = (TextView) findViewById(R.id.photo_edit_top_cancel);
        mTopFinish = (TextView) findViewById(R.id.photo_edit_top_finish);

        mBottomContent = (LinearLayout) findViewById(R.id.photo_edit_bottom_content);
        mBottomDelete = (LinearLayout) findViewById(R.id.photo_edit_bottom_delete);

        mBottomDrawFunction = (LinearLayout) findViewById(R.id.photo_edit_bottom_function);
        mDrawLine = (ImageView) findViewById(R.id.photo_edit_bottom_function_draw_line);
        mDrawText = (ImageView) findViewById(R.id.photo_edit_bottom_function_draw_text);

        mLineView = findViewById(R.id.photo_edit_bottom_line);
        mBottomCancelFinishParent = (LinearLayout) findViewById(R.id.photo_edit_bottom_cancel_finish);
        mBottomCancel = (LinearLayout) findViewById(R.id.photo_edit_bottom_cancel);
        mBottomFinish = (LinearLayout) findViewById(R.id.photo_edit_bottom_finish);

        mColorParent = (LinearLayout) findViewById(R.id.photo_edit_bottom_color_parent);
        mColor0 = (ImageView) findViewById(R.id.photo_edit_bottom_color_0);
        mColor1 = (ImageView) findViewById(R.id.photo_edit_bottom_color_1);
        mColor2 = (ImageView) findViewById(R.id.photo_edit_bottom_color_2);
        mColor3 = (ImageView) findViewById(R.id.photo_edit_bottom_color_3);
        mColor4 = (ImageView) findViewById(R.id.photo_edit_bottom_color_4);
        mColor5 = (ImageView) findViewById(R.id.photo_edit_bottom_color_5);
        mColor6 = (ImageView) findViewById(R.id.photo_edit_bottom_color_6);
        mColor7 = (ImageView) findViewById(R.id.photo_edit_bottom_color_7);
        mColorBack = (ImageView) findViewById(R.id.photo_edit_bottom_color_back);

        mColorBottomView = findViewById(R.id.photo_edit_bottom_color_parent_bottomview);

        mPhotoEditView = (PhotoEditView) findViewById(R.id.photo_edit_background);
        ImageLoader.loadUrl(this, mImagePath, new OnImageLoadListener() {
            @Override
            public void onLoadBitmap(Bitmap bitmap) {
                mPhotoEditView.setImageBitmap(bitmap);
            }
        });
    }

    private void setListener() {
        //划线后的 控制回退按钮的显示
        mPhotoEditView.setmListener(new OnPhotoEditChangeListener() {
            @Override
            public void change(boolean isShowPlayBack) {
                mColorBack.setVisibility(isShowPlayBack ? View.VISIBLE : View.GONE);
            }
        });
        //初次点击划线
        mDrawLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPhotoEditView.setIsCanDraw(true);
                mDrawLine.setBackgroundResource(R.drawable.icon_category_item_mx);
                mTopCancel.setVisibility(View.VISIBLE);
                mTopFinish.setVisibility(View.VISIBLE);
                mColorParent.setVisibility(View.VISIBLE);
                mColorBack.setVisibility(mPhotoEditView.isShowPlayBack() ? View.VISIBLE : View.GONE);
                mBottomCancelFinishParent.setVisibility(View.GONE);
                mTopCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mPhotoEditView.setIsCanDraw(false);
                        mPhotoEditView.cancel();

                        mDrawLine.setBackgroundResource(R.drawable.icon_category_item_tj);
                        mTopCancel.setVisibility(View.GONE);
                        mTopFinish.setVisibility(View.GONE);
                        mColorParent.setVisibility(View.GONE);
                        mBottomCancelFinishParent.setVisibility(View.VISIBLE);
                    }
                });
                mTopFinish.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mPhotoEditView.setIsCanDraw(false);
                        mDrawLine.setBackgroundResource(R.drawable.icon_category_item_tj);
                        mTopCancel.setVisibility(View.GONE);
                        mTopFinish.setVisibility(View.GONE);
                        mColorParent.setVisibility(View.GONE);
                        mBottomCancelFinishParent.setVisibility(View.VISIBLE);
                        //保存到本地
                        saveToLocal(mPhotoEditView);
//                        //展示新图
//                        ImageLoader.loadUrl(mActivity, mSavePath, new OnImageLoadListener() {
//                            @Override
//                            public void onLoadBitmap(Bitmap bitmap) {
//                                mPhotoEditView.setImageBitmap(bitmap);
//                            }
//                        });
                    }
                });
            }
        });

        //划线回退
        mColorBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPhotoEditView.playBack();
            }
        });

        mBottomCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActivity.finish();
            }
        });

        mBottomFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mActivity, CreateCheckListActivity.class);
                if (TextUtils.isEmpty(mSavePath)) {
                    mSavePath = mImagePath;
                }
                intent.putExtra(CommonConfig.IAMGE_SAVE_PATH, mSavePath);
                mActivity.startActivity(intent);
            }
        });

        //初次点击绘制文字
        mDrawText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //从划线的编辑状态  直接切换到输入文字 //保存到本地
                mPhotoEditView.setIsCanDraw(false);
                saveToLocal(mPhotoEditView);

                mTopCancel.setVisibility(View.VISIBLE);
                mTopFinish.setVisibility(View.VISIBLE);

                mBottomDrawFunction.setVisibility(View.GONE);
                mLineView.setVisibility(View.GONE);

                mColorParent.setVisibility(View.VISIBLE);
                mColorBack.setVisibility(View.GONE);

                mBottomCancelFinishParent.setVisibility(View.GONE);

                mEditText.setVisibility(View.VISIBLE);
                mShowText.setVisibility(View.GONE);

                //弹起输入法
                mEditText.setFocusable(true);
                InputMethodutil.ShowKeyboard(mEditText);

                mTopCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mTopCancel.setVisibility(View.GONE);
                        mTopFinish.setVisibility(View.GONE);

                        mBottomDrawFunction.setVisibility(View.VISIBLE);
                        mLineView.setVisibility(View.VISIBLE);

                        mColorParent.setVisibility(View.GONE);
                        mColorBack.setVisibility(View.GONE);

                        mBottomCancelFinishParent.setVisibility(View.VISIBLE);

                        mEditText.setText("");
                        mEditText.setVisibility(View.GONE);
                        mShowText.setText("");
                        mShowText.setVisibility(View.GONE);
                    }
                });
                mTopFinish.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String text = mEditText.getText().toString().trim();
                        if (TextUtils.isEmpty(text)) {
                            mTopCancel.setVisibility(View.GONE);
                            mTopFinish.setVisibility(View.GONE);

                            mBottomDrawFunction.setVisibility(View.VISIBLE);
                            mLineView.setVisibility(View.VISIBLE);

                            mColorParent.setVisibility(View.GONE);
                            mColorBack.setVisibility(View.GONE);

                            mBottomCancelFinishParent.setVisibility(View.VISIBLE);

                            mEditText.setText("");
                            mEditText.setVisibility(View.GONE);
                            mShowText.setText("");
                            mShowText.setVisibility(View.GONE);
                        } else {
                            mTopCancel.setVisibility(View.GONE);
                            mTopFinish.setVisibility(View.GONE);

                            mBottomDrawFunction.setVisibility(View.VISIBLE);
                            mLineView.setVisibility(View.VISIBLE);

                            mColorParent.setVisibility(View.GONE);
                            mColorBack.setVisibility(View.GONE);

                            mBottomCancelFinishParent.setVisibility(View.VISIBLE);

                            mShowText.setText(text);
                            mShowText.setVisibility(View.VISIBLE);
                            mEditText.setText("");
                            mEditText.setVisibility(View.GONE);

                            //输入完文字 保存
                            saveTextToImage(mPhotoEditView);

                            //拖动文字的监听
                            mShowText.setmListener(new OnDragTextListener() {
                                @Override
                                public void onStartDrag() {
                                    mBottomContent.setVisibility(View.GONE);
                                    mBottomDelete.setVisibility(View.VISIBLE);
                                }

                                @Override
                                public void onStopDrag() {
                                    mBottomContent.setVisibility(View.VISIBLE);
                                    mBottomDelete.setVisibility(View.GONE);
                                    if (mShowText.getBottom() > mBottomDelete.getTop()) {
                                        //大于范围删除
                                        mShowText.setText("");
                                        mShowText.setVisibility(View.GONE);
                                        //删除完文字 保存
                                        saveTextToImage(mPhotoEditView);
                                    } else {
                                        //输入完文字 保存
                                        saveTextToImage(mPhotoEditView);
                                    }
                                }
                            });
                        }
                    }
                });
            }
        });

    }

    /**
     * 输入法高度
     */
    private void getInputMethodHeight(){
        rootLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                rootLayout.getWindowVisibleDisplayFrame(r);

                int screenHeight = rootLayout.getRootView().getHeight();
                softHeight = screenHeight - (r.bottom - r.top);

                //更改颜色框位置
//                ((RelativeLayout.LayoutParams) mColorParent.getLayoutParams()).bottomMargin = softHeight;
                if(softHeight>0) {
                    mColorBottomView.getLayoutParams().height = softHeight;
                    mColorBottomView.setVisibility(View.VISIBLE);
                }else{
                    mColorBottomView.setVisibility(View.GONE);
                }
                LogUtil.e("----------",softHeight+"");
                //boolean visible = heightDiff > screenHeight / 3;
            }
        });
    }

    /**
     * 保存划线到图片
     */
    private void saveToLocal(ImageView view) {
        if(TextUtils.isEmpty(mSavePath)){
            mSavePath = CameraUtil.getFilePath();
        }
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        try {
            File imageFile = new File(mSavePath);
            FileOutputStream outStream;
            outStream = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
            outStream.flush();
            outStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mPhotoEditView.setImageBitmap(bitmap);
        mPhotoEditView.cancel();
    }

    /**
     * 保存文字到图片
     */
    private void saveTextToImage(ImageView view) {
        if(TextUtils.isEmpty(mSavePath)){
            mSavePath = CameraUtil.getFilePath();
        }
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        TextPaint tp = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        tp.setColor(Color.RED);
        tp.setStyle(Paint.Style.STROKE);
        tp.setStrokeWidth(2);
        tp.setTextSize(18);
        canvas.drawText(mShowText.getText().toString().trim(), mShowText.getLeft(), mShowText.getTop(), tp);

        try {
            File imageFile = new File(mSavePath);
            FileOutputStream outStream;
            outStream = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
            outStream.flush();
            outStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mPhotoEditView.setImageBitmap(bitmap);
        mPhotoEditView.cancel();
    }

}
