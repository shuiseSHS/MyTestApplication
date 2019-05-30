package com.example.proxy;

/**
 * Created by shisong on 2018/4/18.
 */

public class Test {

    public static void main(String[] args) {
        LDH ldh = new LDH();
        ldh.sing("AAAA");
        ldh.sing("BBBB");

        Object ldhProxy = new LDHProxy(ldh).getLDHProxy();
        ((Star) ldhProxy).sing("CCCCC");
        ((Person) ldhProxy).say("CCCCC");


        Dog dog = new Dog();
        dog.say();
        Animal animal = (Animal) new LDHProxy(dog).getLDHProxy();
        animal.say();
    }

}
