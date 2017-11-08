package com.example.dynamicproxytest;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by shisong on 2017/10/31.
 * Dynamic proxy test
 */
public class ProxyHandler1 implements InvocationHandler {

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Logger.startLog(method.getName());
        Logger.endLog(method.getName());
        return method.getReturnType().newInstance();
    }
}
