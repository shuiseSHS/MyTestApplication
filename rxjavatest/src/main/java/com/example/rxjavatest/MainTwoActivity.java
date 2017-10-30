package com.example.rxjavatest;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@SuppressWarnings({"unused", "deprecation"})
public class MainTwoActivity extends Activity {

    private static final String TAG = "###";

    private static final String imgUrl = "https://www.baidu.com/img/bd_logo1.png";

    private static final String jsonUrl = "http://search.video.iqiyi.com/m?if=defaultQuery&platform=10&is_qipu_platform=1&u=scyrzfrc5g3am5embdvluy4p7g45cygd&response_type=0&callback=&cb=&language=0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        getImage();
        getImageRx();
        getTitleRx();

        testURL();
    }

    private void testURL() {
        String url = "?s_itype=0&s_qr=0&s2=&p2=&s_ilayer=1&s_source=history&mod=tw_s&s_sr=&s_rq=vvh？?&s_e=720dfa1e2b00458e926438daf1f02a38&s_st=5&s_r=vvh？?&s_bkt=onetree_E&s_mode=1&s_relqlist=&purl=&s_page=1&rfr=&s3=&s_subsrc=&s4=";
        Uri uri = Uri.parse(url);
        for (String s : uri.getQueryParameterNames()) {
            Log.e(TAG, s + ": " + uri.getQueryParameter(s));
        }
    }

    public void getImageRx() {
        Observable.just(imgUrl)
                .map(this::getBitmap)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).
                subscribe(bitmap -> {
                            Log.e(TAG, Thread.currentThread().toString() + "setBackgroundDrawable");
                            findViewById(R.id.img_icon).setBackgroundDrawable(new BitmapDrawable(bitmap));
                        },
                        exception -> Log.e(TAG, exception.getMessage()));
    }

    public void getTitleRx() {
        Observable.just(jsonUrl)
                .subscribeOn(Schedulers.computation())
                .map(this::wrapParams)
                .observeOn(Schedulers.io())
                .map(this::getHttpResponse1)
                .observeOn(Schedulers.immediate())
                .map(this::parseCard)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(card -> ((TextView) findViewById(R.id.txt)).setText(card.title),
                        e -> Log.e(TAG, "OnError: " + e.getLocalizedMessage()));
    }

    private String wrapParams(String url) {
        Log.e(TAG, Thread.currentThread().toString() + "wrapUrl");
        return url;
    }

    private JSONObject getHttpResponse(String jsonUrl) {
        Log.e(TAG, Thread.currentThread().toString() + "getJsonObject");
        try {
            JSONObject jsonObject = null;
            URL url = new URL(jsonUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            if (200 == con.getResponseCode()) {
                StringBuilder sb = new StringBuilder();
                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                jsonObject = new JSONObject(sb.toString());
            }
            return jsonObject;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private JSONObject getHttpResponse1(String jsonUrl) {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(jsonUrl).build();
        Call call = okHttpClient.newCall(request);
        try {
            Response response = call.execute();
            return new JSONObject(response.body().string());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Card parseCard(JSONObject json) {
        Card card = new Card();
        card.title = getTitle(json);
        return card;
    }

    private String getTitle(JSONObject json) {
        Log.e(TAG, Thread.currentThread().toString() + "getTitle");
        String title = "";
        try {
            JSONArray jsonArray = json.getJSONArray("data");
            JSONObject jsonObject = (JSONObject) jsonArray.get(0);
            title = jsonObject.getString("query");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return title;
    }

    private Bitmap getBitmap(String imgUrl) {
        Log.e(TAG, Thread.currentThread().toString() + "getBitmap");
        try {
            Bitmap img = null;
            URL url = new URL(imgUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            if (200 == con.getResponseCode()) {
                img = BitmapFactory.decodeStream(con.getInputStream());
            }
            return img;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void test() {
        Observable.zip(
                Observable.range(0, 5),
                Observable.range(0, 3),
                Observable.range(0, 8),
                (i1,i2,i3) -> i1 + " - " + i2 + " - " + i3)
                .count()
                .subscribe(System.out::println);
    }

    private class Card {
        private String title;
    }
}
