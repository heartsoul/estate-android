package com.glodon.bim.business.qualityManage.model;

import com.glodon.bim.basic.config.AppConfig;
import com.glodon.bim.basic.network.NetRequest;
import com.glodon.bim.business.greendao.provider.DaoProvider;
import com.glodon.bim.business.qualityManage.bean.CompanyItem;
import com.glodon.bim.business.qualityManage.bean.CreateCheckListParams;
import com.glodon.bim.business.qualityManage.bean.PersonItem;
import com.glodon.bim.business.qualityManage.bean.SaveBean;
import com.glodon.bim.business.qualityManage.contract.CreateCheckListContract;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import rx.Observable;

/**
 * 描述：新建检查单
 * 作者：zhourf on 2017/10/26
 * 邮箱：zhourf@glodon.com
 */

public class CreateCheckListModel implements CreateCheckListContract.Model {

    /**
     * 获取施工单位列表
     * @param id            项目id
     * @param deptTypeEnums 监理JLDW  业主JSDW     (SJDW SGDW)
     */
    @Override
    public Observable<List<CompanyItem>> getCompaniesList(long id,  List<String> deptTypeEnums){
        return NetRequest.getInstance().getCall(AppConfig.BASE_URL,CreateCheckListApi.class).getCompaniesList(id,deptTypeEnums,new DaoProvider().getCookie());
    }

    /**
     * 获取责任人列表
     * @param id               项目id
     * @param coperationCorpId 施工单位id
     */
    @Override
    public Observable<List<PersonItem>> gePersonList(long id, long coperationCorpId){
        return NetRequest.getInstance().getCall(AppConfig.BASE_URL,CreateCheckListApi.class).getPersonList(id,coperationCorpId,new DaoProvider().getCookie());
    }

    /**
     * 新增提交
     * @param deptId 项目id
     */
    @Override
    public Observable<SaveBean> createSubmit(long deptId,CreateCheckListParams props){
        return NetRequest.getInstance().getCall(AppConfig.BASE_URL,CreateCheckListApi.class).createSubmit(deptId,props,new DaoProvider().getCookie());
    }

    /**
     * 新增 保存
     * @param deptId 项目id
     */
    @Override
    public Observable<SaveBean> createSave(long deptId, CreateCheckListParams props){
        return NetRequest.getInstance().getCall(AppConfig.BASE_URL,CreateCheckListApi.class).createSave(deptId,props,new DaoProvider().getCookie());
    }

    /**
     * 检查单 编辑   提交
     */
    @Override
    public Observable<ResponseBody> editSubmit(long deptId, long id, CreateCheckListParams props){
        return NetRequest.getInstance().getCall(AppConfig.BASE_URL,CreateCheckListApi.class).editSubmit(deptId,id,props,new DaoProvider().getCookie());
    }


    /**
     * 检查单 编辑   保存
     */
    @Override
    public Observable<ResponseBody> editSave(long deptId, long id,  CreateCheckListParams props){
        return NetRequest.getInstance().getCall(AppConfig.BASE_URL,CreateCheckListApi.class).editSave(deptId,id,props,new DaoProvider().getCookie());
    }

    /**
     * 删除
     */
    @Override
    public Observable<ResponseBody> createDelete(long deptId, long id){
        return NetRequest.getInstance().getCall(AppConfig.BASE_URL,CreateCheckListApi.class).createDelete(deptId,id,new DaoProvider().getCookie());
    }

//    /**
//     * 上传图片  获取operationCode
//     */
//    @Override
//    public Observable<ResponseBody> getOperationCode(String containerId, String name, String digest, long length){
//        return NetRequest.getInstance().getCall(AppConfig.BASE_URL,CreateCheckListApi.class).getOperationCode(containerId, name, digest, length, new DaoProvider().getCookie());
//    }
//
//    /**
//     * 上传图片  上传文件
//     */
//    @Override
//    public  Observable<ResponseBody> uploadImage(String operationCode,RequestBody description,MultipartBody.Part file){
//        return NetRequest.getInstance().getCall(AppConfig.BASE_UPLOAD_URL,CreateCheckListApi.class).uploadImage("objects?operationCode="+operationCode,description,file);
//    }
//
//    /**
//     * 上传图片  上传文件
//     */
//    @Override
//    public  Observable<ResponseBody> uploadImage(String operationCode,MultipartBody multipartBody ){
//        return NetRequest.getInstance().getCall(AppConfig.BASE_UPLOAD_URL,CreateCheckListApi.class).uploadImage("objects?operationCode="+operationCode,multipartBody);
//    }

}
