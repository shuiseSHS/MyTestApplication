package com.example.greendaotest.model.user;

import com.example.greendaotest.model.BaseEntity;
import com.example.greendaotest.model.DaoSession;
import com.example.greendaotest.model.item.Item;
import com.example.greendaotest.model.item.ItemDao;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Transient;

/**
 * Created by shisong on 2017/2/13.
 */
@Entity
public class User extends BaseEntity {

    private String name;

    private int age;

    @Transient
    private int count;

    private long itemId;

    @ToOne(joinProperty = "itemId")
    private Item item;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 1507654846)
    private transient UserDao myDao;

    @Generated(hash = 1941164373)
    public User(String name, int age, long itemId) {
        this.name = name;
        this.age = age;
        this.itemId = itemId;
    }

    @Generated(hash = 586692638)
    public User() {
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return this.age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public long getItemId() {
        return this.itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

    @Generated(hash = 1864644456)
    private transient Long item__resolvedKey;

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1320685112)
    public Item getItem() {
        long __key = this.itemId;
        if (item__resolvedKey == null || !item__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ItemDao targetDao = daoSession.getItemDao();
            Item itemNew = targetDao.load(__key);
            synchronized (this) {
                item = itemNew;
                item__resolvedKey = __key;
            }
        }
        return item;
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
    @Generated(hash = 2059241980)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getUserDao() : null;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1075419736)
    public void setItem(@NotNull Item item) {
        if (item == null) {
            throw new DaoException(
                    "To-one property 'itemId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.item = item;
            itemId = item.getId();
            item__resolvedKey = itemId;
        }
    }
}
