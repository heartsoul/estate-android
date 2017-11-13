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
import com.glodon.bim.business.authority.AuthorityManager;
import com.glodon.bim.business.qualityManage.bean.QualityCheckListBeanItem;
import com.glodon.bim.business.qualityManage.listener.OnOperateSheetListener;
import com.glodon.bim.common.config.CommonConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：质检清单列表adapter
 * 作者：zhourf on 2017/9/30
 * 邮箱：zhourf@glodon.com
 */

public class QualityCheckListAdapter2 extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<QualityCheckListBeanItem> mDataList;
    private OnOperateSheetListener mListener;

    public QualityCheckListAdapter2(Context mContext, OnOperateSheetListener listener) {
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
        return new SheetHolder(LayoutInflater.from(mContext).inflate(R.layout.quality_check_list_item_sheet_view, null));

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

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
        View mBottomLineView;

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
            mBottomLineView = itemView.findViewById(R.id.quality_check_list_item_sheet_bottom_line);
        }
    }


}
