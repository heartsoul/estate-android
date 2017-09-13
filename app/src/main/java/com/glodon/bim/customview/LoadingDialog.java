package com.glodon.bim.customview;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.glodon.bim.R;

/**
 * 描述：加载进度框展示
 * 作者：zhourf on 2017/9/13
 * 邮箱：zhourf@glodon.com
 */

public class LoadingDialog extends Dialog{


    public LoadingDialog(@NonNull Context context) {
        super(context);
        init(context);
    }

    public LoadingDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    protected LoadingDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_loading, null);
        setContentView(view);
        Window dialogWindow = getWindow();
        WindowManager manager = ((Activity) context).getWindowManager();
        WindowManager.LayoutParams params = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        dialogWindow.setGravity(Gravity.CENTER);//设置对话框位置
        Display d = manager.getDefaultDisplay(); // 获取屏幕宽、高度
        params.width = (int) (d.getWidth() * 0.6); // 宽度设置为屏幕的0.65，根据实际情况调整
        dialogWindow.setAttributes(params);

    }
}
