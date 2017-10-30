package com.example;

import java.util.ArrayList;
import java.util.List;

import classloader.DiskClassLoader;

/**
 * Created by shisong on 2017/8/22.
 *
 */
public class Test {
    public static void main(String[] a) {
        System.out.println(System.getProperty("user.dir"));//user.dir指定了当前的路径

        DiskClassLoader cl = new DiskClassLoader("F:\\lib");
        System.out.println("ClassLoader is:"+cl.toString());
        Class eClass = null;
        try {
            eClass = cl.loadClass("com.example.dexloader.IHello");
            System.out.println(eClass.toString());
        } catch (Exception e) {
        }
        try {
            eClass = cl.loadClass("com.example.dexloader.Hello_1");
            System.out.println(eClass.toString());
        } catch (Exception e) {
        }
        try {
            eClass = cl.loadClass("com.example.Hello_2");
            System.out.println(eClass.toString());
        } catch (Exception e) {
        }


        List<String> testList = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            testList.add(testList.size(), "" + i);
        }

        System.out.println(testList);
    }
}
