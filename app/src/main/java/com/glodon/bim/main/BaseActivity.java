package com.glodon.bim.main;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;

import com.glodon.bim.R;
import com.glodon.bim.customview.LoadingDialog;

/**
 * 描述：Activity基类
 * 作者：zhourf on 2017/9/11
 * 邮箱：zhourf@glodon.com
 */

public class BaseActivity extends Activity {

    private LoadingDialog mLoadingDialog;//加载进度条
    private boolean mCancelAble;//是否可点击外面关闭进度条
    private RelativeLayout mRootView;
    private View mChildView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_base);
        mRootView = findViewById(R.id.base_rootview);
        mChildView = onCreateView();
        if (mChildView != null) {
            mRootView.addView(onCreateView(), new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        }
        setListener();
        initDataForActivity();
    }

    /**
     * 子类的view
     */
    protected View onCreateView() {
        return null;
    }

    /**
     * 设置监听
     */
    protected void setListener(){

    }

    /**
     * 初始化数据
     */
    protected void initDataForActivity() {

    }

    /**
     * 显示加载数据进度条
     */
    public void showLoadDialog(boolean cancelable) {

        if (mLoadingDialog == null) {
            mCancelAble = cancelable;
            mLoadingDialog = new LoadingDialog(this);
        } else {
            if (mLoadingDialog.isShowing()) {
                mLoadingDialog.dismiss();
            }
        }
        mLoadingDialog.setCancelable(mCancelAble);
        mLoadingDialog.show();
    }

    /**
     * 取消正在显示的dialog
     */
    public void dismissLoadDialog() {
        mCancelAble = true;
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
    }

    public void requestPermission(String[] permissions,int requestCode){

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
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
