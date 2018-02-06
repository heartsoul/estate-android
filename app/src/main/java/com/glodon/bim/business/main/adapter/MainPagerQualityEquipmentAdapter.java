package com.glodon.bim.business.main.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.glodon.bim.R;
import com.glodon.bim.basic.listener.ThrottleClickEvents;
import com.glodon.bim.business.main.bean.MainPagerQualityEquipmentItem;
import com.glodon.bim.business.main.listener.OnPagerItemClickListener;

import java.util.List;

/**
 * 描述：
 * 作者：zhourf on 2018/2/6
 * 邮箱：zhourf@glodon.com
 */

public class MainPagerQualityEquipmentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<MainPagerQualityEquipmentItem> mDatalist;
    private OnPagerItemClickListener mListener;
    private Activity mActivity;

    public MainPagerQualityEquipmentAdapter(Activity mActivity, List<MainPagerQualityEquipmentItem> mDatalist, OnPagerItemClickListener mListener) {
        this.mActivity = mActivity;
        this.mDatalist = mDatalist;
        this.mListener = mListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemHolder(LayoutInflater.from(mActivity).inflate(R.layout.quality_main_fragment_main_page_quality_item,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewholder, final int position) {
        ItemHolder holder = (ItemHolder) viewholder;
        holder.mIconView.setBackgroundResource(mDatalist.get(position).resourceId);
        holder.mNameView.setText(mDatalist.get(position).name);
        ThrottleClickEvents.throttleClick(holder.mParent, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mListener!=null)
                {
                    mListener.onClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDatalist.size();
    }

    class ItemHolder extends RecyclerView.ViewHolder{

        ImageView mIconView;
        TextView mNameView;
        View mParent;
        public ItemHolder(View itemView) {
            super(itemView);
            mParent = itemView;
            mIconView = itemView.findViewById(R.id.main_pager_quality_item_icon);
            mNameView = itemView.findViewById(R.id.main_pager_quality_item_name);
        }
    }
}
