package com.glodon.bim.base;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.glodon.bim.customview.dialog.LoadingDialogManager;

public class BaseActivity extends AppCompatActivity {
    protected Activity mActivity;
    private LoadingDialogManager mLoadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        mLoadingDialog = new LoadingDialogManager(this);
        highApiEffects();
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void highApiEffects() {
        getWindow().getDecorView().setFitsSystemWindows(true);
        //透明状态栏 @顶部
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏 @底部    这一句不要加，目的是防止沉浸式状态栏和部分底部自带虚拟按键的手机（比如华为）发生冲突，注释掉就好了
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
    }

    /**
     * 显示加载数据进度条
     */
    public void showLoadDialog(boolean cancelable) {
        mLoadingDialog.show();
    }

    /**
     * 取消正在显示的dialog
     */
    public void dismissLoadDialog() {
        mLoadingDialog.dismiss();
    }

    public void requestPermission(String[] permissions, int requestCode) {

        int permission = ActivityCompat.checkSelfPermission(this, permissions[0]);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    this,
                    permissions,
                    requestCode
            );
        }
    }

    protected void initStatusBar(View mStatusView) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,  //设置StatusBar透明
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            int id = 0;
            id = getResources().getIdentifier("status_bar_height", "dimen", //获取状态栏的高度
                    "android");
            if (id > 0) {
                mStatusView.getLayoutParams().height = getResources() //设置状态栏的高度
                        .getDimensionPixelOffset(id);
//                mStatusView.getLayoutParams().height = getResources() //设置状态栏的高度
//                        .getDimensionPixelOffset(id);
            }
        }
    }
}
