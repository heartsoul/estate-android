package com.glodon.bim.business.qualityManage.model;

import com.glodon.bim.basic.network.NetRequest;
import com.glodon.bim.business.greendao.provider.DaoProvider;
import com.glodon.bim.business.qualityManage.bean.CompanyItem;
import com.glodon.bim.business.qualityManage.bean.CreateCheckListParams;
import com.glodon.bim.business.qualityManage.bean.InspectionCompanyItem;
import com.glodon.bim.business.qualityManage.bean.ModelElementInfo;
import com.glodon.bim.business.qualityManage.bean.PersonItem;
import com.glodon.bim.business.qualityManage.bean.SaveBean;
import com.glodon.bim.business.qualityManage.contract.CreateCheckListContract;
import com.glodon.bim.common.config.CommonConfig;

import java.util.List;

import okhttp3.ResponseBody;
import rx.Observable;

/**
 * 描述：新建检查单
 * 作者：zhourf on 2017/10/26
 * 邮箱：zhourf@glodon.com
 */

public class CreateCheckListModel implements CreateCheckListContract.Model {

    private String mInspectionType;

    /**
     * 获取施工单位列表
     *
     * @param id            项目id
     * @param deptTypeEnums 监理JLDW  业主JSDW     (SJDW SGDW)
     */
    @Override
    public Observable<List<CompanyItem>> getCompaniesList(long id, List<String> deptTypeEnums) {
        return NetRequest.getInstance().getCall( CreateCheckListApi.class).getCompaniesList(id, deptTypeEnums, new DaoProvider().getCookie());
    }

    /**
     * 获取责任人列表
     *
     * @param id               项目id
     * @param coperationCorpId 施工单位id
     */
    @Override
    public Observable<List<PersonItem>> gePersonList(long id, long coperationCorpId) {
        return NetRequest.getInstance().getCall( CreateCheckListApi.class).getPersonList(id, coperationCorpId, true, new DaoProvider().getCookie());
    }

    /**
     * 新增提交
     *
     * @param deptId 项目id
     */
    @Override
    public Observable<SaveBean> createSubmit(long deptId, CreateCheckListParams props) {
        switch (mInspectionType) {
            case CommonConfig.TYPE_INSPECTION:
                return createSubmitInspection(deptId, props);
            case CommonConfig.TYPE_ACCEPTANCE:
                return createSubmitAcceptance(deptId, props);
        }
        return null;
    }

    /**
     * 新增 保存
     *
     * @param deptId 项目id
     */
    @Override
    public Observable<SaveBean> createSave(long deptId, CreateCheckListParams props) {
        switch (mInspectionType) {
            case CommonConfig.TYPE_INSPECTION:
                return createSaveInspection(deptId, props);
            case CommonConfig.TYPE_ACCEPTANCE:
                return createSaveAcceptance(deptId, props);
        }
        return null;
    }

    /**
     * 编辑   提交
     */
    @Override
    public Observable<ResponseBody> editSubmit(long deptId, long id, CreateCheckListParams props) {
        switch (mInspectionType) {
            case CommonConfig.TYPE_INSPECTION:
                return editSubmitInspection(deptId, id, props);
            case CommonConfig.TYPE_ACCEPTANCE:
                return editSubmitAcceptance(deptId, id, props);
        }
        return null;
    }


    /**
     * 编辑   保存
     */
    @Override
    public Observable<ResponseBody> editSave(long deptId, long id, CreateCheckListParams props) {
        switch (mInspectionType) {
            case CommonConfig.TYPE_INSPECTION:
                return editSaveInspection(deptId, id, props);
            case CommonConfig.TYPE_ACCEPTANCE:
                return editSaveAcceptance(deptId, id, props);
        }
        return null;
    }

    /**
     * 删除
     */
    @Override
    public Observable<ResponseBody> createDelete(long deptId, long id) {
        switch (mInspectionType) {
            case CommonConfig.TYPE_INSPECTION:
                return createDeleteInspection(deptId, id);
            case CommonConfig.TYPE_ACCEPTANCE:
                return createDeleteAcceptance(deptId, id);
        }
        return null;
    }

