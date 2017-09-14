package com.glodon.bim.business.greendao.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import com.glodon.bim.gen.DaoSession;
import com.glodon.bim.gen.UserCookieDao;

/**
 * 描述：存储cookie
 * 作者：zhourf on 2017/9/14
 * 邮箱：zhourf@glodon.com
 */
@Entity(
        active = true
)
public class UserCookie {

    @Id(autoincrement = true)
    private Long id;

    @Property
    @NotNull
    private String userId;

    @Property
    @NotNull
    private String cookie;

/** Used to resolve relations */
@Generated(hash = 2040040024)
private transient DaoSession daoSession;

/** Used for active entity operations. */
@Generated(hash = 552016113)
private transient UserCookieDao myDao;

@Generated(hash = 2028998278)
public UserCookie(Long id, @NotNull String userId, @NotNull String cookie) {
    this.id = id;
    this.userId = userId;
    this.cookie = cookie;
}

@Generated(hash = 2011175185)
public UserCookie() {
}

public Long getId() {
    return this.id;
}

public void setId(Long id) {
    this.id = id;
}

public String getUserId() {
    return this.userId;
}

public void setUserId(String userId) {
    this.userId = userId;
}

public String getCookie() {
    return this.cookie;
}

public void setCookie(String cookie) {
    this.cookie = cookie;
}

/**
 * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
 * Entity must attached to an entity context.
 */
@Generated(hash = 128553479)
public void delete() {
    if (myDao == null) {
        throw new DaoException("Entity is detached from DAO context");
    }
    myDao.delete(this);
}

/**
 * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
 * Entity must attached to an entity context.
 */
@Generated(hash = 1942392019)
public void refresh() {
    if (myDao == null) {
        throw new DaoException("Entity is detached from DAO context");
    }
    myDao.refresh(this);
}

/**
 * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
 * Entity must attached to an entity context.
 */
@Generated(hash = 713229351)
public void update() {
    if (myDao == null) {
        throw new DaoException("Entity is detached from DAO context");
    }
    myDao.update(this);
}

/** called by internal mechanisms, do not call yourself. */
@Generated(hash = 1951555756)
public void __setDaoSession(DaoSession daoSession) {
    this.daoSession = daoSession;
    myDao = daoSession != null ? daoSession.getUserCookieDao() : null;
}

}
