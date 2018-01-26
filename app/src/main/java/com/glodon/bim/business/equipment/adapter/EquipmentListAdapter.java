package com.glodon.bim.business.equipment.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.glodon.bim.R;
import com.glodon.bim.basic.listener.ThrottleClickEvents;
import com.glodon.bim.basic.utils.DateUtil;
import com.glodon.bim.basic.utils.ScreenUtil;
import com.glodon.bim.business.equipment.bean.EquipmentListBeanItem;
import com.glodon.bim.business.equipment.listener.OnOperateEquipmentSheetListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：质检清单列表adapter
 * 作者：zhourf on 2017/9/30
 * 邮箱：zhourf@glodon.com
 */

public class EquipmentListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<EquipmentListBeanItem> mDataList;
    private OnOperateEquipmentSheetListener mListener;

    public EquipmentListAdapter(Context mContext, OnOperateEquipmentSheetListener listener) {
        this.mContext = mContext;
        mDataList = new ArrayList<>();
        this.mListener = listener;
    }

    public void updateList(List<EquipmentListBeanItem> dataList) {
        mDataList.clear();
        mDataList.addAll(dataList);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0:
                return new TimeHolder(LayoutInflater.from(mContext).inflate(R.layout.equipment_list_item_time_view, null));
            case 1:
                return new SheetEditHolder(LayoutInflater.from(mContext).inflate(R.layout.equipment_list_item_edit_sheet_view, null));
            case 2:
                return new SheetHolder(LayoutInflater.from(mContext).inflate(R.layout.equipment_list_item_sheet_view, null));
            default:
                return null;
        }


    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final EquipmentListBeanItem item = mDataList.get(position);
        if(holder instanceof SheetHolder){
            SheetHolder sHolder = (SheetHolder) holder;
            sHolder.mTimeView.setText("进场日期:"+DateUtil.getShowDate(item.approachDate));
            sHolder.mIndexView.setText("批号："+item.batchCode);
            sHolder.mCodeView.setText("编码："+item.facilityCode);
            sHolder.mNameView.setText("名称："+item.facilityName);
            sHolder.mStandardView.setBackgroundResource(item.qualified ?R.drawable.icon_up_to_standard:R.drawable.icon_not_up_to_standard);
            ThrottleClickEvents.throttleClick(sHolder.mParent, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mListener!=null){
                        mListener.detail(item,position);
                    }
                }
            });
        }
        if (holder instanceof SheetEditHolder) {
            SheetEditHolder sHolder = (SheetEditHolder) holder;
            sHolder.mTimeView.setText("进场日期:"+DateUtil.getShowDate(item.approachDate));
            sHolder.mIndexView.setText("批号："+item.batchCode);
            sHolder.mCodeView.setText("编码："+item.facilityCode);
            sHolder.mNameView.setText("名称："+item.facilityName);
            ThrottleClickEvents.throttleClick(sHolder.mParent, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mListener!=null){
                        mListener.toEdit(item,position);
                    }
                }
            });
            sHolder.mDeleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mListener!=null)
                    {
                        mListener.delete(item,position);
                    }
                }
            });

            sHolder.mSubmitBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mListener!=null)
                    {
                        mListener.submit(item,position);
                    }
                }
            });

        }

        if (holder instanceof TimeHolder) {
            TimeHolder tHolder = (TimeHolder) holder;
            if (position == 0) {
                tHolder.mTimeParent.setPadding(ScreenUtil.dp2px(20), ScreenUtil.dp2px(20), ScreenUtil.dp2px(20), 0);
            } else {
                tHolder.mTimeParent.setPadding(ScreenUtil.dp2px(20), ScreenUtil.dp2px(10), ScreenUtil.dp2px(20), 0);
            }
            tHolder.mTimeView.setText(DateUtil.getListDate(Long.parseLong(item.updateTime)));
        }
    }

    @Override
    public int getItemViewType(int position) {
        return mDataList.get(position).showType;
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    //编辑
    class SheetEditHolder extends RecyclerView.ViewHolder {
        TextView mIndexView,mCodeView,mNameView,mTimeView;
        TextView mSubmitBtn, mDeleteBtn;
        View mParent;
        SheetEditHolder(View itemView) {
            super(itemView);
            mTimeView = itemView.findViewById(R.id.equipment_list_edit_item_sheet_time);
            mIndexView = itemView.findViewById(R.id.equipment_list_edit_index);
            mCodeView = itemView.findViewById(R.id.equipment_list_edit_code);
            mNameView = itemView.findViewById(R.id.equipment_list_edit_name);
            mSubmitBtn = itemView.findViewById(R.id.equipment_list_edit_submit);
            mDeleteBtn = itemView.findViewById(R.id.equipment_list_edit_delete);
            mParent = itemView;
        }
    }

    //合格不合格
    class SheetHolder extends RecyclerView.ViewHolder {
        TextView mIndexView,mCodeView,mNameView,mTimeView;
        ImageView mStandardView;
        View mParent;

        SheetHolder(View itemView) {
            super(itemView);
            mTimeView = itemView.findViewById(R.id.equipment_list_item_sheet_time);
            mIndexView = itemView.findViewById(R.id.equipment_list_item_index);
            mCodeView = itemView.findViewById(R.id.equipment_list_item_code);
            mNameView = itemView.findViewById(R.id.equipment_list_item_name);
            mStandardView = itemView.findViewById(R.id.equipment_list_item_standard);
            mParent = itemView;
        }
    }

    //时间
    class TimeHolder extends RecyclerView.ViewHolder {
        TextView mTimeView;
        RelativeLayout mTimeParent;

        public TimeHolder(View itemView) {
            super(itemView);
            mTimeView = itemView.findViewById(R.id.equipment_list_item_time);
            mTimeParent = (RelativeLayout) itemView;
        }
    }
}
