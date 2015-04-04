package com.dobi.jiecon.utils;

import android.database.sqlite.SQLiteOpenHelper;

import com.dobi.jiecon.database.JieconDBHelper;
import com.dobi.jiecon.database.sqlite.SqliteBase;

import java.util.HashMap;

/**
 *
 */
public class Config {
    public static int UPLOAD_TIMER_DURATION = 30; //Second

    public static int MAX_UPLOAD_COUNT = 30;

    public static int REFRESH_TIMER_DATABASE = 24 * 60 * 60; //Second
    public static int MAX_HISTORY_MONTH_COUNT = 2;
    public static int MAX_DURATION_MONTH_COUNT = 10;

    public static int PEEK_TIMER() {
        int ret = 30;
        String peek = SqliteBase.get_config(JieconDBHelper.PEEK_TIME);
        if (peek != null) {
            try {
                ret =  Integer.parseInt(peek);
            } catch (Exception e) {

            }
        }
        return ret;
    }
    public static int APP_COUNT() {
        int ret = 10;
        String peek = SqliteBase.get_config(JieconDBHelper.APP_COUNT);
        if (peek != null) {
            try {
                ret =  Integer.parseInt(peek);
            } catch (Exception e) {

            }
        }
        return ret;
    }
    public static int CHECK_TIMEOUT_TIMER = 5;

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
