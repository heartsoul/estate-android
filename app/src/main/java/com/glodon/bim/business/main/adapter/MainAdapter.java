package com.glodon.bim.business.main.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.View;

import com.glodon.bim.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：主页
 * 作者：zhourf on 2018/2/6
 * 邮箱：zhourf@glodon.com
 */

public class MainAdapter extends FragmentPagerAdapter {

    private List<BaseFragment> mFragmentList;

    public MainAdapter(FragmentManager fm,List<BaseFragment> fragmentList) {
        super(fm);
        this.mFragmentList = fragmentList;
        if(this.mFragmentList==null){
            this.mFragmentList = new ArrayList<>();
        }
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }
}
