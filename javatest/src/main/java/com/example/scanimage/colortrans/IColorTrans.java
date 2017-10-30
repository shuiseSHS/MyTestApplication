package com.example.scanimage.colortrans;

/**
 * Created by shisong on 2016/12/16.
 *
 */
public abstract class IColorTrans {

    public int tran(int color) {
        return color;
    }

    public int[] tran(int[] color, int width) {
        int[] gray = new int[color.length];
        for (int i = 0; i < gray.length; i++) {
            gray[i] = tran(color[i]);
        }
        return gray;
    }

}
