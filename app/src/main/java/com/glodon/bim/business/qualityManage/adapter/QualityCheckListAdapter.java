package com.glodon.bim.business.qualityManage.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.glodon.bim.R;
import com.glodon.bim.basic.image.ImageLoader;
import com.glodon.bim.basic.utils.DateUtil;
import com.glodon.bim.basic.utils.ScreenUtil;
import com.glodon.bim.business.qualityManage.bean.QualityCheckListBeanItem;
import com.glodon.bim.business.qualityManage.listener.OnOperateSheetListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：质检清单列表adapter
 * 作者：zhourf on 2017/9/30
 * 邮箱：zhourf@glodon.com
 */

public class QualityCheckListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<QualityCheckListBeanItem> mDataList;
    private OnOperateSheetListener mListener;

    public QualityCheckListAdapter(Context mContext, OnOperateSheetListener listener) {
        this.mContext = mContext;
        mDataList = new ArrayList<>();
        this.mListener = listener;
    }

    public void updateList(List<QualityCheckListBeanItem> dataList) {
        mDataList.clear();
        mDataList.addAll(dataList);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0:
                return new TimeHolder(LayoutInflater.from(mContext).inflate(R.layout.quality_check_list_item_time_view, null));
            case 1:
                return new SheetHolder(LayoutInflater.from(mContext).inflate(R.layout.quality_check_list_item_sheet_view, null));
            default:
                return null;
        }


    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        QualityCheckListBeanItem item = mDataList.get(position);
        if (holder instanceof SheetHolder) {
            SheetHolder sHolder = (SheetHolder) holder;
            //0待提交 1待整改 2待复查 3 已整改 4已复查 5已延迟 6已验收
            sHolder.mTimeView.setText(DateUtil.getListTime(Long.parseLong(item.updateTime)));

            sHolder.mBottomPreant.setVisibility(View.GONE);
            int color = R.color.c_f39b3d;
            String statusText = "";
            switch (item.sheetStatus){
                case 0:
                    color = R.color.c_f39b3d;
                    statusText = "待提交";
                    sHolder.mBottomPreant.setVisibility(View.VISIBLE);
                    sHolder.mSubmitBtn.setVisibility(View.VISIBLE);
                    sHolder.mDeleteBtn.setVisibility(View.VISIBLE);
                    sHolder.mRepairBtn.setVisibility(View.GONE);
                    sHolder.mReviewBtn.setVisibility(View.GONE);
                    break;
                case 1:
                    color = R.color.c_f33d3d;
                    statusText = "待整改";
                    sHolder.mBottomPreant.setVisibility(View.VISIBLE);
                    sHolder.mSubmitBtn.setVisibility(View.GONE);
                    sHolder.mDeleteBtn.setVisibility(View.GONE);
                    sHolder.mRepairBtn.setVisibility(View.VISIBLE);
                    sHolder.mReviewBtn.setVisibility(View.GONE);
                    break;
                case 2:
                    color = R.color.c_f33d3d;
                    statusText = "待复查";
                    sHolder.mBottomPreant.setVisibility(View.VISIBLE);
                    sHolder.mSubmitBtn.setVisibility(View.GONE);
                    sHolder.mDeleteBtn.setVisibility(View.GONE);
                    sHolder.mRepairBtn.setVisibility(View.GONE);
                    sHolder.mReviewBtn.setVisibility(View.VISIBLE);
                    break;
                case 3:
                    color = R.color.c_28d575;
                    statusText = "已整改";
                    break;
                case 4:
                    color = R.color.c_28d575;
                    statusText = "已复查";
                    break;
                case 5:
                    color = R.color.c_28d575;
                    statusText = "已延迟";
                    break;
                case 6:
                    color = R.color.c_28d575;
                    statusText = "已验收";
                    break;
            }
            sHolder.mStatusView.setText(statusText);
            sHolder.mStatusView.setTextColor(mContext.getResources().getColor(color));

            if(item.files!=null && item.files.size()>0){
                ImageLoader.showImageCenterCrop(mContext,item.files.get(0).url,sHolder.mImageView,R.drawable.icon_default_image);
            }else{
                sHolder.mImageView.setBackgroundResource(R.drawable.icon_default_image);
            }
            sHolder.mDesView.setText(item.description);

            sHolder.mContentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mListener!=null){
                        mListener.detail(position);
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
            if (item.timeType == 0) {
                tHolder.mTodayView.setVisibility(View.VISIBLE);
                tHolder.mBeforeView.setVisibility(View.GONE);
                tHolder.mTodayView.setText("今天");
            } else {
                tHolder.mTodayView.setVisibility(View.GONE);
                tHolder.mBeforeView.setVisibility(View.VISIBLE);
                tHolder.mBeforeTextView.setText(DateUtil.getListDate(Long.parseLong(item.updateTime)));
            }
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

    class SheetHolder extends RecyclerView.ViewHolder {
        TextView mTimeView, mStatusView, mDesView;
        TextView mSubmitBtn, mDeleteBtn, mRepairBtn, mReviewBtn;
        LinearLayout mContentView;
        RelativeLayout mBottomPreant;
        ImageView mImageView;

        SheetHolder(View itemView) {
            super(itemView);
            mTimeView = itemView.findViewById(R.id.quality_check_list_item_sheet_time);
            mStatusView = itemView.findViewById(R.id.quality_check_list_item_sheet_status);
            mImageView = itemView.findViewById(R.id.quality_check_list_item_sheet_image);
            mDesView = itemView.findViewById(R.id.quality_check_list_item_sheet_text);
            mSubmitBtn = itemView.findViewById(R.id.quality_check_list_item_sheet_submit);
            mDeleteBtn = itemView.findViewById(R.id.quality_check_list_item_sheet_delete);
            mRepairBtn = itemView.findViewById(R.id.quality_check_list_item_sheet_repair);
            mReviewBtn = itemView.findViewById(R.id.quality_check_list_item_sheet_review);
            mContentView = itemView.findViewById(R.id.quality_check_list_item_sheet_content);
            mBottomPreant = itemView.findViewById(R.id.quality_check_list_item_sheet_bottom_parent);
        }
    }

    class TimeHolder extends RecyclerView.ViewHolder {
        TextView mTodayView;
        LinearLayout mBeforeView;
        TextView mBeforeTextView;
        RelativeLayout mTimeParent;

        public TimeHolder(View itemView) {
            super(itemView);
            mTodayView = itemView.findViewById(R.id.quality_check_list_item_time_today);
            mBeforeView = itemView.findViewById(R.id.quality_check_list_item_time_before);
            mBeforeTextView = itemView.findViewById(R.id.quality_check_list_item_time_before_text);
            mTimeParent = itemView.findViewById(R.id.quality_check_list_item_time_parent);
        }
    }
}
