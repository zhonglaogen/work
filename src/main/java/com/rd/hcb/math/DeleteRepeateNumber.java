package com.rd.hcb.math;

/**
 * 删除有序数组中重复的数字
 *
 * @author zlx
 * @date 2019-11-18 11:55
 */
public class DeleteRepeateNumber {

    public int removeDuplicates(int[] nums) {
        int p = 0;
        int q = 1;
        int len = nums.length;
        for (; q < len; q++) {
            if (nums[q] != nums[p]) {
                p++;
                nums[p] = nums[q];
            }
        }
        return p + 1;


    }

}
