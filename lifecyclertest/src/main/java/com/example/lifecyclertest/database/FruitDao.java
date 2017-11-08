package com.example.lifecyclertest.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by shisong on 2017/11/8.
 */
@Dao
public interface FruitDao {

    @Query("select * from fruit")
    List<Fruit> getAll();

    @Query("select * from fruit where id in (:ids)")
    List<Fruit> getAllByIds(int[] ids);

    @Insert
    void insert(Fruit fruit);

    @Delete
    void delete(Fruit fruit);
}
