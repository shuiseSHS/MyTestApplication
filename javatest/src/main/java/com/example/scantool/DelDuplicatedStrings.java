package com.example.scantool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by shisong on 2018/2/13.
 */

public class DelDuplicatedStrings {

    public static void main(String[] a) throws IOException {
        List<String> stringForClient = readFile(new File("d:/test/111/strings.xml"));
        List<String> stringForPage = readFile(new File("d:/test/222/strings.xml"));

        System.out.println(stringForPage.size());
        System.out.println("before:" + stringForClient.size());

        for (String pageStr : stringForPage) {
            for (String clientStr : stringForClient) {
                if (clientStr.trim().equals(pageStr.trim())) {
                    stringForClient.remove(clientStr);
                    break;
                }
            }
        }

        System.out.println("after:" + stringForClient.size());

        File outFile = new File("d:/test/222/stringssss.xml");
        if (!outFile.exists()) {
            outFile.createNewFile();
        }

        FileWriter fw = new FileWriter(outFile);
        for (String sline : stringForClient) {
            fw.write(sline);
            fw.write("\n");
        }
        fw.close();
    }

    private static List<String> readFile(File f) {
        List<String> strings = new ArrayList<>();
        try {
            FileReader fr = new FileReader(f);
            BufferedReader sr = new BufferedReader(fr);
            String s;
            do {
                s = sr.readLine();
                if (s != null) {
                    strings.add(s);
                } else {
                    break;
                }
            } while (true);
            sr.close();
        } catch (Exception ignored) {
        }
        return strings;
    }
}
