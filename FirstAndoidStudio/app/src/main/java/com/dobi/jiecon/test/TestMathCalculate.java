package com.dobi.jiecon.test;

import com.dobi.jiecon.utils.MathCalculate;

import junit.framework.TestCase;

import java.util.HashSet;
import java.util.Set;

public class TestMathCalculate extends TestCase {
    public void testCase1() {
        Set<Integer> int_set = new HashSet<Integer>();
        int_set.add(100);
        int_set.add(90);
        int_set.add(54);
        int_set.add(94);
        int_set.add(10);
        assertEquals(MathCalculate.getMaxFromNumbers(int_set), 100);
    }

    public void testCase2() {
        Set<Integer> int_set = new HashSet<Integer>();

        assertEquals(MathCalculate.getMaxFromNumbers(int_set), 0);
    }

    public void testLong2Float() {


        long long1 = 15;
        int scale1 = 2;
        float result = MathCalculate.float2float(long1 / 60.0f, scale1);
        assertEquals(0.25, result, 1e-7);
        long1 = 20;
        scale1 = 1;
        result = MathCalculate.float2float(long1 / 60.0f, scale1);
        assertEquals(0.30, result, 1e-7);
    }
}
