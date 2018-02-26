package com.example.itemlist.adapter;

import com.example.itemlist.DataItem;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shisong on 2017/3/15.
 * 最新更新
 */
public class RecentParser implements ContentParser {

    private String keyword;

    // https://www.baidu.com/s?wd=人民的名义&pn=50
    private final static String BASEURL = ""; //最新更新

    @Override
    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public String getPageUrl(int page) {
        return BASEURL + page + "/";
    }

    public List<DataItem> getVideoList(String result) {
        Document doc = Jsoup.parse(result);
        Elements videos = doc.getElementsByClass("item thumb thumb--videos");
        List<DataItem> datas = new ArrayList<>();
        for (Element video : videos) {
            if (video.getElementsByAttribute("rel").size() == 0) {
                try {
                    DataItem data = new DataItem();
                    data.title = video.getElementsByTag("a").attr("title");
                    data.url = video.getElementsByTag("a").attr("href");
                    data.img = video.getElementsByTag("img").attr("src");
                    data.rate = video.getElementsByTag("span").get(0).childNode(0).toString();
                    data.duration = video.getElementsByTag("li").get(0).childNode(0).toString();
                    data.add = video.getElementsByTag("li").get(1).childNode(0).toString();
                    data.views = video.getElementsByTag("li").get(2).childNode(0).toString();
                    datas.add(data);
                } catch (Exception ignore) {}
            }
        }
        return datas;
    }
}
