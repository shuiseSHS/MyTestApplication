package com.example.socketinandroid;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.socketinandroid.server.MyAdapter;
import com.example.socketinandroid.server.Server;

import java.util.Date;

public class ServerActivity extends Activity implements Server.MsgCallback {

    private Server server;
    private ListView listView;
    private MyAdapter simpleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);
        listView = (ListView) findViewById(R.id.list_log);
        simpleAdapter = new MyAdapter(this);
        listView.setAdapter(simpleAdapter);
        startServer();
    }

    private void startServer() {
        server = new Server(this);
        server.init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        server.quit();
    }

    @Override
    public void number(final int num) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((TextView) findViewById(R.id.txt_server)).setText("客户端数量：" + num);
            }
        });
    }

    @Override
    public void msg(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                simpleAdapter.addData(msg);
                simpleAdapter.notifyDataSetChanged();
                listView.smoothScrollToPosition(simpleAdapter.getCount() - 1);
            }
        });
    }

    public void broadcastTime(View view) {
        if (server != null) {
            server.boardcast("北京时间：" + new Date(System.currentTimeMillis()).toString());
        }
    }
}
