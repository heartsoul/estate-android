package com.glodon.bim.business.qualityManage.view;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

import com.glodon.bim.R;
import com.glodon.bim.base.BaseActivity;
import com.glodon.bim.basic.image.ImageLoader;
import com.glodon.bim.basic.image.OnImageLoadListener;
import com.glodon.bim.common.config.CommonConfig;

public class CreateCheckListActivity extends BaseActivity {

    private String mImagePath;
    private ImageView iv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quality_create_check_list_activity);

        mImagePath = getIntent().getStringExtra(CommonConfig.IAMGE_SAVE_PATH);

        iv = (ImageView) findViewById(R.id.create_check_list_iv);

//        ImageLoader.loadUrl(this, mImagePath, new OnImageLoadListener() {
//            @Override
//            public void onLoadBitmap(Bitmap bitmap) {
//                iv.setImageBitmap(bitmap);
//            }
//        });
        iv.setImageBitmap(BitmapFactory.decodeFile(mImagePath));

    }
}
