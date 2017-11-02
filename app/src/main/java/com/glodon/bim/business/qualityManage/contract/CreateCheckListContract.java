package com.glodon.bim.business.qualityManage.contract;

import com.glodon.bim.base.IBasePresenter;
import com.glodon.bim.base.IBaseView;
import com.glodon.bim.business.qualityManage.bean.CompanyItem;
import com.glodon.bim.business.qualityManage.bean.CreateCheckListParams;
import com.glodon.bim.business.qualityManage.bean.ImageUploadBean;
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
    }

    interface View extends IBaseView {

        /**
         * 默认显示第一个施工单位
         */
        void showCompany(CompanyItem companyItem);

        /**
         * 展示施工单位列表
         *
         * @param mCompanyNameList       列表集合
         * @param mCompanySelectPosition 被选中的position
         */
        void showCompanyList(List<String> mCompanyNameList, int mCompanySelectPosition);

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
         * 上传图片  获取operationCode
         */
        Observable<ResponseBody> getOperationCode(String containerId, String name, String digest, long length);

        /**
         * 上传图片  上传文件
         */
        Observable<ResponseBody> uploadImage(String operationCode, RequestBody description, MultipartBody.Part file);
        Observable<ResponseBody> uploadImage(String operationCode,MultipartBody multipartBody );
    }
}
