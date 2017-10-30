package com.example.shisong.testapplication;

import android.os.Environment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.CRC32;

/**
 * Zip operator tools
 */
public class ZipCRCUtils {

    public static boolean checksumByCRC(File f, String crcValue) {
        byte [] buffer = getBytesBySize(f);
        if(null == buffer){
            return false;
        }
        CRC32 crc32 = new CRC32();
        crc32.update(buffer);
        String value = StringUtils.toStr(Long.toHexString(crc32.getValue()).toUpperCase(), "");
        System.out.println("###" + crcValue + "   " + value);
        return value.equals(crcValue);
    }

    public static void main(String args[]) {
        File root = Environment.getExternalStorageDirectory();
        System.out.println("########## " + root);
        checksumByCRC(new File(root, "Android_bottom_theme_1208.zip"), "1111");
    }

    private static byte[] getBytesBySize(File f) {
        if (null == f || !f.exists() || !f.canRead()) {
            System.out.println("###  file is null");
            return null;
        }
        FileInputStream fis = null;
        ByteArrayOutputStream baos = null;
        try {
            fis = new FileInputStream(f);
            baos = new ByteArrayOutputStream();
            int len;
            byte[] buf = new byte[1024];
            while((len = fis.read(buf))!=-1){
                baos.write(buf,0,len);
            }
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(null != fis) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(null != baos) {
                try {
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }


}
