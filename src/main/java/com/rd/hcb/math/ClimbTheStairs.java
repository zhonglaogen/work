package com.rd.hcb.math;

/**
 * @author zlx
 * @date 2019-11-28 16:10
 */

/**
 * 爬楼梯问题其实质就是斐波那契数列！
 */
public class ClimbTheStairs {
    int total;

    // 1.递归调用
    public int fib01(int n) {
        if (n == 1 || n == 2)
            total = n;
        else
            total = fib01(n - 2) + fib01(n - 1);
        return total;
    }

    // 2.三目运算符
    public int fib02(int n) {
        return (n == 1 || n == 2) ? n : fib02(n - 2) + fib02(n - 1);
    }

    // 3.备忘录法
    public int dfs(int n, int[] array) {
        if (array[n] != 0) {
            return array[n];
        } else {
            array[n] = dfs(n - 1, array) + dfs(n - 2, array);
            return array[n];
        }
    }

    public int fib03(int n) {
        if (n == 0) {
            return 1;
        }
        if (n == 1 || n == 2) {
            return n;
        } else {
            int[] array = new int[n + 1];
            array[1] = 1;
            array[2] = 2;
            return dfs(n, array);
        }
    }

    // 4.动态规划法 (利用数组来存储)
    public int fib04(int n) {
        if (n == 0)
            return 1;
        int[] array = new int[n + 1];
        array[0] = 1;
        array[1] = 1;
        for (int i = 2; i <= n; i++) {
            array[i] = array[i - 1] + array[i - 2];
        }
        return array[n];
    }

    // 5.状态压缩法(又称滚动数组、滑动窗口，用于优化动态规划法的空间复杂度)
    public int fib05(int n) {
        if (n == 1 || n == 0)
            return 1;
        n = n - 1;
        int result = 0;
        int zero = 1;
        int first = 1;
        while (n > 0) {
            result = zero + first;
            zero = first;
            first = result;
            n--;
        }
        return result;
    }

    // 6.斐波那契数列的通项公式
    public int fib06(int n) {
        if (n == 0)
            return 1;
        if (n == 1 || n == 2)
            return n;
        int result = (int) Math.floor(
                1 / Math.sqrt(5) * (Math.pow((1 + Math.sqrt(5)) / 2, n + 1) - Math.pow((1 - Math.sqrt(5)) / 2, n + 1)));
        return result;
    }

    /**
     *
     * @author Cxl
     * @version 2013-1-8 下午2:29:31
     * @Contract http://wzwahl36.net
     * @param n 总的台阶数
     * @param m 一次可以走的最大楼梯阶数
     * @return
     */
    private static int getStepNum(int n, int m) {
        int sumStep = 0;
        //总台阶数为0时，终止递归循环,有点动态规划的思想，求出底层的方法次数，向上累加，加上步数为自己当前台阶（也就是1）
        if (n == 0) {
            return 1;
        }
        if (n >= m) {
            //如果n大于每步最大台阶数，则设置第一步为m之内的一个台阶数，然后递归循环
            for (int i = 1; i <= m; i++) {
                sumStep += getStepNum(n - i, m);
            }
        }
        //如果n小于m，则将一步最大台阶数缩小为n，重新递归
        else {
            sumStep = getStepNum(n, n);
        }
        return sumStep;
    }

    public static void main(String[] args) throws IllegalAccessException, InstantiationException {
//        int i = ClimbTheStairs.class.newInstance().fib06(4);
//        System.out.println(i);
        int stepNum = getStepNum(4, 1);
        System.out.println(stepNum);
    }


}


