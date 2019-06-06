package com.example.scantool;

import com.example.scantool.constant.ScanConstant;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;

public class ClearXhdpiResource extends ScanConstant {

    private final static String XXHDPI = "drawable-xxhdpi";
    private final static HashSet<String> xxhdpiImages = new HashSet<String>();

    private final static String[] COMPARE_DIRS = {
            "drawable-xhdpi",
            "drawable-xxxhdpi",
            "drawable-hdpi",
            "drawable-mdpi",
            "drawable-ldpi"};
    private static int totalNum;
    private static long totalSize;

    public static void main(String[] args) throws IOException {
        File root = new File(ROOT_DIR);
        for (File project : root.listFiles()) {
            if (project == null || project.isFile()) {
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
            loadDirs(project);
        }
        System.out.println("初始文件总数：" + totalNum);
        System.out.println("代码字节数：" + totalSize);
    }

    private static void loadDirs(File moduleDir) throws IOException {
        if (!moduleDir.isDirectory()) {
            return;
        }
        boolean isModule = false;
        for (File file : moduleDir.listFiles()) {
            if (file.getName().endsWith("build.gradle")) {
                isModule = true;
                break;
            }
        }

        if (isModule) {
            loadModule(moduleDir);
        } else {
            for (File file : moduleDir.listFiles()) {
                loadDirs(file);
            }
        }
    }

    private static void loadModule(File moduleDir) throws IOException {
        System.out.println(moduleDir.getAbsolutePath());
        File dir = new File(moduleDir, "/res");
        if (dir.exists()) {
            clearXdpiRes(dir);
        } else {
            dir = new File(moduleDir, "/src/main/res");
            if (dir.exists()) {
                clearXdpiRes(dir);
            }
        }
    }

    // 加载文件夹中的代码
    private static void clearXdpiRes(File root) throws IOException {
        xxhdpiImages.clear();
        File xxhdpiDir = new File(root, XXHDPI);
        loadDirRes(xxhdpiDir);
        for (String dir : COMPARE_DIRS) {
            File compareDir = new File(root, dir);
            compareDirRes(compareDir);
        }
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
                        System.out.println("=======" + f.getAbsolutePath());
                        totalNum++;
                        totalSize += f.length();
                        f.delete();
                    }
                }
            }
        }
    }
}
