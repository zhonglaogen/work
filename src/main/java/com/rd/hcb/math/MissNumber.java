package com.rd.hcb.math;

import java.util.ArrayList;
import java.util.List;

/**
 * 找到数组中消失的数据
 * @author zlx
 * @date 2019-11-18 11:53
 */
public class MissNumber {

    public List<Integer> findDisappearedNumbers(int[] nums) {
        List<Integer> res = new ArrayList<Integer>();
        int n = nums.length;
        if (n == 0) {
            return res;
        }
        for (int i = 0; i < n; i++) {
            nums[nums[i] % n] += n;
        }
        for (int i = 1; i < n; i++) {
            if (nums[i] <= n) {
                res.add(i);
            }
        }
        if (nums[0] <= n) {
            res.add(n);
        }
        return res;
    }

}
