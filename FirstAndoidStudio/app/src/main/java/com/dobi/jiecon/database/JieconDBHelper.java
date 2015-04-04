package com.dobi.jiecon.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.dobi.jiecon.App;


public class JieconDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "com.dobi.jiecon.database.jiejie";

    private static final int DATABASE_VERSION = 34;

    public final static int USERID_KEY = 101;
    public final static int USERNAME_KEY = 102;
    public final static int PASSWD_KEY = 103;
    public final static int VALIDCODE_KEY = 104;
    public final static int BINDFLAG_KEY = 105;
    public final static int PHONENUMBER_KEY = 106;
    public final static int NICKNAME_KEY = 107;


    public final static int MSGFLAG_KEY = 200;
    public final static int SUPERVISION_TIMEOUT = 201;
    public final static int LOCK_SCREEN = 202;

    public final static int TIME_WEEK_BASE = 300 - java.util.Calendar.SUNDAY;
    public final static int TIME_WEEK_1     = TIME_WEEK_BASE + java.util.Calendar.SUNDAY;
    public final static int TIME_WEEK_2     = TIME_WEEK_BASE + java.util.Calendar.MONDAY;
    public final static int TIME_WEEK_3     = TIME_WEEK_BASE + java.util.Calendar.TUESDAY;
    public final static int TIME_WEEK_4     = TIME_WEEK_BASE + java.util.Calendar.WEDNESDAY;
    public final static int TIME_WEEK_5     = TIME_WEEK_BASE + java.util.Calendar.THURSDAY;
    public final static int TIME_WEEK_6     = TIME_WEEK_BASE + java.util.Calendar.FRIDAY;
    public final static int TIME_WEEK_7     = TIME_WEEK_BASE + java.util.Calendar.SATURDAY;

    public final static int HAS_ALARM = 310;
    public final static int HAS_LOCK = 311;



    public final static int NOTIFICATION_USR_ID = 400;
    public final static int NOTIFICATION_NAME = 401;
    public final static int NOTIFICATION_PHONE = 402;


    public final static int PEEK_TIME = 501;
    public final static int APP_COUNT = 502;

    public static SQLiteDatabase getDb() {
        JieconDBHelper dbHelp = new JieconDBHelper(App.getAppContext());
        SQLiteDatabase db = dbHelp.getReadableDatabase();
        return db;
    }

    public JieconDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL("CREATE TABLE  app_duration "
                    + " (day BIGINT unsigned not null, app_pkgname text  not null  ,app_name text not null,duration integer,times integer, primary key(day,app_pkgname));");

            db.execSQL("CREATE TABLE  app_history "
                    + " (id INTEGER   primary key AUTOINCREMENT,app_pkgname text not null ,app_name text not null ,start_time BIGINT unsigned ,end_time BIGINT unsigned, uploaded tinyint(1) DEFAULT 0, CONSTRAINT uk_app_time unique(app_pkgname,start_time));");

            db.execSQL("CREATE TABLE  config  (key int unsigned not null ,value text);");

            db.execSQL("CREATE TABLE  relation_list (seq BIGINT unsigned ,user_id text ,name text,phone text, role INTEGER,time INTEGER,status INTEGER,msg text,read_flag INTEGER,type INTEGER);");

            db.execSQL("CREATE TABLE  filter_app "
                    + " (id INTEGER   primary key AUTOINCREMENT,app_pkgname text not null ,level BIGINT unsigned , CONSTRAINT uk_app_pkgname unique(app_pkgname));");

            db.execSQL("CREATE TABLE  unlock_info  (user_id text  not null,day BIGINT unsigned,time INTEGER);");

            db.execSQL("CREATE TABLE  add_family ( phone text not null, name text, user_id text);");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS app_duration");
        db.execSQL("DROP TABLE IF EXISTS app_history");
        db.execSQL("DROP TABLE IF EXISTS config");
        db.execSQL("DROP TABLE IF EXISTS relation_list");
        db.execSQL("DROP TABLE IF EXISTS filter_app");
        db.execSQL("DROP TABLE IF EXISTS unlock_info");
        db.execSQL("DROP TABLE IF EXISTS add_family");
        onCreate(db);
    }


}
