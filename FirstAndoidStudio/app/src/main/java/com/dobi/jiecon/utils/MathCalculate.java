package com.dobi.jiecon.utils;

import com.dobi.jiecon.UtilLog;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 *
 */
public class MathCalculate {

    public static int getMaxFromNumbers(Set<Integer> int_set) {
        int ret = 0;
        int cur_num;
        if (int_set.size() != 0) {
            for (int i: int_set) {
                if (ret < i) {
                    ret = i;
                }
            }
        }
        return ret;
    }
    public static float float2float(float number, int scale) {
        UtilLog.logWithCodeInfo("float number is "+ number, "float2float","MathCalculate");
        BigDecimal bd = new BigDecimal(number);
        float f = bd.setScale(scale, BigDecimal.ROUND_HALF_UP).floatValue();
        return f;
    }
}
