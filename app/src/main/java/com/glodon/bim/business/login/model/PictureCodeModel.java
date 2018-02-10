package com.glodon.bim.business.login.model;

import com.glodon.bim.basic.network.NetRequest;
import com.glodon.bim.business.login.bean.CheckAccountBean;
import com.glodon.bim.business.login.contract.PictureCodeContract;

import rx.Observable;

/**
 * 描述：重置密码
 * 作者：zhourf on 2017/12/27
 * 邮箱：zhourf@glodon.com
 */

public class PictureCodeModel implements PictureCodeContract.Model {

    /**
     * 验证用户是否存在
     */
    public Observable<CheckAccountBean> checkAccount(String identity){
        return NetRequest.getInstance().getCall(LoginApi.class).checkAccount(identity);
    }

    /**
     * 获取手机验证码
     */
    public Observable<CheckAccountBean> getPhoneCode(String mobile,String captcha,String signupKey){
        return NetRequest.getInstance().getCall(LoginApi.class).getPhoneCode(mobile, captcha, signupKey);
    }
}
