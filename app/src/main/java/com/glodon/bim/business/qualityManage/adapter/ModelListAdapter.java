package com.glodon.bim.business.qualityManage.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.glodon.bim.R;
import com.glodon.bim.business.qualityManage.bean.ModelListBeanItem;
import com.glodon.bim.business.qualityManage.model.OnModelSelectListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：模型列表
 * 作者：zhourf on 2017/10/26
 * 邮箱：zhourf@glodon.com
 */

public class ModelListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Activity mActivity;
    private List<ModelListBeanItem> mDataList;
    private ImageView mLastView;
    private TextView mLastTextView;
    private OnModelSelectListener mListener;
    private long mSelectId = 0;

    public ModelListAdapter(Activity mActivity) {
        this.mActivity = mActivity;
        mDataList = new ArrayList<>();
    }


    public void updateList(List<ModelListBeanItem> dataList, long selectId) {
        this.mSelectId = selectId;
        mDataList = dataList;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ObjHolder(LayoutInflater.from(mActivity).inflate(R.layout.quality_model_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder == null) return;
        final ModelListBeanItem item = mDataList.get(position);
        if (holder instanceof ObjHolder) {
            final ObjHolder lHolder = (ObjHolder) holder;
            lHolder.mNameView.setText(item.name);

            lHolder.mParentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    if (mLastView != null) {
//                        mLastView.setVisibility(View.INVISIBLE);
//                        mLastTextView.setTextColor(mActivity.getResources().getColor(R.color.c_333333));
//                    }
//                    lHolder.mSelectView.setVisibility(View.VISIBLE);
//                    lHolder.mNameView.setTextColor(mActivity.getResources().getColor(R.color.c_00baf3));
//                    mLastView = lHolder.mSelectView;
//                    mLastTextView = lHolder.mNameView;
//                    mSelectId = position;
                    if (mListener != null) {
                        mListener.selectModel(item);
                    }
                }
            });

//            if (item != null && mSelectId == item.id) {
//                lHolder.mSelectView.setVisibility(View.VISIBLE);
//                lHolder.mNameView.setTextColor(mActivity.getResources().getColor(R.color.c_00baf3));
//                mLastView = lHolder.mSelectView;
//                mLastTextView = lHolder.mNameView;
//            } else {
//                lHolder.mSelectView.setVisibility(View.INVISIBLE);
//                lHolder.mNameView.setTextColor(mActivity.getResources().getColor(R.color.c_333333));
//            }
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
        View mParentView;

        public ObjHolder(View itemView) {
            super(itemView);
            mNameView = itemView.findViewById(R.id.quality_model_list_item_name);
            mParentView = itemView;
        }
    }

}
