package com.example.greendaotest.model.item;

import android.graphics.Bitmap;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by shisong on 2017/2/14.
 */
@Entity
public class Item {
    @Id
    private Long id;

    String name = "东西";

    Bitmap blob;

    @Generated(hash = 1532416947)
    public Item(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Generated(hash = 1470900980)
    public Item() {
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
