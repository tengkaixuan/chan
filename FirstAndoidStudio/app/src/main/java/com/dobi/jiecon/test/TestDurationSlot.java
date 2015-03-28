package com.dobi.jiecon.test;
import com.dobi.jiecon.utils.DurationSlot;

import junit.framework.TestCase;

public class TestDurationSlot extends TestCase {
	public void testCase1() {
		DurationSlot ds = new DurationSlot();
		ds.addDay(3);
		ds.addHour(0);
		ds.addMin(61);
		ds.addSec(61);
		ds.alignToHour();
		assertTrue(ds.getDay() == 0);
		assertTrue(ds.getHour() == 73);
		assertTrue(ds.getMin() == 2);
		assertTrue(ds.getSec() == 1);
	}

	public void testCase2() {
		DurationSlot ds = new DurationSlot();
		ds.addDay(3);
		ds.addHour(100);
		ds.addMin(61);
		ds.addSec(61);
		ds.alignToHour();
		assertTrue(ds.getDay() == 0);
		assertTrue(ds.getHour() == 173);
		assertTrue(ds.getMin() == 2);
		assertTrue(ds.getSec() == 1);
		ds.alignToDay();
		assertTrue(ds.getDay() == 7);
		assertTrue(ds.getHour() == (173 - 168));
	}

	public void testCase3() {
		DurationSlot ds = new DurationSlot();
		ds.addDay(3);
		ds.addHour(100);
		ds.addMin(61);
		ds.addSec(61);

		DurationSlot ds1 = new DurationSlot();
		ds1.addDay(3);
		ds1.addHour(0);
		ds1.addMin(61);
		ds1.addSec(61);
		
		assertTrue(ds.isGreater(ds1));
		
		ds = ds.addDuration(ds1);
		ds.alignToHour();

		assertTrue(ds.getDay() == 0);
		assertTrue(ds.getHour() == 246);
		assertTrue(ds.getMin() == 4);
		assertTrue(ds.getSec() == 2);
		ds.alignToDay();
		assertTrue(ds.getDay() == 10);
		assertTrue(ds.getHour() == 6);
	}

	public void testCase4() {
		DurationSlot ds = new DurationSlot();
		ds.addDay(3);
		ds.addHour(100);
		ds.addMin(91);
		ds.addSec(91);

		DurationSlot ds1 = new DurationSlot();
		ds1.addDay(3);
		ds1.addHour(0);
		ds1.addMin(91);
		ds1.addSec(91);
		ds = ds.addDuration(ds1);
		ds.alignToHour();

		assertTrue(ds.getDay() == 0);
		assertTrue(ds.getHour() == 247);
		assertTrue(ds.getMin() == 5);
		assertTrue(ds.getSec() == 2);
		ds.alignToDay();
		assertTrue(ds.getDay() == 10);
		assertTrue(ds.getHour() == 7);
	}
	public void testCase5() {
		DurationSlot ds = new DurationSlot();		
		ds.addSec(889502);
		ds.alignToHour();
		assertTrue(ds.getDay() == 0);
		assertTrue(ds.getHour() == 247);
		assertTrue(ds.getMin() == 5);
		assertTrue(ds.getSec() == 2);
		ds.alignToDay();
		assertTrue(ds.getDay() == 10);
		assertTrue(ds.getHour() == 7);

	}
	
}
