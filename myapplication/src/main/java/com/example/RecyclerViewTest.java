package com.example;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.example.myapplication.R;
import com.example.adapter.HotSearchImageAdapter;

/**
 * Created by shisong on 2017/4/1.
 */

public class RecyclerViewTest extends Activity {

    private ImageView img;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview);
        img = (ImageView) findViewById(R.id.img);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new HotSearchImageAdapter(this, null));
    }

    public void closeImage(View v) {
        img.setVisibility(View.GONE);
    }
}
