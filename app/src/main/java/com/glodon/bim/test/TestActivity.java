package com.glodon.bim.test;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.glodon.bim.R;
import com.glodon.bim.basic.zxing.activity.CaptureActivity;

public class TestActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_testImage,btn_testRecyclerView,btn_zxing,btn_zxing_create,btn_share;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        btn_testImage = (Button) findViewById(R.id.btn_testimage);
        btn_testRecyclerView = (Button) findViewById(R.id.btn_recyclerView);
        btn_zxing = (Button) findViewById(R.id.btn_zxing);
        btn_zxing_create = (Button) findViewById(R.id.btn_zxing_create);
        btn_share = (Button) findViewById(R.id.btn_share);

        setListener();
    }

    @Override
    protected void onStart() {
        super.onStart();
        requestPermissiion();
    }

    private void setListener() {
        btn_testImage.setOnClickListener(this);
        btn_testRecyclerView.setOnClickListener(this);
        btn_zxing.setOnClickListener(this);
        btn_zxing_create.setOnClickListener(this);
        btn_share.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        Intent intent = new Intent();
        switch (id){
            case R.id.btn_testimage:
                intent.setClass(this,TestImageActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_recyclerView:
                intent.setClass(this,TestRecyclerViewActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_zxing:
                intent.setClass(this,CaptureActivity.class);
                startActivityForResult(intent,0);
                break;
            case R.id.btn_zxing_create:
                intent.setClass(this,TestZxingActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_share:
                intent.setClass(this,TestShareActivity.class);
                startActivity(intent);
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data!=null){
            System.out.println("result="+data.getStringExtra("result"));
        }
    }

    private int MY_PERMISSIONS_REQUEST_CALL_CAMERA = 1;
    private void requestPermissiion(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                // 如果没有授权，则请求授权
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_CALL_CAMERA);
            } else {
                // 有授权，直接开启摄像头
            }
        }
    }

    // 新增Android6.0 权限检查
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // 判断请求码
        if (requestCode == MY_PERMISSIONS_REQUEST_CALL_CAMERA) {
            //grantResults授权结果
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 成功，开启摄像头
                // callCamera();
            } else {
                // 授权失败
                Toast.makeText(TestActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.setData(Uri.parse("package:" + TestActivity.this.getPackageName()));
                TestActivity.this.startActivityForResult(intent, 1028);
                this.finish();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
