package com.glodon.bim.business.login.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.glodon.bim.R;

/**
 * 描述：登录错误三次后提示框
 * 作者：zhourf on 2017/10/16
 * 邮箱：zhourf@glodon.com
 */

public class ErrorMessageDialog {
    private Context context;
    private Dialog dialog; //悬浮框
    private Display display;//window展示
    private TextView mSureView,mFindPasswordView;

    public ErrorMessageDialog(Context context) {
        this.context = context;
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
    }


    public ErrorMessageDialog builder(final View.OnClickListener findPasswordClickListener) {
        View view = LayoutInflater.from(context).inflate(R.layout.login_error_message_dialog,null);
        mSureView = view.findViewById(R.id.login_error_message_dialog_sure);
        mFindPasswordView = view.findViewById(R.id.login_error_message_dialog_getPassword);

        //设置点击事件
        mSureView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        mFindPasswordView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findPasswordClickListener.onClick(view);
                dismiss();
            }
        });
        // 设置Dialog最小宽度为屏幕宽度
        view.setMinimumWidth(display.getWidth());

        // 定义Dialog布局和参数
        dialog = new Dialog(context, R.style.transparentDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window dialogWindow = dialog.getWindow();
//        dialogWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
//                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
//        dialogWindow.setGravity(Gravity.LEFT | Gravity.CENTER);
        dialogWindow.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.x = 0;
        lp.y = 0;
        dialogWindow.setAttributes(lp);
        dialog.setContentView(view);

        return this;
    }

    public ErrorMessageDialog setCancelable(boolean cancel) {
        dialog.setCancelable(cancel);
        return this;
    }

    public ErrorMessageDialog setCanceledOnTouchOutside(boolean cancel) {
        dialog.setCanceledOnTouchOutside(cancel);
        return this;
    }

    public void show() {
        if(dialog!=null) {
            dialog.show();
        }
    }

    public void dismiss(){
        if(dialog!=null) {
            dialog.dismiss();
        }
    }
}
