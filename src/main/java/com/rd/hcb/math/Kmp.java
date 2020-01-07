package com.rd.hcb.math;

/**
 * @author zlx
 * @date 2020-01-06 10:01
 */
public class Kmp {

    /**
     * 生成前缀数组
     *
     * @param pattern 比较的串
     * @param prefix  前缀表
     * @param n       表长度
     */
    static void prefixTbale(char[] pattern, int[] prefix, int n) {
        prefix[0] = 0;
        //比较的长度
        int len = 0;
        //开始比较的下标
        int i = 1;
        while (i < n) {
            //比较最后一位和前缀的下一位
            if (pattern[i] == pattern[len]) {
                len++;
                prefix[i] = len;
                i++;
            } else {
                //不一致就kmp找小一位比较
                //当len=0时，就无法再找更小的一位了
                if (len > 0) {
                    len = prefix[len - 1];
                    //此时前缀为len也就是0，然后在比较下一个i
                } else {
                    prefix[i] = len;
                    i++;
                }

            }
        }
    }

    //整体将前缀数组后移一位，第一位补-1
    static void move(int[] prefix, int n) {
        for (int i = n - 1; i > 0; i--) {
            prefix[i] = prefix[i - 1];
        }
        prefix[0] = -1;
    }

    static void kmpSearch(char[] text, char[] pattern) {
        int n = pattern.length;
        int[] prefix = new int[n];
        prefixTbale(pattern, prefix, n);
        move(prefix, n);

        //text[i],pattern[j]
        //len[m]      len[n]
        int i = 0;
        int j = 0;
        int m = text.length;
        while (i < m) {
            if (j == n - 1 && text[i] == pattern[j]) {
                System.out.println("location is:" + (i - j));
                //找到后继续往后匹配
                j = prefix[j];
            }
            //i和j相等，比较下一个元素
            if (text[i] == pattern[j]) {
                i++;
                j++;
            } else {
                //不相等根据kmp通过前缀表移动j的位置
                j = prefix[j];
                //若果j=-1了，就没有这个下表了，不能继续比较了，直接比较表示都不符合
                //继续比较下一个i和下一个j
                if (j == -1) {
                    i++;
                    j++;
                }
            }
        }

    }

    public static void main(String[] args) {
        String str = "ABABCABAA";
        String str12 = "ACACC";
        String str1 = "ABABABCABAABABCABAAWQDSAA";
        //{'A', 'B', 'A', 'B', 'C', 'A', 'B', 'A', 'A'};
        char[] pattern = str12.toCharArray();
        char[] text = str1.toCharArray();
        kmpSearch(text, pattern);


    }

}
