package com.glodon.bim.business.equipment.contract;

import com.glodon.bim.base.IBasePresenter;
import com.glodon.bim.base.IBaseView;
import com.glodon.bim.business.equipment.bean.CreateEquipmentMandatoryInfo;

/**
 * 描述：创建材设进场记录-必填项页面
 * 作者：zhourf on 2017/10/17
 * 邮箱：zhourf@glodon.com
 */

public interface CreateEquipmentMandatoryContract {

    interface View extends IBaseView{

        /**
         * 编辑状态
         */
        void showMandatoryInfo(CreateEquipmentMandatoryInfo info);
    }

    interface Presenter extends IBasePresenter{

        /**
         * 进入下一个页面
         * @param info  当前参数
         */
        void toNotMandatory(CreateEquipmentMandatoryInfo info);
    }

    interface Model{
    }
}
