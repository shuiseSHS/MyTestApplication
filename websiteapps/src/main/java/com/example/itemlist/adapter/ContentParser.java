package com.example.itemlist.adapter;

import com.example.itemlist.DataItem;

import java.util.List;

/**
 * Created by shisong on 2017/3/15.
 */

public interface ContentParser {
    void setKeyword(String keyword);
    String getPageUrl(int page);
    List<DataItem> getVideoList(String result);
}
