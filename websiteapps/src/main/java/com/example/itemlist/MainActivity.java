package com.example.itemlist;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.example.itemlist.adapter.ContentParser;
import com.example.itemlist.adapter.RecentAddParser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends Activity {

    private int currentPage = 1;
    private RewardFeeAdapter adapter;
    private ContentParser contentParser;
    private ProgressBar progressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.grid);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RewardFeeAdapter(this);
        recyclerView.setAdapter(adapter);

        contentParser = new RecentAddParser();
        contentParser.setKeyword("人民的名义");
        loadData(currentPage);
    }

    private void loadData(final int page) {
        adapter.setData(null);
        adapter.notifyDataSetChanged();
        progressbar.setVisibility(View.VISIBLE);
        new Thread() {
            public void run() {
                try {
                    OkHttpClient mOkHttpClient = new OkHttpClient();
                    final Request request = new Request.Builder().url(contentParser.getPageUrl(page))
                            .header("User-Agent", "OkHttp Headers.java")
                            .build();
                    Call call = mOkHttpClient.newCall(request);
                    Response response = call.execute();
                    String result = response.body().string();
                    Document doc = Jsoup.parse(result);
                    Elements videos = doc.getElementsByClass("result c-container ");
                    List<DataItem> datas = new ArrayList<>();
                    for (Element video : videos) {
                        DataItem data = new DataItem();
                        data.title = video.getElementsByTag("a").text();
                        data.url = video.getElementsByTag("a").attr("href");
                        datas.add(data);
                    }
                    adapter.setData(datas);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                            progressbar.setVisibility(View.GONE);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    progressbar.setVisibility(View.VISIBLE);
                }
            }
        }.start();
    }

    public void nextPage(View v) {
        currentPage++;
        loadData(currentPage);
    }

    public void prePage(View v) {
        if (currentPage > 1) {
            currentPage--;
            loadData(currentPage);
        }
    }
}
