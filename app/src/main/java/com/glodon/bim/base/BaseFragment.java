package com.glodon.bim.base;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;

import com.glodon.bim.customview.dialog.LoadingDialogManager;

/**
 * 描述：Fragment基类
 * 作者：zhourf on 2017/9/11
 * 邮箱：zhourf@glodon.com
 */

public class BaseFragment extends Fragment {

    private LoadingDialogManager mLoadingDialog;//加载进度条


    /**
     * 显示加载数据进度条
     */
    public void showLoadDialog(boolean cancelable) {

        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialogManager(getActivity());
        }
        mLoadingDialog.show();
    }

    /**
     * 取消正在显示的dialog
     */
    public void dismissLoadDialog() {
        mLoadingDialog.dismiss();
    }

    public View inflate(int layoutId) {
        return LayoutInflater.from(getActivity()).inflate(layoutId, null);
    }
}
