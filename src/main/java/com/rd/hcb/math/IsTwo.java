package com.rd.hcb.math;

/**
 * 2的次幂
 *
 * @author zlx
 * @date 2019-11-18 13:53
 */
public class IsTwo {

    public boolean isPowerOfTwo1(int n) {
        return (n > 0) && (1 << 30) % n == 0;
    }

    public boolean isPowerOfTwo2(int n) {
        return (n > 0) && (n & n - 1) == 0;
    }

    public boolean isPowerOfTwo3(int n) {
        return (n > 0) && (n & -n) == n;
    }

    public static void main(String[] args) {
        int n = -1;
        for (int i = 31; i >= 0; i--) {
            System.out.print(n >>> i & 1 );
            if (i % 4 == 0){
                System.out.print("\t");
            }
        }
    }


}
