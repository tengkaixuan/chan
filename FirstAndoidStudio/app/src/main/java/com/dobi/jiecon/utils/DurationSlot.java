package com.dobi.jiecon.utils;

public class DurationSlot {
	private long day = 0;
	private long hour = 0;
	private long min = 0;
	private long sec = 0;

	public boolean isGreater(DurationSlot ds) {
		DurationSlot cpofthis = copyDuration(this);
		cpofthis.alignToSec();

		DurationSlot cpofpara = copyDuration(ds);
		cpofpara.alignToSec();
		return (cpofthis.getSec() > cpofpara.getSec());
	}

	public DurationSlot copyDuration(DurationSlot ds) {
		DurationSlot new_ds = new DurationSlot();
		new_ds.setDuration(ds);
		return new_ds;
	}

	public void setDuration(DurationSlot ds) {
		this.day = ds.day;
		this.hour = ds.hour;
		this.min = ds.min;
		this.sec = ds.sec;
	}

	public DurationSlot addDuration(DurationSlot d) {
		this.day += d.day;
		this.hour += d.hour;
		this.min += d.min;
		this.sec += d.sec;
		return this;
	}

	public DurationSlot alignToSec() {

		this.sec = ((this.day * 24 + this.hour) * 60 + this.min) * 60
				+ this.sec;

		this.day = this.hour = this.min = 0;
		return this;
	}

	public DurationSlot alignToHour() {
		alignToHourIgoreDay();
		this.hour += this.day * 24;
		this.day = 0;
		return this;
	}

	public DurationSlot alignToDay() {
		alignToHourIgoreDay();
		this.day += this.hour / 24;
		this.hour = this.hour % 24;
		return this;
	}

	private void alignToHourIgoreDay() {
		this.hour += ((this.sec / 60) + this.min) / 60;
		this.min = (this.sec / 60 + this.min) % 60;
		this.sec = this.sec % 60;
	}

	public long getDay() {
		return day;
	}

	public DurationSlot addDay(long day) {
		this.day += day;
		return this;
	}

	public long getHour() {
		return hour;
	}

	public DurationSlot addHour(long hour) {
		this.hour += hour;
		return this;
	}

	public long getMin() {
		return min;
	}

	public DurationSlot addMin(long min) {
		this.min += min;
		return this;
	}

	public long getSec() {
		return sec;
	}

	public DurationSlot addSec(long sec) {
		this.sec += sec;
		return this;
	}
}
