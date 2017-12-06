package com.glodon.bim.business.setting.model;

import com.glodon.bim.business.setting.bean.FeedBackBean;
import com.glodon.bim.business.setting.bean.FeedBackParams;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * 描述：意见反馈
 * 作者：zhourf on 2017/12/6
 * 邮箱：zhourf@glodon.com
 */

public interface FeedBackApi {

    @POST("/feedbacks")
    Observable<FeedBackBean> addFeedBack(@Body FeedBackParams props);
    @POST("/feedbacks")
    Call<ResponseBody> addFeedBack2(@Body FeedBackParams props);

}
