package com.example.scanimage.colortrans;

/**
 * Created by shisong on 2016/12/16.
 */

public class ReverseColorTrans extends IColorTrans {
    @Override
    public int tran(int color) {
        return ~color;
    }
}
