package com.glodon.bim.business.qualityManage.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.glodon.bim.R;
import com.glodon.bim.basic.image.ImageLoader;
import com.glodon.bim.basic.utils.SharedPreferencesUtil;
import com.glodon.bim.business.qualityManage.bean.BlueprintListBeanItem;
import com.glodon.bim.business.qualityManage.listener.OnChooseBlueprintCataListener;
import com.glodon.bim.business.qualityManage.listener.OnChooseBlueprintObjListener;
import com.glodon.bim.business.qualityManage.util.ThumbnailUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：图纸  目录列表
 * 作者：zhourf on 2017/10/26
 * 邮箱：zhourf@glodon.com
 */

public class BluePrintContentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Activity mActivity;
    private List<BlueprintListBeanItem> mDataList;
    private ImageView mLastView;
    private TextView mLastTextView;
    private OnChooseBlueprintObjListener mObjListener;
    private OnChooseBlueprintCataListener mCataListener;
    private String mSelectId ;

    public BluePrintContentAdapter(Activity mActivity) {
        this.mActivity = mActivity;
        mDataList = new ArrayList<>();
    }


    public void updateList(List<BlueprintListBeanItem> dataList, String selectId){
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
                return new ObjHolder(LayoutInflater.from(mActivity).inflate(R.layout.quality_blue_print_content_item_obj_view,parent,false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if(holder==null) return;
        final BlueprintListBeanItem item = mDataList.get(position);
        if(holder instanceof CataHolder){
            final CataHolder cHolder = (CataHolder) holder;
            cHolder.mNameView.setText(item.name);
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
            lHolder.mNameView.setText(item.name);
            long projectId = SharedPreferencesUtil.getProjectId();
            String projectVerson = SharedPreferencesUtil.getProjectVersionId(projectId);
            ThumbnailUtil.getThumbnail(mActivity,projectId,projectVerson,item.fileId,lHolder.mThumbnailView);

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
                    mSelectId = item.fileId;
                    if(mObjListener !=null)
                    {
                        mObjListener.onSelect(item,mSelectId);
                    }
                }
            });

            if(!TextUtils.isEmpty(mSelectId) && mSelectId .equals(item.fileId)){
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
        return mDataList.get(position).folder?0:1;
    }

    public void setListener(OnChooseBlueprintObjListener objListener, OnChooseBlueprintCataListener cataListener) {
        this.mObjListener = objListener;
        this.mCataListener = cataListener;
    }

    class ObjHolder extends RecyclerView.ViewHolder{

        TextView mNameView;
        ImageView mSelectView;
        View mParentView;
        ImageView mThumbnailView;
        public ObjHolder(View itemView) {
            super(itemView);
            mNameView = itemView.findViewById(R.id.quality_blue_print_content_item_obj_name);
            mSelectView = itemView.findViewById(R.id.quality_blue_print_content_item_obj_select);
            mParentView = itemView;
            mThumbnailView = itemView.findViewById(R.id.quality_blue_print_content_item_obj_thumbnail);
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
