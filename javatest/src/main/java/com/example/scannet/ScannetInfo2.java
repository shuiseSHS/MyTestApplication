package com.example.scannet;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.example.scannet.ScannetInfo.rootUrl;

/**
 * Created by shisong on 2016/12/12.
 */

public class ScannetInfo2 {


    public static void main(String[] a) throws IOException {
        OkHttpClient mOkHttpClient = new OkHttpClient();
        final Request request = new Request.Builder().url(rootUrl).build();
        Call call = mOkHttpClient.newCall(request);
        Response response = call.execute();
        String result = response.body().string();
        System.out.println(result);
    }
}
