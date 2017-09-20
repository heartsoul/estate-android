package com.glodon.bim.test;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * 描述：
 * 作者：zhourf on 2017/9/19
 * 邮箱：zhourf@glodon.com
 */

public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<String> list;

    public MyAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyHolder(new TextView(context));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((MyHolder)holder).tv.setTextSize(30);
        ((MyHolder)holder).tv.setText(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private class MyHolder extends RecyclerView.ViewHolder{

        public TextView tv;
        public MyHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView;
        }
    }

}
