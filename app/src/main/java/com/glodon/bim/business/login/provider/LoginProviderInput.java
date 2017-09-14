package com.glodon.bim.business.login.provider;

import com.glodon.bim.business.greendao.provider.DaoProvider;

/**
 * 描述：
 * 作者：zhourf on 2017/9/14
 * 邮箱：zhourf@glodon.com
 */

public class LoginProviderInput {

    public void updateCookieDb(String cookie){
        new DaoProvider().updateCookieDb(cookie);
    }
}
