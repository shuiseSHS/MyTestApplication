package com.example.shortcuttest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class TestActivity extends AppCompatActivity {

    private View fill_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        fill_view = findViewById(R.id.fill_view);
        int color = getIntent().getIntExtra("bg", 0xFFFFFFFF);
        fill_view.setBackgroundColor(color);
    }
}
