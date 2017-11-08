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
import com.glodon.bim.business.qualityManage.listener.OnChooseModuleListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：选择质检项目
 * 作者：zhourf on 2017/10/26
 * 邮箱：zhourf@glodon.com
 */

public class ChooseModuleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Activity mActivity;
    private List<ModuleListBeanItem> mDataList;
    private ImageView mLastView;
    private TextView mLastTextView;
    private OnChooseModuleListener mListener;
    private long mSelectId = 0;

    public ChooseModuleAdapter(Activity mActivity, List<ModuleListBeanItem> dataList, long selectId) {
        this.mActivity = mActivity;
        if(dataList==null){
            dataList = new ArrayList<>();
        }
        this.mDataList = dataList;
        this.mSelectId = selectId;
    }

    public void updateList(List<ModuleListBeanItem> dataList){
        mDataList=dataList;
        notifyDataSetChanged();
    }

    public void setmListener(OnChooseModuleListener mListener) {
        this.mListener = mListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ListHolder(LayoutInflater.from(mActivity).inflate(R.layout.quality_create_check_list_choose_list,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final ListHolder lHolder = (ListHolder) holder;
        lHolder.mNameView.setText(mDataList.get(position).name);
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
                mSelectId = position;
                if(mListener!=null)
                {
                    mListener.onSelect(mDataList.get(position),mSelectId);
                }
            }
        });

        if(mSelectId == mDataList.get(position).id){
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
