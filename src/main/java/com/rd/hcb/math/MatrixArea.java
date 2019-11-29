package com.rd.hcb.math;

import java.util.Arrays;

/**
 * @author zlx
 * @date 2019-11-21 09:25
 */
public class MatrixArea {

    public static void main(String[] args) {

        int[][] a = {{1, 2, 3}, {1, 2, 3}};
    }


    // ["1","0","1","0","0"],
    //  ["1","0","1","1","1"],
    //  ["1","1","1","1","1"],
    //  ["1","0","0","1","0"]
    //
    //最大矩阵，长*宽
    public int maximalRectangle(char[][] matrix) {
        if (matrix.length == 0) return 0;
        int m = matrix.length;
        int n = matrix[0].length;

        int[] left = new int[n]; // initialize left as the leftmost boundary possible
        int[] right = new int[n];
        int[] height = new int[n];

        Arrays.fill(right, n); // initialize right as the rightmost boundary possible

        int maxarea = 0;
        for (int i = 0; i < m; i++) {
            int cur_left = 0, cur_right = n;
            // update height
            for (int j = 0; j < n; j++) {
                if (matrix[i][j] == '1') height[j]++;
                else height[j] = 0;
            }
            // update left
            for (int j = 0; j < n; j++) {
                if (matrix[i][j] == '1') left[j] = Math.max(left[j], cur_left);
                else {
                    //下一行比较时作用
                    left[j] = 0;
                    //于上一行作比较
                    cur_left = j + 1;
                }
            }
            // update right
            for (int j = n - 1; j >= 0; j--) {
                if (matrix[i][j] == '1') right[j] = Math.min(right[j], cur_right);
                else {
                    //于下一行作比较·,这样的列高度为0，所以不用担心会参与计算
                    right[j] = n;
                    cur_right = j;
                }
            }
            // update area
            for (int j = 0; j < n; j++) {
                maxarea = Math.max(maxarea, (right[j] - left[j]) * height[j]);
            }
            return maxarea;
        }
        return 1;

    }
/**
 * Left:
 *
 * 考虑哪些因素会导致矩形左边界的改变。由于当前行之上的全部0已经考虑在当前版本的left中，唯一能影响left就是在当前行遇到0。
 *
 * 因此我们可以定义:
 *
 * new_left[j] = max(old_left[j], cur_left)
 * cur_left是我们遇到的最右边的0的序号加1。当我们将矩形向左 “扩展” ，我们知道，不能超过该点，否则会遇到0。
 *
 * Right:
 */





}
