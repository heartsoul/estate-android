package com.glodon.bim.base;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.glodon.bim.R;

/**
 * 描述：头布局
 * 作者：zhourf on 2017/9/29
 * 邮箱：zhourf@glodon.com
 */

public class BaseHeaderManger {
    private TextView mTitleView;
    private ImageView mLeftOneView;
    private RelativeLayout mHeaderParent;

    public BaseHeaderManger(RelativeLayout mHeaderLayout) {
        mHeaderParent = mHeaderLayout;
        mTitleView = mHeaderParent.findViewById(R.id.header_title);
        mLeftOneView = mHeaderParent.findViewById(R.id.header_left_one_icon);

    }

    public BaseHeaderManger setVisible(int visibility) {
        mHeaderParent.setVisibility(visibility);
        return this;
    }

    public BaseHeaderManger setTitle(String title) {
        mTitleView.setText(title);
        return this;
    }

    public BaseHeaderManger setTitle(int resid) {
        mTitleView.setText(resid);
        return this;
    }

    public BaseHeaderManger setLeftOneIcon(int resid) {
        mLeftOneView.setBackgroundResource(resid);
        return this;
    }

    public BaseHeaderManger setLeftOneClickListener(View.OnClickListener listener) {
        mLeftOneView.setOnClickListener(listener);
        return this;
    }
}
