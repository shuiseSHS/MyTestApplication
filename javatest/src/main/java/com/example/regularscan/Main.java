package com.example.regularscan;

import com.qiyi.guard.core.config.Configuration;
import com.qiyi.guard.core.config.ConfigurationParser;
import com.qiyi.guard.core.entity.Descriptor;
import com.qiyi.guard.core.entity.MethodDescriptor;
import com.qiyi.guard.core.entity.PermissionDescriptor;
import com.qiyi.guard.core.scan.ScanEngine;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private static List<String> currentClassNames = new ArrayList<>();

    public static void main(String[] args) {
        String apkPath = "C:\\Users\\shisong\\Desktop\\scan\\QYVideoClient-debug.apk";
        String resultPath = "C:\\Users\\shisong\\Desktop\\scan\\result.txt";

        String fileName = "C:\\Users\\shisong\\Desktop\\scan\\configurationToast.json";
        System.out.println("FileName: " + fileName);
        System.out.println("ApkPath: " + apkPath);
        System.out.println("ResultPath: " + resultPath);
        ScanEngine scanEngine = new ScanEngine();
        Configuration configuration = ConfigurationParser.parse(fileName);
        configuration.getUserConfiguration().setTarget(apkPath);

        List<PermissionDescriptor> classList = getDescriptorList();
        configuration.getUserConfiguration().setStringList(classList);

        scanEngine.doScan(configuration);
        List<Descriptor> list = scanEngine.getResultList();
        if (list != null && list.size() > 0) {
            File file = new File(resultPath);
            file.getParentFile().mkdirs();
            try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file))) {
                for (Descriptor descriptor : list) {
                    if (descriptor instanceof MethodDescriptor) {
                        MethodDescriptor methodDescriptor = (MethodDescriptor) descriptor;
                        String owner = methodDescriptor.getOwner();
                        if (owner.contains("$")) {
                            owner = owner.substring(0, owner.indexOf("$"));
                        }
                        owner = owner.replace("/", "\\");
                        if (!currentClassNames.contains(owner)) {
                            bufferedWriter.write(descriptor.toString().trim() + "\n\n");
                        }
                    } else {
                        bufferedWriter.write(descriptor.toString().trim() + "\n\n");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static List<PermissionDescriptor> getDescriptorList() {
        List<PermissionDescriptor> classList = new ArrayList<>();
        try {
            File file = new File("C:\\Users\\shisong\\Desktop\\scan\\QYPlayerCardView.txt");
            FileReader fr = new FileReader(file);
            BufferedReader sr = new BufferedReader(fr);
            String s;
            do {
                s = sr.readLine();
                if (s != null) {
                    if (s.contains("-----") && s.endsWith(".java")) {
                        PermissionDescriptor descriptor = new PermissionDescriptor();
                        int startIndex = s.indexOf("\\src\\main\\java\\");
                        if (startIndex == -1) {
                            startIndex = s.indexOf("\\src\\");
                            if (startIndex != -1) {
                                startIndex += 5;
                            }
                        } else {
                            startIndex += 15;
                        }
                        if (startIndex != -1) {
                            String className = s.substring(startIndex, s.indexOf(".java"));
                            currentClassNames.add(className);
                            descriptor.setName(className.replace("\\", "."));
                            classList.add(descriptor);
                        }
                    }
                } else {
                    break;
                }
            } while (true);
            sr.close();
        } catch (Exception ignored) {
        }

        return classList;
    }
}
