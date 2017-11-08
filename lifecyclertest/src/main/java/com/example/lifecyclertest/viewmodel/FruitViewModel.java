package com.example.lifecyclertest.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.persistence.room.Room;
import android.content.Context;

import com.example.lifecyclertest.database.AppDataBase;
import com.example.lifecyclertest.database.Fruit;

import java.util.List;

/**
 * Created by shisong on 2017/11/8.
 */

public class FruitViewModel extends ViewModel {

    private MutableLiveData<List<Fruit>> fruits;

    private Context mContext;

    public MutableLiveData<List<Fruit>> getFruits(Context context) {
        mContext = context;
        if (fruits == null) {
            fruits = new MutableLiveData<>();
//            loadFruits(context);
        }
        return fruits;
    }

    private void loadFruits(Context context) {
        AppDataBase db = Room.databaseBuilder(context, AppDataBase.class, "database-name").build();
        List<Fruit> datas = db.fruitDao().getAll();
        fruits.setValue(datas);
    }
}
