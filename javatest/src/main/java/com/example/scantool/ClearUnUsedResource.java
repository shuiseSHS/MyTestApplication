package com.example.scantool;

import com.example.scantool.codeloader.CommonCodeLoader;
import com.example.scantool.codeloader.JarCodeLoader;
import com.example.scantool.constant.ScanConstant;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ClearUnUsedResource extends ScanConstant {

    private final static boolean NEED_READ_JAR = false; // 是否需要扫描jar

    private final static boolean FULL_LOG = false;// 是否显示具体Log

    private final static String[] TO_CLEAR_PROJECTS = {
//            "F:\\qiyi_git\\qiyivideo\\app\\QYVideoClient",
//            "F:\\qiyi_git\\qiyivideo\\app\\QYBaseClient",
            "F:\\qiyi_git\\qiyivideo\\biz\\QYSearch",
//            "F:\\qiyi_git\\qiyivideo\\biz\\Player\\VideoPlayer",
//            "F:\\qiyi_git\\qiyivideo\\biz\\Player\\QYPlayerCardView",
//            "F:\\qiyi_git\\qiyivideo\\biz\\Download\\DownloadUI",
//            "F:\\qiyi_git\\Card\\QYCardV3"

//            "F:\\qiyi_git\\qiyivideo\\biz",
//            "F:\\qiyi_git\\qiyivideo\\component"
    };

    // 保存所有代码，以文件名做key
    private final static HashMap<String, String> codeMap = new HashMap<>();

    private static List<File> delFileList = new ArrayList<>();
    private static long totalBytes = 0;
    private static long delXMLBytes = 0;
    private static long delIMGBytes = 0;

    public static void main(String[] args) throws IOException {
//        scanAll(new File("F:\\qiyi_git\\qiyivideo\\component"));
//        scanAll(new File("F:\\qiyi_git\\qiyivideo\\biz"));
        scanOne();
    }

    private static void scanAll(File rootDir) throws IOException {
        for (File file : rootDir.listFiles()) {
            if (!file.isDirectory()) {
                continue;
            }

            if (!isModuleDir(file)) {
                scanAll(file);
                continue;
            }

            codeMap.clear();
            totalBytes = 0;
            loadCodeInModules(ROOT_DIR);
            loadCodeInModules(CARD_ROOT_DIR);
            System.out.println("初始文件总数：" + codeMap.size());
            System.out.println("代码字节数：" + totalBytes);

            int lastDelFiles;
            int count = 1;

            File srcDir = new File(file, "/src");
            File desDir = new File(file, "/res");
            while (true) {
                lastDelFiles = delFileList.size();
                if (!REAL_DEL) {
                    delFileList.clear();
                    delXMLBytes = 0;
                    delIMGBytes = 0;
                }

                delUnusedFile(srcDir);
                delUnusedFile(desDir);

                System.out.println("\n" + count++ + "轮删除文件数：" + delFileList.size());
                if (delFileList.size() == 0 || delFileList.size() == lastDelFiles) {
                    break;
                }
            }

            System.out.println("=============================");
            if (delFileList.size() > 0) {
                File outputFile = new File("C:\\Users\\shisong\\Desktop\\scan_output\\" + file.getName() + ".txt");
                outputFile.createNewFile();
                FileWriter fileWriter = new FileWriter(outputFile);
                System.out.println("最终删除文件数：" + delFileList.size());
                fileWriter.write("最终删除文件数：" + delFileList.size());
                fileWriter.write("\n");

                System.out.println("删除的XML字节数：" + delXMLBytes);
                fileWriter.write("删除的XML字节数：" + delXMLBytes);
                fileWriter.write("\n");

                System.out.println("删除的IMG字节数：" + delIMGBytes);
                fileWriter.write("删除的IMG字节数：" + delIMGBytes);
                fileWriter.write("\n");

                GitCmd.outputWithGitInfo(delFileList, fileWriter);
                fileWriter.close();
            }
        }
    }

    private static void scanOne() throws IOException {
        for (String scanModulePath : TO_CLEAR_PROJECTS) {
            codeMap.clear();
            totalBytes = 0;
            loadCodeInModules(ROOT_DIR);
            loadCodeInModules(CARD_ROOT_DIR);
            System.out.println("初始文件总数：" + codeMap.size());
            System.out.println("代码字节数：" + totalBytes);

            int lastDelFiles;
            int count = 1;

            File dir = new File(scanModulePath);
            File srcDir = new File(scanModulePath + "/src");
            File desDir = new File(scanModulePath + "/res");
            while (true) {
                lastDelFiles = delFileList.size();
                if (!REAL_DEL) {
                    delFileList.clear();
                    delXMLBytes = 0;
                    delIMGBytes = 0;
                }

                delUnusedFile(srcDir);
                delUnusedFile(desDir);

                System.out.println("\n" + count++ + "轮删除文件数：" + delFileList.size());
                if (delFileList.size() == 0 || delFileList.size() == lastDelFiles) {
                    break;
                }
            }

            System.out.println("=============================");
            File outputFile = new File("C:\\Users\\shisong\\Desktop\\scan_output\\" + dir.getName() + ".txt");
            outputFile.createNewFile();
            FileWriter fileWriter = new FileWriter(outputFile);
            System.out.println("最终删除文件数：" + delFileList.size());
            fileWriter.write("最终删除文件数：" + delFileList.size());
            fileWriter.write("\n");

            System.out.println("删除的XML字节数：" + delXMLBytes);
            fileWriter.write("删除的XML字节数：" + delXMLBytes);
            fileWriter.write("\n");

            System.out.println("删除的IMG字节数：" + delIMGBytes);
            fileWriter.write("删除的IMG字节数：" + delIMGBytes);
            fileWriter.write("\n");

            GitCmd.outputWithGitInfo(delFileList, fileWriter);
            fileWriter.close();
        }
    }

    // 加载所有代码（包括xml和java）
    private static void loadCodeInModules(String rootDir) {
        File root = new File(rootDir);
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
    }

    private static void loadDirs(File moduleDir) {
        if (!moduleDir.isDirectory()) {
            return;
        }

        if (isModuleDir(moduleDir)) {
            loadModule(moduleDir);
        } else {
            for (File file : moduleDir.listFiles()) {
                loadDirs(file);
            }
        }
    }

    private static void loadModule(File moduleDir) {
        System.out.println(moduleDir.getAbsolutePath());

        File dir = new File(moduleDir, "/res");
        if (dir.exists()) {
            loadDirCode(dir);
            dir = new File(moduleDir, "/src");
            loadDirCode(dir);
        } else {
            dir = new File(moduleDir, "/src/main/res");
            loadDirCode(dir);
            dir = new File(moduleDir, "/src/main/java");
            loadDirCode(dir);
        }

        dir = new File(moduleDir, "build/generated/source/apt/debug");
        loadDirCode(dir);

        if (NEED_READ_JAR) {
            loadContantsInJar(dir);
        }

        loadFileCode(new File(moduleDir, "/AndroidManifest.xml"));
    }

    // 筛选出没有引用的java文件并且删除
    private static void delUnusedFile(File dir) {
        if (codeMap.size() == 0) {
            System.err.println("textMap没有初始化");
        }
        if (dir == null || !dir.exists() || !dir.isDirectory() || "assets".equals(dir.getName())
                || dir.getName().startsWith("values")) {
            return;
        }

        for (File f : dir.listFiles()) {
            if (f.isDirectory()) {
                delUnusedFile(f);
            } else {
                String fileName = f.getAbsolutePath();
                if (fileName.endsWith(".java")) {
                    if (!containsStringByJava(f)) {
                        delXMLBytes += f.length();
                        doDelete(f);
                    }
                } else if (fileName.endsWith(".xml") && !fileName.endsWith("AndroidManifest.xml")) {
                    if (!containsString(f)) {
                        delXMLBytes += f.length();
                        doDelete(f);
                    }
                } else if (fileName.endsWith(".9.png") || fileName.endsWith(".png") || fileName.endsWith(".jpg")) {
                    if (!containsString(f)) {
                        doDelete(f);
                        delIMGBytes += f.length();
                    }
                }
            }
        }
    }

    private static void doDelete(File f) {
        codeMap.remove(f.getAbsolutePath());
        delFileList.add(f);
        if (REAL_DEL) {
            f.delete();
        }

        if (delFileList.size() % 100 == 0) {
            System.out.println("=");
        } else {
            System.out.print("=");
        }
    }

    // 查找指定字符串
    private static boolean containsString(File currentFile) {
        String currentName = currentFile.getName();
        String currentFilePath = currentFile.getAbsolutePath();
        String fileName = currentName.substring(0, currentName.indexOf("."));

        // TODO; 存在拼接查找资源的逻辑
        for (String s : HOLD) {
            if (fileName.startsWith(s)) {
                return true;
            }
        }

        int matchCount = 0;
        for (String filePath : codeMap.keySet()) {
            if (filePath.equals(currentFilePath)) {
                continue;
            }
            String text = codeMap.get(filePath);
            if (text.contains(fileName)) {
                matchCount++;
                if (filePath.contains(".jar")) {
                    if (FULL_LOG) {
                        System.err.println("###" + filePath + "包含" + currentFilePath);
                    }
                }
                break;
            }
        }

        return matchCount > 0;
    }

    // 查找指定字符串
    private static boolean containsStringByJava(File currentFile) {
        String currentName = currentFile.getName();
        String currentFilePath = currentFile.getAbsolutePath();
        String fileName = currentName.substring(0, currentName.indexOf("."));

        int c = 0;
        for (String filePath : codeMap.keySet()) {
//            if (!filePath.endsWith(".java") && !filePath.endsWith("AndroidManifest.xml")) {
//                continue;
//            }
            if (filePath.equals(currentFilePath)) {
                continue;
            }
            String text = codeMap.get(filePath);
            if (text.contains(fileName)) {
                c++;
                break;
            }
        }

        return c != 0;
    }

    // 只加载jar包中String类型的常量
    private static void loadContantsInJar(File f) {
        if (f == null || !f.exists()) {
            return;
        }
        if (f.isDirectory()) {
            for (File file : f.listFiles()) {
                loadContantsInJar(file);
            }
        } else {
            if (f.getName().endsWith(".jar")) {
                codeMap.put(f.getAbsolutePath(), JarCodeLoader.loadCode(f));
            }
        }
    }

    // 加载文件夹中的代码
    private static void loadDirCode(File dir) {
//		System.out.println("加载代码：" + dir.getAbsolutePath());
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


    private static void loadFileCode(File f) {
        String code = CommonCodeLoader.loadCode(f);
        if (code != null) {
            codeMap.put(f.getAbsolutePath(), code);
            totalBytes += code.length();
        }
    }

}
