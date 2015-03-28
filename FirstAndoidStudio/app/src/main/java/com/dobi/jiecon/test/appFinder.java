package com.dobi.jiecon.test;

import android.content.ComponentName;
import android.content.pm.ApplicationInfo;

import java.lang.reflect.Method;

/**
 * Created by rock on 15/1/10.
 */
public class appFinder {
    public final int compare(ApplicationInfo a, ApplicationInfo b) {

//        ComponentName aName = a.intent.getComponent();
//        ComponentName bName = b.intent.getComponent();
        ComponentName aName = null;
        ComponentName bName = null;

        int aLaunchCount,bLaunchCount;
        long aUseTime,bUseTime;
        int result = 0;

        try {

            //获得ServiceManager类
            Class<?> ServiceManager = Class
                    .forName("android.os.ServiceManager");

            //获得ServiceManager的getService方法
            Method getService = ServiceManager.getMethod("getService", java.lang.String.class);

            //调用getService获取RemoteService
            Object oRemoteService = getService.invoke(null, "usagestats");

            //获得IUsageStats.Stub类
            Class<?> cStub = Class
                    .forName("com.android.internal.app.IUsageStats$Stub");
            //获得asInterface方法
            Method asInterface = cStub.getMethod("asInterface", android.os.IBinder.class);
            //调用asInterface方法获取IUsageStats对象
            Object oIUsageStats = asInterface.invoke(null, oRemoteService);
            //获得getPkgUsageStats(ComponentName)方法
            Method getPkgUsageStats = oIUsageStats.getClass().getMethod("getPkgUsageStats", ComponentName.class);
            //调用getPkgUsageStats 获取PkgUsageStats对象
            Object aStats = getPkgUsageStats.invoke(oIUsageStats, aName);
            Object bStats = getPkgUsageStats.invoke(oIUsageStats, bName);

            //获得PkgUsageStats类
            Class<?> PkgUsageStats = Class.forName("com.android.internal.os.PkgUsageStats");

            aLaunchCount = PkgUsageStats.getDeclaredField("launchCount").getInt(aStats);
            bLaunchCount = PkgUsageStats.getDeclaredField("launchCount").getInt(bStats);
            aUseTime = PkgUsageStats.getDeclaredField("usageTime").getLong(aStats);
            bUseTime = PkgUsageStats.getDeclaredField("usageTime").getLong(bStats);

            if((aLaunchCount>bLaunchCount)||
                    ((aLaunchCount == bLaunchCount)&&(aUseTime>bUseTime)))
                result = 1;
            else if((aLaunchCount<bLaunchCount)||((aLaunchCount ==
                    bLaunchCount)&&(aUseTime<bUseTime)))
                result = -1;
            else {
                result = 0;
            }

        } catch (Exception e) {
//            Log.e("###", e.toString(), e);
        }

        return result;
    }
}
