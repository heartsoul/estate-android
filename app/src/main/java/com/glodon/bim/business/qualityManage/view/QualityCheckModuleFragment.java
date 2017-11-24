package com.glodon.bim.business.qualityManage.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.glodon.bim.basic.utils.SharedPreferencesUtil;
import com.glodon.bim.business.main.bean.ProjectListItem;
import com.glodon.bim.business.qualityManage.bean.ModuleListBeanItem;
import com.glodon.bim.business.qualityManage.contract.QulityCheckModuleContract;
import com.glodon.bim.business.qualityManage.listener.OnTitleChangerListener;
import com.glodon.bim.business.qualityManage.presenter.QualityCheckModulePresenter;
import com.glodon.bim.business.qualityManage.util.IntentManager;
import com.glodon.bim.customview.dialog.PhotoAlbumDialog;

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
    private QualityCheckListView mQualityCheckListView;

    //质检项目列表
    private ScrollView mCheckPointParent;
    private LinearLayout mCheckPointBase;

    private OnTitleChangerListener mTitleListener;

    public void setTitleChangeListener(OnTitleChangerListener listner){
        mTitleListener = listner;
    }
    private String mCurrentTitle = "质检项目";
    private ModuleListBeanItem mCurrentModuleInfo;

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

    public void changeTitle(){
        if(mTitleListener!=null)
        {
            mTitleListener.onTitleChange(mCurrentTitle);
            if(mCurrentModuleInfo!=null){
                SharedPreferencesUtil.setSelectModuleInfo(mCurrentModuleInfo.id.longValue(),mCurrentModuleInfo.name);
            }
        }
    }

    private void initData() {
        mPresenter = new QualityCheckModulePresenter(this);
        mPresenter.initData(getActivity().getIntent());
    }

    private void initView(View view) {
        mListParent = view.findViewById(R.id.quality_check_module_list_parent);
        mListParent.setVisibility(View.GONE);
        mCheckPointParent = view.findViewById(R.id.quality_check_module_checkpoint_parent);
        mCheckPointBase = view.findViewById(R.id.quality_check_module_checkpoint_base);

        mQualityCheckListView = new QualityCheckListView(getActivity(),mListParent,mProjectInfo);
    }

    //返回键,奇幻质检项目和质检清单列表
    public void back(){
        if(mCheckPointParent.getVisibility() == View.VISIBLE){
            getActivity().finish();
        }else {
            mListParent.setVisibility(View.GONE);
            mCheckPointParent.setVisibility(View.VISIBLE);
        }
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
                create();
                SharedPreferencesUtil.setSelectModuleInfo(item.id.longValue(),item.name);
            }
        });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //根据条件查询质检清单列表
                mCheckPointParent.setVisibility(View.GONE);
                mListParent.setVisibility(View.VISIBLE);
                mQualityCheckListView.initData(item);
                mCurrentTitle = item.name;
                mCurrentModuleInfo = item;
                changeTitle();
                SharedPreferencesUtil.setSelectModuleInfo(item.id.longValue(),item.name);
            }
        });
        parent.addView(view);
    }

    private PhotoAlbumDialog mPhotoAlbumDialog;//拍照相册弹出框
    //弹出照片选择框

    private void create() {
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(mPresenter!=null){
            mPresenter.onActivityResult(requestCode, resultCode, data);
        }
        if(mQualityCheckListView!=null)
        {
            mQualityCheckListView.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(mQualityCheckListView!=null)
        {
            mQualityCheckListView.onDestroyView();
        }
    }
}
