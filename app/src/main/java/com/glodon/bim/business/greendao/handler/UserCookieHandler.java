package com.glodon.bim.business.greendao.handler;

import com.glodon.bim.business.greendao.GreenDaoHelper;
import com.glodon.bim.business.greendao.entity.UserCookie;
import com.glodon.bim.gen.UserCookieDao;

import java.util.List;

/**
 * 描述：cookie管理类
 * 作者：zhourf on 2017/9/14
 * 邮箱：zhourf@glodon.com
 */

public class UserCookieHandler {

    /**
     * 插入数据
     */
    public void insert(UserCookie cookie){
        UserCookieDao userCookieDao = GreenDaoHelper.getDaoSession().getUserCookieDao();
        userCookieDao.insert(cookie);
    }

    /**
     * 删除所有数据
     */
    public void deleteAll(){
        UserCookieDao userCookieDao = GreenDaoHelper.getDaoSession().getUserCookieDao();
        userCookieDao.deleteAll();
    }

    /**
     * 更新数据
     */
    public void update(UserCookie cookie){
        UserCookieDao userCookieDao = GreenDaoHelper.getDaoSession().getUserCookieDao();
        userCookieDao.update(cookie);
    }

    /**
     * 查询
     */
    public UserCookie query(){
        UserCookieDao userCookieDao = GreenDaoHelper.getDaoSession().getUserCookieDao();
        List<UserCookie> userCookies = userCookieDao.loadAll();
        if(userCookies!=null && userCookies.size()>0){
            return userCookies.get(0);
        }
        return null;
    }

    /**
     * 查询cookie
     */
    public String queryCookie(){
        UserCookieDao userCookieDao = GreenDaoHelper.getDaoSession().getUserCookieDao();
        List<UserCookie> userCookies = userCookieDao.loadAll();
        if(userCookies!=null && userCookies.size()>0){
            return userCookies.get(0).getCookie();
        }
        return null;
    }
}
