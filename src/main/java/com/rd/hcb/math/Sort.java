package com.rd.hcb.math;

import org.junit.Test;

/**
 * @author zlx
 * @date 2020-01-06 15:27
 */
public class Sort {
    //合并两个有序数组
    void memoryArry(int[] a,int n,int[] b,int m,int[] c){

        int i,j,k;
        i=j=k=0;
        //比较两个数组，将小的放到c里
        while (i<n && j<m){
            if(a[i]<b[j]){
                c[k++]=a[i++];
            }
            else {
                c[k++]=b[j++];
            }
        }
        while (i<n){
            c[k++]=a[i++];
        }
        while (j<m){
            c[k++]=b[j++];
        }
    }

    static void merageArry(int[] a,int start,int middle,int end,int[] temp){
        int i=start;
        int m=middle;
        int j=middle+1;
        int n=end;
        int k=0;
        //比较两个数组，从头开始
        while (i<=m && j<=n){
            if (a[i]<a[j]){
                temp[k++]=a[i++];
            }else {
                temp[k++]=a[j++];
            }
        }
//        检查剩余的元素
        while (i<=m){
            temp[k++]=a[i++];
        }
        while (j<n){
            temp[k++]=a[j++];
        }
        //将temp的值复制给arry
        for (int p=0;p<k;p++){
            a[start+p]=temp[p];
        }
    }

    static void  sort(int[] a,int first,int last,int temp[]){
        if(first < last){
            int middle = (first + last)/2;
            sort(a,first,middle,temp);//左半部分排好序
            sort(a,middle+1,last,temp);//右半部分排好序
            merageArry(a,first,middle,last,temp); //合并左右部分
        }
    }

    public static void main(String[] args) {
        int[] arry=new int[]{3,1,3100,321,53,131,5,13,54};
        int[] temp=new int[arry.length];
        sort(arry, 0,arry.length-1,temp);
        for (int i = 0; i < arry.length; i++) {
            System.out.print(arry[i]+"\t");
        }

    }

}
