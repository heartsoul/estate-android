package com.glodon.bim.business.greendao.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import com.glodon.bim.gen.DaoSession;
import com.glodon.bim.gen.BasicInfoEntityDao;

/**
 * 描述：存储基础数据
 * 作者：zhourf on 2018/4/3
 * 邮箱：zhourf@glodon.com
 */
@Entity(
        active = true
)
public class BasicInfoEntity {
    @Id(autoincrement = true)
    private Long id;

    @Property
    @NotNull
    private String key;

    @Property
    @NotNull
    private String value;

/** Used to resolve relations */
@Generated(hash = 2040040024)
private transient DaoSession daoSession;

/** Used for active entity operations. */
@Generated(hash = 2141189073)
private transient BasicInfoEntityDao myDao;

@Generated(hash = 1972027313)
public BasicInfoEntity(Long id, @NotNull String key, @NotNull String value) {
    this.id = id;
    this.key = key;
    this.value = value;
}

@Generated(hash = 431394679)
public BasicInfoEntity() {
}

public Long getId() {
    return this.id;
}

public void setId(Long id) {
    this.id = id;
}

public String getKey() {
    return this.key;
}

public void setKey(String key) {
    this.key = key;
}

public String getValue() {
    return this.value;
}

public void setValue(String value) {
    this.value = value;
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
@Generated(hash = 989732725)
public void __setDaoSession(DaoSession daoSession) {
    this.daoSession = daoSession;
    myDao = daoSession != null ? daoSession.getBasicInfoEntityDao() : null;
}
}
