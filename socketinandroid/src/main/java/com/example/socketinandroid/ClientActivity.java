package com.example.socketinandroid;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.socketinandroid.client.Client;

public class ClientActivity extends AppCompatActivity implements Client.MsgCallback {

    private Client client;
    private EditText txt_ip;
    private EditText txt_port;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        txt_ip = (EditText) findViewById(R.id.txt_ip);
        txt_port = (EditText) findViewById(R.id.txt_port);

        client = new Client(this);
    }

    public void send(View v) {
        client.write(((EditText) findViewById(R.id.txt_input)).getText().toString());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        client.quit();
    }

    @Override
    public void msg(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((TextView) findViewById(R.id.txt_server)).setText(msg);
            }
        });
    }

    public void connect(View view) {
        new Thread() {
            public void run() {
                client.connect(txt_ip.getText().toString(), Integer.parseInt(txt_port.getText().toString()));
            }
        }.start();

    }
}
