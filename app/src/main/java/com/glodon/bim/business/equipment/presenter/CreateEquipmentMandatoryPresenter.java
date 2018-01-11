package com.glodon.bim.business.equipment.presenter;

import android.content.Intent;

import com.glodon.bim.business.equipment.bean.MandatoryInfo;
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

    public CreateEquipmentMandatoryPresenter(CreateEquipmentMandatoryContract.View mView) {
        this.mView = mView;
    }

    @Override
    public void initData(Intent intent) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void toNotMandatory(MandatoryInfo info) {
        Intent intent = new Intent(mView.getActivity(), CreateEquipmentNotMandatoryActivity.class);
        intent.putExtra(CommonConfig.EQUIPMENT_MANDATORYINFO,info);
        mView.getActivity().startActivityForResult(intent, RequestCodeConfig.REQUEST_CODE_EQUIPMENT_MANDATORY);
    }
}
