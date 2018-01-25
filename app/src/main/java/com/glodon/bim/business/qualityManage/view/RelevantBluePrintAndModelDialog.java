package com.glodon.bim.business.qualityManage.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.glodon.bim.R;
import com.glodon.bim.business.authority.AuthorityManager;
import com.glodon.bim.business.qualityManage.listener.OnEquipmentClickListener;


/**
 * 描述：关联图纸和关联模型的底部弹出框
 * 作者：zhourf on 2017/9/29
 * 邮箱：zhourf@glodon.com
 */
public class RelevantBluePrintAndModelDialog {
    private Context context;
    private Dialog dialog; //悬浮框
    private Display display;//window展示
    //底部框   新建整改单  查看检查单
    private LinearLayout mBottomParent;
    private TextView mCreateView,mDetailView;

    public RelevantBluePrintAndModelDialog(Context context) {
        this.context = context;
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
    }


    /**
     * 新建整改单
     */
    public RelevantBluePrintAndModelDialog getRepairDialog(final View.OnClickListener createRepairListener, final View.OnClickListener detailListener) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_relevant_blueprint_model_dialog,null);
        mBottomParent = view.findViewById(R.id.relevant_blueprint_bottom);
        mCreateView = view.findViewById(R.id.relevant_blueprint_bottom_create);
        mDetailView = view.findViewById(R.id.relevant_blueprint_bottom_detail);
        mCreateView.setText("新建整改单");
        mCreateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(createRepairListener!=null)
                {
                    createRepairListener.onClick(view);
                }
                dismiss();
            }
        });
        mDetailView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(detailListener!=null)
                {
                    detailListener.onClick(view);
                }
                dismiss();
            }
        });
        dialogSetting(view);

        return this;
    }

    /**
     * 新建复查单
     */
    public RelevantBluePrintAndModelDialog getReviewDialog(final View.OnClickListener createReviewListener, final View.OnClickListener detailListener) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_relevant_blueprint_model_dialog,null);
        mBottomParent = view.findViewById(R.id.relevant_blueprint_bottom);
        mCreateView = view.findViewById(R.id.relevant_blueprint_bottom_create);
        mDetailView = view.findViewById(R.id.relevant_blueprint_bottom_detail);
        mCreateView.setText("新建复查单");
        mCreateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(createReviewListener!=null)
                {
                    createReviewListener.onClick(view);
                }
                dismiss();
            }
        });
        mDetailView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(detailListener!=null)
                {
                    detailListener.onClick(view);
                }
                dismiss();
            }
        });
        dialogSetting(view);
        return this;
    }

    /**
     * 新建复查单
     */
    public RelevantBluePrintAndModelDialog getQualityDetailDialog(final View.OnClickListener detailListener) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_relevant_blueprint_model_detail_dialog,null);
        mDetailView = view.findViewById(R.id.relevant_blueprint_bottom_detail_detail);
        mDetailView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(detailListener!=null)
                {
                    detailListener.onClick(view);
                }
                dismiss();
            }
        });
        dialogSetting(view);
        return this;
    }

    /**
     * 材设编辑状态
     */
    public RelevantBluePrintAndModelDialog getEquipmentEditDialog(final OnEquipmentClickListener listener) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_relevant_equipment_dialog,null);
        TextView submitView = view.findViewById(R.id.relevant_equipment_bottom_submit);
        View submitLine = view.findViewById(R.id.relevant_equipment_bottom_submit_line);
        TextView deleteView = view.findViewById(R.id.relevant_equipment_bottom_delete);
        View deleteLine = view.findViewById(R.id.relevant_equipment_bottom_delete_line);
        TextView editView = view.findViewById(R.id.relevant_equipment_bottom_edit);

        if(AuthorityManager.isEquipmentModify()){
            submitLine.setVisibility(View.VISIBLE);
            submitView.setVisibility(View.VISIBLE);
        }else{
            submitLine.setVisibility(View.GONE);
            submitView.setVisibility(View.GONE);
        }
        if(AuthorityManager.isEquipmentDelete()){
            deleteLine.setVisibility(View.VISIBLE);
            deleteView.setVisibility(View.VISIBLE);
        }else{
            deleteLine.setVisibility(View.GONE);
            deleteView.setVisibility(View.GONE);
        }

        submitView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener!=null)
                {
                    listener.submit();
                }
                dismiss();
            }
        });
        deleteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener!=null)
                {
                    listener.delete();
                }
                dismiss();
            }
        });
        editView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener!=null)
                {
                    listener.edit();
                }
                dismiss();
            }
        });
        dialogSetting(view);
        return this;
    }

    /**
     * 材设提交状态
     */
    public RelevantBluePrintAndModelDialog getEquipmentDialog(final View.OnClickListener listener) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_relevant_equipment_detail_dialog,null);
        TextView detailView = view.findViewById(R.id.relevant_equipment_bottom_detail);
        detailView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener!=null)
                {
                    listener.onClick(view);
                }
                dismiss();
            }
        });
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
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.x = 0;
        lp.y = 0;
        dialogWindow.setAttributes(lp);
        dialog.setContentView(view);
    }


    public RelevantBluePrintAndModelDialog setCancelable(boolean cancel) {
        dialog.setCancelable(cancel);
        return this;
    }

    public RelevantBluePrintAndModelDialog setCanceledOnTouchOutside(boolean cancel) {
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
