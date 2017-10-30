package com.example.scanimage;

import com.example.scanimage.colortrans.BlackWhiteTrans;
import com.example.scanimage.colortrans.BrightColorTrans;
import com.example.scanimage.colortrans.GaussianBlurTrans;
import com.example.scanimage.colortrans.GrayColorTrans;
import com.example.scanimage.colortrans.GrayGreenColorTrans;
import com.example.scanimage.colortrans.IColorTrans;
import com.example.scanimage.colortrans.NoneColorTrans;
import com.example.scanimage.colortrans.OldColorTrans;
import com.example.scanimage.colortrans.RedColorTrans;
import com.example.scanimage.colortrans.ReverseColorTrans;
import com.example.scanimage.colortrans.Y1977ColorTrans;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Created by shisong on 2016/12/9.
 */

public class ImageTransTest {
    private static boolean enableGray = true;
    private static IColorTrans colorTrans;

    public static void main(String[] a) throws IOException {
        colorTrans = new NoneColorTrans();
        colorTrans = new ReverseColorTrans();
        colorTrans = new BrightColorTrans();
        colorTrans = new OldColorTrans();
        colorTrans = new GrayGreenColorTrans();
        colorTrans = new GrayColorTrans();
        colorTrans = new RedColorTrans();
        colorTrans = new BlackWhiteTrans();
        colorTrans = new Y1977ColorTrans();
        colorTrans = new GaussianBlurTrans();

        long startTime = System.currentTimeMillis();
        File f = new File("f:/test/q.jpg");
        BufferedImage bufferedImage = ImageIO.read(f);
        getImageHash(bufferedImage);
        System.out.println("complete: " + (System.currentTimeMillis() - startTime));
    }

    public static void getImageHash(BufferedImage image) throws IOException {
        if (enableGray) {
            int[] rgbs = image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth());
            image.setRGB(0, 0, image.getWidth(), image.getHeight(), colorTrans.tran(rgbs, image.getWidth()), 0, image.getWidth());
        }

        outputImage(image);
    }

    private static void outputImage(BufferedImage image) throws IOException {
        File oFile = new File("f:/test/test1.jpg");
        oFile.createNewFile();
        ImageIO.write(image, "jpg", oFile);
        Runtime.getRuntime().exec("cmd /c f:/test/test1.jpg");
    }
}
