package com.glodon.bim.business.main.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.glodon.bim.R;
import com.glodon.bim.basic.utils.ScreenUtil;
import com.glodon.bim.business.main.bean.ChooseCategoryItem;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：选择目录的具体事项界面
 * 作者：zhourf on 2018/1/17
 * 邮箱：zhourf@glodon.com
 */

public class ChooseCategoryItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Activity mActivity;
    private List<ChooseCategoryItem> mDataList;
    private int width = (ScreenUtil.getScreenInfo()[0] - ScreenUtil.dp2px(50)) / 3;

    public ChooseCategoryItemAdapter(Activity mActivity) {
        this.mActivity = mActivity;
        mDataList = new ArrayList<>();
    }

    public void updateList(List<ChooseCategoryItem> list){
        this.mDataList = list;
        if(mDataList==null){
            mDataList = new ArrayList<>();
        }
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(mActivity).inflate(R.layout.main_choose_category_item_view,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder mHolder, int position) {
        Holder holder = (Holder) mHolder;
        ChooseCategoryItem item = mDataList.get(position);
        holder.mIconView.setBackgroundResource(item.resource);
        holder.mNameView.setText(item.name);
        holder.mParent.getLayoutParams().width = width;
        holder.mParent.getLayoutParams().height = width;
        holder.mParent.setOnClickListener(item.listener);
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    class Holder extends RecyclerView.ViewHolder{
        LinearLayout mParent;
        ImageView mIconView;
        TextView mNameView;
        public Holder(View itemView) {
            super(itemView);
            mParent = itemView.findViewById(R.id.category_item_view_parent);
            mIconView = itemView.findViewById(R.id.category_item_view_icon);
            mNameView = itemView.findViewById(R.id.category_item_view_name);
        }
    }
}
