package com.example.lifecyclertest;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.persistence.room.Room;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import com.example.lifecyclertest.adapter.MyAdapter;
import com.example.lifecyclertest.database.AppDataBase;
import com.example.lifecyclertest.database.Fruit;
import com.example.lifecyclertest.database.FruitDao;
import com.example.lifecyclertest.viewmodel.FruitViewModel;

import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements Observer<List<Fruit>> {

    ListView listView;
    MyAdapter myAdapter;
    FruitDao fruitDao;
    List<Fruit> datas;
    MutableLiveData<List<Fruit>> listLiveData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.listView);
        myAdapter = new MyAdapter(this);
        listView.setAdapter(myAdapter);

        FruitViewModel fruitViewModel = ViewModelProviders.of(this).get(FruitViewModel.class);
        listLiveData = fruitViewModel.getFruits();
        listLiveData.observe(this, this);

        AppDataBase db = Room.databaseBuilder(getApplicationContext(), AppDataBase.class, "database-name").build();
        fruitDao = db.fruitDao();
    }

    public void add(View view) {
        new Thread() {
            public void run() {
                Fruit fruit = new Fruit();
                fruit.name = "苹果";
                fruit.description = new Date().toLocaleString();
                fruit.weight = (int) (System.currentTimeMillis() % 100);
                fruitDao.insert(fruit);
                update();
            }
        }.start();
    }

    public void delete(View view) {
        new Thread() {
            public void run() {
                if (datas != null && datas.size() > 0) {
                    Fruit fruit = datas.get(0);
                    fruitDao.delete(fruit);
                    update();
                }
            }
        }.start();
    }

    private void update() {
        datas = fruitDao.getAll();
        listLiveData.postValue(datas);
    }

    @Override
    public void onChanged(@Nullable List<Fruit> fruits) {
        myAdapter.setDatas(fruits);
        myAdapter.notifyDataSetChanged();
    }
}
