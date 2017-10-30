package com.glodon.bim.business.qualityManage.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.glodon.bim.R;
import com.glodon.bim.basic.utils.ScreenUtil;
import com.glodon.bim.business.qualityManage.adapter.ChooseListAdapter;
import com.glodon.bim.business.qualityManage.listener.OnChooseListListener;

import java.util.List;


/**
 * 描述：选择联系人和施工单位
 * 作者：zhourf on 2017/9/29
 * 邮箱：zhourf@glodon.com
 */
public class SaveDeleteDialog {
    private Context context;
    private Dialog dialog; //悬浮框
    private Display display;//window展示
    private TextView mTitleView,mContentView,mLeftView,mMiddleView,mRightView;
    private View mLineView;

    public SaveDeleteDialog(Context context) {
        this.context = context;
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
    }


    public SaveDeleteDialog getHintDialog(String content) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_save_delete_dialog,null);
        mTitleView = view.findViewById(R.id.save_delete_dialog_title);
        mContentView = view.findViewById(R.id.save_delete_dialog_content);
        mLeftView = view.findViewById(R.id.save_delete_dialog_left);
        mMiddleView = view.findViewById(R.id.save_delete_dialog_middle);
        mRightView = view.findViewById(R.id.save_delete_dialog_right);
        mLineView = view.findViewById(R.id.save_delete_dialog_middle_line);

        mTitleView.setText("提示信息");
        mContentView.setText(content);

        dialogSetting(view);

        return this;
    }

    private void dialogSetting(View view) {
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
    }


    public SaveDeleteDialog setCancelable(boolean cancel) {
        dialog.setCancelable(cancel);
        return this;
    }

    public SaveDeleteDialog setCanceledOnTouchOutside(boolean cancel) {
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
