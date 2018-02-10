package com.glodon.bim.business.equipment.model;

import com.glodon.bim.basic.network.NetRequest;
import com.glodon.bim.basic.utils.SharedPreferencesUtil;
import com.glodon.bim.business.equipment.bean.CreateEquipmentBean;
import com.glodon.bim.business.equipment.bean.CreateEquipmentParams;
import com.glodon.bim.business.equipment.bean.EquipmentDetailBean;
import com.glodon.bim.business.equipment.contract.CreateEquipmentContract;
import com.glodon.bim.business.greendao.provider.DaoProvider;

import okhttp3.ResponseBody;
import rx.Observable;

/**
 * 描述：创建材设进场记录-提交页面
 * 作者：zhourf on 2018/1/9
 * 邮箱：zhourf@glodon.com
 */

public class CreateEquipmentModel implements CreateEquipmentContract.Model{
    private String cookie;
    private long projectId;

    public CreateEquipmentModel() {
        cookie = new DaoProvider().getCookie();
        projectId = SharedPreferencesUtil.getProjectId();
    }

    /**
     * 新增保存
     */
    public Observable<CreateEquipmentBean> newSave(CreateEquipmentParams props){
        return NetRequest.getInstance().getCall(CreateEquipmentApi.class).newSave(projectId,props,cookie);
    }

    /**
     * 新增提交
     */
    public Observable<CreateEquipmentBean> newSubmit(CreateEquipmentParams props){
        return NetRequest.getInstance().getCall(CreateEquipmentApi.class).newSubmit(projectId,props,cookie);
    }

    /**
     * 编辑保存
     */
    public Observable<ResponseBody> editSave(long id, CreateEquipmentParams props){
        return NetRequest.getInstance().getCall(CreateEquipmentApi.class).editSave(projectId,id,props,cookie);
    }
    /**
     * 编辑提交
     */
    public Observable<ResponseBody> editSubmit(long id, CreateEquipmentParams props){
        return NetRequest.getInstance().getCall(CreateEquipmentApi.class).editSubmit(projectId,id,props,cookie);
    }

    /**
     * 删除
     */
    public Observable<ResponseBody> delete(long id){
        return NetRequest.getInstance().getCall(CreateEquipmentApi.class).delete(projectId,id,cookie);
    }

    /**
     * 根据id查询详情和保存后的编辑信息
     */
    public Observable<EquipmentDetailBean> detail(long id){
        return NetRequest.getInstance().getCall(CreateEquipmentApi.class).detail(projectId,id,cookie);
    }
}
