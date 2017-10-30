package test;

/**
 * Created by shisong on 2017/10/30.
 */

public class UnicodeTest {

    public static void main(String args[]) {
        String t = "a||b||c||d";
//        String[] temp = t.split("\\|\\|");
        String[] temp = t.split("\\|\\|");
        System.out.println(temp.length);
        System.out.println(temp[0]);
        System.out.println(temp[1]);
        System.out.println(temp[2]);
        System.out.println(temp[3]);
    }
}
