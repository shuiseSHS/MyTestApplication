package com.example.scannet;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by shisong on 2016/12/12.
 */

public class ScannetInfo {

    public static String rootUrl = "http://www.enf.com.cn";

    public static void main(String[] a) throws IOException {
//        ScriptEngineManager manager = new ScriptEngineManager();
//        ScriptEngine engine = manager.getEngineByName("javascript");
//        try {
//            String script = "function hello() {var a=\"2M7G8IQDbo5NxjwXaOYL-_6VWiCpn3@.myr1JTEq49+gUBdv0SlfFzRZPKhsetuHkAc\";var b=a.split(\"\").sort().join(\"\");var c=\"ZddSgBjzB9MsZF9h79e\";var d=\"\";for(var e=0;e<c.length;e++)d+=b.charAt(a.indexOf(c.charAt(e)));return d;}";
//            engine.eval(script);
//            Invocable inv = (Invocable) engine;
//            Object obj = inv.invokeFunction("hello");
//            System.out.println(obj);
//        } catch (ScriptException e) {
//            e.printStackTrace();
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        }


        OkHttpClient mOkHttpClient = new OkHttpClient();
        final Request request = new Request.Builder().url(rootUrl + "/directory/seller").build();
        Call call = mOkHttpClient.newCall(request);
        Response response = call.execute();

        List<Country> countries = new ArrayList<>();
        String result = response.body().string();
        Document doc = Jsoup.parse(result);
        Elements elements = doc.getElementsByClass("no-hover-underline");
        for (Element element : elements) {
            countries.add(new Country(element.getElementsByTag("span").get(1).text(), element.attr("href")));
        }
        for (Country c : countries) {
            c.getCompanys();
            c.getCompanysInfo();
        }
    }
}
