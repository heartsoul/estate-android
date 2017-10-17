package com.glodon.bim.business.main.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.glodon.bim.R;
import com.glodon.bim.business.main.bean.CategoryItem;
import com.glodon.bim.business.main.bean.ProjectItem;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：选择项目列表
 * 作者：zhourf on 2017/10/16
 * 邮箱：zhourf@glodon.com
 */

public class CategoryItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<CategoryItem> mList;
    private Activity mContext;


    public CategoryItemAdapter(Activity mContext) {
        this.mContext = mContext;
        mList = new ArrayList<>();
    }

    public void updateData(List<CategoryItem> list){
        mList.clear();
        mList.addAll(list);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CategoryItemHolder(LayoutInflater.from(mContext).inflate(R.layout.main_choose_category_item_item_view,null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CategoryItem item = mList.get(position);
        CategoryItemHolder cHolder = (CategoryItemHolder) holder;
        cHolder.mNameView.setText(item.name);
        cHolder.mResourceView.setBackgroundResource(item.resource);
        cHolder.mParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mList==null?0:mList.size();
    }

    class CategoryItemHolder extends RecyclerView.ViewHolder{

        private TextView mNameView;
        private ImageView mResourceView;
        private LinearLayout mParent;
        public CategoryItemHolder(View itemView) {
            super(itemView);
            mNameView = itemView.findViewById(R.id.choose_category_item_item_text);
            mResourceView = itemView.findViewById(R.id.choose_category_item_item_icon);
            mParent = itemView.findViewById(R.id.choose_category_item_item_parent);
        }
    }

}
