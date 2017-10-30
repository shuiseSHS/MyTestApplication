package com.example.scanimage.colortrans;

/**
 * Created by shisong on 2016/12/16.
 */

public class BrightColorTrans extends IColorTrans {
    @Override
    public int tran(int color) {
        int pixR = (color & 0x00FF0000) >> 16;
        int pixG = (color & 0x0000FF00) >> 8;
        int pixB = color & 0x000000FF;
        int newR = pixR + 125;
        if (newR > 0xFF) {
            newR = 0xFF;
        }
        int newG = pixG;
        int newB = pixB;
        return newR << 16 | newG << 8 | newB;
    }
}
