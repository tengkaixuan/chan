package com.dobi.jiecon.test;

import com.dobi.jiecon.UtilLog;
import com.dobi.jiecon.utils.PhoneInfo;
import com.dobi.jiecon.utils.TimeFormat;

import junit.framework.TestCase;

import java.util.Calendar;

/**
 * Created by rock on 15/3/21.
 */
public class TestPhoneInfo extends TestCase {
//    public void testEncrypt2md5(){
//        Calendar right_now = Calendar.getInstance();
//        long milis = right_now.getTimeInMillis();
//        String result = PhoneInfo.encryptTomd5(String.valueOf(milis));
//    }
    public void testEncrypt2md5_now_second(){
        Calendar right_now = Calendar.getInstance();
        long milis = right_now.getTimeInMillis()/1000;
        UtilLog.logWithCodeInfo("milisecond right now time is " + milis, "testEncrypt2md5_now_second", "TestPhoneInfo");
        String result = PhoneInfo.encryptTomd5(String.valueOf(milis));
        UtilLog.logWithCodeInfo("After encryption the result is " + result, "testEncrypt2md5_now_second", "TestPhoneInfo");
    }

}
