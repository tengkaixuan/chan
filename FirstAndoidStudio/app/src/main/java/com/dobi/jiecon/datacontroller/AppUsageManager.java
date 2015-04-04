package com.dobi.jiecon.datacontroller;


import com.dobi.jiecon.App;
import com.dobi.jiecon.UtilLog;
import com.dobi.jiecon.database.AppUsage;
import com.dobi.jiecon.database.AppUsageOrg;
import com.dobi.jiecon.database.json.IJsonCallback;
import com.dobi.jiecon.database.json.JsonManager;
import com.dobi.jiecon.database.sqlite.SqliteBase;
import com.dobi.jiecon.utils.Config;
import com.dobi.jiecon.utils.TimeFormat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppUsageManager {
    static HashMap<Long, List<AppUsage>> _data_day = new HashMap();

    /**
     * @param memi
     * @param version
     * @return key:k0  value:返回码,0成功，其他表示错误码
     * key:k1  value:"register" php脚本名称
     * key:k2  value:user_id, 分配的id号
     * key:k3  value:validate code将来要的验证码
     * key:k4 value:nick_name
     */
    static private String register(String memi, String version) {


        HashMap<String, Object> retHashMap = JsonManager.register(memi, version);
        if ("0".equals(retHashMap.get("k0"))) {
            return (String) retHashMap.get("k2");
        } else {
            return null;
        }
    }

    /**
     * @param user_id
     * @param old_pwd
     * @param new_pwd
     * @return
     */
    static public boolean update_password(String user_id, String old_pwd, String new_pwd) {
        HashMap<String, Object> retHashMap = JsonManager.update_password(user_id, old_pwd, new_pwd);
        if ("0".equals(retHashMap.get("k0"))) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @param login_id
     * @param pwd
     * @param memi
     * @return
     */
    static public String login(String login_id, String pwd, String memi) {
        HashMap<String, Object> retHashMap = JsonManager.login(login_id, pwd, memi);
        if ("0".equals(retHashMap.get("k0"))) {
            return (String) retHashMap.get("k2");
        } else {
            return null;
        }
    }


    /**
     * @param user_id
     * @return
     */
    static public String get_user_id_by_phone(String user_id) {
        HashMap<String, Object> retHashMap = JsonManager.get_user_id_by_phone(user_id);
        if ("0".equals(retHashMap.get("k0"))) {
            return (String) retHashMap.get("k2");
        } else {
            return null;
        }
    }

    /**
     * @param user_id
     * @param name
     * @return
     */
    static public boolean update_nickname(String user_id, String name) {
        HashMap<String, Object> retHashMap = JsonManager.update_nickname(user_id, name);
        if ("0".equals(retHashMap.get("k0"))) {
            return Boolean.valueOf((String) retHashMap.get("k1"));
        } else {
            return false;
        }
    }

    /**
     * @param user_id
     * @param day
     * @return
     */
    static public boolean insert_app_info(String user_id, long day) {

        List<AppUsageOrg> appDataList = SqliteBase.get_app_history(day, Config.MAX_UPLOAD_COUNT);
        if (appDataList == null || appDataList.size() == 0) {
            return true;
        }
        HashMap<String, Object> retHashMap = JsonManager.insert_app_info(user_id, appDataList);
        if ("0".equals(retHashMap.get("k0"))) {
            AppUsageOrg app = appDataList.get(appDataList.size() - 1);
            for (int i = 0; i < 10; i++) {
                if (SqliteBase.update_app_history(app.getId())) {
                    return true;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    static public boolean transfer_app_history_to_server(String user_id) {
        return insert_app_info(user_id, TimeFormat.SecondsNow());
    }

    static public boolean delete_app_history() {
        long day = TimeFormat.getSecondForMonth(TimeFormat.Now(), -Config.MAX_HISTORY_MONTH_COUNT);
        return SqliteBase.delete_app_history(day);
    }

    static public boolean delete_app_duration() {
        long day = TimeFormat.getSecondForMonth(TimeFormat.Now(), -Config.MAX_DURATION_MONTH_COUNT);
        return SqliteBase.delete_app_duration(day);
    }

    /**
     * @param user_id
     * @param start_time
     * @param end_time
     * @return
     */
    static public List<AppUsageOrg> get_app_time(String user_id, Calendar start_time, Calendar end_time) {
        HashMap<String, Object> retHashMap = JsonManager.get_app_time(user_id, TimeFormat.getSecond(start_time), TimeFormat.getSecond(end_time));
        List<AppUsageOrg> ret = (List<AppUsageOrg>) retHashMap.get("k2");
        return ret;
    }

    /**
     * @param user_id
     * @param start_day
     * @return
     */
    static public List<AppUsage> get_app_duration_day(String user_id, Calendar start_day) {


        long start_time = TimeFormat.getSecondForDay(start_day, 0);

        long end_time = TimeFormat.getSecondForDay(start_day, 1);

        return getAppUsage(user_id, true, start_time, end_time);
    }

    /**
     * @param user_id
     * @param start_day
     * @return
     */
    static public void get_app_duration_day_async(final String user_id, final Calendar start_day, final IJsonCallback cbk) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                UtilLog.logWithCodeInfo("usrid is " + user_id, "get_app_duration_day_async", "AppUsageManager");
                cbk.refreshData(App.GET_APP_DURATION_DAY, get_app_duration_day(user_id, start_day));
            }
        }
        ).start();

    }

    static public long get_today_app_duration_total() {

        Calendar now = TimeFormat.Now();
        long start_time = TimeFormat.getSecondForDay(now, 0);

        long end_time = TimeFormat.getSecondForDay(now, 1);

        return SqliteBase.get_today_app_duration_total(start_time, end_time);
    }

    /**
     * @param user_id
     * @param start_day
     * @return
     */
    static public List<AppUsage> get_app_duration_month(String user_id, Calendar start_day) {

        long start_time = TimeFormat.getSecondForMonth(start_day, 0);

        long end_time = TimeFormat.getSecondForMonth(start_day, 1);

        return getAppUsage(user_id, false, start_time, end_time);
    }


    /**
     * @param user_id
     * @param by_day
     * @param start_time
     * @param end_time
     * @return
     */
    static private List<AppUsage> getAppUsage(String user_id, boolean by_day, long start_time, long end_time) {
        if (user_id == null) {
            return null;
        }
        List<AppUsage> v = _data_day.get(start_time);
        if (user_id.equals(RegistrationManager.getUserId())) {

            UtilLog.logWithCodeInfo(user_id + " is a RegistrationManager.getUserId()", "getAppUsage", "AppUsageManager");

            if (v == null) {
                // find local db
                // the same month or the
                long now;
                if (by_day) {
                    now = TimeFormat.getSecondForDay(TimeFormat.Now(), 0);
                } else {
                    now = TimeFormat.getSecondForMonth(TimeFormat.Now(), 0);
                }

                long timeHasDuration = TimeFormat.getSecondForMonth(TimeFormat.Now(), -Config.MAX_DURATION_MONTH_COUNT);

                if (timeHasDuration <= start_time) {
                    v = SqliteBase.get_app_duration(start_time);
                }

                if (v == null || v.size() == 0) {
                    //get data from remote db
                    long timeHasHistory = TimeFormat.getSecondForMonth(TimeFormat.Now(), -Config.MAX_HISTORY_MONTH_COUNT);

                    if (timeHasHistory <= start_time) {
                        v = SqliteBase.sum_app_duration(start_time, end_time);
                        if (now - start_time != 0) { //not today or this month
                            _data_day.put(start_time, v);
                            SqliteBase.insert_app_duration(start_time, v);
                        }

                    } else {
                        HashMap<String, Object> retData = JsonManager.get_app_duration(user_id, start_time, end_time);

                        if (retData != null && "0".equals(retData.get("k0"))) {
                            v = (List<AppUsage>) retData.get("k2");
                            if (v != null) {
                                _data_day.put(start_time, v);
                                SqliteBase.insert_app_duration(start_time, v);
                            } else {
                                _data_day.put(start_time, new ArrayList<AppUsage>());
                            }
                        }
                    }
                }
            }
        } else {
            UtilLog.logWithCodeInfo(user_id + " is a contact user id", "getAppUsage", "AppUsageManager");
            v = get_contacts_app_usage_by_day(user_id, start_time, end_time);
        }
        int app_count = Config.APP_COUNT();
        AppUsage app = null;
        for (int i = app_count; i < v.size(); i++) {
            if (i == app_count) {
                app = v.get(i);
                app.setApp_pkgname("others");
                app.setApp_name("others");
            } else {
                AppUsage app_next = v.get(app_count + 1);
                app.setDuration(app.getDuration() + app_next.getDuration());
                app.setTimes(app.getTimes() + app_next.getTimes());
                v.remove(app_count + 1);
            }
        }

        return v;
    }

    static private List<AppUsage> get_contacts_app_usage_by_day(String user_id, long start_time, long end_time) {
        List<AppUsage> v = new ArrayList<AppUsage>();

        HashMap<String, Object> retData = JsonManager.get_app_duration(user_id, start_time, end_time);

        if (retData != null && "0".equals(retData.get("k0"))) {
            v = (List<AppUsage>) retData.get("k2");
        }
        return v;
    }

    /**
     * @param appDataList
     * @return
     */

    static public boolean saveAppListToSqlite(List<AppUsageOrg> appDataList) {
        return SqliteBase.insert_app_history(appDataList);
    }

    /**
     * @param appData
     * @return
     */
    static public boolean saveAppInfoToSqlite(AppUsageOrg appData) {
        List<AppUsageOrg> appDataList = new ArrayList<AppUsageOrg>();
        appDataList.add(appData);
        return SqliteBase.insert_app_history(appDataList);
    }

    /**
     * List<AppUsage> appList = new ArrayList<AppUsage>();
     * Map<String, Map<Integer, AppUsage>> allData = new Hashtable<String, Map<Integer, AppUsage>>();
     * SqliteBase.sum_app_duration_by_day(TimeFormat.getSecondForMonth(TimeFormat.Now(), 0),15,appList,allData);
     *
     * @param[in] start_time 开始时刻，以秒为单位。
     * @param[in] count 取得天数。
     * @param[out] retList 返回前十的app信息，其中包含app name，app的package name，count天内的总时间，总的使用次数。（按使用总时间排序）
     * @param[out] allData 以package name为key，内部再以1---count为key的map，比如取package name为A的应用时
     * 第1天allData.get("A").get(1)
     * 第2天allData.get("A").get(2)
     */
    static public boolean sum_app_duration_by_day(long start_time, int count, List<AppUsage> retList, Map<String, Map<Integer, AppUsage>> allData) {
        return SqliteBase.sum_app_duration_by_day(start_time, count, retList, allData);

    }


    /**
     * 以下两个函数是上面的分割版函数。应用上可以使用sum_app_duration_by_day，或者sum_app_duration_by_day_top10 + sum_app_duration_by_day_package_name
     *
     * @param start_time
     * @param count
     * @return
     */
    static public List<AppUsage> sum_app_duration_by_day_top10(long start_time, int count) {
        return SqliteBase.sum_app_duration_by_day_top10(start_time, count);
    }

    static public Map<Integer, AppUsage> sum_app_duration_by_day_package_name(long start_time, int count, String pkg_name) {
        return SqliteBase.sum_app_duration_by_day_package_name(start_time, count, pkg_name);
    }
}
