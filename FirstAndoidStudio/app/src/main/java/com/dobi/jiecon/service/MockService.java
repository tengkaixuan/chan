package com.dobi.jiecon.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

import com.dobi.jiecon.data.AllAppManager;
import com.dobi.jiecon.data.AppManager;
import com.dobi.jiecon.data.AppInfo;
import com.dobi.jiecon.data.SingleAppRecord;
import com.dobi.jiecon.utils.TimeSlot;

public class MockService {

	private static DateFormat  df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static AllAppManager AllData = new AllAppManager();
	
	public static AllAppManager getMockAppData() {
		init();
		return AllData;
	}

	private static void init() {	
		
		// TODO Auto-generated method stub
		AllData.put("1", app1());
		AllData.put("2", app2());
		AllData.put("3", app3());
		AllData.put("4", app4());
		AllData.put("5", app5());
		AllData.put("6", app6());
		AllData.put("7", create7());
		AllData.put("8", create8());
		AllData.put("9", create9());
		AllData.put("10", create10());
		AllData.put("11", create11());
		AllData.put("12", create12());
	}

	private static AppManager app1() {		
		LinkedList<TimeSlot> ts = new LinkedList<TimeSlot>();
		ts.add(new TimeSlot(ConvertToDate( "2000-01-02 11:30:24"), ConvertToDate("2000-01-02 11:30:24")));		
		ts.add(new TimeSlot(ConvertToDate( "2002-01-04 11:30:24"), ConvertToDate("2002-11-02 12:30:24")));		
		ts.add(new TimeSlot(ConvertToDate( "2003-01-02 11:30:24"), ConvertToDate("2003-01-02 11:30:24")));		
		ts.add(new TimeSlot(ConvertToDate( "2004-01-04 11:30:24"), ConvertToDate("2004-11-02 12:30:24")));	
		ts.add(new TimeSlot(ConvertToDate( "2005-01-02 11:30:24"), ConvertToDate("2005-01-02 11:30:24")));		
		ts.add(new TimeSlot(ConvertToDate( "2006-01-04 11:30:24"), ConvertToDate("2006-11-02 12:30:24")));		
		return createSingleAppRecord("WeChat", "Social", ts);
	}

	private static AppManager app2() {
		LinkedList<TimeSlot> ts = new LinkedList<TimeSlot>();
		ts.add(new TimeSlot(ConvertToDate( "2000-01-02 11:30:24"), ConvertToDate("2000-03-02 11:30:24")));		
		ts.add(new TimeSlot(ConvertToDate( "2002-01-04 11:30:24"), ConvertToDate("2002-12-02 12:30:24")));		
		ts.add(new TimeSlot(ConvertToDate( "2003-01-02 11:30:24"), ConvertToDate("2003-03-02 11:30:24")));		
		ts.add(new TimeSlot(ConvertToDate( "2004-01-04 11:30:24"), ConvertToDate("2004-11-02 12:30:24")));	
		ts.add(new TimeSlot(ConvertToDate( "2005-01-02 11:30:24"), ConvertToDate("2005-05-02 11:30:24")));		
		ts.add(new TimeSlot(ConvertToDate( "2006-01-04 11:30:24"), ConvertToDate("2006-12-02 12:30:24")));	
		return createSingleAppRecord("QQ", "IM", ts);

	}

	private static AppManager app3() {
		LinkedList<TimeSlot> ts = new LinkedList<TimeSlot>();
		ts.add(new TimeSlot(ConvertToDate( "2000-01-02 11:30:24"), ConvertToDate("2004-01-02 11:30:24")));		
		ts.add(new TimeSlot(ConvertToDate( "2002-01-04 11:30:24"), ConvertToDate("2002-11-02 12:30:24")));		
		return createSingleAppRecord("BaiduYun", "Tool", ts);
	}

	private static AppManager app4() {
		LinkedList<TimeSlot> ts = new LinkedList<TimeSlot>();
		ts.add(new TimeSlot(ConvertToDate( "2000-01-02 11:30:24"), ConvertToDate("2004-01-02 11:30:24")));		
		ts.add(new TimeSlot(ConvertToDate( "2002-01-04 11:30:24"), ConvertToDate("2002-11-02 12:30:24")));		
		return createSingleAppRecord("TaoBao", "Business", ts);
	}

