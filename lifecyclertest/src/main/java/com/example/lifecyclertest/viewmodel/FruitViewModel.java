package com.example.lifecyclertest.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.room.Room;
import android.support.annotation.NonNull;

import com.example.lifecyclertest.database.AppDataBase;
import com.example.lifecyclertest.database.Fruit;
import com.example.lifecyclertest.database.FruitDao;

import java.util.List;

/**
 * Created by shisong on 2017/11/8.
 */
public class FruitViewModel extends AndroidViewModel {

    private MutableLiveData<List<Fruit>> fruits;

    private FruitDao mFruitDao;

    public FruitViewModel(@NonNull Application application) {
        super(application);
        AppDataBase db = Room.databaseBuilder(getApplication(), AppDataBase.class, "database-name").build();
        mFruitDao = db.fruitDao();
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
                fruits.postValue(mFruitDao.getAll());
            }
        }.start();
    }

    public void insert(final Fruit fruit) {
        new Thread() {
            public void run() {
                mFruitDao.insert(fruit);
                fruits.postValue(mFruitDao.getAll());
            }
        }.start();
    }

    public void delete(final Fruit fruit) {
        new Thread() {
            public void run() {
                mFruitDao.delete(fruit);
                fruits.postValue(mFruitDao.getAll());
            }
        }.start();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
//        fruits = null;
    }
}
