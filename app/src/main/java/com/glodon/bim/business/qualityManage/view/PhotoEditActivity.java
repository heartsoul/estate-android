package com.glodon.bim.business.qualityManage.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.View;
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
import com.glodon.bim.basic.utils.CameraUtil;
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

    private TextView mTopCancel,mTopFinish;
    private LinearLayout mBottomContent;
    private LinearLayout mBottomDelete;
    private LinearLayout mBottomDrawFunction;
    private ImageView mDrawLine,mDrawText;
    private View mLineView;
    private LinearLayout mBottomCancelFinishParent,mBottomCancel,mBottomFinish;
    private LinearLayout mColorParent;
    private ImageView mColor0,mColor1,mColor2,mColor3,mColor4,mColor5,mColor6,mColor7,mColorBack;
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

    }

    private void initView() {
        mImagePath = getIntent().getStringExtra(CommonConfig.IMAGE_PATH);
        mSavePath = CameraUtil.getFilePath();

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
                mColorBack.setVisibility(isShowPlayBack?View.VISIBLE:View.GONE);
            }
        });
        //初次点击划线
        mDrawLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawLine.setBackgroundResource(R.drawable.icon_category_item_mx);
                mTopCancel.setVisibility(View.VISIBLE);
                mTopFinish.setVisibility(View.VISIBLE);
                mColorParent.setVisibility(View.VISIBLE);
                mColorBack.setVisibility(mPhotoEditView.isShowPlayBack()?View.VISIBLE:View.GONE);
                mBottomCancelFinishParent.setVisibility(View.GONE);
                mTopCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
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
                Intent intent = new Intent(mActivity,CreateCheckListActivity.class);
                intent.putExtra(CommonConfig.IAMGE_SAVE_PATH,mSavePath);
                mActivity.startActivity(intent);
            }
        });

        //初次点击绘制文字
        mDrawText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTopCancel.setVisibility(View.VISIBLE);
                mTopFinish.setVisibility(View.VISIBLE);

                mBottomDrawFunction.setVisibility(View.GONE);
                mLineView.setVisibility(View.GONE);

                mColorParent.setVisibility(View.VISIBLE);
                mColorBack.setVisibility(View.GONE);

                mBottomCancelFinishParent.setVisibility(View.GONE);

                mEditText.setVisibility(View.VISIBLE);
                mShowText.setVisibility(View.GONE);

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
                        if(TextUtils.isEmpty(text)){
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
                        }else{
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
                                    if(mShowText.getBottom()>mBottomDelete.getTop()){
                                        //大于范围删除
                                        mShowText.setText("");
                                        mShowText.setVisibility(View.GONE);
                                        //删除完文字 保存
                                        saveTextToImage(mPhotoEditView);
                                    }else{
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



    private void saveToLocal(ImageView view){
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
    }
    private void saveTextToImage(ImageView view){
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        TextPaint tp = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        tp.setColor(Color.RED);
        tp.setStyle(Paint.Style.STROKE);
        tp.setStrokeWidth(2);
        tp.setTextSize(18);
        canvas.drawText(mShowText.getText().toString().trim(),mShowText.getLeft(),mShowText.getTop(),tp);

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
    }

}
