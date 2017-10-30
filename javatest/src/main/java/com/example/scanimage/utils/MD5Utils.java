package com.example.scanimage.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by shisong on 2016/12/8.
 *
 */
public class MD5Utils {

    private static char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    private static MessageDigest messageDigest = null;

    static {
        try {
            messageDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException nsaex) {
            System.err.println("初始化失败，MessageDigest不支持MD5!");
            nsaex.printStackTrace();
        }
    }

    public static String getFileMD5String(File file) throws IOException {
        FileInputStream in = new FileInputStream(file);
        FileChannel ch = in.getChannel();
        //700000000 bytes are about 670M
        int maxSize = 700000000;

        long startPosition = 0L;
        long step = file.length() / maxSize;

        try {
            if (step == 0) {
                MappedByteBuffer byteBuffer = ch.map(FileChannel.MapMode.READ_ONLY, 0, file.length());
                messageDigest.update(byteBuffer);
                return bufferToHex(messageDigest.digest());
            }

            for (int i = 0; i < step; i++) {
                MappedByteBuffer byteBuffer = ch.map(FileChannel.MapMode.READ_ONLY, startPosition, maxSize);
                messageDigest.update(byteBuffer);
                startPosition += maxSize;
            }

            if (startPosition == file.length()) {
                return bufferToHex(messageDigest.digest());
            }

            MappedByteBuffer byteBuffer = ch.map(FileChannel.MapMode.READ_ONLY, startPosition, file.length() - startPosition);
            messageDigest.update(byteBuffer);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            in.close();
        }
        return bufferToHex(messageDigest.digest());
    }

    private static String bufferToHex(byte bytes[]) {
        return bufferToHex(bytes, 0, bytes.length);
    }

    private static String bufferToHex(byte bytes[], int m, int n) {
        StringBuffer stringbuffer = new StringBuffer(2 * n);
        int k = m + n;
        for (int l = m; l < k; l++) {
            appendHexPair(bytes[l], stringbuffer);
        }
        return stringbuffer.toString();
    }

    private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
        char c0 = hexDigits[(bt & 0xf0) >> 4];
        char c1 = hexDigits[bt & 0xf];
        stringbuffer.append(c0);
        stringbuffer.append(c1);
    }

}
