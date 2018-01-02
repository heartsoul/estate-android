package com.glodon.bim.business.qualityManage.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.glodon.bim.R;
import com.glodon.bim.business.qualityManage.bean.ModuleListBeanItem;
import com.glodon.bim.business.qualityManage.listener.OnModuleHintClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：质检项目  目录切换
 * 作者：zhourf on 2017/10/26
 * 邮箱：zhourf@glodon.com
 */

public class ModuleHintAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Activity mActivity;
    private List<ModuleListBeanItem> mDataList;
    private ImageView mLastView;
    private TextView mLastTextView;
    private OnModuleHintClickListener mListener;
    private long mSelectId = 0;

    public ModuleHintAdapter(Activity mActivity) {
        this.mActivity = mActivity;
        mDataList = new ArrayList<>();
    }

    public void updateList(List<ModuleListBeanItem> dataList, long selectId) {
        mDataList = dataList;
        this.mSelectId = selectId;
        notifyDataSetChanged();
    }

    public void setmListener(OnModuleHintClickListener mListener) {
        this.mListener = mListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ListHolder(LayoutInflater.from(mActivity).inflate(R.layout.quality_module_hint_item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final ListHolder lHolder = (ListHolder) holder;
        final ModuleListBeanItem item = mDataList.get(position);
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
                mSelectId = position;
                if (mListener != null) {
                    mListener.onSelect(item);
                }
            }
        });

        if (item != null && mSelectId == item.id.longValue()) {
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
