package com.example.dynamicproxytest;

import android.util.Log;

/**
 * Created by shisong on 2017/10/31.
 */

public class Logger {

    public static void startLog(String tag) {
        Log.d(tag, "start");
    }

    public static void endLog(String tag) {
        Log.d(tag, "end");
    }
}
