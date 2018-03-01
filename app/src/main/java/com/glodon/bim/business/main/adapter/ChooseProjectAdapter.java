package com.glodon.bim.business.main.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.glodon.bim.R;
import com.glodon.bim.business.main.bean.ProjectListItem;
import com.glodon.bim.business.main.listener.OnProjectClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：选择项目列表
 * 作者：zhourf on 2017/10/16
 * 邮箱：zhourf@glodon.com
 */

public class ChooseProjectAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ProjectListItem> mList;
    private Activity mContext;
    private int mSize=0;
    private View mLastView;
    private OnProjectClickListener mListener;


    public ChooseProjectAdapter(Activity mContext, int size) {
        this.mContext = mContext;
        mList = new ArrayList<>();
        this.mSize = size;
    }

    public void updateData(List<ProjectListItem> list){
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public void setListener(OnProjectClickListener mListener) {
        this.mListener = mListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder ;
        if(mSize>4){
            holder = new ProjectMoreHolder(LayoutInflater.from(mContext).inflate(R.layout.main_choose_project_more_item,parent,false));
        }else{
            holder = new ProjectLessHolder(LayoutInflater.from(mContext).inflate(R.layout.main_choose_project_less_item,parent,false));
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ProjectListItem item = mList.get(position);
        if(mSize>4) {
            final ProjectMoreHolder pHolder = (ProjectMoreHolder) holder;
            pHolder.mNameView.setText(item.name);
            pHolder.mItemParent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   if(mListener!=null){
                       mListener.clickTenant(item);
                   }
                }
            });
        }else{
            final ProjectLessHolder pHolder = (ProjectLessHolder) holder;
            pHolder.mNameView.setText(item.name);
            //如果只有一个项目  直接选中进入
            if(getItemCount()==1){
                pHolder.mItemParent.setBackgroundResource(R.drawable.icon_choose_project_bg_selected);
            }
            pHolder.mItemParent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mListener!=null){
                        mListener.clickTenant(item);
                    }

                    if(mLastView!=null){
                        mLastView.setBackgroundResource(R.drawable.icon_choose_project_bg_normal);
                    }
                    pHolder.mItemParent.setBackgroundResource(R.drawable.icon_choose_project_bg_selected);
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
