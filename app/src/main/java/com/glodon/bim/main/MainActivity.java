package com.glodon.bim.main;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.glodon.bim.R;
import com.glodon.bim.basic.image.ImageLoader;
import com.glodon.bim.basic.image.OnImageLoadListener;
import com.glodon.bim.basic.listener.ThrottleClickEvents;
import com.glodon.bim.basic.log.LogUtil;
import com.glodon.bim.business.greendao.provider.DaoProvider;

public class MainActivity extends BaseActivity {

    private Button btn;
    private ImageView iv;
    private String url = "http://imgsrc.baidu.com/baike/pic/item/adee30dd61e7dde38d1029c3.jpg";
    private Activity context;

    @Override
    protected View onCreateView() {
        context = this;
        View view = LayoutInflater.from(this).inflate(R.layout.activity_main,null);
        btn = view.findViewById(R.id.btn);
        iv = view.findViewById(R.id.iv);
        return view;
    }

    @Override
    protected void initDataForActivity() {
        super.initDataForActivity();
        requestPermissions();
        ThrottleClickEvents.throttleClick(btn, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                ImageLoader.showHeadIcon(context,url,iv);
//                ImageLoader.showImage(context,url,iv);
                ImageLoader.loadUrl(context, url, new OnImageLoadListener() {
                    @Override
                    public void onLoadBitmap(Bitmap bitmap) {
                        iv.setImageBitmap(bitmap);
                    }
                });
            }
        });

        LogUtil.d("====",new DaoProvider().getCookie());
    }

    private int REQUEST_EXTERNAL_STORAGE = 1;
    private void requestPermissions(){
        String[] PERMISSIONS_STORAGE = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        int permission = ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    MainActivity.this,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
