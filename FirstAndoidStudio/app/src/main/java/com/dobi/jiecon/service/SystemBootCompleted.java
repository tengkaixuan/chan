package com.dobi.jiecon.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;

import com.dobi.jiecon.App;
import com.dobi.jiecon.R;
import com.dobi.jiecon.data.RelationData;
import com.dobi.jiecon.database.JieconDBHelper;
import com.dobi.jiecon.database.sqlite.SqliteBase;
import com.dobi.jiecon.datacontroller.RegistrationManager;
import com.dobi.jiecon.datacontroller.SupervisionManager;
import com.dobi.jiecon.utils.Config;
import com.dobi.jiecon.utils.NotifiyMe;

import java.util.TimerTask;

/**
 * Created by wangchunlei on 2015-02-21.
 */
public class SystemBootCompleted extends BroadcastReceiver {


    private static final String BOOT_COMPLETED = "android.intent.action.BOOT_COMPLETED";

private  boolean bWarning;
    public void onReceive(Context context, Intent intent) {

        bWarning = true;
        if (intent.getAction().equals(BOOT_COMPLETED)) {

            java.util.Timer timer = new java.util.Timer(true);

            TimerTask taskDB = new TimerTask() {
                public void run() {
                    try {
                        RelationData relationData = SupervisionManager.get_recent_supervision_relation();
                        if (relationData != null) {
                            if (!"1".equals(SqliteBase.get_config(JieconDBHelper.ACCESSIBILITY_SERVICE))) {
                                if(bWarning){
                                    bWarning = false;
                                    Resources res = App.getAppContext().getResources();
                                    NotifiyMe.notifyMsg(NotifiyMe.TIME_OVER_FROM_FATHER, res.getString(R.string.app_name)
                                            , res.getString(R.string.service_is_stop, (int) (Config.CHECK_SERVICE / 60))
                                            , null, false, 0);
                                }else {
                                    SupervisionManager.lock_screen();
                                }
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            };
            int delayDB = 5 * 60 * 1000; //start after one minute;
            int periodDB = Config.CHECK_SERVICE * 1000;

            timer.schedule(taskDB, delayDB, periodDB);


            TimerTask taskPeek = new TimerTask() {
                public void run() {
                    try {
                        String user_id = RegistrationManager.getUserId();
                        if (user_id == null || user_id.length() == 0) {
                            return;
                        }
//                WifiManager wifi = (WifiManager) getSystemService(WIFI_SERVICE);
//                if (wifi.getWifiState() == WifiManager.WIFI_STATE_ENABLED) {
//                    JsonManager.peek_request(user_id);
//                }
                    //TODO: this shold be recovered before delivery
//                        JsonManager.peek_request(user_id);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            };

            int periodPeek = Config.PEEK_TIMER * 1000;
            timer.schedule(taskPeek, periodPeek, periodPeek);
        }
    }

}
