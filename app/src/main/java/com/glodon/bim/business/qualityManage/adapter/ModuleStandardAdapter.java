package com.glodon.bim.business.qualityManage.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.glodon.bim.R;
import com.glodon.bim.business.qualityManage.bean.ModuleStandardItem;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：质检标准
 * 作者：zhourf on 2017/10/26
 * 邮箱：zhourf@glodon.com
 */

public class ModuleStandardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Activity mActivity;
    private List<ModuleStandardItem> mDataList;

    public ModuleStandardAdapter(Activity mActivity) {
        this.mActivity = mActivity;
        mDataList = new ArrayList<>();
    }

    public void updateList(List<ModuleStandardItem> dataList){
        mDataList=dataList;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ListHolder(LayoutInflater.from(mActivity).inflate(R.layout.quality_module_standard_item_view,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final ListHolder lHolder = (ListHolder) holder;
        ModuleStandardItem item = mDataList.get(position);

        lHolder.mArrowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(lHolder.mTextView.getVisibility() == View.VISIBLE){
                    lHolder.mTextView.setVisibility(View.GONE);
                    lHolder.mArrowView.setBackgroundResource(R.drawable.icon_drawer_arrow_down);
                    lHolder.mlineView.setVisibility(View.GONE);
                }else{
                    lHolder.mTextView.setVisibility(View.VISIBLE);
                    lHolder.mArrowView.setBackgroundResource(R.drawable.icon_draw_arrow_up);
                    lHolder.mlineView.setVisibility(View.VISIBLE);
                }
            }
        });
        lHolder.mNameView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(lHolder.mTextView.getVisibility() == View.VISIBLE){
                    lHolder.mTextView.setVisibility(View.GONE);
                    lHolder.mArrowView.setBackgroundResource(R.drawable.icon_drawer_arrow_down);
                    lHolder.mlineView.setVisibility(View.GONE);
                }else{
                    lHolder.mTextView.setVisibility(View.VISIBLE);
                    lHolder.mArrowView.setBackgroundResource(R.drawable.icon_draw_arrow_up);
                    lHolder.mlineView.setVisibility(View.VISIBLE);
                }
            }
        });
        lHolder.mNameView.setText(item.name);
        lHolder.mTextView.setText(Html.fromHtml(item.content));

    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    class ListHolder extends RecyclerView.ViewHolder{

        TextView mNameView,mTextView;
        ImageView mArrowView;
        View mlineView;
        public ListHolder(View itemView) {
            super(itemView);
            mNameView = itemView.findViewById(R.id.module_standard_item_name);
            mTextView = itemView.findViewById(R.id.module_standard_item_text);
            mArrowView = itemView.findViewById(R.id.module_standard_item_arrow);
            mlineView = itemView.findViewById(R.id.module_standard_item_bottom_line);
        }
    }
}