	private static AppManager app5() {
		LinkedList<TimeSlot> ts = new LinkedList<TimeSlot>();
		ts.add(new TimeSlot(ConvertToDate( "2000-01-02 11:30:24"), ConvertToDate("2004-01-02 11:30:24")));		
		ts.add(new TimeSlot(ConvertToDate( "2002-01-04 11:30:24"), ConvertToDate("2002-11-02 12:30:24")));		
		return createSingleAppRecord("CCB", "Finance", ts);
	}

	private static AppManager app6() {
		LinkedList<TimeSlot> ts = new LinkedList<TimeSlot>();
		ts.add(new TimeSlot(ConvertToDate( "2000-01-02 11:30:24"), ConvertToDate("2004-01-02 11:30:24")));		
		ts.add(new TimeSlot(ConvertToDate( "2002-01-04 11:30:24"), ConvertToDate("2002-11-02 12:30:24")));		
		return createSingleAppRecord("Android Marketing", "Tools", ts);
	}

	private static AppManager create7() {
		LinkedList<TimeSlot> ts = new LinkedList<TimeSlot>();
		ts.add(new TimeSlot(ConvertToDate( "2000-01-02 11:30:24"), ConvertToDate("2004-01-02 11:30:24")));		
		ts.add(new TimeSlot(ConvertToDate( "2002-01-04 11:30:24"), ConvertToDate("2002-11-02 12:30:24")));		
		return createSingleAppRecord("Camara", "Life", ts);
	}

	private static AppManager create8() {
		LinkedList<TimeSlot> ts = new LinkedList<TimeSlot>();
		ts.add(new TimeSlot(ConvertToDate( "2000-01-02 11:30:24"), ConvertToDate("2004-01-02 11:30:24")));		
		ts.add(new TimeSlot(ConvertToDate( "2002-01-04 11:30:24"), ConvertToDate("2002-11-02 12:30:24")));		
		return createSingleAppRecord("Dazhongdianpin", "Shoping", ts);
	}

	private static AppManager create9() {
		LinkedList<TimeSlot> ts = new LinkedList<TimeSlot>();
		ts.add(new TimeSlot(ConvertToDate( "2000-01-02 11:30:24"), ConvertToDate("2004-01-02 11:30:24")));		
		ts.add(new TimeSlot(ConvertToDate( "2002-01-04 11:30:24"), ConvertToDate("2002-11-02 12:30:24")));		
		return createSingleAppRecord("Didi", "Tansportation", ts);
	}

	private static AppManager create10() {
		LinkedList<TimeSlot> ts = new LinkedList<TimeSlot>();
		ts.add(new TimeSlot(ConvertToDate( "2000-01-02 11:30:24"), ConvertToDate("2004-01-02 11:30:24")));		
		ts.add(new TimeSlot(ConvertToDate( "2002-01-04 11:30:24"), ConvertToDate("2002-11-02 12:30:24")));		
		return createSingleAppRecord("E book", "Education", ts);
	}

	private static AppManager create11() {
		LinkedList<TimeSlot> ts = new LinkedList<TimeSlot>();
		ts.add(new TimeSlot(ConvertToDate( "2000-01-02 11:30:24"), ConvertToDate("2004-01-02 11:30:24")));		
		ts.add(new TimeSlot(ConvertToDate( "2002-01-04 11:30:24"), ConvertToDate("2002-11-02 12:30:24")));		
		return createSingleAppRecord("Coursera", "Education", ts);
	}

	private static AppManager create12() {
		LinkedList<TimeSlot> ts = new LinkedList<TimeSlot>();
		ts.add(new TimeSlot(ConvertToDate( "2000-01-02 11:30:24"), ConvertToDate("2004-01-02 11:30:24")));		
		ts.add(new TimeSlot(ConvertToDate( "2002-01-04 11:30:24"), ConvertToDate("2002-11-02 12:30:24")));		
		return createSingleAppRecord("QQ", "IM", ts);
	}

	private static AppManager createSingleAppRecord(String Name, String Type,
			LinkedList<TimeSlot> ts) {

		AppManager appcontainer = new AppManager(new AppInfo(Name, null));
		for (TimeSlot item : ts) {
			SingleAppRecord rec = new SingleAppRecord(item.getStart(),
					item.getEnd());
			appcontainer.addItem(rec);
		}
		return appcontainer;
	}

	private static Date ConvertToDate(String date) {
		try {
			return df.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			return null;
		}
	}

}
