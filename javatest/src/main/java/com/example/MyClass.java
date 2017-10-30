package com.example;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MyClass {
    public static void main(String[] args) {
        String url1 = "http://api-cn.hq.tigerbrokers.com/v1/chart/br/candle/";
        String url2 = "?appVer=1.3.0.0&period=day&count=99";
        for (int i = 0; i < 100; i ++) {
            int stockId = 600000 + i;
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
                System.out.println(sb.toString());
                parseResult(sb.toString());
                Thread.sleep(3000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void parseResult(String result) {

    }

    public void hello2() {
        System.out.println("hello");
    }
}
