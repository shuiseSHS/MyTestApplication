package com.example.lifecyclertest;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.OnLifecycleEvent;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.example.lifecyclertest.adapter.MyAdapter;
import com.example.lifecyclertest.database.Fruit;
import com.example.lifecyclertest.viewmodel.FruitViewModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LifecycleObserver {

    ListView listView;
    MyAdapter myAdapter;
    List<Fruit> datas = new ArrayList<>();
    FruitViewModel fruitViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.listView);
        myAdapter = new MyAdapter(this);
        listView.setAdapter(myAdapter);

        fruitViewModel = ViewModelProviders.of(this).get(FruitViewModel.class);
        fruitViewModel.getFruits().observe(this, new Observer<List<Fruit>>() {
            @Override
            public void onChanged(@Nullable List<Fruit> fruits) {
                datas = fruits;
                myAdapter.setDatas(fruits);
                myAdapter.notifyDataSetChanged();
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
            void test1() {
                Log.d("###", "ON_PAUSE1");
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
            void test2() {
                Log.d("###", "ON_PAUSE2");
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
            void test4() {
                Log.d("###", "ON_RESUME4");
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
            void test3() {
                Log.d("###", "ON_RESUME3");
            }
        });

        getLifecycle().addObserver(this);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    void test1() {
        Log.d("###", "ON_PAUSE1");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    void test2() {
        Log.d("###", "ON_PAUSE2");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    void test4() {
        Log.d("###", "ON_RESUME4");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    void test3() {
        Log.d("###", "ON_RESUME3");
    }

    public void add(View view) {
        Fruit fruit = new Fruit();
        fruit.name = "苹果";
        fruit.description = new Date().toLocaleString();
        fruit.weight = (int) (System.currentTimeMillis() % 100);
        fruitViewModel.insert(fruit);
    }

    public void delete(View view) {
        if (datas != null && datas.size() > 0) {
            Fruit fruit = datas.get(0);
            fruitViewModel.delete(fruit);
        }
    }
}
