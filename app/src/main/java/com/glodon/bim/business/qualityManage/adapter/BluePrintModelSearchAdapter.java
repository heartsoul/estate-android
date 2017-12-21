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
import com.glodon.bim.basic.image.ImageLoader;
import com.glodon.bim.basic.listener.ThrottleClickEvents;
import com.glodon.bim.basic.log.LogUtil;
import com.glodon.bim.business.qualityManage.bean.BluePrintModelSearchBeanItem;
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
            bHolder.mNameView.setText(Html.fromHtml(item.name));
            bHolder.mSelectView.setVisibility(View.GONE);
            ImageLoader.showImageCenterCrop(mActivity,item.thumbnail,bHolder.mThumbnailView,R.drawable.icon_blueprint_default);
            ThrottleClickEvents.throttleClick(bHolder.mParentView, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //图纸展示
                }
            });
        }
        if(holder instanceof ModelHolder){
            ModelHolder mHolder = (ModelHolder) holder;
            mHolder.mNameView.setText(Html.fromHtml(item.name));
            ThrottleClickEvents.throttleClick(mHolder.mParentView, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //模型展示
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

        public ModelHolder(View itemView) {
            super(itemView);
            mNameView = itemView.findViewById(R.id.quality_model_list_item_name);
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
