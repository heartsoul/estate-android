package com.glodon.bim.business.main.view;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.glodon.bim.R;
import com.glodon.bim.base.BaseFragment;
import com.glodon.bim.basic.listener.ThrottleClickEvents;
import com.glodon.bim.basic.utils.ResourceUtil;
import com.glodon.bim.basic.utils.ScreenUtil;
import com.glodon.bim.basic.utils.SharedPreferencesUtil;
import com.glodon.bim.business.authority.AuthorityManager;
import com.glodon.bim.business.main.adapter.MainPagerAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述：首页
 * 作者：zhourf on 2017/9/29
 * 邮箱：zhourf@glodon.com
 */

public class MainPageFragment extends BaseFragment implements View.OnClickListener {
    private RelativeLayout mBackView;
    private TextView mTitleView;
    private ImageView mSearchView;

    private ImageView mEquipmentIcon, mQualityIcon;
    private LinearLayout mCatalogParent;
    private ViewPager mViewPager;
    private MainPagerAdapter mMainPagerAdapter;
    private List<BaseFragment> mFragmentList;

    private Map<Integer, View> mCatalogMap = new HashMap<>();
    private int mCurrentKey = -1;
    private int mIndex = -1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflate(R.layout.quality_main_fragment_main_page);
        initView(view);
        setListener();
        initData();
        return view;
    }

    private void initView(View view) {
        mBackView = view.findViewById(R.id.quality_main_page_header_back);
        mTitleView = view.findViewById(R.id.quality_main_page_title);

        mSearchView = view.findViewById(R.id.quality_main_page_search);
        mEquipmentIcon = view.findViewById(R.id.quality_main_page_icon_equipment);
        mQualityIcon = view.findViewById(R.id.quality_main_page_icon_quality);
        mCatalogParent = view.findViewById(R.id.quality_main_page_catalog);
        mViewPager = view.findViewById(R.id.quality_main_page_catalog_viewpager);
    }

    private void setListener() {
        mBackView.setOnClickListener(this);
        ThrottleClickEvents.throttleClick(mSearchView, this);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                clickCatalog(position);
                if (position == 0) {
                    showQualityAnim();
                }
                if (position == 1) {
                    showEquipmentAnim();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initData() {
        mTitleView.setText(SharedPreferencesUtil.getProjectName());
        mFragmentList = new ArrayList<>();
        initCatalog();
        if (mCatalogMap.size() > 1) {
            clickCatalog(0);
        } else if (mCatalogMap.size() == 1) {
            clickCatalog(mIndex);
        }

        mMainPagerAdapter = new MainPagerAdapter(getChildFragmentManager(), mFragmentList);
        mViewPager.setAdapter(mMainPagerAdapter);
    }

    private void initCatalog() {
        if (AuthorityManager.isQualityBrowser() && AuthorityManager.isEquipmentBrowser()) {
            mCatalogParent.addView(addQualityCatalog());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.leftMargin = ScreenUtil.dp2px(47);
            mCatalogParent.addView(addEquipmentCatalog(), params);
            mEquipmentIcon.setVisibility(View.INVISIBLE);
            mQualityIcon.setVisibility(View.VISIBLE);
            initEquipmentAnim();
        } else if (AuthorityManager.isQualityBrowser()) {
            mCatalogParent.addView(addQualityCatalog());
            mEquipmentIcon.setVisibility(View.INVISIBLE);
            mQualityIcon.setVisibility(View.VISIBLE);
        } else if (AuthorityManager.isEquipmentBrowser()) {
            mCatalogParent.addView(addEquipmentCatalog());
            mEquipmentIcon.setVisibility(View.VISIBLE);
            mQualityIcon.setVisibility(View.INVISIBLE);
        }
    }


    private void clickCatalog(int key) {
        if (mCurrentKey != key && mCatalogMap.size() > 0) {
            mViewPager.setCurrentItem(key);
            for (Map.Entry<Integer, View> entry : mCatalogMap.entrySet()) {
                TextView nameView = entry.getValue().findViewById(R.id.main_page_lab_title);
                View lineView = entry.getValue().findViewById(R.id.main_page_lab_line);
                if (entry.getKey() == key) {
                    nameView.setTextColor(getResources().getColor(R.color.c_00baf3));
                    lineView.setVisibility(View.VISIBLE);
                } else {
                    nameView.setTextColor(getResources().getColor(R.color.c_333333));
                    lineView.setVisibility(View.INVISIBLE);
                }
            }
            mCurrentKey = key;
        }
    }

    private void initEquipmentAnim() {
        int equipX = -ScreenUtil.widthPixels / 2 - mEquipmentIcon.getWidth() / 2;
        ObjectAnimator.ofFloat(mEquipmentIcon, "translationX", equipX)
                .setDuration(600)
                .start();
    }

    private void showQualityAnim() {
        mEquipmentIcon.setVisibility(View.VISIBLE);
        mQualityIcon.setVisibility(View.VISIBLE);
        int qualityX = 0;
        ObjectAnimator.ofFloat(mQualityIcon, "translationX", qualityX)
                .setDuration(500)
                .start();
        int equipX = -ScreenUtil.widthPixels / 2 - mEquipmentIcon.getWidth() / 2;
        ObjectAnimator.ofFloat(mEquipmentIcon, "translationX", equipX)
                .setDuration(600)
                .start();
    }

    private void showEquipmentAnim() {
        mEquipmentIcon.setVisibility(View.VISIBLE);
        mQualityIcon.setVisibility(View.VISIBLE);
        int qualityX = -ScreenUtil.widthPixels / 2 - mQualityIcon.getWidth() / 2;
        ObjectAnimator.ofFloat(mQualityIcon, "translationX", qualityX)
                .setDuration(600)
                .start();

        int equipX = 0;
        ObjectAnimator.ofFloat(mEquipmentIcon, "translationX", equipX)
                .setDuration(500)
                .start();

    }


    private View addQualityCatalog() {
        View qualityView = inflate(R.layout.quality_main_fragment_main_page_lab);
        TextView qualityName = qualityView.findViewById(R.id.main_page_lab_title);
        View qualityLine = qualityView.findViewById(R.id.main_page_lab_line);
        qualityName.setText("质量检查");
        qualityName.setTextColor(getResources().getColor(R.color.c_00baf3));
        qualityLine.setVisibility(View.VISIBLE);
        qualityView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurrentKey != 0) {
                    showQualityAnim();
                }
                clickCatalog(0);

            }
        });
        mCatalogMap.put(0, qualityView);
        mIndex = 0;
        mFragmentList.add(new MainPagerQualityFragment());
        return qualityView;
    }

    private View addEquipmentCatalog() {
        View equipmentView = inflate(R.layout.quality_main_fragment_main_page_lab);
        TextView equipmentName = equipmentView.findViewById(R.id.main_page_lab_title);
        View equipmentLine = equipmentView.findViewById(R.id.main_page_lab_line);
        equipmentName.setText(ResourceUtil.getResourceString(R.string.str_main_equipment_manage));
        equipmentName.setTextColor(getResources().getColor(R.color.c_00baf3));
        equipmentLine.setVisibility(View.VISIBLE);
        equipmentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurrentKey != 1) {
                    showEquipmentAnim();
                }
                clickCatalog(1);
            }
        });
        mCatalogMap.put(1, equipmentView);
        mIndex = 1;
        mFragmentList.add(new MainPagerEquipmentFragment());
        return equipmentView;
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.quality_main_page_header_back:
                ((MainActivity) getActivity()).back();
                break;
            case R.id.quality_main_page_search:
                //跳转混合搜索页面
                toSearchPage();
                break;
        }
    }

    /**
     * 跳转到搜索页面
     */
    private void toSearchPage() {
        Intent intent = new Intent(getActivity(), MainPageSearchActivity.class);
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
