package com.example.scanimage.colortrans;

/**
 * Created by shisong on 2016/12/16.
 * rgb转灰色
 */
public class GrayGreenColorTrans extends IColorTrans {
    @Override
    public int tran(int rgb) {
        int R = (rgb & 0x00FF0000) >> 16;
        int G = (rgb & 0x0000FF00) >> 8;
        int B = rgb & 0x000000FF;
        int gray = (R * 19 + G * 37 + B * 8) >> 6;
//        double distance = getRDistance(R, G, B);
        double distance = getGDistance(R, G, B);
//        double distance = getBDistance(R, G, B);
        if (distance > 50) {
            return rgb;
        } else {
            return gray << 16 | gray << 8 | gray;
        }
    }

    private double getRDistance(int r, int g, int b) {
        return r * 2 - g - b;
    }

    private double getGDistance(int r, int g, int b) {
        return g * 2 - r - b;
    }

    private double getBDistance(int r, int g, int b) {
        return b * 2 - g - r;
    }

}
