package com.example.scantool;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shisong on 2019/6/6
 */
public class GitCmd {
//
//    ddd() {
//        String author = "git log --pretty=format:%ae -1 ${file.absolutePath}".execute([],rootDir).text.trim()
//    }

    public static void main(String[] a) {
        String name = getCmdResult("git log --pretty=format:%ae -1 F:\\qiyi_git\\qiyivideo\\biz\\Page\\QYPage\\src\\main\\res\\anim\\main_iqiyi_logo_animation.xml");
        System.out.println("----" + name);
    }

    public static void outputWithGitInfo(List<File> delFileList) {
        Map<String, List<File>> userFiles = new HashMap<>();
        int column = 0;
        for (File file : delFileList) {
            String user = getGitUser(file.getAbsolutePath());
            if (userFiles.containsKey(user)) {
                List<File> files = userFiles.get(user);
                files.add(file);
            } else {
                List<File> files = new ArrayList<>();
                files.add(file);
                userFiles.put(user, files);
            }
            if (++column == 100) {
                System.out.println("=");
            } else {
                System.out.print("=");
            }
        }

        System.out.println("=");
        for (String user : userFiles.keySet()) {
            List<File> files = userFiles.get(user);
            for (File file : files) {
                System.out.println(user + "-----" + file.getAbsolutePath().substring(12));
            }
        }
    }

    public static String getGitUser(String filePath) {
        return getCmdResult("git log --pretty=format:%ae -1 " + filePath);
    }

    public void runCmd(String cmd) {
        String line = null;
        Runtime r = Runtime.getRuntime();
        try {
            File rootDir = new File("F:\\qiyi_git\\qiyivideo");
            Process proc = r.exec(cmd, new String[]{}, rootDir); // 假设该操作为造成大量内容输出
            // 采用字符流读取缓冲池内容，腾出空间
            BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream(), "gbk"));
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            int exitVal = proc.waitFor();
            System.out.println(exitVal == 0 ? "成功" : "失败");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static String getCmdResult(String cmd) {
        String line = null;
        Runtime r = Runtime.getRuntime();
        try {
            File rootDir = new File("F:\\qiyi_git\\qiyivideo");
            Process proc = r.exec(cmd, new String[]{}, rootDir); // 假设该操作为造成大量内容输出
            // 采用字符流读取缓冲池内容，腾出空间
            BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream(), "gbk"));
            while ((line = reader.readLine()) != null) {
                break;
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return line;
    }

}
