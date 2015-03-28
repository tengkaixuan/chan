package com.dobi.jiecon.test;

import com.dobi.jiecon.data.AppInfo;
import com.dobi.jiecon.utils.AppsNameFilter;
import com.dobi.jiecon.utils.IName;

import junit.framework.TestCase;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

public class TestUtilsFilter extends TestCase {
	public void testNameFilter() {
		LinkedList<IName> oriList = new LinkedList<IName>();
		AppInfo ai1 = new AppInfo("A", null);
		AppInfo ai2 = new AppInfo("B", null);
		AppInfo ai3 = new AppInfo("C", null);
		AppInfo ai4 = new AppInfo("com.android.launcher", null);
		oriList.add(ai1);
		oriList.add(ai2);
		oriList.add(ai3);
		oriList.add(ai4);
		Set ex = new HashSet();
		ex.add("com.android");
		ex.add("Camera");
		AppsNameFilter.matchFilter(oriList, ex);		
		
		System.out.println("filter start ...");
		
		for (Iterator<IName> iter = oriList.iterator(); iter.hasNext();)
			System.out.println(iter.next().getName());
		
		System.out.println();
		System.out.println("Keyword filter start ...");

		AppsNameFilter.keywordsFilter(oriList, ex);
		for (Iterator<IName> iter = oriList.iterator(); iter.hasNext();)
			System.out.println(iter.next().getName());	
		
	}
}
