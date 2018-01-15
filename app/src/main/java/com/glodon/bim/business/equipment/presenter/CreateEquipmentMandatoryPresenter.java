package com.glodon.bim.business.equipment.presenter;

import android.app.Activity;
import android.content.Intent;

import com.glodon.bim.business.equipment.bean.CreateEquipmentMandatoryInfo;
import com.glodon.bim.business.equipment.contract.CreateEquipmentMandatoryContract;
import com.glodon.bim.business.equipment.view.CreateEquipmentNotMandatoryActivity;
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

    public CreateEquipmentMandatoryPresenter(CreateEquipmentMandatoryContract.View mView) {
        this.mView = mView;
    }

    @Override
    public void initData(Intent intent) {
        CreateEquipmentMandatoryInfo info = (CreateEquipmentMandatoryInfo) intent.getSerializableExtra(CommonConfig.EQUIPMENT_EDIT_MANDATORY_INFO);
        if(info!=null){
            mView.showMandatoryInfo(info);
            mIsEdit = true;
        }
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
            mView.getActivity().startActivityForResult(intent, RequestCodeConfig.REQUEST_CODE_EQUIPMENT_MANDATORY);
        }
    }
}
