package com.dobi.jiecon;

import java.util.Date;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;
/**
 * Created by wangchunlei on 2015-01-17.
 */
public class Test {


    public static void main(String[ ] arg){
        Calendar start = Calendar.getInstance(TimeZone.getTimeZone("GMT"), Locale.ENGLISH);

        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"), Locale.ENGLISH);
        cal.setTimeInMillis(0);
        cal.set(Calendar.YEAR, 2015);
        cal.set(Calendar.MONTH, 2);
        System.out.printf(start.toString());
    }
}
