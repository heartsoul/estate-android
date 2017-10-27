package com.glodon.bim.business.main.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.glodon.bim.R;
import com.glodon.bim.business.main.listener.OnTenantClickListener;
import com.glodon.bim.business.main.view.ChooseProjectActivity;
import com.glodon.bim.common.login.UserTenant;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：选择项目列表
 * 作者：zhourf on 2017/10/16
 * 邮箱：zhourf@glodon.com
 */

public class ChooseTenantAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<UserTenant> mList;
    private Activity mContext;
    private View mLastView;
    private OnTenantClickListener mListener;

    public ChooseTenantAdapter(Activity mContext,OnTenantClickListener listener) {
        this.mContext = mContext;
        this.mListener = listener;
        mList = new ArrayList<>();
    }

    public void updateData(List<UserTenant> list){
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ProjectLessHolder(LayoutInflater.from(mContext).inflate(R.layout.main_choose_project_less_item,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final UserTenant item = mList.get(position);
        final ProjectLessHolder pHolder = (ProjectLessHolder) holder;
        pHolder.mNameView.setText(item.tenantName);
        pHolder.mIconView.setBackgroundResource(R.drawable.icon_choose_tenant_item);
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

    @Override
    public int getItemCount() {
        return mList==null?0:mList.size();
    }


    class ProjectLessHolder extends RecyclerView.ViewHolder{
        public TextView mNameView;
        public LinearLayout mItemParent;
        public ImageView mIconView;
        public ProjectLessHolder(View itemView) {
            super(itemView);
            mNameView = itemView.findViewById(R.id.choose_project_less_item_name);
            mItemParent = itemView.findViewById(R.id.choose_project_less_item_parent);
            mIconView = itemView.findViewById(R.id.choose_project_less_item_icon);
        }
    }
}
