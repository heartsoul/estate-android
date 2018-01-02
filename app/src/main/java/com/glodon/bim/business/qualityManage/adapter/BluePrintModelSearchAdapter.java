package com.glodon.bim.business.qualityManage.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.glodon.bim.R;
import com.glodon.bim.basic.listener.ThrottleClickEvents;
import com.glodon.bim.basic.log.LogUtil;
import com.glodon.bim.basic.utils.SharedPreferencesUtil;
import com.glodon.bim.business.qualityManage.bean.BluePrintModelSearchBeanItem;
import com.glodon.bim.business.qualityManage.listener.OnBluePrintModelSearchResultClickListener;
import com.glodon.bim.business.qualityManage.util.ThumbnailUtil;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：模型图纸搜索结果
 * 作者：zhourf on 2017/10/26
 * 邮箱：zhourf@glodon.com
 */

public class BluePrintModelSearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Activity mActivity;
    private List<BluePrintModelSearchBeanItem> mDataList;
    private OnBluePrintModelSearchResultClickListener mListener;

    public void setmListener(OnBluePrintModelSearchResultClickListener mListener) {
        this.mListener = mListener;
    }

    public BluePrintModelSearchAdapter(Activity mActivity) {
        this.mActivity = mActivity;
        mDataList = new ArrayList<>();
    }


    public void updateList(List<BluePrintModelSearchBeanItem> dataList){
        mDataList=dataList;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == 0){
            return new BluePrintHolder(LayoutInflater.from(mActivity).inflate(R.layout.quality_blue_print_content_item_obj_view,parent,false));
        }else{
            return new ModelHolder(LayoutInflater.from(mActivity).inflate(R.layout.quality_model_list_item,parent,false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final BluePrintModelSearchBeanItem item = mDataList.get(position);
        LogUtil.e("item="+new GsonBuilder().create().toJson(item));
        if(holder instanceof BluePrintHolder){
            BluePrintHolder bHolder = (BluePrintHolder) holder;
            if(!TextUtils.isEmpty(item.name)) {
                bHolder.mNameView.setText(Html.fromHtml(item.name));
            }else{
                bHolder.mNameView.setText("");
            }
            bHolder.mSelectView.setVisibility(View.GONE);
            long projectId = SharedPreferencesUtil.getProjectId();
            String projectVerson = SharedPreferencesUtil.getProjectVersionId(projectId);
            ThumbnailUtil.getThumbnail(mActivity,projectId,projectVerson,item.fileId,bHolder.mThumbnailView,R.drawable.icon_default_blueprint);
            ThrottleClickEvents.throttleClick(bHolder.mParentView, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //图纸展示
                    if(mListener!=null){
                        mListener.onSelectBluePrint(item);
                    }
                }
            });
        }
        if(holder instanceof ModelHolder){
            ModelHolder mHolder = (ModelHolder) holder;
            if(!TextUtils.isEmpty(item.name)) {
                mHolder.mNameView.setText(Html.fromHtml(item.name));
            }else{
                mHolder.mNameView.setText("");
            }

            long projectId = SharedPreferencesUtil.getProjectId();
            String projectVerson = SharedPreferencesUtil.getProjectVersionId(projectId);
            ThumbnailUtil.getThumbnail(mActivity,projectId,projectVerson,item.fileId,mHolder.mThumbView,R.drawable.icon_default_model);
            ThrottleClickEvents.throttleClick(mHolder.mParentView, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //模型展示
                    if(mListener!=null){
                        mListener.onSelectModel(item);
                    }
                }
            });
        }

    }

    @Override
    public int getItemViewType(int position) {
        BluePrintModelSearchBeanItem item = mDataList.get(position);
        if("dwg".equals(item.suffix)){
            return 0;//图纸
        }else {
            return 1;//模型
        }
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }


    class ModelHolder extends RecyclerView.ViewHolder{

        TextView mNameView;
        View mParentView;
        ImageView mThumbView;
        public ModelHolder(View itemView) {
            super(itemView);
            mNameView = itemView.findViewById(R.id.quality_model_list_item_name);
            mThumbView = itemView.findViewById(R.id.quality_model_list_item_icon);
            mParentView = itemView;
        }
    }

    class BluePrintHolder extends RecyclerView.ViewHolder{

        TextView mNameView;
        ImageView mSelectView;
        View mParentView;
        ImageView mThumbnailView;
        public BluePrintHolder(View itemView) {
            super(itemView);
            mNameView = itemView.findViewById(R.id.quality_blue_print_content_item_obj_name);
            mSelectView = itemView.findViewById(R.id.quality_blue_print_content_item_obj_select);
            mParentView = itemView;
            mThumbnailView = itemView.findViewById(R.id.quality_blue_print_content_item_obj_thumbnail);
        }
    }

}
