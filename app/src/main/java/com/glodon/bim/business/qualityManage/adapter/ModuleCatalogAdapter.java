package com.glodon.bim.business.qualityManage.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.glodon.bim.R;
import com.glodon.bim.basic.log.LogUtil;
import com.glodon.bim.business.qualityManage.bean.ModuleListBeanItem;
import com.glodon.bim.business.qualityManage.listener.OnModuleCatalogClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：质检项目  顶部目录
 * 作者：zhourf on 2017/10/26
 * 邮箱：zhourf@glodon.com
 */

public class ModuleCatalogAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Activity mActivity;
    private List<ModuleListBeanItem> mDataList;
//    private ImageView mLastView;
//    private TextView mLastTextView;
    private OnModuleCatalogClickListener mListener;
    private int mLastPosition = -1;

    public ModuleCatalogAdapter(Activity mActivity) {
        this.mActivity = mActivity;
        mDataList = new ArrayList<>();
    }

    public void updateList(List<ModuleListBeanItem> dataList){
        mDataList=dataList;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ListHolder(LayoutInflater.from(mActivity).inflate(R.layout.quality_module_catalog_item_view,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final ListHolder lHolder = (ListHolder) holder;
        final ModuleListBeanItem item = mDataList.get(position);
        lHolder.mNameView.setText(item.inspectItem);
        LogUtil.e("position=--"+position);
        if(position == getItemCount()-1){
            lHolder.mIconUpdown.setVisibility(View.VISIBLE);
            lHolder.mIconNormal.setVisibility(View.GONE);
            lHolder.mNameView.setTextColor(mActivity.getResources().getColor(R.color.c_00b5f2));
        }else{
            lHolder.mIconUpdown.setVisibility(View.GONE);
            lHolder.mIconNormal.setVisibility(View.VISIBLE);
            lHolder.mNameView.setTextColor(mActivity.getResources().getColor(R.color.c_b5b5b5));
        }
        lHolder.mParentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if(mLastView!=null){
//                    mLastView.setVisibility(View.INVISIBLE);
//                    mLastTextView.setTextColor(mActivity.getResources().getColor(R.color.c_333333));
//                }
//                lHolder.mIconUpdown.setVisibility(View.VISIBLE);
//                lHolder.mNameView.setTextColor(mActivity.getResources().getColor(R.color.c_00baf3));
//                mLastView = lHolder.mIconUpdown;
//                mLastTextView = lHolder.mNameView;

                if(mListener!=null)
                {
                    if(mLastPosition == position){
                        //点的同一个
                        mListener.onSelect(item,false);
                        mLastPosition = -1;
                        lHolder.mIconUpdown.setBackgroundResource(R.drawable.icon_blue_arrow_down);
                    }else{
                        mLastPosition = position;
                        mListener.onSelect(item,true);
                        lHolder.mIconUpdown.setBackgroundResource(R.drawable.icon_blue_arrow_up);

                    }

                }
            }
        });

//        if(mDataList.get(position)!=null && mSelectId == mDataList.get(position).fileId){
//            lHolder.mIconUpdown.setVisibility(View.VISIBLE);
//            lHolder.mNameView.setTextColor(mActivity.getResources().getColor(R.color.c_00baf3));
//            mLastView = lHolder.mIconUpdown;
//            mLastTextView = lHolder.mNameView;
//        }else{
//            lHolder.mIconUpdown.setVisibility(View.INVISIBLE);
//            lHolder.mNameView.setTextColor(mActivity.getResources().getColor(R.color.c_333333));
//        }
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public void setListener(OnModuleCatalogClickListener onModuleCatalogClickListener) {
        this.mListener = onModuleCatalogClickListener;
    }

    class ListHolder extends RecyclerView.ViewHolder{

        TextView mNameView;
        TextView mIconNormal;
        ImageView mIconUpdown;
        View mParentView;
        public ListHolder(View itemView) {
            super(itemView);
            mParentView = itemView;
            mNameView = itemView.findViewById(R.id.quality_module_catalog_item_name);
            mIconNormal = itemView.findViewById(R.id.quality_module_catalog_item_icon_normal);
            mIconUpdown = itemView.findViewById(R.id.quality_module_catalog_item_icon_updown);
        }
    }
}
