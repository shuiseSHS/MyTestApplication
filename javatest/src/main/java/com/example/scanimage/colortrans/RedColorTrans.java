package com.example.scanimage.colortrans;

/**
 * Created by shisong on 2016/12/16.
 * rgb转灰色
 */
public class RedColorTrans extends IColorTrans {
    @Override
    public int tran(int rgb) {
        return rgb & 0x00FF0000;
    }
}
