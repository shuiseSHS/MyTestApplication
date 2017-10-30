package com.example.scanimage;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import static com.example.scanimage.utils.ImageUtils.bufferImage;
import static java.awt.Image.SCALE_DEFAULT;

/**
 * Created by shisong on 2016/12/9.
 *
 */
public class Perceptualhash {

    private static final int width = 8;
    private static final int height = 8;

    public static void main(String[] a) throws IOException {
        long startTime = System.currentTimeMillis();
        File f = new File("f:/test/b.png");
        BufferedImage bufferedImage = ImageIO.read(f);
        getImageHash(bufferedImage);
        System.out.println("complete: " + (System.currentTimeMillis() - startTime));
    }

    public static long getImageHash(BufferedImage image) throws IOException {
        BufferedImage sImg = bufferImage(image.getScaledInstance(width, height, SCALE_DEFAULT));
        writeImage(sImg);
        int[] rgbs = sImg.getRGB(0, 0, width, width, null, 0, width);
        int[] grays = new int[rgbs.length];
        int totalGray = 0;
        for (int i = 0; i < rgbs.length; i++) {
            grays[i] = tran(rgbs[i]);
            totalGray += grays[i];
        }

        int avgGray = totalGray / rgbs.length;
        long hash = 0l;
        for (int i = 0; i < grays.length; i++) {
            int bit = 0;
            if (grays[i] >= avgGray) { // 1
                bit = 1;
            }
            hash = hash << 1 | bit;
        }
        System.out.println(Long.toBinaryString(hash));


        return hash;
    }

    private static int tran(int rgb) {
        System.out.println(Long.toBinaryString(rgb));
        int R = (rgb & 0x00FF0000) >> 16;
        int G = (rgb & 0x0000FF00) >> 8;
        int B = rgb & 0x000000FF;
        int gray = (R * 19 + G * 37 + B * 8) >> 6;
        System.out.println(gray);
        return gray;
    }

    private static void writeImage(BufferedImage image) throws IOException {
        File oFile = new File("f:/test/test1.jpg");
        oFile.createNewFile();
        ImageIO.write(image, "jpg", oFile);
    }
}
