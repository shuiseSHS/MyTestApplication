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

import static com.example.scannet.ScannetInfo.rootUrl;

/**
 * Created by shisong on 2016/12/12.
 */

public class Country {
    String name;
    String url;
    List<Company> companies;

    public Country(String name, String url) {
        this.name = name;
        this.url = url;
        companies = new ArrayList<>();
    }

    @Override
    public String toString() {
        return name + ": " + url;
    }

    public void getCompanys() throws IOException {
        OkHttpClient mOkHttpClient = new OkHttpClient();
        final Request request = new Request.Builder().url(rootUrl + url).build();
        Call call = mOkHttpClient.newCall(request);
        Response response = call.execute();
        System.out.println("======================" + name + "=========================");
        String result = response.body().string();
        Document doc = Jsoup.parse(result);
        Elements elements = doc.getElementsByClass("enf-list-table").get(0).getElementsByTag("tbody").get(0).getElementsByTag("tr");
        for (Element element : elements) {
            companies.add(new Company(element.getElementsByTag("a").text(), element.getElementsByTag("a").attr("href")));
        }
    }

    public void getCompanysInfo() throws IOException {
        OkHttpClient mOkHttpClient = new OkHttpClient();
        for (Company company : companies) {
            final Request request = new Request.Builder().url(rootUrl + company.url).build();
            Call call = mOkHttpClient.newCall(request);
            Response response = call.execute();
            String result = response.body().string();
            Document doc = Jsoup.parse(result);
            company.name = doc.getElementsByClass("blue-title").get(0).text();
            for (Element e : doc.getElementsByClass("enf-company-profile-info-main-spec position-relative").get(0).getElementsByClass("pull-right")) {
                if ("address".equals(e.attr("itemprop"))) {
                    company.address = e.text();
                }
                if ("telephone".equals(e.attr("itemprop"))) {
                    company.telephone = e.text();
                }
                if ("faxNumber".equals(e.attr("itemprop"))) {
                    company.faxNumber = e.text();
                }
                if ("numberOfEmployees".equals(e.attr("itemprop"))) {
                    company.numberOfEmployees = e.text();
                }
                if (e.getElementsByTag("a").size() > 0) {
                    company.url = e.getElementsByTag("a").get(0).attr("href");
                }
                if ("email".equals(e.attr("itemprop"))) {
                    if (e.children().size() == 0) {
                        company.email = e.text();
                    }
                }
            }

            System.out.println(name + "\t" + company.name + "\t" + company.address + "\t" + company.telephone + "\t"
            + company.url + "\t" + company.email);
        }
    }
}
