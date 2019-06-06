package com.example.scantool.codeloader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * Created by shisong on 2019/6/6
 */
public class CommonCodeLoader {

    // 加载代码
    public static String loadCode(File f) {
        if (f == null || f.isDirectory() || !f.exists()) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        try {
            FileReader fr = new FileReader(f);
            BufferedReader sr = new BufferedReader(fr);
            String s;
            do {
                s = sr.readLine();
                if (s != null) {
                    sb.append(s).append("\n");
                } else {
                    break;
                }
            } while (true);
            sr.close();
        } catch (Exception ignored) {
        }
        return sb.toString();
    }
}
