package com.glodon.bim.customview;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
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
     * 提示文字
     */
    public static void show(String text) {
        Context context = BaseApplication.getInstance();
        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        View view = LayoutInflater.from(context).inflate(R.layout.toast_show_text_view, null);
        TextView tv = view.findViewById(R.id.toast_show_view_text);
        tv.setText(text);
        toast.setView(view);
        toast.show();
    }

    /**
     * 提示文字和icon
     */
    public static void show(String text,int imgId) {
        Context context = BaseApplication.getInstance();
        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        View view = LayoutInflater.from(context).inflate(R.layout.toast_show_text_icon_view, null);
        TextView tv = view.findViewById(R.id.toast_show_icon_view_text);
        ImageView iv = view.findViewById(R.id.toast_show_icon_view_icon);
        tv.setText(text);
        iv.setBackgroundResource(imgId);
        toast.setView(view);
        toast.show();
    }

    /**
     * 网络差提示
     */
    public static void showNetWorkToast(){
        show(BaseApplication.getInstance().getResources().getString(R.string.str_toast_network_bad),R.drawable.icon_toast_network);
    }

    /**
     * 图片上传失败提示
     */
    public static void showUploadPictureFailedToast(){
        show(BaseApplication.getInstance().getResources().getString(R.string.str_toast_upload_fail));
    }
    /**
     * 新建检查单 提交成功提示
     */
    public static void showSubmitToast(){
        show(BaseApplication.getInstance().getResources().getString(R.string.str_toast_submit_success),R.drawable.icon_toast_submit_success);
    }

    /**
     * 新建检查单 保存成功提示
     */
    public static void showSaveToast(){
        show(BaseApplication.getInstance().getResources().getString(R.string.str_toast_save_success),R.drawable.icon_toast_submit_success);
    }
}
