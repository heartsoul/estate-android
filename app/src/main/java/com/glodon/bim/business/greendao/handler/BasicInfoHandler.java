package com.glodon.bim.business.greendao.handler;

import com.glodon.bim.business.greendao.GreenDaoHelper;
import com.glodon.bim.business.greendao.entity.BasicInfoEntity;
import com.glodon.bim.gen.BasicInfoEntityDao;

/**
 * 描述：基础数据处理
 * 作者：zhourf on 2018/4/3
 * 邮箱：zhourf@glodon.com
 */

public class BasicInfoHandler {
    /**
     * 插入数据
     */
    public static void insert(BasicInfoEntity entity){
        BasicInfoEntityDao dao = GreenDaoHelper.getDaoSession().getBasicInfoEntityDao();
        dao.insertOrReplace(entity);
    }

    /**
     * 删除所有数据
     */
    public static void deleteAll(){
        BasicInfoEntityDao dao = GreenDaoHelper.getDaoSession().getBasicInfoEntityDao();
        dao.deleteAll();
    }

    /**
     * 删除一条数据
     */
    public static void delete(BasicInfoEntity entity){
        BasicInfoEntityDao dao = GreenDaoHelper.getDaoSession().getBasicInfoEntityDao();
        dao.delete(entity);
    }


    /**
     * 更新数据
     */
    public static void update(BasicInfoEntity entity){
        BasicInfoEntityDao dao = GreenDaoHelper.getDaoSession().getBasicInfoEntityDao();
        dao.update(entity);
    }

    /**
     * 查询
     */
    public static BasicInfoEntity query(String key){
        BasicInfoEntityDao dao = GreenDaoHelper.getDaoSession().getBasicInfoEntityDao();
        return dao.queryBuilder().where(BasicInfoEntityDao.Properties.Key.eq(key)).unique();
    }
}
