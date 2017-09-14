package com.glodon.bim.business.greendao.provider;

import com.glodon.bim.business.greendao.entity.UserCookie;
import com.glodon.bim.business.greendao.manager.UserCookieManager;

/**
 * 描述：
 * 作者：zhourf on 2017/9/14
 * 邮箱：zhourf@glodon.com
 */

public class DaoProvider {


    /**
     * 更新数据库的cookie
     */
    public void updateCookieDb(String userId,String cookie){
        UserCookie uc = new UserCookie();
        uc.setCookie(cookie);
        uc.setUserId(userId);
        UserCookieManager userCookieManager = new UserCookieManager();
        userCookieManager.deleteAll();
        userCookieManager.insert(uc);
    }

    /**
     * 更新数据库的cookie
     */
    public void updateCookieDb(String cookie){
        updateCookieDb("123",cookie);
    }

    /**
     * 查询cookie
     */
    public String getCookie(){
        return new UserCookieManager().queryCookie();
    }
}
