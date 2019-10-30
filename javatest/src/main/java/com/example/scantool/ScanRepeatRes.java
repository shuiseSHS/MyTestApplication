package com.example.scantool;

import com.example.scanimage.utils.MD5Utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ScanRepeatRes {
    // 保存MD5和对应的所有图片文件路径
    private final static HashMap<String, List<String>> codeMap = new HashMap<String, List<String>>();

    private static long TOTAL_SIZE = 0;
    private static long TOTAL_NUM = 0;
    private static long IMAGE_NUM = 0;

    private final static String ROOT_DIR = "C:\\Users\\shisong\\Desktop\\r";

    public static void main(String[] args) throws IOException {
        loadCode();
    }

    // 加载所以代码（包括xml和java）
    private static void loadCode() throws IOException {
        codeMap.clear();
        File root = new File(ROOT_DIR);
        for (File project : root.listFiles()) {
            if (project.isFile()) {
                continue;
            }
            loadDirRes(project);
        }

        for (String md5 : codeMap.keySet()) {
            List<String> ls = codeMap.get(md5);
            if (ls.size() > 1) {
                File f = new File(ls.get(0));
                long size = (f.length() * (ls.size() - 1));
                TOTAL_SIZE += size;
                TOTAL_NUM += ls.size() - 1;
                System.out.println("相同图片数量" + ls.size() + ", 冗余空间：" + size);
                for (String sss : ls) {
                    System.out.println("----------: " + sss);
                }
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
                    String md5 = MD5Utils.getFileMD5String(f);
                    List<String> fs = codeMap.get(md5);
                    if (fs == null) {
                        fs = new ArrayList<>();
                        codeMap.put(md5, fs);
                    }
                    fs.add(f.getAbsolutePath());
                    IMAGE_NUM ++;
//                    System.out.println(md5 + "---" + f.getAbsolutePath());
                }
            }
        }
    }

}
