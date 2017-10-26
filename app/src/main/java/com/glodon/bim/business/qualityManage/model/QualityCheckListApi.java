package com.glodon.bim.business.qualityManage.model;

import com.glodon.bim.business.qualityManage.bean.QualityCheckListBean;
import com.glodon.bim.common.login.User;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;

/**
 * 描述：质检清单接口
 * 作者：zhourf on 2017/9/11
 * 邮箱：zhourf@glodon.com
 */

public interface QualityCheckListApi {



    /**
     * 获取质检清单
     */
    @GET("quality/{deptId}/qualityInspection")
    Observable<QualityCheckListBean> getQualityCheckList(@Path("deptId") long deptId, @Query("page") int page, @Query("size") int size,@Header("cookie") String cookie);
}
