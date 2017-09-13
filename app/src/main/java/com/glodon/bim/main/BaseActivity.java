package com.glodon.bim.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.glodon.bim.R;
import com.glodon.bim.customview.LoadingDialog;

/**
 * 描述：Activity基类
 * 作者：zhourf on 2017/9/11
 * 邮箱：zhourf@glodon.com
 */

public class BaseActivity extends AppCompatActivity {

    private LoadingDialog mLoadingDialog;//加载进度条
    private boolean mCancelAble;//是否可点击外面关闭进度条
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
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
}
