package com.example.sort;

/**
 * Created by shisong on 2017/1/4.
 */

public class QuickSort {
    private static int[] input = new int[]{2,4,1,7,5,23,423,5,7,23,21,9,6,99,3,678};

    public static void main(String[] args) {
        sort(input, 0, input.length - 1);
        for (int i :input) {
            System.out.println(i);
        }
    }

    private static void sort(int[] input,int start, int end) {
        if (start >= end) {
            return;
        }
        int flag = input[start];
        int indexStart = start + 1;
        int indexEnd = end;
        while (indexStart <= indexEnd) {
            if (input[indexStart] < flag) {
                indexStart ++;
            } else {
                int temp = input[indexStart];
                input[indexStart] = input[indexEnd];
                input[indexEnd] = temp;
                indexEnd --;
            }
        }

        int mid = indexStart - 1;
        input[start] = input[mid];
        input[mid] = flag;
        sort(input, start, mid - 1);
        sort(input, mid + 1, end);
    }

}
