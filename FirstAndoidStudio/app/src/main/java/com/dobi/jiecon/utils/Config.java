package com.dobi.jiecon.utils;

import java.util.HashMap;

/**
 *
 */
public class Config {
    public static int UPLOAD_TIMER_DURATION = 30; //Second

    public  static int MAX_UPLOAD_COUNT = 30;

    public static int REFRESH_TIMER_DATABASE = 24 * 60 * 60; //Second
    public static int MAX_HISTORY_MONTH_COUNT = 2;
    public static int MAX_DURATION_MONTH_COUNT = 10;

    public static int PEEK_TIMER = 30;
    public static int CHECK_TIMEOUT_TIMER = 60;

    public static int CHECK_SERVICE = 60;

    public static int LOCK_SCREEN_WARNING = 60; //Second
    public static HashMap<String, Boolean> EXCLUDE_APP = new HashMap<String, Boolean>() {
        {
            put("com.android.keyguard", true);
            put("com.android.settings", true);
            put("com.example.harvey.firstandoidstudio", true);
            put("com.sec.android.app.launcher", true);
        }
    };

}
