package com.glodon.bim.business.qualityManage.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.glodon.bim.R;
import com.glodon.bim.base.BaseFragment;
import com.glodon.bim.business.main.bean.ProjectListItem;
import com.glodon.bim.business.qualityManage.OnClassifyItemClickListener;
import com.glodon.bim.business.qualityManage.adapter.QualityCheckListClassifyAdapter;
import com.glodon.bim.business.qualityManage.bean.ClassifyItem;
import com.glodon.bim.business.qualityManage.bean.ModuleListBeanItem;
import com.glodon.bim.business.qualityManage.contract.QulityCheckModuleContract;
import com.glodon.bim.business.qualityManage.presenter.QualityCheckModulePresenter;
import com.glodon.bim.business.qualityManage.util.IntentManager;
import com.glodon.bim.common.config.CommonConfig;
import com.glodon.bim.customview.pullrefreshview.OnPullRefreshListener;
import com.glodon.bim.customview.pullrefreshview.PullRefreshView;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：质检项目
 * 作者：zhourf on 2017/9/29
 * 邮箱：zhourf@glodon.com
 */

public class QualityCheckModuleFragment extends BaseFragment implements QulityCheckModuleContract.View{
    private QulityCheckModuleContract.Presenter mPresenter;
    //项目信息
    private ProjectListItem mProjectInfo;

    //分类与列表的父
    private LinearLayout mListParent;
    //顶部横向的分类信息
    private RecyclerView mClassifesView;
    private QualityCheckListClassifyAdapter mCalssifyAdapter;
    private String mCurrentState = "";//当前选择的列表状态
    private List<ClassifyItem> mDataList;//分类数据

    //清单列表
    private PullRefreshView mPullRefreshView;
    private RecyclerView mRecyclerView;

    //质检项目列表
    private ScrollView mCheckPointParent;
    private LinearLayout mCheckPointBase;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflate(R.layout.quality_check_module_fragment);
        initView(view);
        setListener();
        initData();
        return view;
    }

    private void setListener() {

    }

    private void initData() {
        mPresenter = new QualityCheckModulePresenter(this);
        mPresenter.initData(getActivity().getIntent());
    }

    private void initView(View view) {
        mClassifesView = view.findViewById(R.id.quality_check_module_classifes);
        mListParent = view.findViewById(R.id.quality_check_module_list_parent);
        mPullRefreshView = view.findViewById(R.id.quality_check_module_recyclerview);
        mRecyclerView = mPullRefreshView.getmRecyclerView();
        mCheckPointParent = view.findViewById(R.id.quality_check_module_checkpoint_parent);
        mCheckPointBase = view.findViewById(R.id.quality_check_module_checkpoint_base);
        initClassify();
        initRecyclerView();
    }

    /**
     * 初始化分类
     */
    private void initClassify(){
        mDataList = new ArrayList<>();

        for(int i = 0;i<8;i++){
            ClassifyItem item = new ClassifyItem();
            item.name = CommonConfig.CLASSIFY_NAMES[i];
            mDataList.add(item);
        }
        mCalssifyAdapter = new QualityCheckListClassifyAdapter(getActivity(), mDataList, new OnClassifyItemClickListener() {
            @Override
            public void onClassifyItemClick(int position, ClassifyItem item) {
                mCurrentState = CommonConfig.CLASSIFY_STATES[position];
            }
        });
        LinearLayoutManager llm = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        mClassifesView.setLayoutManager(llm);
        mClassifesView.setAdapter(mCalssifyAdapter);
    }

    private void initRecyclerView() {
        mPullRefreshView.setPullDownEnable(true);
        mPullRefreshView.setPullUpEnable(true);
        mPullRefreshView.setOnPullRefreshListener(new OnPullRefreshListener() {
            @Override
            public void onPullDown() {
                mPresenter.pullDown();
                mPullRefreshView.onPullDownComplete();
            }

            @Override
            public void onPullUp() {
                mPresenter.pullUp();
                mPullRefreshView.onPullUpComplete();
            }
        });
        mRecyclerView = mPullRefreshView.getmRecyclerView();

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setVerticalScrollBarEnabled(true);
        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(manager);

    }

    @Override
    public void updateList(List<ModuleListBeanItem> mRootList) {
        addList(mCheckPointBase,mRootList);
    }

    private void addList(LinearLayout parent,List<ModuleListBeanItem> mRootList){
        if(mRootList!=null && mRootList.size()>0){
            for(ModuleListBeanItem item:mRootList){
                //0目录，1具体的项
                switch (item.viewType)
                {
                    case 0:
                        addCatalog(parent,item);
                        break;
                    case 1:
                        addObj(parent,item);
                        break;
                }
            }
        }
    }

    private void addCatalog(LinearLayout parent, final ModuleListBeanItem item){
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.quality_modulelist_item_catalog,null);
        //名称
        TextView mNameView = view.findViewById(R.id.quality_model_list_item_catalog_name);
        //箭头
        final ImageView mArrowView = view.findViewById(R.id.quality_model_list_item_catalog_arrow);
        //子view的父
        final LinearLayout mChildParent = view.findViewById(R.id.quality_model_list_item_catalog_child_parent);

        mNameView.setText(item.name);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //点击整体item
                if(mChildParent.getVisibility() == View.GONE)
                {
                    if(mChildParent.getChildCount()==0){
                        addList(mChildParent,mPresenter.getChildList(item.id));
                    }
                    mChildParent.setVisibility(View.VISIBLE);
                    mArrowView.setBackgroundResource(R.drawable.icon_draw_arrow_up);

                }else{
                    mChildParent.setVisibility(View.GONE);
                    mArrowView.setBackgroundResource(R.drawable.icon_drawer_arrow_down);
                }
            }
        });
        parent.addView(view);
    }

    private void addObj(LinearLayout parent,final ModuleListBeanItem item){
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.quality_modulelist_item_obj,null);
        //名称
        TextView mNameView = view.findViewById(R.id.quality_model_list_item_obj_name);
        //标准
        RelativeLayout mStandardView = view.findViewById(R.id.quality_model_list_item_obj_standard);
        //创建检查单
        RelativeLayout mCreateView = view.findViewById(R.id.quality_model_list_item_obj_create);

        mNameView.setText(item.name);
        mStandardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentManager.toModuleStandard(getActivity(),item.id);
            }
        });
        mCreateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //新建检查单
            }
        });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //根据条件查询质检清单列表
            }
        });
        parent.addView(view);
    }

    @Override
    public void showLoadingDialog() {
        showLoadDialog(true);
    }

    @Override
    public void dismissLoadingDialog() {
        dismissLoadDialog();
    }

    public void setProjectInfo(ProjectListItem info)
    {
        this.mProjectInfo = info;
    }
}
