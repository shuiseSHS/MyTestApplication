package com.example.shisong.testapplication;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class StockActivity extends Activity {

    private EditText editText;

    private StockItemAdapter adapter;

    private boolean exit = false;

    private List<StockItem> itemList = new ArrayList<>();

    String url1 = "http://api-cn.hq.tigerbrokers.com/v1/chart/br/candle/";
    String url2 = "?appVer=1.3.0.0&period=day&count=299";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);
        editText = (EditText) findViewById(R.id.editText);
        adapter = new StockItemAdapter(this, itemList);
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
    }

    public void getStock(View v) {
        itemList.clear();
        adapter.notifyDataSetChanged();

        new Thread(){
            public void run() {
//                for (int i = 0; i < 1; i ++) {
                if (exit) {
                    return;
                }
                String stockId = editText.getText().toString();
                if (stockId.length() == 0) {
                    stockId = "300401";
                }
                try {
                    URL url = new URL(url1 + stockId + url2);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();
                    String sLine;
                    if (connection.getResponseCode() == 200) {
                        InputStream is = connection.getInputStream();
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
                        while ((sLine = bufferedReader.readLine()) != null) {
                            sb.append(sLine);
                        }
                        is.close();
                    }
                    Log.d("###", sb.toString());
                    parseResult(sb.toString());
                    Thread.sleep(3000);
                } catch (Exception e) {
                    e.printStackTrace();
                    parseResult(e.getMessage());
                }
//                }
            }
        }.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        exit = true;
    }

    private void parseResult(final String result) {
        try {
            JSONObject jo = new JSONObject(result);
            if (jo.has("items")) {
                JSONArray items = jo.getJSONArray("items");
                StockItem lastItem = null;
                for (int i = 0; i < items.length(); i++) {
                    StockItem curItem = new StockItem(items.getString(i), lastItem);
                    itemList.add(curItem);
                    lastItem = curItem;
                }
                if (itemList.size() > 0) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                        }
                    });
                    return;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(StockActivity.this, result, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
