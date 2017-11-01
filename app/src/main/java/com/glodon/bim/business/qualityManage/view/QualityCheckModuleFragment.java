package com.glodon.bim.business.qualityManage.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.glodon.bim.R;
import com.glodon.bim.base.BaseFragment;
import com.glodon.bim.business.main.bean.ProjectListItem;
import com.glodon.bim.business.qualityManage.OnClassifyItemClickListener;
import com.glodon.bim.business.qualityManage.adapter.QualityCheckListClassifyAdapter;
import com.glodon.bim.business.qualityManage.bean.ClassifyItem;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：质检项目
 * 作者：zhourf on 2017/9/29
 * 邮箱：zhourf@glodon.com
 */

public class QualityCheckModuleFragment extends BaseFragment {
    private ProjectListItem mProjectInfo;

    private RecyclerView mClassifesView;
    private QualityCheckListClassifyAdapter mCalssifyAdapter;
    private String mCurrentState = "";//当前选择的列表状态

    private List<ClassifyItem> mDataList;//分类数据

    public void setProjectInfo(ProjectListItem info)
    {
        this.mProjectInfo = info;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflate(R.layout.quality_check_module_fragment);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mClassifesView = view.findViewById(R.id.quality_check_module_classifes);
        initClassify();
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
                mCurrentState = states[position];
            }
        });
        LinearLayoutManager llm = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        mClassifesView.setLayoutManager(llm);
        mClassifesView.setAdapter(mCalssifyAdapter);
    }

}
