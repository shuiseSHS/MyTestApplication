package com.example.lifecyclertest.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

/**
 * Created by shisong on 2017/11/8.
 */
@Database(entities = {Fruit.class}, version = 1, exportSchema = false)
public abstract class AppDataBase extends RoomDatabase {

    public abstract FruitDao fruitDao();

}
