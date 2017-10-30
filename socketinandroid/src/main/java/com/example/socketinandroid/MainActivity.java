package com.example.socketinandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void toClient(View view) {
        startActivity(new Intent(this, ClientActivity.class));
    }

    public void toServer(View view) {
        startActivity(new Intent(this, ServerActivity.class));
    }
}
