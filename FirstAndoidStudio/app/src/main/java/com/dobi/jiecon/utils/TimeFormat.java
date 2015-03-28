package com.dobi.jiecon.utils;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by rock on 15/1/18.
 */
public class TimeFormat {
    // Change to a long which is the number of seconds
    public static long dateRefine(Date reqTime) {
        reqTime.setHours(0);
        reqTime.setMinutes(0);
        reqTime.setSeconds(0);
        return reqTime.getTime() / 1000;
    }

    /*
    *   @param i 从－1开始，表示昨天，－2表示前天， －3 大前天....
     */
    public  static Calendar whichDay(int i) {
        Calendar cal = Calendar.getInstance();
//        cal.add(Calendar.DATE, -1);
        cal.add(Calendar.DATE, i);
        return cal;
    }

    public static Calendar Now() {
        Calendar date = Calendar.getInstance();
        return date;
    }

    public static long SecondsNow() {
        Calendar date = Now();
        return date.getTimeInMillis() / 1000;
    }

    public static long getSecond(Calendar date) {
        return date.getTimeInMillis() / 1000;
    }

    public static long getWeek(Calendar date) {
        return date.get(Calendar.DAY_OF_WEEK);
    }

    public static int getWeekOfToday() {
        return Now().get(Calendar.DAY_OF_WEEK);
    }

    public static long getSecondForDay(Calendar cal, int day) {
        Calendar date = Now();
        date.setTimeInMillis(0);
        date.setTimeZone(cal.getTimeZone());
        date.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE), 0, 0, 0);
        if (day != 0) {
            date.add(Calendar.DATE, day);
        }
        return date.getTimeInMillis() / 1000;
    }

    public static long getSecondForMonth(Calendar cal, int month) {
        Calendar date = Now();
        date.setTimeInMillis(0);
        date.setTimeZone(cal.getTimeZone());
        date.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), 1, 0, 0, 0);
        if (month != 0) {
            date.add(Calendar.MONTH, month);
        }
        return date.getTimeInMillis() / 1000;
    }
}
