package com.example.dynamicproxytest;

import java.util.Date;

/**
 * Created by shisong on 2017/10/31.
 */

public class MyTime implements ITime {
    @Override
    public String getCurTime() {
        return new Date().toString();
    }

    @Override
    public String getCurTime1() {
        return new Date().toGMTString();
    }

    @Override
    public String getCurTime2() {
        return new Date().toLocaleString();
    }
}
