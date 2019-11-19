package com.rd.hcb.math;

/**
 * 移动0到末尾，快速排序第二种方法
 * @author zlx
 * @date 2019-11-18 13:47
 */
public class ZeroEnd {

    public void moveZeroes(int[] nums) {
        int i=0;
        int j=0;
        int n=nums.length;
        for(;i<n;i++){
            if(nums[i]!=0){
                swap(nums,i,j);
                j++;
            }else{
                swap(nums,i,j);
            }
        }



    }
    void swap(int[] arr,int i,int j){
        int tmp=arr[i];
        arr[i]=arr[j];
        arr[j]=tmp;
    }

}
