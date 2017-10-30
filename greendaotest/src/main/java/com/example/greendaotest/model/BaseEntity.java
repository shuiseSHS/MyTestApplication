package com.example.greendaotest.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by shisong on 2017/2/14.
 */
@Entity
public class BaseEntity {
    @Id
    private Long id = 0L;

    @Generated(hash = 2062308837)
    public BaseEntity(Long id) {
        this.id = id;
    }

    @Generated(hash = 943368157)
    public BaseEntity() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
