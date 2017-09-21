package com.glodon.bim.test;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.glodon.bim.R;
import com.glodon.bim.basic.utils.BitmapUtil;
import com.glodon.bim.basic.zxing.utils.QRCodeUtil;

public class TestZxingActivity extends AppCompatActivity {

    private Button btn;
    private ImageView iv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_zxing);
        btn = (Button) findViewById(R.id.test_btn_zxing);
        iv = (ImageView) findViewById(R.id.test_zxing_iv);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Bitmap bm = QRCodeUtil.createQRImage("测试测试测试测试测试测试测试测",150,150);
                Bitmap bm = QRCodeUtil.createQRImageWithLogoByResource("测试测试测试测试测试测试测试测",R.drawable.ic_launcher,TestZxingActivity.this);
                System.out.println("字节："+BitmapUtil.getBitmapSize(bm));
                iv.setBackground(new BitmapDrawable(bm));
            }
        });
    }
}
