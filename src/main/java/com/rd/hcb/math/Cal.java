package com.rd.hcb.math;

import java.math.BigDecimal;

/**
 * @author zlx
 * @date 2019-12-30 20:02
 */
public class Cal {
    /**
     * 计算值
     * @param str
     * @return
     */

    public static BigDecimal cal(String str) {
        if (str == null) {
            return null;
        }

        String fuhao = "";
        int index = 0;


        str = str.replaceAll("--", "+"); // 等价替换;
        str = str.replaceAll(" ", ""); // 去掉空格
        str = str.replaceAll("[\\[\\{]", "(").replaceAll("[\\]\\}]", ")");

        fuhao = "(";
        index = str.lastIndexOf(fuhao);
        if (index >= 0) {
            int rightIndex = str.indexOf(")", index);


            String left = str.substring(0, index);
            String right = "";
            if (rightIndex + 1 < str.length()) {
                right = str.substring(rightIndex + 1);
            }


            BigDecimal middle = cal(str.substring(index + 1, rightIndex));
            return cal(left + middle + right);
        }


        fuhao = "+";
        index = str.lastIndexOf(fuhao);
        if (index > 0) {
            BigDecimal left = cal(str.substring(0, index));
            BigDecimal right = cal(str.substring(index + 1));
            return left.add(right);
        }

        fuhao = "*";
        index = str.lastIndexOf(fuhao);
        if (index > 0) {
            BigDecimal left = cal(str.substring(0, index));
            BigDecimal right = cal(str.substring(index + 1));
            return left.multiply(right);
        }

        fuhao = "/";
        index = str.lastIndexOf(fuhao);
        if (index > 0) {
            BigDecimal left = cal(str.substring(0, index));
            BigDecimal right = cal(str.substring(index + 1));
            return left.divide(right,10,BigDecimal.ROUND_HALF_UP);
        }

        fuhao = "-";
        index = str.lastIndexOf(fuhao);
        if (index == 0) { // 负数处理
            BigDecimal result = cal(str.substring(index + 1));
            if (result.compareTo(new BigDecimal("0")) == -1) { // 小于0
                return result.abs(); // 绝对值
            } else {
                return result.negate(); // 相反数
            }
        } else if (index > 0) {
            BigDecimal left = cal(str.substring(0, index));
            BigDecimal right = cal(str.substring(index + 1));
            return left.subtract(right);
        }
        return new BigDecimal(str);
    }
}
