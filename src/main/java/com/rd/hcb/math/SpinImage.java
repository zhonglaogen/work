package com.rd.hcb.math;

/**
 * 旋转图像，另外可旋转两个，外面的依次旋转
 *
 * @author zlx
 * @date 2019-11-18 11:57
 */
public class SpinImage {


    public void rotate(int[][] matrix) {
        int len = matrix.length;
        for (int i = 0; i < len; i++) {
            for (int j = i; j < len; j++) {
                int temp = matrix[j][i];
                matrix[j][i] = matrix[i][j];
                matrix[i][j] = temp;
            }
        }
        for (int i = 0; i < len; i++) {
            for (int j = 0; j < len / 2; j++) {
                int temp = matrix[i][j];
                matrix[i][j] = matrix[i][len - 1 - j];
                matrix[i][len - 1 - j] = temp;
            }
        }
    }
}
