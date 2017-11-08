package com.example.lifecyclertest;

import android.arch.persistence.room.Room;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import com.example.lifecyclertest.adapter.MyAdapter;
import com.example.lifecyclertest.database.AppDataBase;
import com.example.lifecyclertest.database.Fruit;
import com.example.lifecyclertest.database.FruitDao;

import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    MyAdapter myAdapter;
    FruitDao fruitDao;
    List<Fruit> datas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.listView);
        myAdapter = new MyAdapter(this);
        listView.setAdapter(myAdapter);

        AppDataBase db = Room.databaseBuilder(getApplicationContext(), AppDataBase.class, "database-name").build();
        fruitDao = db.fruitDao();
        new Thread() {
            public void run() {
                update();
            }
        }.start();
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
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                myAdapter.setDatas(datas);
                myAdapter.notifyDataSetChanged();
            }
        });
    }
}
