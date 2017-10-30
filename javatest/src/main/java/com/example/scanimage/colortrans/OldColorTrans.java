package com.example.scanimage.colortrans;

/**
 * Created by shisong on 2016/12/16.
 */

public class OldColorTrans extends IColorTrans {
    @Override
    public int tran(int color) {
        int pixR = (color & 0x00FF0000) >> 16;
        int pixG = (color & 0x0000FF00) >> 8;
        int pixB = color & 0x000000FF;
        int newR = (int) (0.393 * pixR + 0.769 * pixG + 0.189 * pixB);
        if (newR > 0xFF) {
            newR = 0xFF;
        }
        int newG = (int) (0.349 * pixR + 0.686 * pixG + 0.168 * pixB);
        if (newG > 0xFF) {
            newG = 0xFF;
        }
        int newB = (int) (0.272 * pixR + 0.534 * pixG + 0.131 * pixB);
        if (newB > 0xFF) {
            newB = 0xFF;
        }
        return newR << 16 | newG << 8 | newB;
    }
}
