package com.glodon.bim.business.qualityManage.contract;

import com.glodon.bim.base.IBasePresenter;
import com.glodon.bim.base.IBaseView;
import com.glodon.bim.business.qualityManage.bean.CompanyItem;
import com.glodon.bim.business.qualityManage.bean.PersonItem;

import java.util.List;

import rx.Observable;

/**
 * 描述：新建检查单
 * 作者：zhourf on 2017/10/9
 * 邮箱：zhourf@glodon.com
 */

public interface CreateCheckListContract {

    interface Presenter extends IBasePresenter {

        /**
         * 显示施工单位列表
         */
        void showCompanyList();

        /**
         * 设置选中的施工单位的position
         */
        void setCompanySelectedPosition(int position);

        /**
         * 展示责任人列表
         */
        void getPersonList();

        /**
         * 设置选中责任人的position
         */
        void setPersonSelectedPosition(int position);
    }

    interface View extends IBaseView {

        /**
         * 默认显示第一个施工单位
         */
        void showCompany(CompanyItem companyItem);

        /**
         * 展示施工单位列表
         * @param mCompanyNameList 列表集合
         * @param mCompanySelectPosition  被选中的position
         */
        void showCompanyList(List<String> mCompanyNameList, int mCompanySelectPosition);

        /**
         * 展示责任人列表
         * @param mPersonNameList 责任人列表
         * @param mPersonSelectPosition 选中的责任人
         */
        void showPersonList(List<String> mPersonNameList, int mPersonSelectPosition);
    }

    interface Model {
        /**
         * 获取施工单位列表
         * @param id  项目id
         * @param deptTypeEnums  监理JLDW  业主JSDW     (SJDW SGDW)
         */
        Observable<List<CompanyItem>> getCompaniesList(long id, List<String> deptTypeEnums);

        /**
         * 查询施工单位负责人
         * @param id 项目id
         * @param coperationCorpId 施工单位id
         */
        Observable<List<PersonItem>> gePersonList(long id, long coperationCorpId);
    }
}
