package com.glodon.bim.business.qualityManage.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.glodon.bim.R;
import com.glodon.bim.business.qualityManage.bean.ModuleListBeanItem;
import com.glodon.bim.business.qualityManage.listener.OnChooseModuleCataListener;
import com.glodon.bim.business.qualityManage.listener.OnChooseModuleObjListener;
import com.glodon.bim.business.qualityManage.util.IntentManager;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：质检项目  目录列表
 * 作者：zhourf on 2017/10/26
 * 邮箱：zhourf@glodon.com
 */

public class ModuleContentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Activity mActivity;
    private List<ModuleListBeanItem> mDataList;
    private ImageView mLastView;
    private TextView mLastTextView;
    private OnChooseModuleObjListener mObjListener;
    private OnChooseModuleCataListener mCataListener;
    private long mSelectId = 0;

    public ModuleContentAdapter(Activity mActivity) {
        this.mActivity = mActivity;
        mDataList = new ArrayList<>();
    }


    public void updateList(List<ModuleListBeanItem> dataList, long selectId){
        this.mSelectId = selectId;
        mDataList=dataList;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType)
        {
            case 0:
                return new CataHolder(LayoutInflater.from(mActivity).inflate(R.layout.quality_module_content_item_catalog_view,parent,false));
            case 1:
                return new ObjHolder(LayoutInflater.from(mActivity).inflate(R.layout.quality_module_content_item_obj_view,parent,false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if(holder==null) return;
        final ModuleListBeanItem item = mDataList.get(position);
        if(holder instanceof CataHolder){
            final CataHolder cHolder = (CataHolder) holder;
            cHolder.mNameView.setText(item.inspectItem);
            cHolder.mParentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mCataListener!=null){
                        mCataListener.onSelect(item);
                    }
                }
            });
        }

        if(holder instanceof ObjHolder){
            final ObjHolder lHolder = (ObjHolder) holder;
            lHolder.mNameView.setText(item.inspectItem);
            lHolder.mBenchMarkView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    IntentManager.toModuleStandard(mActivity,item.id.longValue(),item.inspectItem);
                }
            });
            lHolder.mParentView.setOnClickListener(new View.OnClickListener() {
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
                    if(mObjListener !=null)
                    {
                        mObjListener.onSelect(item,mSelectId);
                    }
                }
            });

            if(item!=null && mSelectId == item.id.longValue()){
                lHolder.mSelectView.setVisibility(View.VISIBLE);
                lHolder.mNameView.setTextColor(mActivity.getResources().getColor(R.color.c_00baf3));
                mLastView = lHolder.mSelectView;
                mLastTextView = lHolder.mNameView;
            }else{
                lHolder.mSelectView.setVisibility(View.INVISIBLE);
                lHolder.mNameView.setTextColor(mActivity.getResources().getColor(R.color.c_333333));
            }
        }

    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mDataList.get(position).viewType;
    }

    public void setListener(OnChooseModuleObjListener onChooseModuleObjListener, OnChooseModuleCataListener onChooseModuleCataListener) {
        this.mObjListener = onChooseModuleObjListener;
        this.mCataListener = onChooseModuleCataListener;
    }

    class ObjHolder extends RecyclerView.ViewHolder{

        TextView mNameView;
        ImageView mSelectView;
        View mParentView;
        RelativeLayout mBenchMarkView;
        public ObjHolder(View itemView) {
            super(itemView);
            mNameView = itemView.findViewById(R.id.quality_module_content_item_obj_name);
            mSelectView = itemView.findViewById(R.id.quality_module_content_item_obj_select);
            mParentView = itemView;
            mBenchMarkView = itemView.findViewById(R.id.quality_module_content_item_obj_benchmark);
        }
    }

    class CataHolder extends RecyclerView.ViewHolder{

        TextView mNameView;
        View mParentView;
        public CataHolder(View itemView) {
            super(itemView);
            mNameView = itemView.findViewById(R.id.quality_module_content_item_catalog_name);
            mParentView = itemView;
        }
    }
}
