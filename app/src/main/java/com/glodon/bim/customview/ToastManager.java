package com.glodon.bim.customview;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.glodon.bim.R;
import com.glodon.bim.base.BaseApplication;

/**
 * 描述：Toast提示
 * 作者：zhourf on 2017/9/29
 * 邮箱：zhourf@glodon.com
 */

public class ToastManager {

    /**
     * 提示
     */
    public static void show(String text) {
        Context context = BaseApplication.getInstance();
        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        View view = LayoutInflater.from(context).inflate(R.layout.toast_show_view, null);
        TextView tv = view.findViewById(R.id.toast_show_view_text);
        tv.setText(text);
        toast.setView(view);
        toast.show();
    }
}
