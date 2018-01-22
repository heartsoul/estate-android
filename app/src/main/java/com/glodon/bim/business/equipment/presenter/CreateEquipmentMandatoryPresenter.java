package com.glodon.bim.business.equipment.presenter;

import android.app.Activity;
import android.content.Intent;

import com.glodon.bim.business.equipment.bean.CreateEquipmentMandatoryInfo;
import com.glodon.bim.business.equipment.contract.CreateEquipmentMandatoryContract;
import com.glodon.bim.business.equipment.view.CreateEquipmentNotMandatoryActivity;
import com.glodon.bim.business.qualityManage.bean.ModelListBeanItem;
import com.glodon.bim.common.config.CommonConfig;
import com.glodon.bim.common.config.RequestCodeConfig;

/**
 * 描述：创建材设进场记录-必填项页面
 * 作者：zhourf on 2018/1/9
 * 邮箱：zhourf@glodon.com
 */

public class CreateEquipmentMandatoryPresenter implements CreateEquipmentMandatoryContract.Presenter {
    private CreateEquipmentMandatoryContract.View mView;
    private CreateEquipmentMandatoryContract.Model mModel;
    private boolean mIsEdit = false;
    private ModelListBeanItem mModelSelectInfo;

    public CreateEquipmentMandatoryPresenter(CreateEquipmentMandatoryContract.View mView) {
        this.mView = mView;
    }

    @Override
    public void initData(Intent intent) {
        //编辑状态
        CreateEquipmentMandatoryInfo info = (CreateEquipmentMandatoryInfo) intent.getSerializableExtra(CommonConfig.EQUIPMENT_EDIT_MANDATORY_INFO);
        if(info!=null){
            mView.showMandatoryInfo(info);
            mIsEdit = true;
        }
        //从模型过来
        mModelSelectInfo = (ModelListBeanItem) intent.getSerializableExtra(CommonConfig.RELEVANT_MODEL);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode)
        {
            case RequestCodeConfig.REQUEST_CODE_EQUIPMENT_MANDATORY:
                if(resultCode == Activity.RESULT_OK){
                    if(mView!=null){
                        mView.getActivity().finish();
                    }
                }
                break;
        }
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void toNotMandatory(CreateEquipmentMandatoryInfo info) {
        if(mIsEdit){
            Intent data = new Intent();
            data.putExtra(CommonConfig.EQUIPMENT_EDIT_MANDATORY_INFO,info);
            mView.getActivity().setResult(Activity.RESULT_OK,data);
            mView.getActivity().finish();
        }else {
            Intent intent = new Intent(mView.getActivity(), CreateEquipmentNotMandatoryActivity.class);
            intent.putExtra(CommonConfig.EQUIPMENT_MANDATORYINFO, info);
            //从模型选择过来
            if(mModelSelectInfo!=null){
                intent.putExtra(CommonConfig.RELEVANT_MODEL,mModelSelectInfo);
            }
            mView.getActivity().startActivityForResult(intent, RequestCodeConfig.REQUEST_CODE_EQUIPMENT_MANDATORY);
        }
    }
}
