package test;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by shisong on 2016/12/27.
 */

public class VolatileTest {

    public static int count = 0;

    static AtomicInteger count1 = new AtomicInteger(0);

    public static void inc() {
        //这里延迟1毫秒，使得结果明显
        count ++;
        count1.incrementAndGet();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
    }

    public static void main(String[] args) {
        //同时启动1000个线程，去进行i++计算，看看实际结果
        for (int i = 0; i < 1000; i++) {
            new Thread() {
                public void run() {
                    VolatileTest.inc();
                }
            }.start();
        }
        //这里每次运行的值都有可能不同,可能为1000
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
        }
        System.out.println("运行结果:Counter.count=" + VolatileTest.count);
        System.out.println("运行结果:Counter.count1=" + VolatileTest.count1);
    }

}
