package com.rd.hcb.math;

/**
 * 输入，输出，正确，确定（基本操作组成的序列），可行（基本操作都可实现，在常数时间内完成），有穷性（又穷次操作）
 * 好算法：正确（正确的处理），健壮（异常操作），可读，效率（快，资源少）
 * （算法+数据结构 ）* 效率=计算
 * 1.利用勾股定理 的特点，345 求垂线
 * 2.利用射线取点连接线段，相似三角形的特性求等量分割
 * <p>
 * <p>
 * 有穷性的测试，规则为：
 * |    n=1 return
 * |    n%2 == 0   n=n/2
 * |    else n=3*n+1
 *
 * @author zlx
 * @date 2019-11-12 19:39
 */
public class Hailstone {
    static int hailstone(int n) {


        int len = 0;
        while (1 < n) {
            if (n % 2 == 0) {
                n = n / 2;
            } else {
                n = 3 * n + 1;
            }
            len++;
        }

//        n = n % 2 == 0 ? (n *= 2) : (n = n / 3 + 1);
        return len;
    }

    public static void main(String[] args) {
        System.out.println(hailstone(27));
    }
}
