package com.dobi.jiecon.utils;

import java.util.Date;

public class TimeSlot {

	private Date start;
	private Date end;
//	private long days;
//	private long hours;
//	private long mins;
//	private long secs;

	public TimeSlot(Date s, Date e){
		start = s;
		end =e;
	}
	public TimeSlot() {
		// TODO Auto-generated constructor stub
	}
//	public long getDuration_str() {
//		long dur = (end.getTime() - start.getTime());
//		days = dur / (1000 * 60 * 60 * 24);
//		hours = (dur - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
//		mins = (dur - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60))
//				/ (1000 * 60);
//		secs = (dur - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60) - mins
//				* (1000 * 60)) / 1000;
//		return dur;
//	}

	public long getDayDuration() {
		return (getInterval() / (1000 * 60 * 60 * 24));

	}

	public long getHourDuration() {
		return (getInterval() - getDayDuration() * (1000 * 60 * 60 * 24))
				/ (1000 * 60 * 60);

	}

	public long getMinDuration() {

		return (getInterval() - getDayDuration() * (1000 * 60 * 60 * 24) - getHourDuration()
				* (1000 * 60 * 60))
				/ (1000 * 60);
	}

	public long getSecDuration() {
		return (getInterval() - getDayDuration() * (1000 * 60 * 60 * 24) - getHourDuration() * (1000 * 60 * 60) - getMinDuration()
				* (1000 * 60)) / 1000;
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}
	private long getInterval() {
		return end.getTime() - start.getTime();
	}
}
