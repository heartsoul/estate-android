package com.glodon.bim.business.welcome.adapter;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.glodon.bim.R;
import com.glodon.bim.business.welcome.bean.WelcomeItem;

import java.util.List;

/**
 * 描述：viewpager适配
 * 作者：zhourf on 2017/9/28
 * 邮箱：zhourf@glodon.com
 */

public class WelcomeAdapter extends PagerAdapter {
    private Activity mActivity;
    private List<WelcomeItem> mDataList;

    public WelcomeAdapter(Activity mActivity, List<WelcomeItem> mDataList) {
        this.mActivity = mActivity;
        this.mDataList = mDataList;
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(final ViewGroup container, int position) {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.welcome_item_pager_view,null);
        TextView tv = view.findViewById(R.id.welcome_item_text);
        tv.setText(mDataList.get(position).text);
        container.addView(view);
        return view;
    }
}
