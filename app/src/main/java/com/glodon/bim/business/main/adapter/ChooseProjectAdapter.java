package com.glodon.bim.business.main.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.glodon.bim.R;
import com.glodon.bim.business.main.bean.ProjectItem;
import com.glodon.bim.business.main.view.ChooseCategoryItemActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：选择项目列表
 * 作者：zhourf on 2017/10/16
 * 邮箱：zhourf@glodon.com
 */

public class ChooseProjectAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ProjectItem> mList;
    private Activity mContext;
    private int mSize=0;
    private View mLastView;


    public ChooseProjectAdapter(Activity mContext, int size) {
        this.mContext = mContext;
        mList = new ArrayList<>();
        this.mSize = size;
    }

    public void updateData(List<ProjectItem> list){
        mList.clear();
        mList.addAll(list);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder ;
        if(mSize>4){
            holder = new ProjectMoreHolder(LayoutInflater.from(mContext).inflate(R.layout.main_choose_project_more_item,null));
        }else{
            holder = new ProjectLessHolder(LayoutInflater.from(mContext).inflate(R.layout.main_choose_project_less_item,null));
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ProjectItem item = mList.get(position);
        if(mSize>4) {
            final ProjectMoreHolder pHolder = (ProjectMoreHolder) holder;
            pHolder.mNameView.setText(item.name);
            pHolder.mItemParent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext,ChooseCategoryItemActivity.class);
                    mContext.startActivity(intent);
                }
            });
        }else{
            final ProjectLessHolder pHolder = (ProjectLessHolder) holder;
            pHolder.mNameView.setText(item.name);
            pHolder.mItemParent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext,ChooseCategoryItemActivity.class);
                    mContext.startActivity(intent);

                    if(mLastView!=null){
                        mLastView.setBackground(null);
                    }
                    pHolder.mItemParent.setBackgroundResource(R.drawable.corner_radius_4_choose_project_bg);
                    mLastView = pHolder.mItemParent;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mList==null?0:mList.size();
    }

    class ProjectMoreHolder extends RecyclerView.ViewHolder{

        public TextView mNameView;
        private RelativeLayout mItemParent;
        public ProjectMoreHolder(View itemView) {
            super(itemView);
            mNameView = itemView.findViewById(R.id.choose_project_item_text);
            mItemParent = itemView.findViewById(R.id.choose_project_more_item_parent);
        }
    }

    class ProjectLessHolder extends RecyclerView.ViewHolder{
        public TextView mNameView;
        public LinearLayout mItemParent;
        public ProjectLessHolder(View itemView) {
            super(itemView);
            mNameView = itemView.findViewById(R.id.choose_project_less_item_name);
            mItemParent = itemView.findViewById(R.id.choose_project_less_item_parent);
        }
    }
}
