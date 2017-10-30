package com.example.scanimage.utils;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

/**
 * Created by shisong on 2016/12/9.
 */

public class ImageUtils {
    private static final int DEFAULT_IMAGE_TYPE = BufferedImage.TYPE_INT_RGB;

    public static BufferedImage bufferImage(Image image) {
        return bufferImage(image, DEFAULT_IMAGE_TYPE);
    }

    public static BufferedImage bufferImage(Image image, int type) {
        BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
        Graphics2D g = bufferedImage.createGraphics();
        g.drawImage(image, null, null);
        g.dispose();
        return bufferedImage;
    }

}
