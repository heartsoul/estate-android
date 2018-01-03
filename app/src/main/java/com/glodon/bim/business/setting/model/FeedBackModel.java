package com.glodon.bim.business.setting.model;

import com.glodon.bim.basic.config.AppConfig;
import com.glodon.bim.basic.network.NetRequest;
import com.glodon.bim.business.setting.bean.FeedBackBean;
import com.glodon.bim.business.setting.bean.FeedBackParams;
import com.glodon.bim.business.setting.contract.FeedBackContract;

import rx.Observable;

/**
 * 描述：意见反馈
 * 作者：zhourf on 2017/12/6
 * 邮箱：zhourf@glodon.com
 */

public class FeedBackModel implements FeedBackContract.Model{
    public Observable<FeedBackBean> addFeedBack(FeedBackParams props){
        return NetRequest.getInstance().getCall(AppConfig.BASE_URL,FeedBackApi.class).addFeedBack(props);
    }
}
