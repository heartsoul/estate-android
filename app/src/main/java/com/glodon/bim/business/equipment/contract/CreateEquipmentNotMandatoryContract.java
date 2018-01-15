package com.glodon.bim.business.equipment.contract;

import com.glodon.bim.base.IBasePresenter;
import com.glodon.bim.base.IBaseView;
import com.glodon.bim.business.equipment.bean.CreateEquipmentMandatoryNotInfo;

/**
 * 描述：创建材设进场记录-非必填项页面
 * 作者：zhourf on 2017/10/17
 * 邮箱：zhourf@glodon.com
 */

public interface CreateEquipmentNotMandatoryContract {

    interface View extends IBaseView{

        /**
         * 展示构件名称
         */
        void showModelName(String elementName);

        /**
         * 展示编辑的信息
         */
        void showEditInfo(CreateEquipmentMandatoryNotInfo info);
    }

    interface Presenter extends IBasePresenter{


        /**
         * 跳过
         */
        void toSkip();

        /**
         * 下一步
         */
        void toNext(CreateEquipmentMandatoryNotInfo info);

        /**
         * 跳转到选择模型
         */
        void toModel();

    }

    interface Model{
    }
}
