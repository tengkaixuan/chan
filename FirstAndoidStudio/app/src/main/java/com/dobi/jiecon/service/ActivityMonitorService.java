package com.dobi.jiecon.service;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.dobi.jiecon.UtilLog;
import com.dobi.jiecon.data.AppInfo;
import com.dobi.jiecon.data.AppManager;

import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class ActivityMonitorService extends Service {
	private static String tag = "AppService";
	private static String AppTag = "AppInfo";
    private Timer keepAlive;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            int msgType = msg.what;
            UtilLog.logControlD("what is =" + msgType);
            if(msgType == 0) {
                mHandler.removeMessages(0);
                getActivities_option2();

            } else if (msgType == 1) {
                mHandler.removeMessages(1);
            }
        }
    };

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
        keepAlive();
	}

	@Override
	public void onStart(Intent intent, int startid) {
        keepAlive();
    }

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
	}

	public List<RunningAppProcessInfo> getAllActivities() {
        AppManager apps = AppManager.getInstance();
		ActivityManager am = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> procList = am.getRunningAppProcesses();
        return procList;
	}

	private LinkedList<AppInfo> getAllAppsInfo() {
		LinkedList<AppInfo> appList = new LinkedList<AppInfo>();
		Drawable appIcon;
		String appName;

		List<PackageInfo> packs = getPackageManager().getInstalledPackages(0);

        for (PackageInfo p : packs) {
			appIcon = p.applicationInfo.loadIcon(getPackageManager());
			PackageManager pm = this.getPackageManager();
			appName =pm.getApplicationLabel(p.applicationInfo).toString() ;

            AppInfo ai = new AppInfo(appName, appIcon);
			appList.add(ai);
		}

        return appList;
	}

    private void getCurrentRunningApp()
    {
        String aName = "Not Found";
        List<RunningAppProcessInfo> procList = getAllActivities();

        ActivityManager activityManager = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);

        List<ActivityManager.RunningTaskInfo> services = activityManager
                .getRunningTasks(Integer.MAX_VALUE);
        boolean isActivityFound = false;
        for (RunningAppProcessInfo info : procList) {
            aName = info.processName.toString();
            if (services.get(0).topActivity.getPackageName().toString().equalsIgnoreCase(aName)) {
                isActivityFound = true;
                break;
            }
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
        final String applicationName = (String) (ai != null ? pm.getApplicationLabel(ai) : "(unknown)");
        return applicationName;
    }

    public void getActivities_option2() {
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
        ComponentName componentInfo = taskInfo.get(0).topActivity;

        String st =  "CURRENT Activity ::" + taskInfo.get(0).topActivity.getClassName()+
                " Package Name : "+componentInfo.getPackageName() +
                " name=" + getAppName(componentInfo.getPackageName());
        UtilLog.logControlD(st);
    }

    private void keepAlive() {
        if (keepAlive != null) {
            keepAlive.cancel();
            keepAlive = null;
        }
        keepAlive = new Timer();
        keepAlive.schedule(new TimerTask() {
            @Override
            public void run() {
                if(mHandler!=null) {
                    Message msg = new Message();
                    msg.what = 0;
                    mHandler.sendMessage(msg);
                }
            }
        }, 0, 1000 * 2);
    }
}
