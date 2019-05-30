package com.example.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by shisong on 2018/4/18.
 */

public class LDHProxy {

    private Object ldh;

    public LDHProxy(Object ldh) {
        this.ldh = ldh;
    }

    private InvocationHandler gun = new InvocationHandler() {
        @Override
        public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
            System.out.print("gun    ");
            return method.invoke(ldh, objects);
        }
    };

    Object getLDHProxy() {
        return Proxy.newProxyInstance(ldh.getClass().getClassLoader(), ldh.getClass().getInterfaces(), gun);
    }
}
