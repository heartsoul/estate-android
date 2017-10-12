package com.glodon.bim.test;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.glodon.bim.R;
import com.glodon.bim.basic.image.ImageLoader;
import com.glodon.bim.basic.image.OnImageLoadListener;
import com.glodon.bim.basic.listener.ThrottleClickEvents;
import com.glodon.bim.base.BaseTitleActivity;

public class TestImageActivity extends BaseTitleActivity {

    private Button btn;
    private ImageView iv;
    private String url = "http://imgsrc.baidu.com/baike/pic/item/adee30dd61e7dde38d1029c3.jpg";
    private Activity context;

    @Override
    protected View onCreateView() {
        context = this;
        View view = LayoutInflater.from(this).inflate(R.layout.activity_test_image, null);
        btn = view.findViewById(R.id.btn);
        iv = view.findViewById(R.id.iv);
        return view;
    }

    @Override
    protected void initDataForActivity() {
        super.initDataForActivity();
        ThrottleClickEvents.throttleClick(btn, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageLoader.showHeadIcon(context,url,iv);
                ImageLoader.showImage(context,url,iv);
                ImageLoader.loadUrl(context, url, new OnImageLoadListener() {
                    @Override
                    public void onLoadBitmap(Bitmap bitmap) {
                        iv.setImageBitmap(bitmap);
                    }
                });
            }
        });
    }
}
