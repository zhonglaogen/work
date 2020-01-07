package com.rd.hcb.math;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * All rights Reserved, Designed By www.rongdasoft.com
 *
 * @version V1.0
 * @Title: CalculateNumUtil
 * @Description:财务数据四则运算 先算乘除，后算加减
 * @author:jichao
 * @date: 2018/12/27
 * @Copyright: 2018/12/27 www.rongdasoft.com
 * Inc. All rights reserved.
 */

public class CalculateNumUtil {
    @Test
    public void test123() {
        BigDecimal cal = cal("(1+2)/0");
        System.out.println(cal);

    }

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
            return left.divide(right, 10, BigDecimal.ROUND_HALF_UP);
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


    /**
     * 获取集合中和目标值最接近的值
     * 目标值不超过1000000(100万)解决方案
     *
     * @param m    目标值 不超过1000000
     * @param data 数据集合 四舍五入取小数点两位  不包含负数
     * @return
     */
    public static double nestSum(List<Double> data, double m) {
        data = data.stream().map(e -> new BigDecimal(e).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()).collect(Collectors.toList());
        int size = data.size();
        int[] value = new int[size];
        for (int i = 0; i < size; i++) {
            value[i] = (int) (data.get(i) * 100);
        }
        double v = nestSum(m, value);
        if (Math.abs(v - m) < 0.02) {
            return v;
        } else {
            return nestSum(m + 0.02, value);
        }

    }

    /**
     * 动态规划运算过程
     *
     * @param m
     * @param value
     * @return
     */
    public static double nestSum(double m, int[] value) {
        int n = (int) (m * 100);
        int[] result = new int[n + 1];
        for (int i = 1; i <= value.length; i++) {
            for (int j = n; j >= 1; j--) {
                if (j >= value[i - 1]) {
                    result[j] = Math.max(result[j], result[(j - value[i - 1])] + value[i - 1]);
                }
            }
        }

        return (double) result[n] / 100;
    }


    /**
     * 交叉运算合计逻辑
     * 获取数据集合中所有数的组合合计值
     *
     * @param candidates 数据集合
     * @param target     目标值
     * @return
     */
    public static Set<Double> combinationSum2(List<Double> candidates, Double target) {
        int len = candidates.size();
        //所有运算结果
        Set<Double> resAll = new HashSet<>();
        //正确的运算结果
        Set<Double> resCorrect = new HashSet<>();
        Set<Double> totalSet = new HashSet<>();
        if (len == 0) {
            return totalSet;
        }
        //如果目标集合相加总数都没有目标数大 并且误差大于10% 则提前跳出不需要计算
        double sum = candidates.stream().reduce(Double::sum).orElse(0.0);
        if (sum < target) {
            double percent = BigDecimalUtil.div(sum, target, 2);
//            误差大于10%
            if (percent < 0.90) {
                return totalSet;
            }
        }

        // 先将数组排序，这一步很关键
        candidates = candidates.stream().sorted().collect(Collectors.toList());
        findCombinationSum2(candidates, 0, len, target, new Stack<>(), resAll, resCorrect);
        if (CollectionUtils.isEmpty(resCorrect)) {
            totalSet.addAll(resAll);
        } else {
            totalSet.addAll(resCorrect);
        }
        return totalSet;
    }


    // residue 表示剩余，这个值一开始等于 target，基于题目中说明的"所有数字（包括目标数）都是正整数"这个条件
    // residue 在递归遍历中，只会越来越小
    public static void findCombinationSum2(List<Double> candidates, int begin, int len, double residue, Stack<Double> stack, Set<Double> resAll, Set<Double> resCorrect) {
        //计算值和目标值误差0.02就提前跳出
        if (Math.abs(residue) < 0.02) {
            resCorrect.add(new ArrayList<>(stack).stream().reduce(Double::sum).orElse(0.0));
            return;
        }
        if (CollectionUtils.isNotEmpty(resCorrect)) {
            return;
        }
        for (int i = begin; i < len; i++) {
            if (CollectionUtils.isNotEmpty(resCorrect)) {
                continue;
            }
            // 这一步之所以能够生效，其前提是数组一定是排好序的，这样才能保证：
            // 在递归调用的统一深度（层）中，一个元素只使用一次。
            // 这一步剪枝操作基于 candidates 数组是排序数组的前提下
            if (i > begin && candidates.get(i) == candidates.get(i - 1)) {
                continue;
            }
            stack.add(candidates.get(i));
            resAll.add(new ArrayList<>(stack).stream().reduce(Double::sum).orElse(0.0));
//            System.out.println(stack + "==" + stack.stream().reduce(Double::sum).orElse(0.0));
            // 【关键】因为元素不可以重复使用，这里递归传递下去的是 i + 1 而不是 i
            findCombinationSum2(candidates, i + 1, len, residue - candidates.get(i), stack, resAll, resCorrect);
            stack.pop();
        }
    }


