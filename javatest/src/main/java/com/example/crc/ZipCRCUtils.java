package com.example.crc;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.CRC32;

/**
 * Zip operator tools
 */
public class ZipCRCUtils {

    public static void main(String[] args) {
        ZipCRCUtils.checksumByCRC(new File("d:/test/bottom (1).zip"));
    }

    public static void checksumByCRC(File f) {
        byte [] buffer = getBytesBySize(f);
        if(null == buffer){
            return;
        }
        CRC32 crc32 = new CRC32();
        crc32.update(buffer);
        String value = Long.toHexString(crc32.getValue()).toUpperCase();
        System.out.println("###" + f.getName() + "   " + value);
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
