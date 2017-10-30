package com.example.scanimage.colortrans;

/**
 * Created by shisong on 2016/12/16.
 * 高斯模糊
 */
public class GaussianBlurTrans extends IColorTrans {

    int RSum = 0;
    int GSum = 0;
    int BSum = 0;
    int colorNum = 0;

    int r = 20;

    @Override
    public int tran(int rgb) {
        return rgb;
    }

    @Override
    public int[] tran(int[] color, int width) {
        int[] reColors = new int[color.length];
        for (int i = 0; i < color.length - 2; i++) {
            RSum = 0;
            GSum = 0;
            BSum = 0;
            colorNum = 0;
            for (int rx = -r; rx <= r; rx++) {
                for (int ry = -r; ry <= r; ry++) {
                    addColor(i + rx + ry * width, color);
                }
            }
            addColor(i - 1, color);
            addColor(i + 1, color);
            addColor(i - width, color);
            addColor(i + width, color);
            reColors[i] = (RSum / colorNum) << 16 | (GSum / colorNum) << 8 | (BSum / colorNum);
        }
        return reColors;
    }

    private void addColor(int i, int[] colors) {
        if (i >= 0 && i < colors.length) {
            RSum += (colors[i] & 0x00FF0000) >> 16;
            GSum += (colors[i] & 0x0000ff00) >> 8;
            BSum += (colors[i] & 0x000000FF);
            colorNum++;
        }
    }
}
