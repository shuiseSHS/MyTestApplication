package com.example.scanimage;

import com.example.scanimage.model.ImageInfo;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;


public class ScanRepeatRes1 {
    // 保存MD5和对应的所有图片文件路径
//    private final static HashMap<String, ImageInfo> codeMap = new HashMap<>();

    private final static List<ImageInfo> imageList = new ArrayList<>();

    private static long TOTAL_SIZE = 0;
    private static long TOTAL_NUM = 0;
    private static long IMAGE_NUM = 0;

    public final static String ROOT_DIR = "C:\\Users\\shisong\\Desktop\\冗余资源删除";

    public static void main(String[] args) throws IOException {
        loadCode();
    }

    // 加载所以代码（包括xml和java）
    private static void loadCode() throws IOException {
        imageList.clear();
        File root = new File(ROOT_DIR);
        for (File project : root.listFiles()) {
            if (project.isFile()) {
                continue;
            }
            loadDirRes(project);
        }

        for (int i = 0; i < imageList.size(); i++) {
            ImageInfo imageInfo = imageList.get(i);
            int same = 0;
            for (int j = i + 1; j < imageList.size(); j++) {
                ImageInfo imageInfo1 = imageList.get(j);
                if (imageInfo.equals(imageInfo1)) {
                    System.out.println(imageInfo.path + " 相同：" + imageInfo1.path);
                    same += 1;
                    TOTAL_SIZE += imageInfo1.size;
                    TOTAL_NUM++;
                    imageList.remove(j);
                    j--;
                }
            }
            if (same > 0) {
                System.out.println(imageInfo.path + " 相同数量：" + same);
            }
        }
        System.out.println("共有图片：" + IMAGE_NUM);
        System.out.println("冗余图片：" + TOTAL_NUM);
        System.out.println("冗余空间：" + TOTAL_SIZE);
    }

    private static void loadDirRes(File dir) throws IOException {
        if (dir == null || !dir.exists() || !dir.isDirectory()) {
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
                    BufferedImage image = ImageIO.read(f);
                    ImageInfo imageInfo = new ImageInfo(image.getWidth(), image.getHeight(), Perceptualhash.getImageHash(image));
                    imageInfo.path = f.getAbsolutePath();
                    imageInfo.size = f.length();
                    imageList.add(imageInfo);
                    IMAGE_NUM++;
                    System.out.println(Long.toBinaryString(imageInfo.code) + "---" + f.getAbsolutePath());
                }
            }
        }
    }
}
