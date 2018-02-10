package com.glodon.bim.business.login.model;

import com.glodon.bim.basic.network.NetRequest;
import com.glodon.bim.business.login.bean.CheckAccountBean;
import com.glodon.bim.business.login.contract.ResetPasswordContract;

import rx.Observable;

/**
 * 描述：重置密码
 * 作者：zhourf on 2017/12/27
 * 邮箱：zhourf@glodon.com
 */

public class ResetPasswordModel implements ResetPasswordContract.Model {

    /**
     * 重置密码
     */
    public Observable<CheckAccountBean> resetPwd(String mobile, String code, String pwd){
        return NetRequest.getInstance().getCall(LoginApi.class).resetPwd(mobile, code, pwd);
    }
}
