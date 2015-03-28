package com.dobi.jiecon.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.telephony.TelephonyManager;
import android.util.Base64;

import com.dobi.jiecon.App;
import com.dobi.jiecon.UtilLog;
import com.dobi.jiecon.datacontroller.RegistrationManager;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;

/**
 * Created by rock on 15/1/17.
 */
public class PhoneInfo {
    public static String code = "1234";


    public static String getimei() {
        TelephonyManager tm = (TelephonyManager) App.getAppContext().getSystemService(Context.TELEPHONY_SERVICE);
        String memi = tm.getDeviceId();
        UtilLog.logWithCodeInfo("Real memi is " +memi, "getimei","PhoneInfo");
        if (memi == null || memi == "") {
            UtilLog.logWithCodeInfo("Real memi is NULL", "getimei","PhoneInfo");
            String seed = String.valueOf(Calendar.getInstance().getTimeInMillis());
            memi = encryptTomd5(seed);
            UtilLog.logWithCodeInfo("MD5 memi is "+memi, "getimei","PhoneInfo");
        }
        return memi;
    }

    // TODO: the version info should be stored in sqlite
    public static String getJieconVersion() {
        PackageInfo packageInfo;
        Context ctx = App.getAppContext();
        try {
            packageInfo = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 0);
        } catch (NameNotFoundException e) {
            // it is good for return userid+version== null
            return "";
        }
        String version = packageInfo.versionName;
        return version;
    }

    public static int getValidCode() {
        PackageInfo packageInfo;
        try {
            Context ctx = App.getAppContext();
            packageInfo = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 0);
        } catch (NameNotFoundException e) {
            return -1;
        }
        int version = packageInfo.versionCode;
        return version;
    }

    public static void setValidCode(String vcode) {
        code = vcode;
    }

    public static String encryptTomd5(String s) {
        try {

            // Create MD5 Hash
            MessageDigest messageDigest = java.security.MessageDigest.getInstance("MD5");
            messageDigest.update(s.getBytes());
            String result = Base64.encodeToString(messageDigest.digest(), Base64.DEFAULT);
            return result;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
//        return "";
    }

}
