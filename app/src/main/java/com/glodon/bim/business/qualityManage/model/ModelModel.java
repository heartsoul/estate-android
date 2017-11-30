package com.glodon.bim.business.qualityManage.model;

import android.text.TextUtils;

import com.glodon.bim.basic.config.AppConfig;
import com.glodon.bim.basic.network.NetRequest;
import com.glodon.bim.business.greendao.provider.DaoProvider;
import com.glodon.bim.business.qualityManage.bean.ModelListBean;
import com.glodon.bim.business.qualityManage.bean.ModelSingleListItem;
import com.glodon.bim.business.qualityManage.bean.ModelSpecialListItem;
import com.glodon.bim.business.qualityManage.bean.ProjectVersionBean;
import com.glodon.bim.business.qualityManage.contract.ModelContract;

import java.util.List;

import rx.Observable;

/**
 * 描述：选择模型
 * 作者：zhourf on 2017/11/22
 * 邮箱：zhourf@glodon.com
 */

public class ModelModel implements ModelContract.Model {
    public String cookie;

    public ModelModel() {
        cookie = new DaoProvider().getCookie();
    }

    /**
     * 查询单体列表
     *
     * @param id 项目id
     */
    @Override
    public Observable<List<ModelSingleListItem>> getSingleList(long id) {
        return NetRequest.getInstance().getCall(AppConfig.BASE_URL, ModelApi.class).getSingleList(id, cookie);
    }

    /**
     * 查询专业列表
     */
    @Override
    public Observable<List<ModelSpecialListItem>> getSpecialList() {
        return NetRequest.getInstance().getCall(AppConfig.BASE_URL, ModelApi.class).getSpecialList(cookie);
    }

    /**
     * 获取当前已发布的最新版本
     */
    @Override
    public Observable<ProjectVersionBean> getLatestVersion(long projectId) {
        return NetRequest.getInstance().getCall(AppConfig.BASE_URL, ModelApi.class).getLatestVersion(projectId, cookie);
    }

    /**
     * 根据专业和单体查询模型列表
     *
     * @param projectId        项目id
     * @param projectVersionId 最新版本
     * @param buildingId       单体
     * @param specialtyCode    专业
     */
    @Override
    public Observable<ModelListBean> getModelList(long projectId, String projectVersionId, long buildingId, String specialtyCode) {
        if (buildingId != 0 && !TextUtils.isEmpty(specialtyCode)) {
            return NetRequest.getInstance().getCall(AppConfig.BASE_URL, ModelApi.class).getModelList(projectId, projectVersionId, buildingId, specialtyCode, cookie);
        } else if (buildingId != 0) {
            return NetRequest.getInstance().getCall(AppConfig.BASE_URL, ModelApi.class).getModelList(projectId, projectVersionId, buildingId, cookie);
        } else if (!TextUtils.isEmpty(specialtyCode)) {
            return NetRequest.getInstance().getCall(AppConfig.BASE_URL, ModelApi.class).getModelList(projectId, projectVersionId, specialtyCode, cookie);
        }
        return null;
    }

}
