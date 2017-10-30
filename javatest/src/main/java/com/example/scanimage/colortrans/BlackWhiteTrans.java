package com.example.scanimage.colortrans;

import java.util.Random;

/**
 * Created by shisong on 2016/12/16.
 * rgb转灰色
 */
public class BlackWhiteTrans extends IColorTrans {

    private Random random = new Random(System.currentTimeMillis());

    @Override
    public int tran(int rgb) {
        int R = (rgb & 0x00FF0000) >> 16;
        int G = (rgb & 0x0000FF00) >> 8;
        int B = rgb & 0x000000FF;
        int gray = (R * 19 + G * 37 + B * 8) >> 6;
        if (gray < 0xFF >> 1) {
            gray = 0;
        } else {
            gray = 0xFF;
        }
        if (random.nextInt() % 10 == 0) {
            return 0;
        } else {
            return gray << 16 | gray << 8 | gray;
        }
    }
}
