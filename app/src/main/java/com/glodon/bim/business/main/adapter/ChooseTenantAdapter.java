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


    public ChooseTenantAdapter(Activity mContext) {
        this.mContext = mContext;
        mList = new ArrayList<>();
    }

    public void updateData(List<UserTenant> list){
        mList.clear();
        mList.addAll(list);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ProjectLessHolder(LayoutInflater.from(mContext).inflate(R.layout.main_choose_project_less_item,null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        UserTenant item = mList.get(position);
        final ProjectLessHolder pHolder = (ProjectLessHolder) holder;
        pHolder.mNameView.setText(item.tenantName);
        pHolder.mIconView.setBackgroundResource(R.drawable.icon_choose_tenant_item);
        pHolder.mItemParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext,ChooseProjectActivity.class);
                mContext.startActivity(intent);

                if(mLastView!=null){
                    mLastView.setBackground(null);
                }
                pHolder.mItemParent.setBackgroundResource(R.drawable.corner_radius_4_choose_project_bg);
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
