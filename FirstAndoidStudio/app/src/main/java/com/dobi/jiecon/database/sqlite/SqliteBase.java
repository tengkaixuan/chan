package com.dobi.jiecon.database.sqlite;

import android.content.ContentValues;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dobi.jiecon.UtilLog;
import com.dobi.jiecon.data.FamilyMember;
import com.dobi.jiecon.data.RelationData;
import com.dobi.jiecon.database.AppUsage;
import com.dobi.jiecon.database.AppUsageOrg;
import com.dobi.jiecon.database.JieconDBHelper;
import com.dobi.jiecon.utils.Config;
import com.dobi.jiecon.utils.TimeFormat;


import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class SqliteBase {

//    public

    /**
     * @param day
     * @param retList
     */
    public static void insert_app_duration(long day, List<AppUsage> retList) {
        SQLiteDatabase db = JieconDBHelper.getDb();
        try {

            String table = "app_duration";

            //   String nullColumnHack = "id";
            for (AppUsage app : retList) {
                ContentValues values = new ContentValues();

                values.put("day", day);

                values.put("app_pkgname", app.getApp_pkgname());
                values.put("app_name", app.getApp_name());
                values.put("duration", app.getDuration());
                values.put("times", app.getTimes());
                db.insert(table, null, values);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }

    }

    /**
     * @param day
     * @return
     */
    public static List<AppUsage> get_app_duration(long day) {
        List<AppUsage> retList = new ArrayList<AppUsage>();
        SQLiteDatabase db = JieconDBHelper.getDb();
        try {

            String table = "app_duration";
            String[] columns = new String[]{"app_pkgname", "app_name", "duration", "times"};

            String selection = "day =?";
            // String[] selectionArgs = new String[] { "0", "roiding.com" };

            String[] selectionArgs = new String[]{String.valueOf(day)};
            String groupBy = null;


            String having = null;

            String orderBy = "duration desc";

            // String limit = "1";
            String limit = null;
            Cursor rs = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);

            rs.moveToFirst();

            for (int i = 0; i < rs.getCount(); i++) {
                AppUsage app = new AppUsage();
                app.setApp_pkgname(rs.getString(0));
                app.setApp_name(rs.getString(1));
                app.setDuration(rs.getLong(2));
                app.setTimes(rs.getLong(3));

                retList.add(app);
                rs.moveToNext();

            }

            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            db.close();
        }
        return retList;
    }

    /**
     * @param retList
     */
    public static boolean insert_app_history(List<AppUsageOrg> retList) {
        SQLiteDatabase db = JieconDBHelper.getDb();
        try {
            String table = "app_history";
            db.beginTransaction();
            //   String nullColumnHack = "id";
            for (AppUsageOrg app : retList) {
                ContentValues values = new ContentValues();
                values.put("app_name", app.getApp_name());
                values.put("app_pkgname", app.getApp_pkgname());
                values.put("start_time", app.getStart_time());
                values.put("end_time", app.getEnd_time());
                values.put("uploaded", 0);
                long id = db.insert(table, null, values);
            }
            db.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
        return false;
    }

    public static List<AppUsageOrg> get_app_history(long day, long count) {
        List<AppUsageOrg> retList = new ArrayList<AppUsageOrg>();
        SQLiteDatabase db = JieconDBHelper.getDb();
        try {

            String table = "app_history";
            String[] columns = new String[]{"app_pkgname", "app_name", "start_time", "end_time", "id"};
            String selection = " uploaded = 0 ";
            String[] selectionArgs = null;
            if (day > 0) {
                selection = selection + " AND end_time < ? ";
                // String[] selectionArgs = new String[] { "0", "roiding.com" };
                selectionArgs = new String[]{String.valueOf(day)};
            }
            String groupBy = null;

            String having = null;

            String orderBy = "id asc";

            // String limit = "1";
            String limit = null;
            if (count != 0) {
                limit = String.valueOf(count);
            }

            Cursor rs = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);

            rs.moveToFirst();

            for (int i = 0; i < rs.getCount(); i++) {
                AppUsageOrg app = new AppUsageOrg();
                app.setApp_pkgname(rs.getString(0));
                app.setApp_name(rs.getString(1));
                app.setStart_time(rs.getLong(2));
                app.setEnd_time(rs.getLong(3));
                app.setId(rs.getLong(4));
                retList.add(app);
                rs.moveToNext();

            }

            rs.close();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            db.close();
        }
        return retList;
    }

    public static boolean update_app_history(long id) {
        SQLiteDatabase db = JieconDBHelper.getDb();
        try {

            String table = "app_history";

            //   String nullColumnHack = "id";


            String selection = " uploaded = 0 AND id <= ?";
            String[] selectionArgs = new String[]{String.valueOf(id)};
            String[] whereArgs = selectionArgs;
            String whereClause = selection;
            ContentValues values = new ContentValues();

            values.put("uploaded", 1);

            db.update(table, values, whereClause, whereArgs);


            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        return false;
    }

    public static boolean delete_app_history(long day) {
        SQLiteDatabase db = JieconDBHelper.getDb();
        try {

            String table = "app_history";

            //   String nullColumnHack = "id";


            String selection = " uploaded = 1 AND end_time <= ?";
            String[] selectionArgs = new String[]{String.valueOf(day)};
            String[] whereArgs = selectionArgs;
            String whereClause = selection;

            db.delete(table, whereClause, whereArgs);


            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        return false;
    }

    public static boolean delete_app_duration(long day) {
        SQLiteDatabase db = JieconDBHelper.getDb();
        try {

            String table = "app_duration";

            //   String nullColumnHack = "id";


            String selection = "day < ?";
            String[] selectionArgs = new String[]{String.valueOf(day)};
            String[] whereArgs = selectionArgs;
            String whereClause = selection;

            db.delete(table, whereClause, whereArgs);


            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        return false;
    }

    public static boolean insert_update_config(int key, String value) {
        SQLiteDatabase db = JieconDBHelper.getDb();
        String table = "config";

        //   String nullColumnHack = "id";
        try {

            String selection = "key = ? ";
            String[] selectionArgs = new String[]{String.valueOf(key)};
            String[] whereArgs = selectionArgs;
            String whereClause = selection;
            ContentValues values = new ContentValues();

            values.put("value", value);

            int count = db.update(table, values, whereClause, whereArgs);
            if (count == 1) return true;
            return insert_config(key, value);
        } catch (Exception e) {

        } finally {
            db.close();
        }
        return false;
    }

    public static boolean insert_config(int key, String value) {
        SQLiteDatabase db = JieconDBHelper.getDb();
        try {

            String table = "config";

            //   String nullColumnHack = "id";

            ContentValues values = new ContentValues();

            values.put("key", key);
            values.put("value", value);

            db.insert(table, null, values);

            return true;
        } catch (Exception e) {
        } finally {
            db.close();
        }
        return false;
    }


    public static boolean update_config(int key, String value) {
        SQLiteDatabase db = JieconDBHelper.getDb();
        try {

            String table = "config";

            //   String nullColumnHack = "id";


            String selection = "key = ? ";
            String[] selectionArgs = new String[]{String.valueOf(key)};
            String[] whereArgs = selectionArgs;
            String whereClause = selection;
            ContentValues values = new ContentValues();

            values.put("value", value);

            db.update(table, values, whereClause, whereArgs);


            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        return false;
    }

    public static String get_config(int key) {
        SQLiteDatabase db = JieconDBHelper.getDb();
        String ret = null;
        try {

            String table = "config";
            String[] columns = new String[]{"value"};
            String selection = null;
            String[] selectionArgs = null;

            selection = "key =?";
            // String[] selectionArgs = new String[] { "0", "roiding.com" };
            selectionArgs = new String[]{String.valueOf(key)};

            String groupBy = null;

            String having = null;

            String orderBy = null;// "app_pkgname desc";

            String limit = "1";

            Cursor rs = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);

            rs.moveToFirst();

            if (0 < rs.getCount()) {
                ret = rs.getString(0);
            }

            rs.close();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            db.close();
        }
        return ret;
    }

    static public void set_config(int key, String value) {
        if (null != SqliteBase.get_config(key)) {
            SqliteBase.update_config(key, value);
        } else {
            SqliteBase.insert_config(key, value);
        }
    }

    static public List<AppUsage> sum_app_duration(long start_time, long end_time) {
        List<AppUsage> retList = new ArrayList<AppUsage>();
        SQLiteDatabase db = JieconDBHelper.getDb();
        try {

            String sql = "select  app_pkgname, app_name,";
            sql = sql + " sum((case when end_time  < ?"; // end_time .
            sql = sql + " then end_time else ? "; //. end_time ;
            sql = sql + "   end)   " + " -(case when  ? "; //. start_time
            sql = sql + " < start_time then start_time else ?"; //. start_time
            sql = sql + " end) ) as duration,";
            sql = sql + " count(1) as times";
            sql = sql + " from  app_history a";
            sql = sql + " where ";
            sql = sql + " ( start_time <= ? ";// . end_time
            sql = sql + "   AND  end_time >= ? )";//start_time
            sql = sql + "  AND app_pkgname not in (select app_pkgname from filter_app)";
            sql = sql + " group by app_pkgname";
            sql = sql + " order by duration desc";

            // String[] selectionArgs = new String[] { "0", "roiding.com" };

            String[] selectionArgs = new String[]{String.valueOf(end_time),
                    String.valueOf(end_time),
                    String.valueOf(start_time),
                    String.valueOf(start_time),
                    String.valueOf(end_time),
                    String.valueOf(start_time)};

            Cursor rs = db.rawQuery(sql, selectionArgs);

            rs.moveToFirst();
            AppUsage app = null;

            for (int i = 0; i < rs.getCount(); i++) {
                app = new AppUsage();
                app.setApp_pkgname(rs.getString(0));
                app.setApp_name(rs.getString(1));
                app.setDuration(rs.getLong(2));
                app.setTimes(rs.getLong(3));
                retList.add(app);
                rs.moveToNext();

            }

            rs.close();


        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            db.close();
        }
        return retList;

    }

    static public boolean sum_app_duration_by_day(long start_time, int count, List<AppUsage> retList, Map<String, Map<Integer, AppUsage>> allData) {
        retList.clear();
        allData.clear();
        SQLiteDatabase db = JieconDBHelper.getDb();
        try {

            String sql = "select  app_pkgname, app_name,";
            sql = sql + " sum((case when end_time  < ?"; // end_time .
            sql = sql + " then end_time else ? "; //. end_time ;
            sql = sql + "   end)   " + " -(case when  ? "; //. start_time
            sql = sql + " < start_time then start_time else ?"; //. start_time
            sql = sql + " end) ) as duration,";
            sql = sql + " count(1) as times";
            sql = sql + " from  app_history a";
            sql = sql + " where ";
            sql = sql + " ( start_time <= ? ";// . end_time
            sql = sql + "   AND  end_time >= ? )";//start_time
            sql = sql + "  AND app_pkgname not in (select app_pkgname from filter_app)";
            sql = sql + " group by app_pkgname";
            sql = sql + " order by duration desc";

            // String[] selectionArgs = new String[] { "0", "roiding.com" };
            long end_time = start_time + 24 * 60 * 60 * count;
            String[] selectionArgs = new String[]{String.valueOf(end_time),
                    String.valueOf(end_time),
                    String.valueOf(start_time),
                    String.valueOf(start_time),
                    String.valueOf(end_time),
                    String.valueOf(start_time)};

            Cursor rs = db.rawQuery(sql, selectionArgs);

            rs.moveToFirst();
            AppUsage app = null;
            String sParam = "";
            int realCount = 10;
            if (rs.getCount() < 10) {
                realCount = rs.getCount();
            }
            String[] pkgNameArgs = new String[realCount + 6];
            int app_count = Config.APP_COUNT();
            for (int i = 0; i < rs.getCount() && i < app_count; i++) {

                app = new AppUsage();
                app.setApp_pkgname(rs.getString(0));
                app.setApp_name(rs.getString(1));
                app.setDuration(rs.getLong(2));
                app.setTimes(rs.getLong(3));
                retList.add(app);
                if (i == 0) {
                    sParam = "?";
                } else {
                    sParam = sParam + ",?";
                }
                pkgNameArgs[i + 6] = app.getApp_pkgname();
                rs.moveToNext();

            }

            rs.close();
            if (realCount < 1) {
                return true;
            }
            String sql_base = "select  app_pkgname, app_name,";
            sql_base = sql_base + " sum((case when end_time  < ?"; // end_time .
            sql_base = sql_base + " then end_time else ? "; //. end_time ;
            sql_base = sql_base + "   end)   " + " -(case when  ? "; //. start_time
            sql_base = sql_base + " < start_time then start_time else ?"; //. start_time
            sql_base = sql_base + " end) ) as duration,";
            sql_base = sql_base + " count(1) as times";
            sql_base = sql_base + " from  app_history a";
            sql_base = sql_base + " where ";
            sql_base = sql_base + " ( start_time <= ? ";// . end_time
            sql_base = sql_base + "   AND  end_time >= ? )";//start_time
            sql_base = sql_base + "  AND app_pkgname  in ";
            sql_base = sql_base + "(" + sParam + ")";
            sql_base = sql_base + " group by app_pkgname";
            sql_base = sql_base + " order by duration desc";

            for (int i = 0; i < count; i++) {
//                end_time = start_time + 24 * 60 * 60 * (i + 1);
                end_time = start_time + 24 * 60 * 60; //ROCK MODIFIED
                pkgNameArgs[0] = String.valueOf(end_time);
                pkgNameArgs[1] = String.valueOf(end_time);
                pkgNameArgs[2] = String.valueOf(start_time);
                pkgNameArgs[3] = String.valueOf(start_time);
                pkgNameArgs[4] = String.valueOf(end_time);
                pkgNameArgs[5] = String.valueOf(start_time);

                rs = db.rawQuery(sql_base, pkgNameArgs);
                rs.moveToFirst();
                for (int j = 0; j < rs.getCount(); j++) {
                    app = new AppUsage();
                    app.setApp_pkgname(rs.getString(0));
                    app.setApp_name(rs.getString(1));
                    app.setDuration(rs.getLong(2));
                    app.setTimes(rs.getLong(3));
                    Map<Integer, AppUsage> data = allData.get(app.getApp_pkgname());
                    if (data == null) {
                        data = new Hashtable<Integer, AppUsage>();
                        allData.put(app.getApp_pkgname(), data);
                    }
                    data.put(i + 1, app);
                    rs.moveToNext();
                }
                rs.close();
                start_time = start_time + 24 * 60 * 60; //ROCK MODIFIED
            }


        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            db.close();
        }
        return true;

    }

    static public List<AppUsage> sum_app_duration_by_day_top10(long start_time, int count) {
        List<AppUsage> retList = new ArrayList<AppUsage>();
        SQLiteDatabase db = JieconDBHelper.getDb();
        try {

            String sql = "select  app_pkgname, app_name,";
            sql = sql + " sum((case when end_time  < ?"; // end_time .
            sql = sql + " then end_time else ? "; //. end_time ;
            sql = sql + "   end)   " + " -(case when  ? "; //. start_time
            sql = sql + " < start_time then start_time else ?"; //. start_time
            sql = sql + " end) ) as duration,";
            sql = sql + " count(1) as times";
            sql = sql + " from  app_history a";
            sql = sql + " where ";
            sql = sql + " ( start_time <= ? ";// . end_time
            sql = sql + "   AND  end_time >= ? )";//start_time
            sql = sql + "  AND app_pkgname not in (select app_pkgname from filter_app)";
            sql = sql + " group by app_pkgname";
            sql = sql + " order by duration desc";

            // String[] selectionArgs = new String[] { "0", "roiding.com" };
            long end_time = start_time + 24 * 60 * 60 * count;
            String[] selectionArgs = new String[]{String.valueOf(end_time),
                    String.valueOf(end_time),
                    String.valueOf(start_time),
                    String.valueOf(start_time),
                    String.valueOf(end_time),
                    String.valueOf(start_time)};

            Cursor rs = db.rawQuery(sql, selectionArgs);

            rs.moveToFirst();
            AppUsage app = null;
            int app_count = Config.APP_COUNT();
            for (int i = 0; i < rs.getCount() && i < app_count; i++) {

                app = new AppUsage();
                app.setApp_pkgname(rs.getString(0));
                app.setApp_name(rs.getString(1));
                app.setDuration(rs.getLong(2));
                app.setTimes(rs.getLong(3));
                retList.add(app);
                rs.moveToNext();

            }

            rs.close();


        } catch (Exception e) {
            e.printStackTrace();
            return retList;
        } finally {
            db.close();
        }
        return retList;

    }

    static public Map<Integer, AppUsage> sum_app_duration_by_day_package_name(long start_time, int count, String pkg_name) {
        Map<Integer, AppUsage> retMap = new Hashtable<Integer, AppUsage>();
        SQLiteDatabase db = JieconDBHelper.getDb();
        try {
            AppUsage app = null;
            String sql_base = "select  app_pkgname, app_name,";
            sql_base = sql_base + " sum((case when end_time  < ?"; // end_time .
            sql_base = sql_base + " then end_time else ? "; //. end_time ;
            sql_base = sql_base + "   end)   " + " -(case when  ? "; //. start_time
            sql_base = sql_base + " < start_time then start_time else ?"; //. start_time
            sql_base = sql_base + " end) ) as duration,";
            sql_base = sql_base + " count(1) as times";
            sql_base = sql_base + " from  app_history a";
            sql_base = sql_base + " where ";
            sql_base = sql_base + " ( start_time <= ? ";// . end_time
            sql_base = sql_base + "   AND  end_time >= ? )";//start_time
            sql_base = sql_base + "  AND app_pkgname   = ? ";
            sql_base = sql_base + " group by app_pkgname ";
            String[] pkgNameArgs = new String[7];
            pkgNameArgs[6] = pkg_name;
            for (int i = 0; i < count; i++) {
                long end_time = start_time + 24 * 60 * 60 * (i + 1);
                pkgNameArgs[0] = String.valueOf(end_time);
                pkgNameArgs[1] = String.valueOf(end_time);
                pkgNameArgs[2] = String.valueOf(start_time);
                pkgNameArgs[3] = String.valueOf(start_time);
                pkgNameArgs[4] = String.valueOf(end_time);
                pkgNameArgs[5] = String.valueOf(start_time);

                Cursor rs = db.rawQuery(sql_base, pkgNameArgs);
                rs.moveToFirst();
                for (int j = 0; j < rs.getCount(); j++) {
                    app = new AppUsage();
                    app.setApp_pkgname(rs.getString(0));
                    app.setApp_name(rs.getString(1));
                    app.setDuration(rs.getLong(2));
                    app.setTimes(rs.getLong(3));

                    retMap.put(i + 1, app);
                    rs.moveToNext();
                }
                rs.close();
            }


        } catch (Exception e) {
            e.printStackTrace();
            return retMap;
        } finally {
            db.close();
        }
        return retMap;

    }

    static public long get_today_app_duration_total(long start_time, long end_time) {
        long ret = 0;
        SQLiteDatabase db = JieconDBHelper.getDb();
        try {

            String sql = "select  ";
            sql = sql + " sum((case when end_time  < ?"; // end_time .
            sql = sql + " then end_time else ? "; //. end_time ;
            sql = sql + "   end)   " + " -(case when  ? "; //. start_time
            sql = sql + " < start_time then start_time else ?"; //. start_time
            sql = sql + " end) ) as duration ";
            sql = sql + " from  app_history";
            sql = sql + " where ";
            sql = sql + " ( start_time <= ? ";// . end_time
            sql = sql + "   AND  end_time >= ? )";//start_time
            sql = sql + "  AND app_pkgname not in (select app_pkgname from filter_app)";


            // String[] selectionArgs = new String[] { "0", "roiding.com" };

            String[] selectionArgs = new String[]{String.valueOf(end_time),
                    String.valueOf(end_time),
                    String.valueOf(start_time),
                    String.valueOf(start_time),
                    String.valueOf(end_time),
                    String.valueOf(start_time)};

            Cursor rs = db.rawQuery(sql, selectionArgs);

            rs.moveToFirst();
            AppUsage app = null;
            if (0 < rs.getCount()) {

                ret = rs.getLong(0);

            }

            rs.close();


        } catch (Exception e) {
            e.printStackTrace();
            return ret;
        } finally {
            db.close();
        }
        return ret;

    }


    static public List<FamilyMember> get_friends(int type) {
        List<FamilyMember> retList = new ArrayList<FamilyMember>();
        SQLiteDatabase db = JieconDBHelper.getDb();
        try {
//            String sql = " select distinct user_id, name, phone, type ";
//            sql = sql + " from  relation_list";
//            String[] cols = new String[]{"user_id", "name", "phone","status","role"};
            String[] cols = new String[]{"user_id", "name", "phone"};
            String selection = null;
            if (type == 1) {
                selection = "  role = 0 ";
            } else if (type == 2) {
                selection = " role = 1 ";
            }
            Cursor rs = db.query(true, "relation_list", cols, selection, null, null, null, null, null);

            rs.moveToFirst();
            FamilyMember relation = null;
            for (int i = 0; i < rs.getCount(); i++) {

                relation = new FamilyMember();
                relation.setUser_id(rs.getString(0));
                relation.setName(rs.getString(1));
                relation.setPhone(rs.getString(2));
//                relation.setStatus(rs.getInt(3));
//                relation.setRole(rs.getInt(3));
                retList.add(relation);
                rs.moveToNext();
            }

            rs.close();


        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            db.close();
        }
        return retList;
    }

    static public List<RelationData> get_individual_relation_list(String usrid) {
        List<RelationData> retList = new ArrayList<RelationData>();
        SQLiteDatabase db = JieconDBHelper.getDb();
        try {
            if (usrid == null || usrid.equals("") || usrid.equals("null")) {
                retList = null;
            } else {

                String sql = " select seq, user_id , name,role,phone ,time ,status ,msg ,read_flag ";
                sql = sql + " from  relation_list";
                sql = sql + " where user_id=";
                sql = sql + usrid;
                sql = sql + " order by status asc";
                String[] selectionArgs = null;

                Cursor rs = db.rawQuery(sql, selectionArgs);
                rs.moveToFirst();
                RelationData relation = null;
                if (rs.getCount() == 0) {
                    retList = null;
                }
                for (int i = 0; i < rs.getCount(); i++) {

                    relation = new RelationData();
                    relation.setSeq(rs.getLong(0));
                    relation.setUser_id(rs.getString(1));
                    relation.setName(rs.getString(2));
                    relation.setRole(rs.getLong(3));
                    relation.setPhone(rs.getString(4));
                    relation.setTime(rs.getLong(5));
                    relation.setStatus(rs.getLong(6));
                    relation.setMsg(rs.getString(7));
                    relation.setRead_flag(rs.getLong(8));
                    retList.add(relation);
                    rs.moveToNext();
                }

                rs.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            db.close();
        }
        return retList;
    }


    static public List<RelationData> get_relation_list(int type) {
        List<RelationData> retList = new ArrayList<RelationData>();
        SQLiteDatabase db = JieconDBHelper.getDb();
        try {

            String sql = " select seq  ,user_id , name,role,phone ,time ,status ,msg ,read_flag ";
            sql = sql + " from  relation_list";
            sql = sql + " where (status > 1 ";
            if (type == 1) {
                sql = sql + " and role = 0 )";
            } else if (type == 2) {
                sql = sql + " and role = 1 )";
            } else {
                sql = sql + " )  or ((status <= 1) and user_id not in(select user_id from relation_list where status > 1))";
            }
            String[] selectionArgs = null;

            Cursor rs = db.rawQuery(sql, selectionArgs);

            rs.moveToFirst();
            RelationData relation = null;
            for (int i = 0; i < rs.getCount(); i++) {

                relation = new RelationData();
                relation.setSeq(rs.getLong(0));
                relation.setUser_id(rs.getString(1));
                relation.setName(rs.getString(2));
                relation.setRole(rs.getLong(3));
                relation.setPhone(rs.getString(4));
                relation.setTime(rs.getLong(5));
                relation.setStatus(rs.getLong(6));
                relation.setMsg(rs.getString(7));
                relation.setRead_flag(rs.getLong(8));
                retList.add(relation);
                rs.moveToNext();
            }

            rs.close();


        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            db.close();
        }
        return retList;
    }

    static public RelationData get_recent_supervision_relation() {
        RelationData recent_supervision = null;
        SQLiteDatabase db = JieconDBHelper.getDb();
        try {


            String sql = "select r.seq  ,r.user_id ,r.name,r.phone ,r.time + ifnull(u.time,0) as time  ";
            sql = sql + " from  relation_list r";
            sql = sql + " left join unlock_info u on r.user_id = u.user_id and u.day = ? ";
            sql = sql + "  where r.role = 0 and r.status = 1";
            sql = sql + " order by time asc";
            sql = sql + " limit 1";
            String[] selectionArgs = new String[]{String.valueOf(TimeFormat.getSecondForDay(TimeFormat.Now(), 0))};

            Cursor rs = db.rawQuery(sql, selectionArgs);

            rs.moveToFirst();

            if (0 < rs.getCount()) {
                recent_supervision = new RelationData();
                recent_supervision.setSeq(rs.getLong(0));
                recent_supervision.setUser_id(rs.getString(1));
                recent_supervision.setName(rs.getString(2));
                recent_supervision.setPhone(rs.getString(3));
                recent_supervision.setTime(rs.getLong(4));
            }

            rs.close();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            db.close();
        }
        return recent_supervision;
    }

    public static boolean update_relation_list(List<RelationData> relationList) {
        SQLiteDatabase db = JieconDBHelper.getDb();
        try {
            String table = "relation_list";

            db.beginTransaction();

            db.delete(table, null, null);
            //   String nullColumnHack = "id";
            for (RelationData relation : relationList) {
                ContentValues values = new ContentValues();

                values.put("seq", relation.getSeq());
                values.put("user_id", relation.getUser_id());
                values.put("name", relation.getName());
                values.put("phone", relation.getPhone());
                values.put("role", relation.getRole());
                values.put("time", relation.getTime());
                values.put("status", relation.getStatus());
                values.put("msg", relation.getMsg());
                values.put("read_flag", relation.getRead_flag());

                db.insert(table, null, values);
            }
            db.setTransactionSuccessful();
            //          recent_supervision = null;
//            db.endTransaction();
//            db.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
        return false;
    }

    public static boolean update_relation(long seq_m, RelationData relation) {
        SQLiteDatabase db = JieconDBHelper.getDb();
        try {
            String table = "relation_list";

            db.beginTransaction();
            String selection = null;
            String[] columns = null;
            String[] selectionArgs = null;
            int count = 0;
            Cursor rs = null;
            if (relation.getStatus() != 31) {
                selection = "seq = ? and status > 1 ";
                columns = new String[]{"seq"};
                selectionArgs = new String[]{String.valueOf(relation.getSeq())};
                rs = db.query(table, columns, selection, selectionArgs, null, null, null, "1");
                count = rs.getCount();
                rs.close();
                //request info 表中不存在
                if (count == 0) {

                    ContentValues values = new ContentValues();

                    values.put("seq", relation.getSeq());
                    values.put("user_id", relation.getUser_id());
                    values.put("time", relation.getTime());
                    values.put("name", relation.getName());
                    values.put("type", relation.getType());
                    values.put("role", relation.getRole());
                    values.put("phone", relation.getPhone());
                    values.put("status", relation.getStatus());
                    values.put("msg", relation.getMsg());
                    values.put("read_flag", relation.getRead_flag());

                    db.insert(table, null, values);
                } else {
                    ContentValues values = new ContentValues();

                    values.put("seq", relation.getSeq());
                    values.put("user_id", relation.getUser_id());
                    values.put("time", relation.getTime());
                    values.put("name", relation.getName());
                    values.put("type", relation.getType());
                    values.put("role", relation.getRole());
                    values.put("phone", relation.getPhone());
                    values.put("status", relation.getStatus());
                    values.put("msg", relation.getMsg());
                    values.put("read_flag", relation.getRead_flag());

                    db.update(table, values, selection, selectionArgs);
                }
            }
            if (relation.getStatus() == 11) {

                selection = "seq = ? and status = 1 and role = ? ";
                columns = new String[]{"seq"};
                selectionArgs = new String[]{String.valueOf(seq_m), String.valueOf(relation.getRole())};
                rs = db.query(table, columns, selection, selectionArgs, null, null, null, "1");
                count = rs.getCount();
                rs.close();

                //monitor info 表中不存在(承认时11)
                if (count == 0) {

                    ContentValues values = new ContentValues();

                    values.put("seq", seq_m);
                    values.put("user_id", relation.getUser_id());
                    values.put("time", relation.getTime());
                    values.put("name", relation.getName());
                    values.put("type", relation.getType());
                    values.put("role", relation.getRole());
                    values.put("phone", relation.getPhone());
                    values.put("status", relation.getStatus());
                    values.put("msg", relation.getMsg());
                    values.put("read_flag", relation.getRead_flag());

                    db.insert(table, null, values);
                } else {
                    ContentValues values = new ContentValues();


                    values.put("time", relation.getTime());
                    values.put("status", relation.getStatus());
                    values.put("msg", relation.getMsg());
                    values.put("role", relation.getRole());
                    values.put("read_flag", relation.getRead_flag());

                    selection = "seq = ? and status = 0 ";
                    selectionArgs = new String[]{String.valueOf(seq_m)};
                    db.update(table, values, selection, selectionArgs);

                }

            } else if (relation.getStatus() == 21) {

                selection = "seq = ? ";
                selectionArgs = new String[]{String.valueOf(seq_m)};
                db.delete(table, selection, selectionArgs);
            } else if (relation.getStatus() == 31) {

                selection = "user_id = ? ";
                columns = new String[]{"day"};
                selectionArgs = new String[]{relation.getUser_id()};
                rs = db.query("unlock_info", columns, selection, selectionArgs, null, null, null, "1");
                count = rs.getCount();
                rs.close();
                if (count == 0) {
                    ContentValues values = new ContentValues();

                    values.put("user_id", relation.getUser_id());
                    values.put("time", relation.getTime());
                    values.put("day", TimeFormat.getSecondForDay(TimeFormat.Now(), 0));
                    db.insert("unlock_info", null, values);
                } else {
                    ContentValues values = new ContentValues();

                    values.put("time", relation.getTime());
                    values.put("day", TimeFormat.getSecondForDay(TimeFormat.Now(), 0));
                    db.update("unlock_info", values, selection, selectionArgs);
                }
            }

            db.setTransactionSuccessful();
            //        recent_supervision = null;
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
        return false;


    }

    public static boolean update_add_unilateral_family(FamilyMember friend) {
        SQLiteDatabase db = JieconDBHelper.getDb();
        try {
            String table = "add_family";

            db.beginTransaction();

            String phone = friend.getPhone();

            if (null != query_family(db, phone)) {
                UtilLog.logWithCodeInfo("Update family info", "update_add_unilateral_family", "SqliteBase");
                ContentValues values = new ContentValues();
                if (friend.getName()!=null || !friend.getName().equals(""))
                    values.put("name", friend.getName());
                if (null != friend.getUser_id() || !friend.getUser_id().equals(""))
                    values.put("user_id", friend.getUser_id());

                String whereClause = "phone = ?";

                String[] selectionArgs = new String[]{phone};
                String[] whereArgs = selectionArgs;
                int numberOfRowAffected = db.update(table, values, whereClause, whereArgs);
                if (numberOfRowAffected == 0) {
                    UtilLog.logWithCodeInfo("Failed to update family info", "update_add_unilateral_family", "SqliteBase");
                }
            } else {

                ContentValues values = new ContentValues();

                values.put("name", friend.getName());
                values.put("phone", friend.getPhone());
                values.put("user_id", friend.getUser_id());

                UtilLog.logWithCodeInfo(" Add to Sqlite ", "update_add_unilateral_family", "SqliteBase.java");
                UtilLog.logWithCodeInfo(" Name =  " + friend.getName(), "update_add_unilateral_family", "SqliteBase.java");
                UtilLog.logWithCodeInfo(" Phone =  " + friend.getPhone(), "update_add_unilateral_family", "SqliteBase.java");
                UtilLog.logWithCodeInfo(" User_id =  " + friend.getUser_id(), "update_add_unilateral_family", "SqliteBase.java");

                long numberOfRowAffected = db.insert(table, null, values);
                if (numberOfRowAffected == -1) {
                    UtilLog.logWithCodeInfo("Failed to add family to Sqlite", "update_add_unilateral_family", "SqliteBase.java");
                }
            }
            db.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
        return false;
    }

    public static FamilyMember get_unilateral_friend(String phone) {
        return friend_is_existing(phone);
    }
    public static boolean update_usrid_by_phone(String phone, String usrid) {
        FamilyMember m = get_unilateral_friend(phone);
        if (m !=null){
            m.setUser_id(usrid);
        }
       return update_add_unilateral_family(m);
    }

    private static Map<String, Long> _filter_map = null;

    public static Map<String, Long> get_filter_map() {
        if (_filter_map != null) {
            return _filter_map;
        }
        _filter_map = new Hashtable<String, Long>();
        SQLiteDatabase db = JieconDBHelper.getDb();
        try {
            String table = "filter_app";
            String[] columns = new String[]{"app_pkgname", "level"};
            String selection = null;
            String[] selectionArgs = null;

            String groupBy = null;

            String having = null;

            String orderBy = null;

            // String limit = "1";
            String limit = null;

            Cursor rs = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);

            rs.moveToFirst();

            for (int i = 0; i < rs.getCount(); i++) {
                _filter_map.put(rs.getString(0), rs.getLong(4));
                rs.moveToNext();

            }

            rs.close();

        } catch (Exception e) {

            e.printStackTrace();
            return null;
        } finally {
            db.close();
        }
        return _filter_map;
    }

    public static boolean update_filter_app(Map<String, Long> filterList) {
        SQLiteDatabase db = JieconDBHelper.getDb();
        try {
            String table = "filter_app";

            db.beginTransaction();

            db.delete(table, null, null);
            //   String nullColumnHack = "id";
            for (String filter : filterList.keySet()) {
                ContentValues values = new ContentValues();
                values.put("seq", filter);
                values.put("app_pkgname", filterList.get(filter));


                db.insert(table, null, values);
            }
            db.setTransactionSuccessful();
            db.endTransaction();
            _filter_map.clear();
            _filter_map.putAll(filterList);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
        return false;
    }

    public static String get_usrid_from_family_list(String phone) {
        UtilLog.logWithCodeInfo(" Query phone = " + phone + "from family list", "get_usrid_from_family_list", "SqliteBase");

        String ret_user_id = null;
        SQLiteDatabase db = JieconDBHelper.getDb();
        String table = "add_family";
        String[] columns = new String[]{"user_id"};
        String selection = null;
        String[] selectionArgs = null;

        selection = "phone = ?";
        selectionArgs = new String[]{phone};

        String groupBy = null;
        String having = null;
        String orderBy = null;
        String limit = "1";
        Cursor res = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
        if (res.getCount() == 1) {
            res.moveToFirst();
            ret_user_id = res.getString(0);
        } else {
            UtilLog.logWithCodeInfo(" Failed to get usrid from relation list by phone number! " + phone, "get_usrid_from_family_list", "SqliteBase");
        }
        res.close();
        db.close();
        return ret_user_id;
    }

    public static String get_userid_from_relation_list(String phone) {
        UtilLog.logWithCodeInfo(" Query phone = " + phone + "from relation list", "get_userid_from_relation_list", "SqliteBase");

        String ret_user_id = null;
        SQLiteDatabase db = JieconDBHelper.getDb();
        String table = "relation_list";
        String[] columns = new String[]{"user_id"};
        String selection = null;
        String[] selectionArgs = null;

        selection = "phone = ?";
        selectionArgs = new String[]{phone};

        String groupBy = null;
        String having = null;
        String orderBy = null;
        String limit = "1";
        Cursor res = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
        if (res.getCount() == 1) {
            res.moveToFirst();
            ret_user_id = res.getString(0);
        } else {
            UtilLog.logWithCodeInfo(" Failed to get usrid from relation list by phone number! " + phone + "from relation list", "query_family", "SqliteBase");
        }
        res.close();
        db.close();
        return ret_user_id;
    }

    private static FamilyMember query_family(SQLiteDatabase db, String phone) {
        UtilLog.logWithCodeInfo(" Query phone = " + phone, "query_family", "SqliteBase");
        FamilyMember ret = null;
        String table = "add_family";

        String[] columns = new String[]{"phone", "name"};

        String selection = null;
        String[] selectionArgs = null;

        selection = "phone = ?";
        selectionArgs = new String[]{phone};

        String groupBy = null;
        String having = null;
        String orderBy = null;
        String limit = "1";

        Cursor rs = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
        rs.moveToFirst();
        if (rs.getCount() == 1) {
            UtilLog.logWithCodeInfo("phone is existing " + phone, "query_family", "SqliteBase");
            ret = new FamilyMember();
            ret.setPhone(rs.getString(0));
            ret.setName(rs.getString(1));
            UtilLog.logWithCodeInfo("FamilyMember.getPhone is " + ret.getPhone(), "query_family", "SqliteBase");
            UtilLog.logWithCodeInfo("FamilyMember.getName is " + ret.getName(), "query_family", "SqliteBase");
        }

        rs.close();
        return ret;
    }

    public static FamilyMember friend_is_existing(String phone) {
        SQLiteDatabase db = JieconDBHelper.getDb();
        FamilyMember ret = query_family(db, phone);
        db.close();
        return ret;
    }

    public static List<FamilyMember> get_unilateral_families() {
        List<FamilyMember> retList = new ArrayList<FamilyMember>();
        SQLiteDatabase db = JieconDBHelper.getDb();
        try {
            String table = "add_family";

            String[] columns = new String[]{"phone", "name"};

            String selection = null;
            String[] selectionArgs = null;
            String groupBy = null;
            String having = null;
            String orderBy = null;
            String limit = null;

            Cursor rs = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
            rs.moveToFirst();
            FamilyMember relation = null;
            if (rs.getCount() == 0) {
                UtilLog.logWithCodeInfo("No families in Sqlite", "get_unilateral_families", "SqliteBase");
                retList = null;
            }
            for (int i = 0; i < rs.getCount(); i++) {
                relation = new FamilyMember();
                relation.setPhone(rs.getString(0));
                relation.setName(rs.getString(1));
                UtilLog.logWithCodeInfo("Phone" + i + " " + relation.getPhone() + " families in Sqlite", "get_unilateral_families", "SqliteBase.java");
                UtilLog.logWithCodeInfo("Name" + i + " " + relation.getName() + " families in Sqlite", "get_unilateral_families", "SqliteBase.java");
                retList.add(relation);
                rs.moveToNext();
            }
            rs.close();


        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            db.close();
        }
        return retList;
    }

    public static boolean relationIsEmpty() {
        boolean ret = true;
        SQLiteDatabase db = JieconDBHelper.getDb();
        String table = "relation_list";
        String[] columns = new String[]{"seq"};
        String selection = null;
        String[] selectionArgs = null;

        selection = null;
        selectionArgs = null;

        String groupBy = null;
        String having = null;
        String orderBy = null;
        String limit = "1";

        Cursor res = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);

        if (res.getCount() >= 1) {
            ret = false;
        }
        res.close();
        db.close();
        return ret;
    }

}