    /**
     * 检查单 新增提交
     *
     * @param deptId 项目id
     */
    @Override
    public Observable<SaveBean> createSubmitInspection(long deptId, CreateCheckListParams props) {
        return NetRequest.getInstance().getCall( CreateCheckListApi.class).createSubmitInspection(deptId, props, new DaoProvider().getCookie());
    }

    /**
     * 检查单 新增 保存
     *
     * @param deptId 项目id
     */
    @Override
    public Observable<SaveBean> createSaveInspection(long deptId, CreateCheckListParams props) {
        return NetRequest.getInstance().getCall( CreateCheckListApi.class).createSaveInspection(deptId, props, new DaoProvider().getCookie());
    }

    /**
     * 检查单 编辑   提交
     */
    @Override
    public Observable<ResponseBody> editSubmitInspection(long deptId, long id, CreateCheckListParams props) {
        return NetRequest.getInstance().getCall( CreateCheckListApi.class).editSubmitInspection(deptId, id, props, new DaoProvider().getCookie());
    }


    /**
     * 检查单 编辑   保存
     */
    @Override
    public Observable<ResponseBody> editSaveInspection(long deptId, long id, CreateCheckListParams props) {
        return NetRequest.getInstance().getCall( CreateCheckListApi.class).editSaveInspection(deptId, id, props, new DaoProvider().getCookie());
    }

    /**
     * 检查单 删除
     */
    @Override
    public Observable<ResponseBody> createDeleteInspection(long deptId, long id) {
        return NetRequest.getInstance().getCall( CreateCheckListApi.class).createDeleteInspection(deptId, id, new DaoProvider().getCookie());
    }

    /**
     * 验收单 新增   提交
     */
    @Override
    public Observable<SaveBean> createSubmitAcceptance(long deptId, CreateCheckListParams props) {
        return NetRequest.getInstance().getCall( CreateCheckListApi.class).createSubmitAcceptance(deptId, props, new DaoProvider().getCookie());
    }

    /**
     * 验收单 新增   保存
     */
    @Override
    public Observable<SaveBean> createSaveAcceptance(long deptId, CreateCheckListParams props) {
        return NetRequest.getInstance().getCall( CreateCheckListApi.class).createSaveAcceptance(deptId, props, new DaoProvider().getCookie());
    }

    /**
     * 验收单 编辑   提交
     */
    @Override
    public Observable<ResponseBody> editSubmitAcceptance(long deptId, long id, CreateCheckListParams props) {
        return NetRequest.getInstance().getCall( CreateCheckListApi.class).editSubmitAcceptance(deptId, id, props, new DaoProvider().getCookie());
    }


    /**
     * 验收单 编辑   保存
     */
    @Override
    public Observable<ResponseBody> editSaveAcceptance(long deptId, long id, CreateCheckListParams props) {
        return NetRequest.getInstance().getCall(CreateCheckListApi.class).editSaveAcceptance(deptId, id, props, new DaoProvider().getCookie());
    }


    /**
     * 验收单 删除
     */
    @Override
    public Observable<ResponseBody> createDeleteAcceptance(long deptId, long id) {
        return NetRequest.getInstance().getCall( CreateCheckListApi.class).createDeleteAcceptance(deptId, id, new DaoProvider().getCookie());
    }


    @Override
    public void setInspectionType(String typeInspection) {
        mInspectionType = typeInspection;
    }

    /**
     * 获取项目下检查单位列表
     */
    @Override
    public Observable<List<InspectionCompanyItem>> getInspectionCompanies( long deptId){
        return NetRequest.getInstance().getCall(CreateCheckListApi.class).getInspectionCompanies(deptId,new DaoProvider().getCookie());
    }

    /**
     * 根据构件id获取构件名称
     */
    public Observable<ModelElementInfo> getElementProperty(long projectId,String versionId,String fileId,String elementId){
        return NetRequest.getInstance().getCall(CreateCheckListApi.class).getElementProperty(projectId,versionId,fileId,elementId,new DaoProvider().getCookie());
    }
}
