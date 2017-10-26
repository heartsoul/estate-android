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
public class ChooseListDialog {
    private Context context;
    private Dialog dialog; //悬浮框
    private Display display;//window展示
    private TextView mSureView,mCancelView;
    private RecyclerView mRecyclerView;
    private RelativeLayout mRecyclerViewRoot;
    private ChooseListAdapter mAdapter;
    private int mSelectPosition=0;

    public ChooseListDialog(Context context) {
        this.context = context;
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
    }
    public ChooseListDialog(Context context,int selectPosition) {
        this.context = context;
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
        this.mSelectPosition = selectPosition;
    }


    public ChooseListDialog builder(final OnChooseListListener listener, List<String> list) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_choose_list_dialog,null);
        mSureView = view.findViewById(R.id.choose_list_sure);
        mCancelView = view.findViewById(R.id.choose_list_cancel);
        mRecyclerView = view.findViewById(R.id.choose_list_recyclerview);
        mRecyclerViewRoot = view.findViewById(R.id.choose_list_recyclerview_root);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mRecyclerViewRoot.getLayoutParams();
        params.width = LinearLayout.LayoutParams.MATCH_PARENT;
        if(list!=null){
            int size = list.size();
            if(size>5){
                params.height = ScreenUtil.dp2px(235);
            }else{
                params.height = LinearLayout.LayoutParams.WRAP_CONTENT;
                mRecyclerView.getLayoutParams().height = LinearLayout.LayoutParams.WRAP_CONTENT;
            }
        }
        mRecyclerViewRoot.setLayoutParams(params);

        mAdapter = new ChooseListAdapter((Activity) context,list,mSelectPosition);
        mAdapter.setmListener(new OnChooseListListener() {
            @Override
            public void onSelect(int position) {
                mSelectPosition = position;
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        //设置点击事件
        mSureView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener!=null) {
                    listener.onSelect(mSelectPosition);
                }
                dismiss();
            }
        });

        mCancelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.x = 0;
        lp.y = 0;
        dialogWindow.setAttributes(lp);
        dialog.setContentView(view);

        return this;
    }



    public ChooseListDialog setCancelable(boolean cancel) {
        dialog.setCancelable(cancel);
        return this;
    }

    public ChooseListDialog setCanceledOnTouchOutside(boolean cancel) {
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
