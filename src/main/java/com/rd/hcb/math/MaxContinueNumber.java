package com.rd.hcb.math;

/**
 *
 * 最大连续个数的1
 * @author zlx
 * @date 2019-11-18 13:42
 */
public class MaxContinueNumber {
    public int findMaxConsecutiveOnes(int[] nums) {
        int count=0;
        int max=0;
        for(int i=0;i<nums.length;i++){
            if(nums[i]==1){
                count++;
                if(max<count){
                    max=count;
                }
            }else{
                count=0;
            }
        }
        return max;
    }
}
