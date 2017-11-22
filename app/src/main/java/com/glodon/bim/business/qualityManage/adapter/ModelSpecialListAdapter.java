package com.glodon.bim.business.qualityManage.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.glodon.bim.R;
import com.glodon.bim.business.qualityManage.bean.ModelSpecialListItem;
import com.glodon.bim.business.qualityManage.model.OnModelSelectListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：专业列表
 * 作者：zhourf on 2017/10/26
 * 邮箱：zhourf@glodon.com
 */

public class ModelSpecialListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Activity mActivity;
    private List<ModelSpecialListItem> mDataList;
    private ImageView mLastView;
    private TextView mLastTextView;
    private OnModelSelectListener mListener;
    private long mSelectId = 0;

    public ModelSpecialListAdapter(Activity mActivity) {
        this.mActivity = mActivity;
        mDataList = new ArrayList<>();
    }


    public void updateList(List<ModelSpecialListItem> dataList, long selectId) {
        this.mSelectId = selectId;
        mDataList = dataList;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ObjHolder(LayoutInflater.from(mActivity).inflate(R.layout.quality_model_special_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder == null) return;
        final ModelSpecialListItem item = mDataList.get(position);


        if (holder instanceof ObjHolder) {
            final ObjHolder lHolder = (ObjHolder) holder;
            lHolder.mNameView.setText(item.name);

            lHolder.mParentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mLastView != null) {
                        mLastView.setVisibility(View.INVISIBLE);
                        mLastTextView.setTextColor(mActivity.getResources().getColor(R.color.c_565656));
                    }
                    lHolder.mSelectView.setVisibility(View.VISIBLE);
                    lHolder.mNameView.setTextColor(mActivity.getResources().getColor(R.color.c_00baf3));
                    mLastView = lHolder.mSelectView;
                    mLastTextView = lHolder.mNameView;
                    mSelectId = item.id;
                    if (mListener != null) {
                        mListener.selectSpecial(item);
                    }
                }
            });

            if (item != null && mSelectId == item.id) {
                lHolder.mSelectView.setVisibility(View.VISIBLE);
                lHolder.mNameView.setTextColor(mActivity.getResources().getColor(R.color.c_00baf3));
                mLastView = lHolder.mSelectView;
                mLastTextView = lHolder.mNameView;
            } else {
                lHolder.mSelectView.setVisibility(View.INVISIBLE);
                lHolder.mNameView.setTextColor(mActivity.getResources().getColor(R.color.c_565656));
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
        ImageView mSelectView;
        View mParentView;

        public ObjHolder(View itemView) {
            super(itemView);
            mNameView = itemView.findViewById(R.id.quality_model_special_list_item_name);
            mSelectView = itemView.findViewById(R.id.quality_model_special_list_item_select);
            mParentView = itemView;
        }
    }

}
