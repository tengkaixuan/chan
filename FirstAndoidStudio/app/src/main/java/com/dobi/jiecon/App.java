package com.dobi.jiecon;

import android.app.Application;

public class App extends Application {
    public static String KEY_JSON_REGISTER = "json_register";
    public static String KEY_JSON_LOGIN = "json_login";

    public static String KEY_FIRSTTIME = "firsttimerun";
    //register接口
    public static String KEY_USERNAME = "USERNAME";//用户名
    public static String KEY_NICKNAME = "NICKNAME";//昵称
    public static String KEY_RECOMMENDCODE = "RECOMMEND_CODE";//推荐码
    public static String KEY_A = "KEY_A";//login时返回的key A

    //refresh接口
    public static String KEY_TOTAL_COIN = "KEY_TOTAL_COIN";//总收入
    public static String KEY_ADV_COIN = "KEY_ADV_COIN";//广告收入
    public static String KEY_LEFT = "KEY_LEFT";//余额
    public static String KEY_TOTAL_PAY = "KEY_TOTAL_PAY";//总提取
    public static String KEY_LOTTERY = "KEY_LOTTERY";//抽奖所得

    //WITHDRAWDEPOSIT接口
    public static String KEY_PERSONAL_NOTIFY = "KEY_PERSONAL_NOTITY";//个人信息，和跑马灯一起发过来的消息
    public static String KEY_MAQUEE = "KEY_MAQUEE";//跑马灯

    //The message key between activity
    public static String KEY_CURRENT_USR_ID = "KEY_CURRENT_USR_ID";
    public static String KEY_CURRENT_USR_NAME = "KEY_CURRENT_USR_NAME";
    public static String KEY_CURRENT_USR_PHONE = "KEY_CURRENT_USR_PHONE";
    public static String KEY_CURRENT_CONTACTS_ID = "KEY_CURRENT_CONTACT_ID";
    public static String KEY_FROM_CXT = "KEY_ACTIVITY_FROM";
    public static String KEY_FAMILY2DAY_USERID = "FAMILY_DAY_USERID";

    public static String KEY_RELATION_LIST_UPDATED = "KEY_RELATION_LIST_UPDATED";

    public static final int RELATION_LIST_UPDATED_YES = 1;
    public static final int RELATION_LIST_UPDATED_NO = 0;

    //Activity ID
    public static int ID_NOTIFICATION = 100;
    public static int ID_FAMILY_LIST_ADAPTER = 101;

    //以下为client_config接口
    public static String KEY_CLIENTCONFIG_SINA = "KEY_CLIENTCONFIG_SINA";
    public static String KEY_CLIENTCONFIG_WXSHARE = "KEY_CLIENTCONFIG_WXSHARE";
    public static String KEY_CLIENTCONFIG_RECOMMEND = "KEY_CLIENTCONFIG_RECOMMEND";
    public static String KEY_CLIENTCONFIG_VALIDATE = "KEY_CLIENTCONFIG_VALIDATE";
    public static String KEY_CLIENTCONFIG_COMMENT = "KEY_CLIENTCONFIG_COMMENT";
    public static String KEY_CLIENTCONFIG_VERSION = "KEY_CLIENTCONFIG_VERSION";
    public static String KEY_CLIENTCONFIG_URL = "KEY_CLIENTCONFIG_URL";
    public static String KEY_CLIENTCONFIG_ADV_URL = "KEY_CLIENTCONFIG_ADV_URL";
    public static String KEY_CLIENTCONFIG_SHANGJIA = "KEY_CLIENTCONFIG_SHANGJIA";

    //advswitch_update接口
    public static String KEY_ADV_SWITCH = "KEY_ADV_SWITCH_KEY%d";

    //recommenduser接口
    public static String KEY_RECOMMEND_USER = "%d_%d";

    //modifynickname接口

    //IJsonCallback ID defintion
    public static final int ID_CHECK_PHONE_BINDING = 1000;
    public static final int ID_CHECK_JIEJIE_USER= 1001;
    public static final int ID_GET_USER_BY_PHONE= 1002;
    public static final int ID_ADD_FRIEND= 1003;
    public static final int GET_APP_DURATION_DAY= 1004;
    public static final int ID_BINDING_PHONE = 1005;

    //Internal Message definition
    public static final int SUPERVISION_ACTIVITY_START_MONITOR= 100000;

    //	public static String[] KEY_ADV_SWITCH;
    private static App instance;
    public static App getAppContext() { return instance; }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}