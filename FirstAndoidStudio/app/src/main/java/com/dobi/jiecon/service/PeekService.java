package com.dobi.jiecon.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import com.dobi.jiecon.database.json.JsonManager;
import com.dobi.jiecon.datacontroller.RegistrationManager;
import com.dobi.jiecon.utils.Config;

import java.util.TimerTask;

public class PeekService extends Service {
    public PeekService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        peek_server_update();
    }
    //TODO: this will be delete on the real device. The peek is called in SystemBootCompleted.java
    private void peek_server_update(){
        java.util.Timer timer = new java.util.Timer(true);
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
                    JsonManager.peek_request(user_id);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };

        int periodPeek = Config.PEEK_TIMER * 1000;
        timer.schedule(taskPeek, periodPeek, periodPeek);
    }
}
