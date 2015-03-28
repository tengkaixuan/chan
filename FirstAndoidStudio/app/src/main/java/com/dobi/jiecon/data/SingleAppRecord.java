package com.dobi.jiecon.data;

import java.util.Date;
import com.dobi.jiecon.utils.TimeSlot;
import com.dobi.jiecon.utils.DurationSlot;


public class SingleAppRecord {	
	private TimeSlot dur = new TimeSlot();	
	public SingleAppRecord(Date s, Date e){
		dur.setStart(s);
		dur.setEnd(e);
	}

	public Date getStartTime(){
		return dur.getStart();
	}

	public void setStartTime(Date s){
		dur.setStart(s);
	}

    public Date getEndTime(){
        return dur.getEnd();
    }

	public void setEndTime(Date e){
		dur.setEnd(e);
	}

	public Date getEndDate(){
		return dur.getEnd();
	}
	public long getDayDuration() {
		return dur.getDayDuration();
	}
	public long getHourDuration() {
		return dur.getHourDuration();
	}
	public long getMinDuration() {
		return dur.getMinDuration();
	}
	public long getSecDuration() {
		return dur.getSecDuration();
	}
    public DurationSlot getOccupiedTime(Date req_day){

        Date day_begin = req_day;
        day_begin.setHours(0);
        day_begin.setMinutes(0);
        day_begin.setSeconds(0);

        Date day_end = req_day;
        day_end.setHours(23);
        day_end.setMinutes(59);
        day_end.setSeconds(59);

        if (getEndTime().before(day_begin)){
            return null;
        }
        if (getStartTime().after(day_end)){
            return null;
        }
        Date app_day_run = getStartTime();
        Date app_day_stop = getEndTime();

        if (getStartTime().before(day_begin)){
            app_day_run = day_begin;
        }
        //Adjust the time to the current day
        if (getEndTime().after(day_end)){
            app_day_stop = day_end;
        }
        TimeSlot ts = new TimeSlot(app_day_run, app_day_stop);
        DurationSlot ds = new DurationSlot();
        ds.addHour(ts.getDayDuration());
        ds.addMin(ts.getMinDuration());
        ds.addSec(ts.getSecDuration());
        return ds;
    }
}
