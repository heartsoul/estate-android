package com.glodon.bim.business.main.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.glodon.bim.R;
import com.glodon.bim.basic.listener.ThrottleClickEvents;
import com.glodon.bim.business.qualityManage.listener.OnSearchHistoryClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cwj on 2018/3/6.
 * Description:质检清单、材设清单搜索历史
 */

public class QualityEquipmentSearchHistoryAdapter extends RecyclerView.Adapter {
    private Activity mActivity;
    private List<String> mDataList;
    private OnSearchHistoryClickListener mListener;

    public QualityEquipmentSearchHistoryAdapter(Activity mActivity, OnSearchHistoryClickListener mListener) {
        this.mActivity = mActivity;
        this.mListener = mListener;
        mDataList = new ArrayList<>();
    }

    public void updateList(List<String> mDataList) {
        this.mDataList = mDataList;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new HistoryHolder(LayoutInflater.from(mActivity).inflate(R.layout.quality_equipment_search_history_view, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        HistoryHolder hHolder = (HistoryHolder) holder;
        final String name = mDataList.get(position);
        hHolder.mNameView.setText(name);
        ThrottleClickEvents.throttleClick(hHolder.mNameView, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.click(name);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return (mDataList == null) ? 0 : mDataList.size();
    }

    class HistoryHolder extends RecyclerView.ViewHolder {

        TextView mNameView;

        public HistoryHolder(View itemView) {
            super(itemView);
            mNameView = itemView.findViewById(R.id.quality_equipment_search_history_name);
        }
    }

}
