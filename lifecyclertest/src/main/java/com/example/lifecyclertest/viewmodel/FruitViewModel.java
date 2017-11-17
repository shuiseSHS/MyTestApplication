package com.example.lifecyclertest.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.room.Room;
import android.support.annotation.NonNull;

import com.example.lifecyclertest.database.AppDataBase;
import com.example.lifecyclertest.database.Fruit;

import java.util.List;

/**
 * Created by shisong on 2017/11/8.
 */

public class FruitViewModel extends AndroidViewModel {

    private MutableLiveData<List<Fruit>> fruits;

    public FruitViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<List<Fruit>> getFruits() {
        if (fruits == null) {
            fruits = new MutableLiveData<>();
            loadFruits();
        }
        return fruits;
    }

    private void loadFruits() {
        new Thread() {
            public void run() {
                AppDataBase db = Room.databaseBuilder(getApplication(), AppDataBase.class, "database-name").build();
                List<Fruit> datas = db.fruitDao().getAll();
                fruits.postValue(datas);
            }
        }.start();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        fruits = null;
    }
}
