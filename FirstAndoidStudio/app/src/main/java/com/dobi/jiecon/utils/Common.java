package com.dobi.jiecon.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.dobi.jiecon.database.AppUsage;

/**
 * Created by wangchunlei on 2015-03-29.
 */
public class Common {
    public static String getAppName(Context context,AppUsage appUsage) {
        ApplicationInfo ai;
        PackageManager pm = context.getPackageManager();
        String applicationName = null;
        try {
            ai = pm.getApplicationInfo(appUsage.getApp_pkgname(), 0);
              applicationName =  (String)pm.getApplicationLabel(ai) ;
        } catch (final PackageManager.NameNotFoundException e) {
            applicationName = appUsage.getApp_name();
        }

        return applicationName;
    }
}
