package com.dobi.jiecon.test;

import java.util.Iterator;
import java.util.LinkedList;

import junit.framework.TestCase;

import com.dobi.jiecon.data.AllAppManager;
import com.dobi.jiecon.data.AppManager;
import com.dobi.jiecon.data.SingleAppRecord;
import com.dobi.jiecon.service.MockService;

public class TestMockService extends TestCase{
	public void testDuration() {
		AllAppManager data = MockService.getMockAppData();
		AppManager app1 = data.get("1");
		
		String name = 	app1.getApp().getName();

		System.out.println("----------------" + app1.getApp().getName()
				+ "----------------\n");

		LinkedList<SingleAppRecord> ls = app1.getAllItem();

		for (Iterator iter = ls.iterator(); iter.hasNext();) {
			SingleAppRecord rec = (SingleAppRecord) iter.next();			
			System.out.println("Start:    " + rec.getStartTime().toString());
			System.out.println("End:      " + rec.getEndDate().toString());
			System.out.println("Duration: " + rec.getDayDuration() + " Days " + rec.getHourDuration() + " Hours "+ rec.getMinDuration() + " Mintues "+ rec.getSecDuration() + " Seconds");
			System.out.println("");			
		}	
		
		AppManager app2 = data.get("2");
		
		name = app1.getApp().getName();

		System.out.println("----------------" + app2.getApp().getName()
				+ "----------------\n");

		ls = app1.getAllItem();

		for (Iterator iter = ls.iterator(); iter.hasNext();) {
			SingleAppRecord rec = (SingleAppRecord) iter.next();			
			System.out.println("Start:    " + rec.getStartTime().toString());
			System.out.println("End:      " + rec.getEndDate().toString());
			System.out.println("Duration: " + rec.getDayDuration() + " Days " + rec.getHourDuration() + " Hours "+ rec.getMinDuration() + " Mintues "+ rec.getSecDuration() + " Seconds");
			System.out.println("");			
		}	
	}
}
