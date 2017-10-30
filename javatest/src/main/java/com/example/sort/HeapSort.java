package com.example.sort;

/**
 * Created by shisong on 2017/1/4.
 */

public class HeapSort {
    private static int[] input = new int[]{2, 4, 1, 7, 5, 23, 423, 5, 7, 23, 21, 9, 6, 99, 3};

    public static void main(String[] args) {
        makeHeap(input);
        for (int i = input.length - 1; i > 0; i--) {
            changeP(input, 0, i);
            resort(input, 0, i);
        }

        for (int i : input) {
            System.out.println(i);
        }
    }

    private static void makeHeap(int[] input) {
        int length = input.length;
        for (int i = length / 2 - 1; i >= 0; i--) {
            resort(input, i, length);
        }
    }

    private static void changeP(int[] input, int i, int pi) {
        int temp = input[i];
        input[i] = input[pi];
        input[pi] = temp;
    }

    private static void resort(int[] input, int head, int end) {
        int largest = head;
        while (true) {
            int p = largest;
            int lc = largest * 2 + 1;
            int rc = largest * 2 + 2;
            if (lc < end && input[lc] > input[p]) {
                largest = lc;
            }
            if (rc < end && input[rc] > input[largest]) {
                largest = rc;
            }
            if (largest != p) {
                changeP(input, largest, p);
            } else {
                break;
            }
        }
    }
}
