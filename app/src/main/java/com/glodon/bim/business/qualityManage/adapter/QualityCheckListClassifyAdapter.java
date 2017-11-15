package com.glodon.bim.business.qualityManage.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.glodon.bim.R;
import com.glodon.bim.basic.listener.ThrottleClickEvents;
import com.glodon.bim.basic.log.LogUtil;
import com.glodon.bim.basic.utils.ScreenUtil;
import com.glodon.bim.business.qualityManage.OnClassifyItemClickListener;
import com.glodon.bim.business.qualityManage.bean.ClassifyItem;

import java.util.List;

/**
 * 描述：清单分类
 * 作者：zhourf on 2017/9/30
 * 邮箱：zhourf@glodon.com
 */

public class QualityCheckListClassifyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<ClassifyItem> mDataList;
    private OnClassifyItemClickListener mListener;
    private ImageView mLastLineView;
    private TextView mLastTextView;
    private int mLastPosition = 0;
    private TextPaint mPaint;

    public QualityCheckListClassifyAdapter(Context mContext, List<ClassifyItem> mDataList,OnClassifyItemClickListener listener) {
        this.mContext = mContext;
        this.mDataList = mDataList;
        this.mListener = listener;
    }

    /**
     * 更新分类数量
     */
    public void updateNums(List<ClassifyItem> list){
        mDataList = list;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ClassifyHolder(LayoutInflater.from(mContext).inflate(R.layout.quality_check_list_classify_item_view,null));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof ClassifyHolder){
            final ClassifyHolder cHolder = (ClassifyHolder) holder;
            ClassifyItem item = mDataList.get(position);
            cHolder.mNameView.setText(item.name);
            if(item.count<=0){
                cHolder.mNumView.setVisibility(View.GONE);
            }else{
                cHolder.mNumView.setVisibility(View.VISIBLE);
                if(item.count>99){
                    cHolder.mNumView.setText("99+");
                }else{
                    cHolder.mNumView.setText(item.count+"");
                }
            }
            mPaint = cHolder.mNameView.getPaint();
            if(position==0){
                cHolder.mParent.setPadding(ScreenUtil.dp2px(20),0,0,0);
                cHolder.mParent.setMinimumWidth((int) (ScreenUtil.dp2px(56)+mPaint.measureText(cHolder.mNameView.getText().toString().trim())+0.5f));
            }else{
                cHolder.mParent.setPadding(0,0,0,0);
                cHolder.mParent.setMinimumWidth((int) (ScreenUtil.dp2px(36)+mPaint.measureText(cHolder.mNameView.getText().toString().trim())+0.5f));
            }

            if(mLastPosition == position){
                mLastLineView = cHolder.mLineView;
                mLastLineView.setVisibility(View.VISIBLE);
                mLastTextView = cHolder.mNameView;
                mLastTextView.setTextColor(mContext.getResources().getColor(R.color.c_00b4f2));
            }else{
                cHolder.mLineView.setVisibility(View.INVISIBLE);
                cHolder.mNameView.setTextColor(mContext.getResources().getColor(R.color.c_535353));
            }
            ThrottleClickEvents.throttleClick(cHolder.mNameView, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(position != mLastPosition){
                        mLastPosition = position;
                        if(mLastLineView !=null){
                            mLastLineView.setVisibility(View.INVISIBLE);
                        }
                        if(mLastTextView!=null){
                            mLastTextView.setTextColor(mContext.getResources().getColor(R.color.c_535353));
                        }
                        mListener.onClassifyItemClick(position,mDataList.get(position));
                        cHolder.mLineView.setVisibility(View.VISIBLE);
                        mLastLineView = cHolder.mLineView;
                        cHolder.mNameView.setTextColor(mContext.getResources().getColor(R.color.c_00b4f2));
                        mLastTextView = cHolder.mNameView;
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
         TextView mNameView;
         ImageView mLineView;
         TextView mNumView;
         RelativeLayout mParent;
         ClassifyHolder(View itemView) {
            super(itemView);
            mNameView = itemView.findViewById(R.id.quality_check_list_classify_item_name);
            mNumView = itemView.findViewById(R.id.quality_check_list_classify_item_num);
            mLineView = itemView.findViewById(R.id.quality_check_list_classify_item_line);
            mParent = itemView.findViewById(R.id.quality_check_list_classify_item_parent);
        }
    }
}
