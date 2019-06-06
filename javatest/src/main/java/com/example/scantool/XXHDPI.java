package com.example.scantool;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;

/**
 * Created by shisong on 2019/6/5
 */
public class XXHDPI {
    // 保存MD5和对应的所有图片文件路径
    private final static HashSet<String> xxhdpiImages = new HashSet<String>();
    private final static HashSet<String> sameImages = new HashSet<String>();

    private final static String XXHDPI = "C:\\Users\\shisong\\Desktop\\res\\drawable-xxhdpi-v4";

    private final static String[] COMPARE_DIRS = {
            "C:\\Users\\shisong\\Desktop\\res\\drawable-xhdpi-v4",
            "C:\\Users\\shisong\\Desktop\\res\\drawable-xxxhdpi-v4",
            "C:\\Users\\shisong\\Desktop\\res\\drawable-hdpi-v4",
            "C:\\Users\\shisong\\Desktop\\res\\drawable-mdpi-v4",
            "C:\\Users\\shisong\\Desktop\\res\\drawable-ldpi-v4"};

    private static int totalNum = 0;
    private static int totalSize = 0;

    public static void main(String[] args) throws IOException {
        File xxhdpiDir = new File(XXHDPI);
        loadDirRes(xxhdpiDir);

        for (String dir : COMPARE_DIRS) {
            File compareDir = new File(dir);
            compareDirRes(compareDir);
        }

        System.out.println("重复图片：" + totalNum);
        System.out.println("占用空间：" + totalSize);
    }

    private static void loadDirRes(File dir) throws IOException {
        if (dir == null || !dir.exists() || !dir.isDirectory() || dir.listFiles() == null) {
            return;
        }

        for (File f : dir.listFiles()) {
            if (!f.exists()) {
                continue;
            }
            if (f.isDirectory()) {
                loadDirRes(f);
            } else {
                String fileName = f.getName();
                if (fileName.endsWith(".9.png") || fileName.endsWith(".png") || fileName.endsWith(".jpg")) {
                    xxhdpiImages.add(f.getName());
                }
            }
        }
    }


    private static void compareDirRes(File dir) throws IOException {
        if (dir == null || !dir.exists() || !dir.isDirectory() || dir.listFiles() == null) {
            return;
        }

        for (File f : dir.listFiles()) {
            if (!f.exists()) {
                continue;
            }
            if (f.isDirectory()) {
                loadDirRes(f);
            } else {
                String fileName = f.getName();
                if (fileName.endsWith(".9.png") || fileName.endsWith(".png") || fileName.endsWith(".jpg")) {
                    if (xxhdpiImages.contains(f.getName())) {
                        sameImages.add(f.getName());
                        System.out.println(f.getAbsolutePath());
                        totalNum++;
                        totalSize += f.length();
                    }
                }
            }
        }
    }

}
