package com.example;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class ClassRowsStatistics {
    private final static String ROOT_DIR = "F:/qiyi_git/qiyivideo/";

    private final static String[] EXCLUDE_DIR = {
            ".git",
            ".gitignore",
            ".gradle",
            ".idea",
            ".navigation",
            "BaiduWalletSDKLib",
            "qywallet",
            "build",
            "gradle",
            "Networklib",
            "tools",
            "WebView",
            "android_support",
            "appstore"
    };

    private static List<String> TO_CLEAR = new ArrayList<>();

    // 保存所有代码，以文件名做key
    private final static HashMap<String, Integer> codeMap = new HashMap<String, Integer>();

    private static long totalRows = 0;

    public static void main(String[] args) {
        codeMap.clear();
        File root = new File(ROOT_DIR);
        for (File project : root.listFiles()) {
            if (project.isFile()) {
                continue;
            }
            boolean exclude = false;
            for (String s : EXCLUDE_DIR) {
                if (s.equals(project.getName())) {
                    exclude = true;
                    break;
                }
            }
            if (exclude) {
                continue;
            }
            TO_CLEAR.add(project.getName());
            File dir = new File(project, "/src");
            loadDirCode(dir);
        }

        System.out.println("文件总数：" + codeMap.size());
        System.out.println("代码总行数：" + totalRows);

        LinkedHashMap<String, Integer> linkedHashMap = new LinkedHashMap<>();
        for (int i = 0; i < 100; i++) {
            linkedHashMap.put(i * 100 + "-" + (i + 1) * 100, 0);
        }
        for (String key : codeMap.keySet()) {
            int row = codeMap.get(key);
            String lkey;
            lkey = ((row / 100) * 100) + "-" + ((row / 100 + 1) * 100);
            linkedHashMap.put(lkey, linkedHashMap.get(lkey) + 1);
        }

        for (String key : linkedHashMap.keySet()) {
            if (linkedHashMap.get(key) > 0) {
                System.out.println(key + "：" + linkedHashMap.get(key));
            }
        }

//        LinkedHashMap<String, Integer> linkedHashMap = new LinkedHashMap<>();
//        for (int i = 0; i < 100; i++) {
//            linkedHashMap.put(i  + ":", 0);
//        }
//        for (String key : codeMap.keySet()) {
//            int row = codeMap.get(key);
//            if (row < 100) {
//                String lkey;
//                lkey = row + ":";
//                linkedHashMap.put(lkey, linkedHashMap.get(lkey) + 1);
//            }
//        }
//
//        for (String key : linkedHashMap.keySet()) {
//            if (linkedHashMap.get(key) > 0) {
//                System.out.println(key + "：" + linkedHashMap.get(key));
//            }
//        }
    }

    // 加载文件夹中的代码
    private static void loadDirCode(File dir) {
        if (dir == null || !dir.exists() || dir.isFile() || dir.listFiles() == null) {
            return;
        }
        for (File f : dir.listFiles()) {
            if (f.isDirectory()) {
                loadDirCode(f);
            } else {
                if (f.getName().endsWith(".xml") || f.getName().endsWith(".java")) {
                    loadFileCode(f);
                }
            }
        }
    }

    // 加载代码
    private static void loadFileCode(File f) {
        if (f == null || f.isDirectory() || !f.exists() || !f.getName().endsWith(".java")) {
            return;
        }
        int rows = 0;
        try {
            FileReader fr = new FileReader(f);
            BufferedReader sr = new BufferedReader(fr);
            String s = null;
            do {
                s = sr.readLine();
                if (s != null) {
                    rows++;
                } else {
                    break;
                }
            } while (true);
            codeMap.put(f.getAbsolutePath(), rows);
            totalRows += rows;
            sr.close();
            if (rows < 10) {
                System.out.println(rows + "    " + f.getName());
            }
        } catch (Exception e) {
        }
    }
}
