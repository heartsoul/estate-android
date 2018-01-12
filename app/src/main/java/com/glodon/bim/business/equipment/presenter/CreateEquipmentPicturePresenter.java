package com.glodon.bim.business.equipment.presenter;

import android.content.Intent;

import com.glodon.bim.business.equipment.bean.MandatoryInfo;
import com.glodon.bim.business.equipment.contract.CreateEquipmentMandatoryContract;
import com.glodon.bim.business.equipment.contract.CreateEquipmentPictureContract;
import com.glodon.bim.business.equipment.view.CreateEquipmentNotMandatoryActivity;
import com.glodon.bim.common.config.CommonConfig;
import com.glodon.bim.common.config.RequestCodeConfig;

/**
 * 描述：创建材设进场记录-拍照页面
 * 作者：zhourf on 2018/1/9
 * 邮箱：zhourf@glodon.com
 */

public class CreateEquipmentPicturePresenter implements CreateEquipmentPictureContract.Presenter {
    private CreateEquipmentPictureContract.View mView;
    private CreateEquipmentPictureContract.Model mModel;

    public CreateEquipmentPicturePresenter(CreateEquipmentPictureContract.View mView) {
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


}
