package com.rd.hcb;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 排序，爬楼梯，纸条
 *
 * @author zlx
 * @date 2020-01-08 12:04
 */
public class TestZhengZe {
    @Test
    public void test() {
        String a = "流动比率①①①①";
        String mastar = "[①②③④⑤⑥⑦⑧]{1}\\s*[①②③④⑤⑥⑦⑧]{0,1}\\s*[①②③④⑤⑥⑦⑧]{0,1}$";

        Pattern compile = Pattern.compile(mastar);
        Matcher matcher = compile.matcher(a);
        while (matcher.find()) {
            a = a.replace(matcher.group(), "");
        }
        System.out.println(a);

    }


    void mySwap(int[] arry, int i, int j) {
        int temp = arry[i];
        arry[i] = arry[j];
        arry[j] = temp;
    }


    @Test
    public void test1() {
//        int[] arry1 = new int[]{6, 1, 7, 9, 2, 3,3};
        int[] arry1 = new int[]{6, 1, 7, 9, 2, 3, 3};
        int[] arry2 = new int[]{6, 1, 2, 7, 9, 3};
        int[] temp = new int[arry1.length];
//        gui_bing(arry1, 0, arry1.length - 1,temp);
//        quick_sort(arry1, 0, arry1.length - 1);
//        quick_sort2(arry1, 0, arry1.length - 1);
        dui_sort(arry1,arry1.length);
        for (int i = 0; i < arry1.length; i++) {
            System.out.print(arry1[i] + "\t");
        }
    }

    private void gui_bing(int[] arry1, int L, int R, int[] temp) {
        if (L < R) {
            int mid = (L + R) / 2;
            gui_bing(arry1, L, mid, temp);
            gui_bing(arry1, mid + 1, R, temp);
            merage(arry1, L, mid, R, temp);
        }


    }

    private void merage(int[] arry1, int L, int Mid, int R, int[] temp) {
        int i = L;
        int m = Mid;
        int j = Mid + 1;
        int n = R;
        int k = 0;
        while (i <= m && j <= n) {
            if (arry1[i] < arry1[j]) {
                temp[k++] = arry1[i++];
            } else {
                temp[k++] = arry1[j++];
            }
        }
        while (i <= m) {
            temp[k++] = arry1[i++];
        }
        while (j <= n) {
            temp[k++] = arry1[j++];
        }
        for (int p = 0; p < k; p++) {
            arry1[L + p] = temp[p];
        }
    }

    private void quick_sort(int[] arry1, int L, int R) {
        if (L >= R) {
            return;
        }
        int i = L;
        int j = R;
        int key = arry1[L];
        while (i < j) {
            while (i < j && arry1[j] > key) {
                j--;
            }
            if (i < j) {
                mySwap(arry1, i, j);
                i++;
            }
            while (i < j && arry1[i] < key) {
                i++;
            }
            if (i < j) {
                mySwap(arry1, i, j);
                j--;
            }
        }
        arry1[i] = key;
        quick_sort(arry1, L, i - 1);
        quick_sort(arry1, i + 1, R);

    }

    private void quick_sort2(int[] arry1, int L, int R) {
        if (L < R) {
            int key = partation(arry1, L, R);
            quick_sort2(arry1, L, key - 1);
            quick_sort2(arry1, key + 1, R);
        }
    }

    private int partation(int[] arry1, int L, int R) {
        int key = arry1[R];
        int j = L;
        for (int i = L; i < R; i++) {
            if (arry1[i] < key) {
                int temp = arry1[i];
                arry1[i] = arry1[j];
                arry1[j] = temp;
                j++;
            }
        }
        mySwap(arry1, j, R);
        return j;
    }

    void heapify(int[] arry, int n, int i) {
        int left = 2 * i + 1;
        int right = 2 * i + 2;
        int max = i;
        if (left < n && arry[left] > arry[max]) {
            max = left;
        }
        if (right < n && arry[right] > arry[max]) {
            max = right;
        }
        if (max != i) {
            mySwap(arry, i, max);
            heapify(arry, n, max);
        }

    }
    //总结点个数的2^n -1 每层节点为2^（n-1）,n为高度，所以要求最后一层的，就要总+ 1 在/2
    //满二叉树叶子节点的个数为总结点个数 + 1/2
    //当总结点个数为偶数（也就是叶子节点不成对的出现），
    // 会影响到最后一个父节点的位置n = n + 1 (n为孤儿)

    //每当无孩子父亲增加节点是，都会影响到最后一个父亲的位置
    //为什么（last_node - 1）/2 是最后一个父亲，叶子节点个数 + 非叶子节点个数 = 总个数
    //正常情况满二叉树叶子节点个数就是 总节点个数,根据此思想父亲节点投影孩子节点，多的点，
    //每次多两个点，/2 的结果值就会+1，父亲节点就会加一个，
    // 投影思想，加上一个父亲节点，和当前父亲节点，就会让当前父亲节点 + 1，
    // 实际上/2解决的是分开叶子节点和飞叶子节点，随着叶子节点增加，末尾父亲点也会加
    // 3/2 2/2等效，4/2 5/2 所以计算机中以0为开始的根节点就等效于 1/2 0/2，
    // 为了定位以0开始的节点，所以要 (x - 2 )/2 ,其中x为总节点的个数
    //
    void build_heap(int[] arry, int n) {
        //由下向上的构建堆
        int last_node = n - 1;
        int parent = (last_node - 1) / 2;
        for (int i = parent; i >= 0 ; i--) {
            heapify(arry,n,i);
        }
    }

    void dui_sort(int[] arry, int n){
        build_heap(arry,n);
        for (int i = arry.length -1; i > 0; i--){
            mySwap(arry,0,i);
            build_heap(arry,i);
        }
    }


    @Test
    public void ss() {

        String reg2 = "^((19)|(20))\\d{2}\\.[01]?\\d$";
        String reg3 = "^((19)|(20))\\d{2}\\.[01]?\\d\\.[0123]?\\d$";
        String reg4 = "^((19)|(20))\\d{2}\\.[01]?\\d(\\.[0123]?\\d)?$";


        String fx = "xy";
        String year = "1998.11.1.1";
        boolean matches = Pattern.matches(reg4, year);
        System.out.println(matches);
    }


}
