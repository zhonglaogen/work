package com.rd.hcb.test;

import org.junit.Test;

/**
 * @author zlx
 * @date 2020-01-07 09:40
 */
public class TestSort {
    //快速，特点为每次只定位一个位置的数字，尽量保证两侧数据量一致，
    //否则就是冒泡排序了
    //先快速排序，递归深度超过最大值，切换为堆排序，最后几个数字用插入排序
    public void quickSort(int[] arry, int L, int R, int len) {
        if (L < R) {
            int seed = seedSort(arry, L, R);
            quickSort(arry, L, seed - 1, len);
            quickSort(arry, seed + 1, R, len);
        }


    }

    private int seedSort(int[] arry, int L, int R) {
        int key = arry[R];
        //j指向的永远都是关键字key的位置
        int j = L;
        for (int i = L; i < R; i++) {
            if (arry[i] < key) {
                int temp = arry[j];
                arry[j] = arry[i];
                arry[i] = temp;
                j++;
            }
        }
        int temp = arry[j];
        arry[j] = arry[R];
        arry[R] = temp;
        return j;
    }

    public void quickSort2(int[] arry, int L, int R, int len) {
        if (L < R) {
            int i = L;
            int j = R;
            int key = arry[L];
            while (i < j) {
                while (i < j && arry[j] > key) {
                    j--;
                }
                if (i < j) {
                    int temp = arry[i];
                    arry[i] = arry[j];
                    arry[j] = temp;
                    i++;
                }


                while (i < j && arry[i] < key) {
                    i++;
                }

                if (i < j) {
                    int temp = arry[i];
                    arry[i] = arry[j];
                    arry[j] = temp;
                    j++;
                }
            }


        }
    }

    public static void sort(int[] arry, int start, int end, int n) {
        if (start > end) {
            return;
        }
        int i = start;
        int j = end;
        int key = arry[start];
        while (i < j) {
            //从右向左找第一个小于key的值
            while (i < j && arry[j] >= key) {
                j--;
            }
            //把j位置的数放在位置i
            if (i < j) {
                arry[i] = arry[j];
                i++;
            }
            //从左向右找第一个大于key的值
            while (i < j && arry[i] <= key) {
                i++;
            }
            //把i位置的数放在位置j
            if (i < j) {
                arry[j] = arry[i];
                j--;
            }
        }
//      此时位置i是无用的数字，把key给位置i
        arry[i] = key;
        sort(arry, start, i - 1, arry.length);
        sort(arry, i + 1, end, arry.length);

    }

    void quick_sort(int s[], int l, int r)
    {
        if (l < r)
        {
            //Swap(s[l], s[(l + r) / 2]); //将中间的这个数和第一个数交换 参见注1
            int i = l, j = r, x = s[l];
            while (i < j)
            {
                while(i < j && s[j] >= x) // 从右向左找第一个小于x的数
                    j--;
                if(i < j)
                    s[i++] = s[j];

                while(i < j && s[i] < x) // 从左向右找第一个大于等于x的数
                    i++;
                if(i < j)
                    s[j--] = s[i];
            }
            s[i] = x;
            quick_sort(s, l, i - 1); // 递归调用
            quick_sort(s, i + 1, r);
        }
    }


    @Test
    public void result_test() {
        int[] arry = new int[]{3, 1, 3100, 321, 53, 131, 5, 13, 54};
        int[] arry1 = new int[]{6, 1, 2, 7, 9,3};
        int[] temp = new int[arry1.length];
        quickSort(arry1, 0, arry1.length - 1,arry1.length);
//        sort(arry1,0,arry1.length -1, arry1.length);
        for (int i = 0; i < arry1.length; i++) {
            System.out.print(arry1[i] + "\t");
        }

    }
    /*
        在堆排序（小根堆）的时候，每次总是将最小的元素移除，然后将最后的元素放到堆顶，再让其自我调整。
     这样一来，有很多比较将是被浪费的，因为被拿到堆顶的那个元素几乎肯定是很大的，而靠近堆顶的元素又几乎肯定是很小的，
     最后一个元素能留在堆顶的可能性微乎其微，最后一个元素很有可能最终再被移动到底部。在堆排序里面有大量这种近乎无效的比较。
     随着数据规模的增长，比较的开销最差情况应该在（线性*对数）级别，如果数据量是原来的10倍，那么用于比较的时间开销可能是原来的10log10倍。
    堆排序的过程中，需要有效的随机存取。
    比较父节点和字节点的值大小的时候，虽然计算下标会很快完成，但是在大规模的数据中对数组指针寻址也需要一定的时间。
    而快速排序只需要将数组指针移动到相邻的区域即可。在堆排序中，会大量的随机存取数据；而在快速排序中，
    只会大量的顺序存取数据。随着数据规模的扩大，这方面的差距会明显增大。在这方面的时间开销来说，快速排序只会线性增长，而堆排序增加幅度很大，会远远大于线性。
    在快速排序中，每次数据移动都意味着该数据距离它正确的位置越来越近，而在堆排序中，类似将堆尾部的数据移到堆顶这样的操作只会使相应的数据远离它正确的位置，
    后续必然有一些操作再将其移动，即“做了好多无用功”。
    就像标准库中的sort，是通过 先快排，递归深度超过一个阀值就改成堆排，然后对最后的几个进行插入排序来实现的。
     */

}

