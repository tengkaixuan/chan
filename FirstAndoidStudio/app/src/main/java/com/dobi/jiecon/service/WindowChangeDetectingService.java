package com.dobi.jiecon.service;

import android.accessibilityservice.AccessibilityService;
import android.app.KeyguardManager;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.accessibility.AccessibilityEvent;

import com.dobi.jiecon.App;
import com.dobi.jiecon.R;
import com.dobi.jiecon.UtilLog;
import com.dobi.jiecon.data.RelationData;
import com.dobi.jiecon.database.AppUsageOrg;
import com.dobi.jiecon.database.JieconDBHelper;
import com.dobi.jiecon.database.sqlite.SqliteBase;
import com.dobi.jiecon.datacontroller.AppUsageManager;
import com.dobi.jiecon.datacontroller.RegistrationManager;
import com.dobi.jiecon.datacontroller.SupervisionManager;
import com.dobi.jiecon.utils.Config;
import com.dobi.jiecon.utils.NotifiyMe;
import com.dobi.jiecon.utils.TimeFormat;

import java.util.TimerTask;

/**
 * Created by rock on 15/1/12.
 */
public class WindowChangeDetectingService extends AccessibilityService {
    public static AppUsageOrg curApp = new AppUsageOrg();

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        try {
            CharSequence packageName = event.getPackageName();
            String appPkgName = null;
            if (packageName != null) {
                appPkgName = event.getPackageName().toString();
            }
            if ("".equals(curApp.getApp_pkgname())) {
                curApp.setApp_pkgname(appPkgName);
                curApp.setStart_time(TimeFormat.SecondsNow());
            } else {
                if (!curApp.getApp_pkgname().equals(appPkgName)) {

                    long end_time = TimeFormat.SecondsNow();
                    // App is not on foreground, save it
                    curApp.setEnd_time(end_time);
                    if (appPkgName != null) {
                        curApp.setApp_name(getAppName(curApp.getApp_pkgname()));
                    }
                    if (false == AppUsageManager.saveAppInfoToSqlite(curApp)) {
                        // TODO Log file
                    }
                    curApp.setApp_pkgname(appPkgName);
                    curApp.setStart_time(end_time);
                }
            }
            if (curApp.getApp_pkgname() == null) {
                UtilLog.logWithCodeInfo("PackageName is  null", "onAccessibilityEvent", "WindowChangeDetectingService");
                curApp.setApp_pkgname("");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private String getAppName(String packageName) {
        ApplicationInfo ai;
        PackageManager pm = this.getPackageManager();
        try {
            ai = pm.getApplicationInfo(packageName, 0);
        } catch (final PackageManager.NameNotFoundException e) {

            ai = null;
        }
        final String applicationName = (String) (ai != null ? pm
                .getApplicationLabel(ai) : "(unknown)");
        return applicationName;
    }

    @Override
    public void onServiceConnected() {
        super.onServiceConnected();
        curApp.setApp_pkgname("");
        java.util.Timer timer = new java.util.Timer(true);
        TimerTask task = new TimerTask() {
            public void run() {
                try {
                    String user_id = RegistrationManager.getUserId();

                    if (user_id == null || user_id.length() == 0) {
                        return;
                    }

                    //      WifiManager wifi = (WifiManager) getSystemService(WIFI_SERVICE);
                    //        if (wifi.getWifiState() == WifiManager.WIFI_STATE_ENABLED) {
                    synchronized (this) {
                        AppUsageManager.transfer_app_history_to_server(user_id);
                    }
                    //     }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };
        int delay = Config.UPLOAD_TIMER_DURATION * 1000;
        int period = Config.UPLOAD_TIMER_DURATION * 1000;
        timer.schedule(task, delay, period);

        startMonitor();

    }
    public static void startMonitor() {
        java.util.Timer timer = new java.util.Timer(true);

        TimerTask taskDB = new TimerTask() {
            public void run() {
                try {
                    AppUsageManager.delete_app_history();
                    AppUsageManager.delete_app_duration();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };
        int delayDB = 1 * 60 * 1000; //start after one minute;
        int periodDB = Config.REFRESH_TIMER_DATABASE * 1000;

        timer.schedule(taskDB, delayDB, periodDB);


        TimerTask taskTimeout = new TimerTask() {
            public void run() {
                try {
                    monitor();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };

        int periodTimeout = Config.CHECK_TIMEOUT_TIMER * 1000;
        timer.schedule(taskTimeout, periodTimeout, periodTimeout);
    }
    private static void monitor() {

        KeyguardManager mKeyguardManager = (KeyguardManager) App.getAppContext().getSystemService(KEYGUARD_SERVICE);
        if (mKeyguardManager.inKeyguardRestrictedInputMode()) {
            return;
        }
        if (curApp.getApp_pkgname().length() > 0) {
            if (SupervisionManager.get_filter_map().containsKey(curApp.getApp_pkgname())) {
                return;
            }
        }
        long total = AppUsageManager.get_today_app_duration_total();
        if (curApp.getApp_pkgname().length() > 0 && !SupervisionManager.get_filter_map().containsKey(curApp.getApp_pkgname())) {
            total += TimeFormat.SecondsNow() - curApp.getStart_time();
        }
        RelationData relationData = SupervisionManager.get_recent_supervision_relation();
        if (relationData != null) {

            if (relationData.getTime() < total) {
                RelationData relation = new RelationData();
                relation.setUser_id(relationData.getUser_id());
                relation.setRole(0);
                relation.setTime(24 * 60 * 60);
                relation.setStatus(31);
                SqliteBase.update_relation(0, relation);
                SupervisionManager.lock_screen();
                return;
            }

            if (relationData.getTime() < total - Config.LOCK_SCREEN_WARNING) {

                Bundle bundle = new Bundle();

                bundle.putInt("FLAG", 2);
                Resources res = App.getAppContext().getResources();
                NotifiyMe.notifyMsg(NotifiyMe.TIME_OVER_FROM_FATHER, res.getString(R.string.app_name)
                        , res.getString(R.string.lock_screen_warning, relationData.getTime(), (int) (total / 60), (int) (Config.LOCK_SCREEN_WARNING / 60), relationData.getName())
                        , bundle, false, 0);
                return;
            }
        }
        String useAlarm = SqliteBase.get_config(JieconDBHelper.HAS_ALARM);
        String useLock = SqliteBase.get_config(JieconDBHelper.HAS_LOCK);
        if ("1".equals(useAlarm) || "1".equals(useLock)) {  // has alarm
            String sMySetTime = SqliteBase.get_config(JieconDBHelper.TIME_WEEK_BASE + TimeFormat.getWeekOfToday());
            if (sMySetTime == null || sMySetTime.length() < 1) {
                return;
            }
            long lMySetTime = Long.parseLong(sMySetTime);


            if (lMySetTime < total && "1".equals(useLock)) {
                SupervisionManager.lock_screen();
                return;
            }
            if ("1".equals(useAlarm) && lMySetTime < total - Config.LOCK_SCREEN_WARNING) {
                Resources res = App.getAppContext().getResources();
                String msg;
                if ("1".equals(useLock)) {
                    msg = res.getString(R.string.lock_screen_warning_me_lock, (int) (total / 60), (int) (lMySetTime / 60), (int) (Config.LOCK_SCREEN_WARNING / 60));
                } else {
                    msg = res.getString(R.string.lock_screen_warning_me, (int) (total / 60), (int) (lMySetTime / 60));
                }
                NotifiyMe.notifyMsg(NotifiyMe.TIME_OVER_FROM_ME, res.getString(R.string.app_name), msg, null, false, 0);
            }

        }

    }

    @Override
    public void onInterrupt() {
        UtilLog.logControlR("Interupt");
    }


    @Override
    public void onDestroy() {
        SupervisionManager.remove_lock_screen_policy();
    }
}
