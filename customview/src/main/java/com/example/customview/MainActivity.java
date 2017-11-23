package com.example.customview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void toCloseView(View view) {
        startActivity(new Intent(this, IconCloseActivity.class));
    }

    public void toArrowView(View view) {
        startActivity(new Intent(this, IconArrowActivity.class));
    }
}
