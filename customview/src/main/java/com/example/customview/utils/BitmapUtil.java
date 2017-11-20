package com.example.customview.utils;

import android.graphics.Bitmap;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by shisong on 2017/11/20.
 */

public class BitmapUtil {
    /**
     * 保存到内存卡
     */
    public static boolean saveBitmapToSdCard(Bitmap mBitmap, String fileName) {
        File f = new File("/sdcard/" + fileName + ".png");
        try {
            if (!f.exists() && !f.createNewFile()) {
                return false;
            }
        } catch (IOException ignored) {
        }

        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            return false;
        }
        //原封不动的保存在内存卡上
        mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
        try {
            fOut.flush();
            fOut.close();
        } catch (IOException ignored) {
        }
        return true;
    }
}
