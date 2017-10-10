package com.glodon.bim.business.qualityManage.view;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.glodon.bim.R;
import com.glodon.bim.basic.image.ImageLoader;
import com.glodon.bim.basic.image.OnImageLoadListener;
import com.glodon.bim.basic.utils.CameraUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 描述：拍照后编辑
 * 作者：zhourf on 2017/9/8
 * 邮箱：zhourf@glodon.com
 */
public class PhotoEditActivity extends AppCompatActivity {

//    private ImageView iv;
    private String mImagePath;
    private PhotoEditView mPhotoEditView;
    private Button mPlayBack,mSave,mWrite,mFinish,mFinish2;
    private String mSavePath;
    private ImageView mShow;
    private EditText mEditText;
    private DragTextView mShowText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.quality_photo_edit_activity);

        mImagePath = getIntent().getStringExtra("imagePath");
        mSavePath = CameraUtil.getFilePath();
        mPlayBack = (Button) findViewById(R.id.photo_edit_play_back);
        mSave = (Button) findViewById(R.id.photo_edit_save);
        mShow = (ImageView) findViewById(R.id.photo_edit_show);
        mWrite = (Button) findViewById(R.id.photo_edit_write);
        mFinish = (Button) findViewById(R.id.photo_edit_finish);
        mFinish2 = (Button) findViewById(R.id.photo_edit_finish2);
        mEditText = (EditText) findViewById(R.id.photo_edit_et);
        mShowText = (DragTextView) findViewById(R.id.photo_edit_show_text);

        mPhotoEditView = (PhotoEditView) findViewById(R.id.photo_edit_background);
//        ImageLoader.showImage(this,mImagePath,mPhotoEditView);
        ImageLoader.loadUrl(this, mImagePath, new OnImageLoadListener() {
            @Override
            public void onLoadBitmap(Bitmap bitmap) {
                mPhotoEditView.setImageBitmap(bitmap);
            }
        });

        mPlayBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPhotoEditView.playBack();
            }
        });
        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveToLocal(mPhotoEditView);
                mPhotoEditView.setVisibility(View.GONE);
                mShow.setVisibility(View.VISIBLE);
//                ImageLoader.showImage(PhotoEditActivity.this,mSavePath,mShow);
                ImageLoader.loadUrl(PhotoEditActivity.this, mSavePath, new OnImageLoadListener() {
                    @Override
                    public void onLoadBitmap(Bitmap bitmap) {
                        mShow.setImageBitmap(bitmap);
                    }
                });
            }
        });

        mWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEditText.setVisibility(View.VISIBLE);
                mShowText.setVisibility(View.GONE);
            }
        });

        mFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEditText.setVisibility(View.GONE);
                mShowText.setText(mEditText.getText().toString().trim());
                mShowText.setVisibility(View.VISIBLE);
            }
        });

        mFinish2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveTextToImage(mPhotoEditView);
                mPhotoEditView.setVisibility(View.GONE);
                mShow.setVisibility(View.VISIBLE);
                ImageLoader.loadUrl(PhotoEditActivity.this, mSavePath, new OnImageLoadListener() {
                    @Override
                    public void onLoadBitmap(Bitmap bitmap) {
                        mShow.setImageBitmap(bitmap);
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
