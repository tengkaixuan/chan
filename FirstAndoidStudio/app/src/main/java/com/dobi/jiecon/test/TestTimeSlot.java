package com.dobi.jiecon.test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import junit.framework.TestCase;

import com.dobi.jiecon.utils.TimeSlot;

public class TestTimeSlot extends TestCase {

	private DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public void testDuration() {
		TimeSlot tw = new TimeSlot();

		Date sd = new Date();
		Date ed = new Date();
		try {
			sd = df.parse("2004-01-02 11:30:24");
			ed = df.parse("2004-01-02 23:31:25");

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		tw.setStart(sd);
		tw.setEnd(ed);

		long h = tw.getHourDuration();
		long d = tw.getDayDuration();
		long m = tw.getMinDuration();
		long s = tw.getSecDuration();

		System.out.println("The duration is ");
		System.out.println(d + "      Days");
		System.out.println(h + "        Hours");
		System.out.println(m + "        Mins");
		System.out.println(s + "        Secs");

	}
}
