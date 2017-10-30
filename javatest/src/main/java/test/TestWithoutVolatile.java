package test;

/**
 * Created by shisong on 2016/12/27.
 */

public class TestWithoutVolatile {

    private static boolean bChanged;

    public static void main(String[] args) throws InterruptedException {
        new Thread() {
            @Override
            public void run() {
                for (; ; ) {
                    boolean a = bChanged;
                    boolean b = !bChanged;
                    if (a == b) {
                        System.out.println("!=");
                        System.exit(0);
                    }
                }
            }
        }.start();
//
        Thread.sleep(1);

        new Thread() {
            @Override
            public void run() {
                for (; ; ) {
                    bChanged = false;
                    bChanged = true;
                }
            }
        }.start();
    }

}
