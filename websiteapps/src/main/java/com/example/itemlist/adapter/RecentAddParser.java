package com.example.itemlist.adapter;

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
    public String getVideoListId() {
        return "list_videos_latest_videos_list_items";
    }
}
