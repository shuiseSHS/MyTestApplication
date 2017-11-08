package com.example.dynamicproxytest;

import java.lang.reflect.Proxy;

/**
 * Created by shisong on 2017/10/31.
 */

public class DynamicFactory {

    public static <T> T addLoggerProxy(T src) {
        return (T)Proxy.newProxyInstance(src.getClass().getClassLoader(), src.getClass().getInterfaces(), new LoggerHandler(src));
    }
}
