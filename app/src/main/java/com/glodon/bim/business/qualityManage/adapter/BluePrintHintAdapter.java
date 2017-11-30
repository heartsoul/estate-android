package com.glodon.bim.business.qualityManage.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.glodon.bim.R;
import com.glodon.bim.business.qualityManage.bean.BlueprintListBeanItem;
import com.glodon.bim.business.qualityManage.listener.OnBlueprintHintClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：图纸 目录切换
 * 作者：zhourf on 2017/10/26
 * 邮箱：zhourf@glodon.com
 */

public class BluePrintHintAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Activity mActivity;
    private List<BlueprintListBeanItem> mDataList;
    private ImageView mLastView;
    private TextView mLastTextView;
    private OnBlueprintHintClickListener mListener;
    private String mSelectId ;

    public BluePrintHintAdapter(Activity mActivity) {
        this.mActivity = mActivity;
        mDataList = new ArrayList<>();
    }

    public void updateList(List<BlueprintListBeanItem> dataList, String selectId) {
        mDataList = dataList;
        this.mSelectId = selectId;
        notifyDataSetChanged();
    }

    public void setmListener(OnBlueprintHintClickListener mListener) {
        this.mListener = mListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ListHolder(LayoutInflater.from(mActivity).inflate(R.layout.quality_module_hint_item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final ListHolder lHolder = (ListHolder) holder;
        final BlueprintListBeanItem item = mDataList.get(position);
        lHolder.mNameView.setText(item.name);
        lHolder.mParentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mLastView != null) {
                    mLastView.setVisibility(View.INVISIBLE);
                    mLastTextView.setTextColor(mActivity.getResources().getColor(R.color.c_333333));
                }
                lHolder.mSelectView.setVisibility(View.VISIBLE);
                lHolder.mNameView.setTextColor(mActivity.getResources().getColor(R.color.c_00baf3));
                mLastView = lHolder.mSelectView;
                mLastTextView = lHolder.mNameView;
                mSelectId = item.fileId;
                if (mListener != null) {
                    mListener.onSelect(item);
                }
            }
        });

        if (item != null && mSelectId .equals(item.fileId)) {
            lHolder.mSelectView.setVisibility(View.VISIBLE);
            lHolder.mNameView.setTextColor(mActivity.getResources().getColor(R.color.c_00baf3));
            mLastView = lHolder.mSelectView;
            mLastTextView = lHolder.mNameView;
        } else {
            lHolder.mSelectView.setVisibility(View.INVISIBLE);
            lHolder.mNameView.setTextColor(mActivity.getResources().getColor(R.color.c_333333));
        }
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    class ListHolder extends RecyclerView.ViewHolder {

        TextView mNameView;
        ImageView mSelectView;
        View mParentView;
        public ListHolder(View itemView) {
            super(itemView);
            mParentView = itemView;
            mNameView = itemView.findViewById(R.id.quality_module_hint_item_name);
            mSelectView = itemView.findViewById(R.id.quality_module_hint_item_select);
        }
    }
}
