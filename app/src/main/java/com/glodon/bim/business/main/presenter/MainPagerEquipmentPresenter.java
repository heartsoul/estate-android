package com.glodon.bim.business.main.presenter;

import android.content.Intent;

import com.glodon.bim.R;
import com.glodon.bim.basic.log.LogUtil;
import com.glodon.bim.business.main.bean.MainPagerQualityEquipmentItem;
import com.glodon.bim.business.main.contract.MainPagerEquipmentContract;
import com.glodon.bim.business.main.listener.OnPagerItemClickListener;
import com.glodon.bim.business.main.model.MainPagerEquipmentModel;
import com.glodon.bim.business.qualityManage.view.QualityMangeMainActivity;
import com.glodon.bim.common.config.CommonConfig;
import com.glodon.bim.common.config.RequestCodeConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：首页材设
 * 作者：zhourf on 2018/2/6
 * 邮箱：zhourf@glodon.com
 */

public class MainPagerEquipmentPresenter implements MainPagerEquipmentContract.Presenter {
    private MainPagerEquipmentContract.View mView;
    private MainPagerEquipmentContract.Model mModel;
    private List<MainPagerQualityEquipmentItem> mDataList;
//    private ProjectListItem mProjectInfo;
    private OnPagerItemClickListener mListener = new OnPagerItemClickListener() {

        @Override
        public void onClick(int position) {
            toQualityChickList(position+4);
        }
    };

    public void toQualityChickList(int type) {
        LogUtil.e("type="+type);
        Intent intent = new Intent(mView.getActivity(), QualityMangeMainActivity.class);
//        intent.putExtra(CommonConfig.PROJECT_LIST_ITEM,mProjectInfo);
        intent.putExtra(CommonConfig.MAIN_FROM_TYPE,type);
        mView.getActivity().startActivityForResult(intent, RequestCodeConfig.REQUEST_CODE_CHANGE_PROJECT);
    }

    public MainPagerEquipmentPresenter(MainPagerEquipmentContract.View mView) {
        this.mView = mView;
        mModel = new MainPagerEquipmentModel();
        mDataList = new ArrayList<>();
    }

    @Override
    public void initData(Intent intent) {
//        mProjectInfo = (ProjectListItem) intent.getSerializableExtra(CommonConfig.PROJECT_LIST_ITEM);
//        SharedPreferencesUtil.setProjectInfo(mProjectInfo);
        getList();
        if(mView!=null){
            mView.updateList(mDataList,mListener);
        }
    }

    private void getList(){
        mDataList.add(new MainPagerQualityEquipmentItem(R.drawable.icon_main_pager_csjc,"材设清单"));
        mDataList.add(new MainPagerQualityEquipmentItem(R.drawable.icon_main_pager_equipment_model,"模型预览"));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void onDestroy() {

    }
}
