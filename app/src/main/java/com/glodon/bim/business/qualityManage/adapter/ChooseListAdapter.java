package com.glodon.bim.business.qualityManage.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.glodon.bim.R;
import com.glodon.bim.business.qualityManage.listener.OnChooseListListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：选择联系人和施工单位
 * 作者：zhourf on 2017/10/26
 * 邮箱：zhourf@glodon.com
 */

public class ChooseListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Activity mActivity;
    private List<String> mDataList;
    private ImageView mLastView;
    private TextView mLastTextView;
    private OnChooseListListener mListener;
    private int mSelectPosition = 0;

    public ChooseListAdapter(Activity mActivity, List<String> dataList, int selectPosition) {
        this.mActivity = mActivity;
        if(dataList==null){
            dataList = new ArrayList<>();
        }
        this.mDataList = dataList;
        this.mSelectPosition = selectPosition;
    }

    public void setmListener(OnChooseListListener mListener) {
        this.mListener = mListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ListHolder(LayoutInflater.from(mActivity).inflate(R.layout.quality_create_check_list_choose_list,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final ListHolder lHolder = (ListHolder) holder;
        lHolder.mNameView.setText(mDataList.get(position));
        lHolder.mNameView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mLastView!=null){
                    mLastView.setVisibility(View.INVISIBLE);
                    mLastTextView.setTextColor(mActivity.getResources().getColor(R.color.c_333333));
                }
                lHolder.mSelectView.setVisibility(View.VISIBLE);
                lHolder.mNameView.setTextColor(mActivity.getResources().getColor(R.color.c_00baf3));
                mLastView = lHolder.mSelectView;
                mLastTextView = lHolder.mNameView;
                mSelectPosition = position;
                if(mListener!=null)
                {
                    mListener.onSelect(position);
                }
            }
        });

        if(mSelectPosition == position){
            lHolder.mSelectView.setVisibility(View.VISIBLE);
            lHolder.mNameView.setTextColor(mActivity.getResources().getColor(R.color.c_00baf3));
            mLastView = lHolder.mSelectView;
            mLastTextView = lHolder.mNameView;
        }else{
            lHolder.mSelectView.setVisibility(View.INVISIBLE);
            lHolder.mNameView.setTextColor(mActivity.getResources().getColor(R.color.c_333333));
        }
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    class ListHolder extends RecyclerView.ViewHolder{

        TextView mNameView;
        ImageView mSelectView;
        public ListHolder(View itemView) {
            super(itemView);
            mNameView = itemView.findViewById(R.id.quality_create_check_list_choose_list_name);
            mSelectView = itemView.findViewById(R.id.quality_create_check_list_choose_list_select);
        }
    }
}
