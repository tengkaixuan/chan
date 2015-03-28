package com.dobi.jiecon.utils;

import android.telephony.SmsManager;

/**
 * Created by rock on 15/3/5.
 */
public class SmsMgr {
    public static void sendSMS(String phoneNumber, String message) {
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, getSrcNum(), message, null, null);
    }

    private static String getSrcNum() {
        return "00001020300123";
    }
}
