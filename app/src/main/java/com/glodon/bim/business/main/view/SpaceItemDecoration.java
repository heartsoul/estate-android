package com.glodon.bim.business.main.view;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 描述：RecyclerView间距
 * 作者：zhourf on 2017/10/17
 * 邮箱：zhourf@glodon.com
 */

class SpaceItemDecoration extends RecyclerView.ItemDecoration {
    int mSpace;

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.left = mSpace;
        outRect.right = mSpace;
        outRect.bottom = mSpace;
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.top = mSpace;
        }
    }

    public SpaceItemDecoration(int space) {
        this.mSpace = space;
    }
}