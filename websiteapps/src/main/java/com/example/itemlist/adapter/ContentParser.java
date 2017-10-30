package com.example.itemlist.adapter;

/**
 * Created by shisong on 2017/3/15.
 */

public interface ContentParser {
    void setKeyword(String keyword);
    String getPageUrl(int page);
    String getVideoListId();
}
