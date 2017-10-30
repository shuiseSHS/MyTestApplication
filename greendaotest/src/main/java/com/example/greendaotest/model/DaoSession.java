package com.example.greendaotest.model;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.example.greendaotest.model.BaseEntity;
import com.example.greendaotest.model.user.User;
import com.example.greendaotest.model.item.Item;

import com.example.greendaotest.model.BaseEntityDao;
import com.example.greendaotest.model.user.UserDao;
import com.example.greendaotest.model.item.ItemDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig baseEntityDaoConfig;
    private final DaoConfig userDaoConfig;
    private final DaoConfig itemDaoConfig;

    private final BaseEntityDao baseEntityDao;
    private final UserDao userDao;
    private final ItemDao itemDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        baseEntityDaoConfig = daoConfigMap.get(BaseEntityDao.class).clone();
        baseEntityDaoConfig.initIdentityScope(type);

        userDaoConfig = daoConfigMap.get(UserDao.class).clone();
        userDaoConfig.initIdentityScope(type);

        itemDaoConfig = daoConfigMap.get(ItemDao.class).clone();
        itemDaoConfig.initIdentityScope(type);

        baseEntityDao = new BaseEntityDao(baseEntityDaoConfig, this);
        userDao = new UserDao(userDaoConfig, this);
        itemDao = new ItemDao(itemDaoConfig, this);

        registerDao(BaseEntity.class, baseEntityDao);
        registerDao(User.class, userDao);
        registerDao(Item.class, itemDao);
    }
    
    public void clear() {
        baseEntityDaoConfig.clearIdentityScope();
        userDaoConfig.clearIdentityScope();
        itemDaoConfig.clearIdentityScope();
    }

    public BaseEntityDao getBaseEntityDao() {
        return baseEntityDao;
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public ItemDao getItemDao() {
        return itemDao;
    }

}
