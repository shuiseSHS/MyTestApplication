package com.example.dynamicproxytest;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by shisong on 2017/10/31.
 * Dynamic proxy test
 */
public class LoggerHandler implements InvocationHandler {

    private Object target;

    public LoggerHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Logger.startLog(method.getName());
        Object result = method.invoke(target, args);
        Logger.endLog(method.getName());
        return result;
    }
}
