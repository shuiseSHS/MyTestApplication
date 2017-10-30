package com.example.shisong.testapplication;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ListActivity extends Activity {

    private StockItemAdapter adapter;
    private JsonObjectAdapter jsonObjectAdapter;
    private ListView listView;
    private List<StockItem> itemList = new ArrayList<>();
    private List<JSONObject> jsonList = new ArrayList<>();

    String url1 = "http://api-cn.hq.tigerbrokers.com/v1/chart/br/candle/";
    String url2 = "?appVer=1.3.0.0&period=day&count=299";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        listView = (ListView) findViewById(R.id.listView);
        getStock();
    }

    private void getStock() {
//        get(url1 + "300401" + url2);
        get("http://111.206.13.99/views/3.0/subscribe_more_recommend?current_v_uid=1211569757&from_rpage=pageInfo.statistics.rpage&page_st=similar&pg_num=2&pg_size=1&app_k=69842642483add0a63503306d63f0443&dev_os=6.0.1&dev_ua=Nexus+6&dev_hw={%22cpu%22:0,%22gpu%22:%22%22,%22mem%22:%22442.1MB%22}&net_sts=1&app_v=7.9&net_ip={%22country%22:%22%E4%B8%AD%E5%9B%BD%22,%22province%22:%22%E5%8C%97%E4%BA%AC%22,%22city%22:%22%E5%8C%97%E4%BA%AC%22,%22cc%22:%22%E8%81%94%E9%80%9A%22,%22area%22:%22%E5%8D%8E%E5%8C%97%22,%22timeout%22:0,%22respcode%22:0}&scrn_sts=0&scrn_res=1440,2392&scrn_dpi=560&qyid=355455060462832&cupid_id=355455060462832&cupid_v=3.1.004&psp_uid=&psp_cki=&psp_vip=0&secure_v=1&req_sn=1473313843078&core=1&api_v=4.2&app_t=0&platform_id=10&secure_p=GPhone&service_filter=&service_sort=&qyidv2=A315E170F9ADAF6BF02FD155F0C12453&pps=0&cupid_uid=355455060462832&pu=&psp_status=1&app_gv=&lang=zh_CN&app_lm=cn&aqyid=0_434e2904b7b0845d_9CZD9Z17Z42Z1EZ76&unlog_sub=1&profile={%22group%22:%221,2%22}&cust_count=&req_times=1");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 解析json数据
     */
    private void parseResult(String result) {
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
                            adapter = new StockItemAdapter(ListActivity.this, itemList);
                            listView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /*
     * 展示json结构
     */
    private void showJson(String result) {
        try {
            final JSONObject jo = new JSONObject(result);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    jsonObjectAdapter = new JsonObjectAdapter(ListActivity.this, jo);
                    listView.setAdapter(jsonObjectAdapter);
                    jsonObjectAdapter.notifyDataSetChanged();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void get(String url) {
        //创建okHttpClient对象
        OkHttpClient mOkHttpClient = new OkHttpClient();
        final Request request = new Request.Builder().url(url).build();
        Call call = mOkHttpClient.newCall(request);
        //请求加入调度
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                showJson(response.body().string());
            }
        });
    }
}
