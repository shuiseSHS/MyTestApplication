package sort;

import java.util.Arrays;

/**
 * Created by shisong on 2017/4/24.
 * 归并排序
 */
public class MergeSort {
    private static int[] nums = new int[]{937, 66, 20, 100, 1301, 38, 8, 1, 22, 345, 5, 673, 0, 93, 202, 6, 99, 32, 392};
    private static int[] nums1 = new int[nums.length];
    private static int curLength = 1;

    public static void main(String args[]) {
        sort(nums);
    }

    private static void sort(int[] nums) {
        while (curLength < nums.length) {
            int e2 = 0;
            for (int i = 0; i + curLength < nums.length;) {
                e2 = i + curLength * 2 - 1;
                if (e2 > nums.length - 1) {
                    e2 = nums.length - 1;
                }
                merge(nums, i, i + curLength - 1, i + curLength,  e2);
                i = e2 + 1;
            }

            e2 ++;
            while (e2 < nums.length) {
                nums1[e2] = nums[e2++];
            }
            curLength = curLength * 2;
            nums = Arrays.copyOf(nums1, nums1.length);
            print(nums1);
        }
    }

    private static void merge(int[] nums, int s1, int e1, int s2, int e2) {
        int sortIndex = s1;
        while (s1 <= e1 && s2 <= e2) {
            if (nums[s1] < nums[s2]) {
                nums1[sortIndex++] = nums[s1++];
            } else {
                nums1[sortIndex++] = nums[s2++];
            }
        }

        while (s1 <= e1) {
            nums1[sortIndex++] = nums[s1++];
        }

        while (s2 <= e2) {
            nums1[sortIndex++] = nums[s2++];
        }
    }

    private static void print(int nums[]) {
        for (int n : nums) {
            System.out.print(n + "  ");
        }
        System.out.println("");
    }
}
