package com.example.lifecyclertest.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by shisong on 2017/11/8.
 */
@Entity
public class Fruit {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;

    @ColumnInfo(name = "desc")
    public String description;

    public int weight;

    @Override
    public String toString() {
        return id + " " + name + " " + description + " " + weight;
    }
}
