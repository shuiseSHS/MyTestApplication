package com.example.scanimage.model;

import java.awt.image.BufferedImage;

/**
 * Created by shisong on 2016/12/8.
 */

public class ImageInfo {

    public String path;
    public int width;
    public int height;
    public long code;
    public long size;
    public int[] colors;
    public int[] allColor;
    
    public ImageInfo(int w, int h, long c) {
        width = w;
        height = h;
        code = c;
    }

    public ImageInfo(BufferedImage image) {
        width = image.getWidth();
        height = image.getHeight();
        colors = new int[10];
        int w = width - 1;
        int h = height - 1;
        colors[0] = image.getRGB(0, 0);
        colors[1] = image.getRGB(0, h / 2);
        colors[2] = image.getRGB(0, h);
        colors[3] = image.getRGB(w / 2, 0);
        colors[4] = image.getRGB(w / 2, h /2);
        colors[5] = image.getRGB(w / 2, h);
        colors[6] = image.getRGB(w, 0);
        colors[7] = image.getRGB(w, h / 2);
        colors[8] = image.getRGB(w, h);
        colors[9] = image.getRGB((int) (w * 0.618), (int) (h * 0.618));
        allColor = image.getRGB(0, 0, width, height, null, 0, width);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ImageInfo)) {
            return false;
        }

        ImageInfo imageInfo = (ImageInfo) obj;
        if (width != imageInfo.width) {
            return false;
        }
        if (height != imageInfo.height) {
            return false;
        }

        return code == imageInfo.code;
    }
}
