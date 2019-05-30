package com.example.proxy;

/**
 * Created by shisong on 2018/4/18.
 */

public class LDH implements Person, Star {
    @Override
    public void sing(String s) {
        System.out.println("刘德华唱歌：" + s);
    }

    @Override
    public void dance(String s) {
        System.out.println("刘德华跳舞：" + s);
    }

    @Override
    public void say(String s) {
        System.out.println("刘德华说话：" + s);
    }
}
