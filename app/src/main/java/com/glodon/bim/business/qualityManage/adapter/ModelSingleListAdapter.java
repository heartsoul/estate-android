package com.glodon.bim.business.qualityManage.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.glodon.bim.R;
import com.glodon.bim.basic.utils.ScreenUtil;
import com.glodon.bim.business.qualityManage.bean.ModelSingleListItem;
import com.glodon.bim.business.qualityManage.model.OnModelSelectListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：单体列表
 * 作者：zhourf on 2017/10/26
 * 邮箱：zhourf@glodon.com
 */

public class ModelSingleListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Activity mActivity;
    private List<ModelSingleListItem> mDataList;
    private TextView mLastTextView;
    private OnModelSelectListener mListener;
    private long mSelectId = 0;
    private int mWidth;
    public ModelSingleListAdapter(Activity mActivity) {
        this.mActivity = mActivity;
        mDataList = new ArrayList<>();
        mWidth = (ScreenUtil.getScreenInfo()[0]-ScreenUtil.dp2px(37+37+40))/3;
    }


    public void updateList(List<ModelSingleListItem> dataList, long selectId) {
        this.mSelectId = selectId;
        mDataList = dataList;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ObjHolder(LayoutInflater.from(mActivity).inflate(R.layout.quality_model_single_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder == null) return;
        final ModelSingleListItem item = mDataList.get(position);


        if (holder instanceof ObjHolder) {
            final ObjHolder lHolder = (ObjHolder) holder;
            lHolder.mNameView.setText(item.name);
//            if(position%3==2){
//                lHolder.mEmptyView.setVisibility(View.GONE);
//            }else{
//                lHolder.mEmptyView.setVisibility(View.VISIBLE);
//            }
            lHolder.mNameView.getLayoutParams().width = mWidth;
            lHolder.mNameView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mLastTextView != null) {
                        mLastTextView.setTextColor(mActivity.getResources().getColor(R.color.c_333333));
                        mLastTextView.setBackgroundResource(R.drawable.corner_radius_4_gray_bg);
                    }
                    lHolder.mNameView.setTextColor(mActivity.getResources().getColor(R.color.c_00b0f1));
                    lHolder.mNameView.setBackgroundResource(R.drawable.corner_radius_4_blue_bg);
                    mLastTextView = lHolder.mNameView;
                    mSelectId = item.id;
                    if (mListener != null) {
                        mListener.selectSingle(item);
                    }
                }
            });

            if (item != null && mSelectId == item.id) {
                lHolder.mNameView.setTextColor(mActivity.getResources().getColor(R.color.c_00b0f1));
                lHolder.mNameView.setBackgroundResource(R.drawable.corner_radius_4_blue_bg);
                mLastTextView = lHolder.mNameView;
            } else {
                lHolder.mNameView.setTextColor(mActivity.getResources().getColor(R.color.c_333333));
                lHolder.mNameView.setBackgroundResource(R.drawable.corner_radius_4_gray_bg);
            }

        }

    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public void setListener(OnModelSelectListener listener) {
        this.mListener = listener;
    }

    class ObjHolder extends RecyclerView.ViewHolder {

        TextView mNameView;
//        View mEmptyView;

        public ObjHolder(View itemView) {
            super(itemView);
            mNameView = itemView.findViewById(R.id.quality_model_single_item_name);
//            mEmptyView = itemView.findViewById(R.fileId.quality_model_single_item_empty);
        }
    }

}
