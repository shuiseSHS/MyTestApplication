package com.rxjava;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.os.Build;

/**
 * Created by shisong on 2016/11/4.
 */

public class Cat implements Comparable<Cat>{
    Bitmap image;
    int cuteness;

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public int compareTo(Cat another) {
        return Integer.compare(cuteness, another.cuteness);
    }
}