    /**
     * 获取最接近值
     *
     * @param x
     * @param src
     * @return
     */
    public static Double getApproximate(Double x, List<Double> src) {
        if (src == null) {
            return 0.0;
        }
        if (src.size() == 1) {
            return src.get(0);
        }
        Double minDifference = Math.abs(src.get(0) - x);
        int minIndex = 0;
        for (int i = 1; i < src.size(); i++) {
            Double temp = Math.abs(src.get(i) - x);
            if (temp < minDifference) {
                minIndex = i;
                minDifference = temp;
            }
        }
        return src.get(minIndex);
    }


    /**
     * 位运算
     * 数据集合所有组合 运算合计
     * 返回运算结果
     *
     * @param keys
     * @param kill
     * @return
     */
    static Set<Double> getNum(List<Double> keys, double kill) {
        Set<Double> totalSet = new HashSet<>();
        //如果目标集合相加总数都没有目标数大 则提前跳出不需要计算
        double sum = keys.stream().reduce(Double::sum).orElse(0.0);
        if (sum - kill < 0.02) {
            return totalSet;
        }

        int n = keys.size();
        int nbit = 1 << n;
        double in;

        for (int i = 0; i < nbit; i++) {
            in = 0;
            for (int j = 0; j < n; j++) {
                int tmp = 1 << j; // 由0到n右移位
                if ((tmp & i) != 0) { // 与运算，同为1时才会是1
                    System.out.print(keys.get(j) + " + ");
                    in += keys.get(j);
                    //误差小于0.02 提前跳出
                    if (Math.abs(in - kill) < 0.02) {
                        totalSet.clear();
                        totalSet.add(in);
                        return totalSet;
                    }
                    //本轮循环计算结果大于目标值 提前跳出
                    if (in > kill) {
                        break;
                    }
                }
            }
            System.out.println("-----" + in);
            totalSet.add(in);
        }
        return totalSet;
    }


    /**
     * 通过消除法进行合计计算
     *
     * @param data   数据
     * @param target 目标值
     * @return 近似值
     */
    public static double getSum(List<Double> data, double target) {
        int top = 0, down = 1;
        double sum = 0;
        while (top < data.size() && Math.abs(sum - target) > 0.02) {
            Double topVal = data.get(top);
            if (down < data.size() - 1 && topVal.equals(data.get(down))) {
                sum += topVal;
                top++;
                down++;
                continue;
            } else if (down == data.size() - 1 && topVal.equals(data.get(down))) {
                if (Math.abs(sum + topVal - target) < 0.02) {
                    sum += topVal;
                    break;
                } else {
                    sum += 2 * topVal;
                }
            }
            // 存储子集合
            double temp = 0;
            while (temp < topVal && down < data.size()) {
                temp += data.get(down++);
            }
            if (temp == topVal) {
                top = down++;
                sum += topVal;
            } else {
                top++;
                down = top + 1;
            }
        }
        return sum;
    }


    /**
     * 获取误差为百分之10范围内的所有组合
     *
     * @param data   数据
     * @param target 目标值
     * @return 所有组合
     */
    public static List<List<Double>> getNestSum(List<Double> data, double target) {
        if (data.size() == 0) {
            return new ArrayList<>();
        }
        Collections.sort(data);
        return sol(data, 0, target);
    }


    public static List<List<Double>> sol(List<Double> data, int start, double target) {
        List<List<Double>> res = new ArrayList<>();
        for (int i = start; i < data.size(); ++i) {
            // 避免重复计算
            if (i > start && data.get(i).equals(data.get(i - 1))) {
                continue;
            }
            if (Math.abs(data.get(i) - target) < 0.02) {
                List<Double> temp = new ArrayList<>();
                temp.add(data.get(i));
                res.add(temp);
                break;
            } else if (Math.abs(data.get(i) - target) < (target * 0.1)) {
                List<Double> temp = new ArrayList<>();
                temp.add(data.get(i));
                res.add(temp);
                break;
            } else if (data.get(i) > target) {
                break;
            } else {
                List<List<Double>> r = sol(data, i + 1, target - data.get(i));
                for (List<Double> item : r) {
                    item.add(0, data.get(i));
                    res.add(item);
                }
            }
        }
        return res;
    }


    @Test
    public void test() {
        List<Double> candidates = Arrays.asList(10.0, 5.0, 5.0, 10.0, 5.0, 5.0, 1.0, 2.0);
        double sum = getSum(candidates, 25);
        System.out.println(sum);
    }

}
