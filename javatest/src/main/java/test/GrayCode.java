package test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shisong on 2017/3/7.
 */

public class GrayCode {
    public static void main(String[] a) {
        List<String> codes = gray(9);
        for (String s : codes) {
            System.out.println(s);
        }
    }

    private static List<String> gray(int n) {
        List<String> codes = new ArrayList<>();
        if (n == 1) {
            codes.add("0");
            codes.add("1");
        } else {
            List<String> half = gray(n - 1);
            for (String s : half) {
                codes.add("0" + s);
            }
            for (int i = half.size() - 1; i >= 0; i --) {
                codes.add("1" + half.get(i));
            }
        }
        return codes;
    }
}
