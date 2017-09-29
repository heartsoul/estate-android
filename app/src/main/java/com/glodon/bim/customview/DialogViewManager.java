package com.glodon.bim.customview;

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
 * 描述：弹出框
 * 作者：zhourf on 2017/9/29
 * 邮箱：zhourf@glodon.com
 */
public class DialogViewManager {
    private Context context;
    private Dialog dialog; //悬浮框
    private Display display;//window展示
    private TextView textView,cancelView,sureView;//相册

    public DialogViewManager(Context context) {
        this.context = context;
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
    }

    /**
     * 设置显示内容
     */
    public DialogViewManager setText(String text){
        if(textView!=null) {
            textView.setText(text);
        }
        return this;
    }

    public DialogViewManager builder(View.OnClickListener listener) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_view,null);
        textView = (TextView) view.findViewById(R.id.item_trends_delete_dialog_text);
        cancelView = (TextView) view.findViewById(R.id.item_trends_delete_dialog_no);
        sureView = (TextView) view.findViewById(R.id.item_trends_delete_dialog_yes);

        //设置点击事件
        cancelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        sureView.setOnClickListener(listener);
        // 设置Dialog最小宽度为屏幕宽度
        view.setMinimumWidth(display.getWidth());

        // 定义Dialog布局和参数
        dialog = new Dialog(context, R.style.transparentDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window dialogWindow = dialog.getWindow();
//        dialogWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
//                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        dialogWindow.setGravity(Gravity.LEFT | Gravity.CENTER);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.x = 0;
        lp.y = 0;
        dialogWindow.setAttributes(lp);
        dialog.setContentView(view);

        return this;
    }

    public DialogViewManager setCancelable(boolean cancel) {
        dialog.setCancelable(cancel);
        return this;
    }

    public DialogViewManager setCanceledOnTouchOutside(boolean cancel) {
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
