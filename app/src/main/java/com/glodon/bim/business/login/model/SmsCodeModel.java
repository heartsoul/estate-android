package com.glodon.bim.business.login.model;

import com.glodon.bim.basic.config.AppConfig;
import com.glodon.bim.basic.network.NetRequest;
import com.glodon.bim.business.login.bean.CheckAccountBean;
import com.glodon.bim.business.login.contract.SmsCodeContract;

import rx.Observable;

/**
 * 描述：重置密码
 * 作者：zhourf on 2017/12/27
 * 邮箱：zhourf@glodon.com
 */

public class SmsCodeModel implements SmsCodeContract.Model {

    /**
     * 验证短信验证码
     */
    public Observable<CheckAccountBean> checkSmsCode(String mobile, String verifyCode){
        return NetRequest.getInstance().getCall(AppConfig.BASE_URL,LoginApi.class).checkSmsCode(mobile, verifyCode);
    }
}
