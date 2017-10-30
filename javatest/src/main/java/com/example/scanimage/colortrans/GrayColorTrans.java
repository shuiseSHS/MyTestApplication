package com.example.scanimage.colortrans;

/**
 * Created by shisong on 2016/12/16.
 * rgb转灰色
 */
public class GrayColorTrans extends IColorTrans {
    @Override
    public int tran(int rgb) {
        int R = (rgb & 0x00FF0000) >> 16;
        int G = (rgb & 0x0000FF00) >> 8;
        int B = rgb & 0x000000FF;
        int gray = (R * 19 + G * 37 + B * 8) >> 6;
        return gray << 16 | gray << 8 | gray;
    }
}
