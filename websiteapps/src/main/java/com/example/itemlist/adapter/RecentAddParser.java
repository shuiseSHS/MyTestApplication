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
public class RecentAddParser implements ContentParser {

    private String keyword;

    // https://www.baidu.com/s?wd=人民的名义&pn=50
    private final static String BASEURL = "http://www.baidu.com/s?wd="; //最新更新

    @Override
    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public String getPageUrl(int page) {
        return BASEURL + keyword + "&pn=" + page + "0/";
    }

    @Override
    public List<DataItem> getVideoList(String result) {
        Document doc = Jsoup.parse(result);
        Elements videos = doc.getElementsByClass(getVideoListId());
        List<DataItem> datas = new ArrayList<>();
        for (Element video : videos) {
            DataItem data = new DataItem();
            data.title = video.getElementsByTag("a").text();
            data.url = video.getElementsByTag("a").attr("href");
            datas.add(data);
        }
        return datas;
    }

    private String getVideoListId() {
        return "list_videos_latest_videos_list_items";
    }
}
