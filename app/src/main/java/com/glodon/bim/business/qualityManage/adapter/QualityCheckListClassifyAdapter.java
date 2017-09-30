package com.glodon.bim.business.qualityManage.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.glodon.bim.R;
import com.glodon.bim.basic.listener.ThrottleClickEvents;
import com.glodon.bim.business.qualityManage.OnClassifyItemClickListener;
import com.glodon.bim.business.qualityManage.bean.ClassifyItem;

import java.util.List;

/**
 * 描述：
 * 作者：zhourf on 2017/9/30
 * 邮箱：zhourf@glodon.com
 */

public class QualityCheckListClassifyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<ClassifyItem> mDataList;
    private OnClassifyItemClickListener mListener;
    private ImageView mLastView;
    private int mLastPosition = 0;

    public QualityCheckListClassifyAdapter(Context mContext, List<ClassifyItem> mDataList,OnClassifyItemClickListener listener) {
        this.mContext = mContext;
        this.mDataList = mDataList;
        this.mListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ClassifyHolder(LayoutInflater.from(mContext).inflate(R.layout.quality_check_list_classify_item_view,null));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof ClassifyHolder){
            final ClassifyHolder cHolder = (ClassifyHolder) holder;
            cHolder.mNameView.setText(mDataList.get(position).name);
            if(mLastPosition == position){
                mLastView = cHolder.mRedView;
                mLastView.setVisibility(View.VISIBLE);
            }else{
                cHolder.mRedView.setVisibility(View.INVISIBLE);
            }
            ThrottleClickEvents.throttleClick(cHolder.mNameView, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(position != mLastPosition){
                        mLastPosition = position;
                        if(mLastView!=null){
                            mLastView.setVisibility(View.INVISIBLE);
                        }
                        mListener.onClassifyItemClick(position,mDataList.get(position));
                        cHolder.mRedView.setVisibility(View.VISIBLE);
                        mLastView = cHolder.mRedView;
                    }
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    class ClassifyHolder extends RecyclerView.ViewHolder{
        public TextView mNameView;
        public ImageView mRedView;
        public ClassifyHolder(View itemView) {
            super(itemView);
            mNameView = itemView.findViewById(R.id.quality_check_list_classify_item_name);
            mRedView = itemView.findViewById(R.id.quality_check_list_classify_item_red);
        }
    }
}
