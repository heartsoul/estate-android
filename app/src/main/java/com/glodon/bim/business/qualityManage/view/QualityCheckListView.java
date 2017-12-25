package com.glodon.bim.business.qualityManage.view;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.glodon.bim.R;
import com.glodon.bim.basic.listener.ThrottleClickEvents;
import com.glodon.bim.business.main.bean.ProjectListItem;
import com.glodon.bim.business.qualityManage.OnClassifyItemClickListener;
import com.glodon.bim.business.qualityManage.adapter.QualityCheckListAdapter;
import com.glodon.bim.business.qualityManage.adapter.QualityCheckListClassifyAdapter;
import com.glodon.bim.business.qualityManage.bean.ClassifyItem;
import com.glodon.bim.business.qualityManage.bean.ClassifyNum;
import com.glodon.bim.business.qualityManage.bean.ModuleListBeanItem;
import com.glodon.bim.business.qualityManage.bean.QualityCheckListBeanItem;
import com.glodon.bim.business.qualityManage.contract.QualityCheckListContract;
import com.glodon.bim.business.qualityManage.presenter.QualityCheckListPresenter;
import com.glodon.bim.common.config.CommonConfig;
import com.glodon.bim.customview.dialog.LoadingDialogManager;
import com.glodon.bim.customview.dialog.PhotoAlbumDialog;
import com.glodon.bim.customview.pullrefreshview.OnPullRefreshListener;
import com.glodon.bim.customview.pullrefreshview.PullRefreshView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述：质检清单
 * 作者：zhourf on 2017/9/29
 * 邮箱：zhourf@glodon.com
 */

public class QualityCheckListView implements QualityCheckListContract.View {
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

    private PhotoAlbumDialog mPhotoAlbumDialog;//拍照相册弹出框
    private LoadingDialogManager mLoadingDialog;


    private Activity mActivity;
    public QualityCheckListView(Activity activity,View view,ProjectListItem info){
        this.mActivity = activity;
        this.mProjectInfo = info;
        initView(view);
    }


    private void initView(View view) {
        mPullRefreshView = view.findViewById(R.id.quality_check_list_recyclerview);
        mToTopView = view.findViewById(R.id.quality_check_list_to_top);
        mClassifesView = view.findViewById(R.id.quality_check_list_classifes);

        setListener();
        initClassify();
        initRecyclerView();
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
        final LinearLayoutManager manager = new LinearLayoutManager(mActivity);
        mRecyclerView.setLayoutManager(manager);

    }

    /**
     * 初始化分类
     */
    private void initClassify(){
        mDataList = new ArrayList<>();
        for(int i = 0;i<8;i++){
            ClassifyItem item = new ClassifyItem();
            item.name = CommonConfig.CLASSIFY_NAMES[i];
            item.qcState = CommonConfig.CLASSIFY_STATES[i];
            mDataList.add(item);
        }
        mCalssifyAdapter = new QualityCheckListClassifyAdapter(getActivity(), mDataList, new OnClassifyItemClickListener() {
            @Override
            public void onClassifyItemClick(int position, ClassifyItem item) {
                if(!mCurrentState.equals(CommonConfig.CLASSIFY_STATES[position])) {
                    mCurrentState = CommonConfig.CLASSIFY_STATES[position];
                    mPresenter.getClassifyData(mCurrentState);
                }
            }
        });
        LinearLayoutManager llm = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        mClassifesView.setLayoutManager(llm);
        mClassifesView.setAdapter(mCalssifyAdapter);
    }

    @Override
    public void updateClassifyCount(List<ClassifyNum> list){
        if(list!=null && list.size()>0){
            Map<String,Integer> map = new HashMap<>();
            List<String> keyList = new ArrayList<>();
            keyList.add(CommonConfig.QC_STATE_STAGED);
            keyList.add(CommonConfig.QC_STATE_UNRECTIFIED);
            keyList.add(CommonConfig.QC_STATE_UNREVIEWED);
            keyList.add(CommonConfig.QC_STATE_DELAYED);

            for(ClassifyNum item : list){
//                {"全部","待提交",  "待整改",      "待复查",    "已检查",    "已复查",  "已延迟",  "已验收"};
//                {"",   "staged",  "unrectified","unreviewed","inspected","reviewed","delayed","accepted"};
                if(keyList.contains(item.qcState)) {
                    map.put(item.qcState, item.count);
                }else{
                    map.put(item.qcState,0);
                }
            }
            if(mDataList!=null && mDataList.size()>0){
                List<ClassifyItem> tempList = new ArrayList<>();
                for(ClassifyItem item :mDataList){
                    ClassifyItem temp = new ClassifyItem();
                    temp.name = item.name;
                    temp.qcState = item.qcState;
                    Integer tempCount = map.get(item.qcState);
                    if(tempCount==null){
                        temp.count = 0;
                    }else{
                        temp.count = tempCount.intValue();
                    }
                    tempList.add(temp);
                }
                mDataList=tempList;
                mCalssifyAdapter.updateNums(mDataList);
            }
        }
    }

    public void initData() {
        mPresenter = new QualityCheckListPresenter(this);
        mAdapter = new QualityCheckListAdapter(mActivity, mPresenter.getListener());
        mRecyclerView.setAdapter(mAdapter);
        mPresenter.initData(mProjectInfo);

    }

    /**
     * 根据质检项目查询质检清单列表
     */
    public void initData(ModuleListBeanItem item){
        mPresenter = new QualityCheckListPresenter(this);
        mPresenter.setModuleSelectInfo(item);
        mAdapter = new QualityCheckListAdapter(mActivity, mPresenter.getListener());
        mRecyclerView.setAdapter(mAdapter);
        mPresenter.initData(mProjectInfo);

    }

    @Override
    public void create() {
        if (mPhotoAlbumDialog == null) {
            mPhotoAlbumDialog = new PhotoAlbumDialog(getActivity()).builder(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mPresenter.openPhoto();
                }
            }, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mPresenter.openAlbum();
                }
            }, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mPresenter.toCreate();
                }
            });
        }
        mPhotoAlbumDialog.show();
    }


    @Override
    public void showLoadingDialog() {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialogManager(getActivity());
        }
        mLoadingDialog.show();
    }

    @Override
    public void dismissLoadingDialog() {
        mLoadingDialog.dismiss();
    }

    @Override
    public Activity getActivity() {
        return mActivity;
    }

    @Override
    public void updateData(List<QualityCheckListBeanItem> mDataList) {
        mAdapter.updateList(mDataList);

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(mPresenter!=null) {
            mPresenter.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void onDestroyView() {
        if(mPresenter!=null) {
            mPresenter.onDestroy();
        }
    }

}
