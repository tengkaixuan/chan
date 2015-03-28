package com.dobi.jiecon.utils;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;

import com.dobi.jiecon.App;
import com.dobi.jiecon.R;
import com.dobi.jiecon.activities.NotificationTapActivity;
import com.dobi.jiecon.activities.TabSample;


/**
 * Created by wcl on 15/2/19.
 */
public class NotifiyMe {


    public final static int TIME_OVER_FROM_FATHER = 1;
    public final static int TIME_OVER_FROM_ME = 2;

    public final static int PEEK_BASE = 100;

    public static void notifyMsg(int notifyId, String title, String msg, Bundle bundle, boolean depends, long status) {
        String service = Application.NOTIFICATION_SERVICE;
        NotificationManager nManager = (NotificationManager) App.getAppContext().getSystemService(service);

        Notification notification = new Notification();
        Resources res = App.getAppContext().getResources();
        // 显示时间
        long when = System.currentTimeMillis();

        notification.icon = R.drawable.jiejie_icon32;// 设置通知的图标
        notification.tickerText = res.getString(R.string.app_name); // 显示在状态栏中的文字
        notification.when = when; // 设置来通知时的时间

        notification.flags = Notification.FLAG_ONGOING_EVENT; // 点击清除按钮不会清除消息通知,可以用来表示在正在运行
        notification.flags |= Notification.FLAG_AUTO_CANCEL; // 点击清除按钮或点击通知后会自动消失
        notification.flags |= Notification.FLAG_ONLY_ALERT_ONCE; // 一直进行，比如音乐一直播放，知道用户响应

        notification.defaults = Notification.DEFAULT_ALL; // 把所有的属性设置成默认

        // notify request

        Intent intent;
        if(depends) {
            intent = new Intent(App.getAppContext(), NotificationTapActivity.class);
            intent.putExtra("displaymsg", msg);
            intent.putExtra("notifitystatus", status);
            intent.putExtra("name", title);
        } else {
            intent = new Intent(App.getAppContext(), TabSample.class);
        }


        if (bundle != null) {
            intent.addFlags(Intent.FILL_IN_DATA);
            intent.putExtras(bundle);
        }

        PendingIntent pIntent = PendingIntent.getActivity(App.getAppContext(), 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        // 设置通知的标题和内容
        notification.setLatestEventInfo(App.getAppContext(), title,
                msg, pIntent);

        // 发出通知
        nManager.cancel(notifyId);
        nManager.notify(notifyId, notification);
    }

}
