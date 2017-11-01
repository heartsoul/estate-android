package com.glodon.bim.business.qualityManage.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.glodon.bim.R;
import com.glodon.bim.base.BaseFragment;
import com.glodon.bim.basic.listener.ThrottleClickEvents;
import com.glodon.bim.business.main.bean.ProjectListItem;
import com.glodon.bim.business.qualityManage.OnClassifyItemClickListener;
import com.glodon.bim.business.qualityManage.adapter.QualityCheckListAdapter;
import com.glodon.bim.business.qualityManage.adapter.QualityCheckListClassifyAdapter;
import com.glodon.bim.business.qualityManage.bean.ClassifyItem;
import com.glodon.bim.business.qualityManage.bean.QualityCheckListBeanItem;
import com.glodon.bim.business.qualityManage.contract.QualityCheckListContract;
import com.glodon.bim.business.qualityManage.presenter.QualityCheckListPresenter;
import com.glodon.bim.customview.pullrefreshview.OnPullRefreshListener;
import com.glodon.bim.customview.pullrefreshview.PullRefreshView;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：质检清单
 * 作者：zhourf on 2017/9/29
 * 邮箱：zhourf@glodon.com
 */

public class QualityCheckListFragment extends BaseFragment implements QualityCheckListContract.View {
    private PullRefreshView mPullRefreshView;
    private RecyclerView mRecyclerView;
    private ImageView mToTopView;
    private QualityCheckListAdapter mAdapter;
    private QualityCheckListContract.Presenter mPresenter;
    private ProjectListItem mProjectInfo;

    private RecyclerView mClassifesView;
    private QualityCheckListClassifyAdapter mCalssifyAdapter;
    private String mCurrentState = "";//当前选择的列表状态

    private List<ClassifyItem> mDataList;//分类数据

    public void setProjectInfo(ProjectListItem info) {
        this.mProjectInfo = info;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflate(R.layout.quality_check_list_fragment);
        initView(view);
        return view;
    }


    private void initView(View view) {
        mPullRefreshView = view.findViewById(R.id.quality_check_list_recyclerview);
        mToTopView = view.findViewById(R.id.quality_check_list_to_top);
        mClassifesView = view.findViewById(R.id.quality_check_list_classifes);

        setListener();
        initClassify();
        initRecyclerView();
        initData();
    }

    private void setListener() {
        ThrottleClickEvents.throttleClick(mToTopView, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRecyclerView.smoothScrollToPosition(0);
            }
        });
    }

    private void initRecyclerView() {
        mPullRefreshView.setPullDownEnable(false);
        mPullRefreshView.setPullUpEnable(false);
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

    /**
     * 初始化分类
     */
    private void initClassify(){
        mDataList = new ArrayList<>();
//        String[] names = {"全部","待提交","待整改","待复查","已整改","已复查","已延迟","已验收"};
        String[] names = {"全部","待提交","待整改","待复查","已检查","已复查","已延迟","已验收"};
        final String[] states = {"","staged","unrectified","unreviewed","inspected","reviewed","delayed","accepted"};

        for(int i = 0;i<8;i++){
            ClassifyItem item = new ClassifyItem();
            item.name = names[i];
            mDataList.add(item);
        }
        mCalssifyAdapter = new QualityCheckListClassifyAdapter(getActivity(), mDataList, new OnClassifyItemClickListener() {
            @Override
            public void onClassifyItemClick(int position, ClassifyItem item) {
                if(!mCurrentState.equals(states[position])) {
                    mCurrentState = states[position];
                    mPresenter.getClassifyData(mCurrentState);
                }
            }
        });
        LinearLayoutManager llm = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        mClassifesView.setLayoutManager(llm);
        mClassifesView.setAdapter(mCalssifyAdapter);
    }

    private void initData() {
        mPresenter = new QualityCheckListPresenter(this);
        mAdapter = new QualityCheckListAdapter(getContext(), mPresenter.getListener());
        mRecyclerView.setAdapter(mAdapter);
        mPresenter.initData(mProjectInfo);
    }


//    private List<QualityCheckListBeanItem> getDataList(){
//        List<QualityCheckListBeanItem> list = new ArrayList<>();
//        for(int i = 0;i<22;i++){
//            QualityCheckListBeanItem item = new QualityCheckListBeanItem();
//            item.showType = i%2;
//            if(i>0){
//                item.timeType= 1;
//            }
//            item.sheetStatus = i%7;
//            list.add(item);
//        }
//        return list;
//    }


    @Override
    public void showLoadingDialog() {
        showLoadDialog(true);
    }

    @Override
    public void dismissLoadingDialog() {
        dismissLoadDialog();
    }

    @Override
    public void updateData(List<QualityCheckListBeanItem> mDataList) {
        mAdapter.updateList(mDataList);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPresenter.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }

}
