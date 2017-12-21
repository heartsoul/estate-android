package com.glodon.bim.business.qualityManage.adapter;

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
 * 描述：模型图纸搜索历史
 * 作者：zhourf on 2017/10/26
 * 邮箱：zhourf@glodon.com
 */

public class BluePrintModelSearchHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Activity mActivity;
    private List<String> mDataList;
    private OnSearchHistoryClickListener mListener;

    public BluePrintModelSearchHistoryAdapter(Activity mActivity,OnSearchHistoryClickListener listener) {
        this.mActivity = mActivity;
        mListener = listener;
        mDataList = new ArrayList<>();
    }


    public void updateList(List<String> dataList){
        mDataList=dataList;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new HistoryHolder(LayoutInflater.from(mActivity).inflate(R.layout.quality_blueprint_model_search_history_view,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        HistoryHolder hHolder = (HistoryHolder) holder;
        final String name = mDataList.get(position);
        hHolder.mNameView.setText(name);
        ThrottleClickEvents.throttleClick(hHolder.mNameView, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mListener!=null){
                    mListener.click(name);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }


    class HistoryHolder extends RecyclerView.ViewHolder{

        TextView mNameView;
        public HistoryHolder(View itemView) {
            super(itemView);
            mNameView = itemView.findViewById(R.id.quality_blueprint_model_search_history_name);
        }
    }

}
