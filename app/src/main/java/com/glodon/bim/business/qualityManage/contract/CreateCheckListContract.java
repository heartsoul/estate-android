package com.glodon.bim.business.qualityManage.contract;

import com.glodon.bim.base.IBasePresenter;
import com.glodon.bim.base.IBaseView;
import com.glodon.bim.business.qualityManage.bean.CompanyItem;
import com.glodon.bim.business.qualityManage.bean.CreateCheckListParams;
import com.glodon.bim.business.qualityManage.bean.ImageUploadBean;
import com.glodon.bim.business.qualityManage.bean.InspectionCompanyItem;
import com.glodon.bim.business.qualityManage.bean.PersonItem;
import com.glodon.bim.business.qualityManage.bean.SaveBean;
import com.glodon.bim.customview.album.TNBImageItem;

import java.util.LinkedHashMap;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
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

        /**
         * 跳转到质检项目列表
         */
        void toModuleList();

        /**
         * 点击提交
         */
        void submit(CreateCheckListParams createCheckListParams);

        /**
         * 删除当前单据
         */
        void deleteCheckList();

        /**
         * 点击保存
         *
         * @param mParams
         */
        void save(CreateCheckListParams mParams);

        /**
         * 打开相册
         */
        void openAlbum();

        /**
         * 点击拍照
         */
        void takePhoto();

        /**
         * 设置选中的图片
         */
        void setSelectedImages(LinkedHashMap<String, TNBImageItem> map);

        /**
         * 跳转到图片预览页
         *
         * @param position 当前的图片
         */
        void toPreview(int position);

        /**
         * 设置为编辑状态
         */
        void setEditState(CreateCheckListParams mParams);

        /**
         * 是否保存了
         */
        boolean isChange();

        /**
         * 页面数据变化了吗
         */
        boolean isDifferent(CreateCheckListParams currentParams);

        /**
         * 设置单据类型
         */
        void setInspectionType(String typeInspection);

        /**
         * 设置选中的施工单位
         * @param position
         */
        void setInspectionCompanySelectedPosition(int position);

        /**
         * 显示检查单位
         */
        void showInspectionCompanyList();

    }

    interface View extends IBaseView {

        /**
         * 默认显示第一个施工单位
         */
        void showCompany(CompanyItem companyItem);

        /**
         * 显示检查单位
         */
        void showInspectionCompany(InspectionCompanyItem companyItem);

        /**
         * 展示施工单位列表
         *
         * @param mCompanyNameList       列表集合
         * @param mCompanySelectPosition 被选中的position
         */
        void showCompanyList(List<String> mCompanyNameList, int mCompanySelectPosition);

        /**
         * 展示检查单位列表
         * @param mInspectionCompanyNameList  列表
         * @param mInspectionCompanySelectPosition  选中的position
         */
        void showInspectionCompanyList(final List<String> mInspectionCompanyNameList, final int mInspectionCompanySelectPosition);

        /**
         * 展示责任人列表
         *
         * @param mPersonNameList       责任人列表
         * @param mPersonSelectPosition 选中的责任人
         */
        void showPersonList(List<String> mPersonNameList, int mPersonSelectPosition);

        /**
         * 显示选中的质检项目
         */
        void showModuleName(String name);

        /**
         * 保存成功后显示删除按钮
         */
        void showDeleteButton();

        /**
         * 展示图片
         *
         * @param mSelectedMap
         */
        void showImages(LinkedHashMap<String, TNBImageItem> mSelectedMap);

        /**
         * 设置检查单和验收单的title
         * @param mInspectionType
         */
        void setTitle(String mInspectionType);
    }

    interface Model {
        /**
         * 获取施工单位列表
         *
         * @param id            项目id
         * @param deptTypeEnums 监理JLDW  业主JSDW     (SJDW SGDW)
         */
        Observable<List<CompanyItem>> getCompaniesList(long id, List<String> deptTypeEnums);

        /**
         * 查询施工单位负责人
         *
         * @param id               项目id
         * @param coperationCorpId 施工单位id
         */
        Observable<List<PersonItem>> gePersonList(long id, long coperationCorpId);

        /**
         * 新建检查单 提交
         *
         * @param deptId 项目id
         */
        Observable<SaveBean> createSubmit(long deptId, CreateCheckListParams props);

        /**
         * 编辑检查单 提交
         *
         * @param deptId 项目id
         * @param id     检查单id
         */
        Observable<ResponseBody> editSubmit(long deptId, long id, CreateCheckListParams props);

        /**
         * 新建检查单 保存
         *
         * @param deptId 项目id
         */
        Observable<SaveBean> createSave(long deptId, CreateCheckListParams props);

        /**
         * 编辑检查单 保存
         *
         * @param deptId 项目id
         * @param id     检查单id
         */
        Observable<ResponseBody> editSave(long deptId, long id, CreateCheckListParams props);

        /**
         * 删除检查单
         *
         * @param deptId 项目id
         * @param id     检查单id
         */
        Observable<ResponseBody> createDelete(long deptId, long id);

        /**
         * 新建检查单 提交
         *
         * @param deptId 项目id
         */
        Observable<SaveBean> createSubmitInspection(long deptId, CreateCheckListParams props);

        /**
         * 编辑检查单 提交
         *
         * @param deptId 项目id
         * @param id     检查单id
         */
        Observable<ResponseBody> editSubmitInspection(long deptId, long id, CreateCheckListParams props);

        /**
         * 新建检查单 保存
         *
         * @param deptId 项目id
         */
        Observable<SaveBean> createSaveInspection(long deptId, CreateCheckListParams props);

        /**
         * 编辑检查单 保存
         *
         * @param deptId 项目id
         * @param id     检查单id
         */
        Observable<ResponseBody> editSaveInspection(long deptId, long id, CreateCheckListParams props);

        /**
         * 删除检查单
         *
         * @param deptId 项目id
         * @param id     检查单id
         */
        Observable<ResponseBody> createDeleteInspection(long deptId, long id);

        /**
         * 验收单 新增   提交
         */
        Observable<SaveBean> createSubmitAcceptance(long deptId, CreateCheckListParams props);

        /**
         * 验收单 新增   保存
         */
        Observable<SaveBean> createSaveAcceptance(long deptId,  CreateCheckListParams props);

        /**
         * 验收单 编辑   提交
         */
        Observable<ResponseBody> editSubmitAcceptance(long deptId, long id, CreateCheckListParams props);


        /**
         * 验收单 编辑   保存
         */
        Observable<ResponseBody> editSaveAcceptance(long deptIdlong ,long id,CreateCheckListParams props);


        /**
         * 验收单 删除
         */
        Observable<ResponseBody> createDeleteAcceptance(long deptId, long id);

        /**
         * 设置当前单据类型是检查单还是验收单
         * @param typeInspection
         */
        void setInspectionType(String typeInspection);

        /**
         * 获取项目下检查单位列表
         */
        Observable<List<InspectionCompanyItem>> getInspectionCompanies(long deptId);
    }
}
