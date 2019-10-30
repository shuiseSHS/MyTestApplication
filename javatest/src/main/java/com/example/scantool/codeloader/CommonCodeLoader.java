package com.example.scantool.codeloader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * Created by shisong on 2019/6/6
 */
public class CommonCodeLoader {

    static String S1 = "删除的XML字节数：";
    static String S2 = "删除的IMG字节数：";

    public static void main(String[] args) {
        File root = new File("C:\\Users\\shisong\\Desktop\\scan_output");
        int javaSize = 0;
        int pngSize = 0;
        for (File file : root.listFiles()) {
            try {
                FileReader fr = new FileReader(file);
                BufferedReader sr = new BufferedReader(fr);
                String s;
                do {
                    s = sr.readLine();
                    if (s != null) {
                        if (s.startsWith(S1)) {
                            String ints = s.substring(s.indexOf("：") + 1);
                            javaSize += Integer.parseInt(ints);
                        }

                        if (s.startsWith(S2)) {
                            String ints = s.substring(s.indexOf("：") + 1);
                            pngSize += Integer.parseInt(ints);
                            break;
                        }
                    } else {
                        break;
                    }
                } while (true);
                sr.close();
            } catch (Exception ignored) {
            }
        }

        System.out.println("JAVA:" + javaSize);
        System.out.println("PNG:" + pngSize);
    }



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
