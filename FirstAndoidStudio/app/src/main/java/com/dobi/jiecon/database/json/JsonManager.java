package com.dobi.jiecon.database.json;

import com.dobi.jiecon.UtilLog;
import com.dobi.jiecon.data.AddressBook;
import com.dobi.jiecon.data.FamilyMember;
import com.dobi.jiecon.data.RelationData;
import com.dobi.jiecon.database.AppUsage;
import com.dobi.jiecon.database.AppUsageOrg;
import com.dobi.jiecon.database.JieconDBHelper;
import com.dobi.jiecon.database.sqlite.SqliteBase;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class JsonManager {


    public static final String REGISTER = "register.php";
    public static final String UPDATE_PASSWORD = "update_password.php";
    public static final String LOGIN = "login.php";
    public static final String GET_USER_ID_BY_PHONE = "get_user_id_by_phone.php";
    public static final String GET_USERS_ID_BY_PHONES = "get_users_id_by_phones.php";
    public static final String BINDING_PHONE = "binding_phone.php";
    public static final String UPDATE_NICKNAME = "update_nickname.php";
    public static final String UPDATE_USERNAME = "update_username.php";
    public static final String INSERT_APP_INFO = "insert_app_info.php";
    public static final String GET_APP_DURATION = "get_app_duration.php";
    public static final String GET_APP_TIME = "get_app_time.php";
    public static final String SUPERVISION_NOTIFY = "supervision_notify.php";
    public static final String GET_RELATION_LIST = "get_relation_list.php";
    public static final String SEND_REQUEST_READ_FLAG = "send_request_read_flag.php";
    public static final String ADD_FRIEND = "add_friend.php";
    public static final String PEEK_REQUEST = "peek_request.php";

    public static final String INSERT_CONTROL = "insert_control.php";
    public static final String GET_CONTROL = "get_control.php";

    public static final String MONITOR_BUDDY = "monitor_buddy.php";

    public static final String INSERT_ADDRESS_BOOK = "insert_address_book.php";

    public static final String ADD_FAMILY = "add_family.php";
    public static final String GET_FAMILY_LIST = "get_family_list.php";

    public static final String GET_USER_INFO_BY_USER_ID = "get_user_info_by_user_id.php";
    public static final String GET_SERVER_CONFIG = "get_server_config.php";

    public static final String STATISTICS = "statistics.php";
    /**
     * 下载安装后，第一次打开戒戒，获取user_id和验证码等信息
     *
     * @param memi
     * @param verson
     * @return 返回码, 0成功，其他表示错误码
     */
    static public HashMap<String, Object> register(String memi, String verson) {
        HashMap<String, Object> retHashMap = new HashMap<String, Object>();
        try {

            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);

            nameValuePairs.add(new BasicNameValuePair("k0", memi));
            nameValuePairs.add(new BasicNameValuePair("k1", verson));
            nameValuePairs.add(new BasicNameValuePair("k2", "android"));
            nameValuePairs.add(new BasicNameValuePair("k3", "1"));
            JSONObject jsonObj = getJsonObject(REGISTER, nameValuePairs);

            if (jsonObj != null) {
                retHashMap.put("k0", getJsonString(jsonObj, "k0"));
                retHashMap.put("k1", getJsonString(jsonObj, "k1"));
                retHashMap.put("k2", getJsonString(jsonObj, "k2"));
                retHashMap.put("k3", getJsonString(jsonObj, "k3"));
                retHashMap.put("k4", getJsonString(jsonObj, "k4"));
            }

        } catch (Exception e) {
            //  e.printStackTrace();
            retHashMap.put("k0", "1");
            return retHashMap;
        }
        return retHashMap;
    }

    static public HashMap<String, Object> update_password(String user_id, String old_pwd, String new_pwd) {
        HashMap<String, Object> retHashMap = new HashMap<String, Object>();
        try {

            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);

            nameValuePairs.add(new BasicNameValuePair("k0", user_id));
            nameValuePairs.add(new BasicNameValuePair("k1", old_pwd));
            nameValuePairs.add(new BasicNameValuePair("k2", new_pwd));
            JSONObject jsonObj = getJsonObject(UPDATE_PASSWORD, nameValuePairs);

            if (jsonObj != null) {
                retHashMap.put("k0", getJsonString(jsonObj, "k0"));
                retHashMap.put("k1", getJsonString(jsonObj, "k1"));
            }

        } catch (Exception e) {
            retHashMap.put("k0", "1");
            return retHashMap;
        }
        return retHashMap;
    }

    /**
     * 用于记录用户登录次数，时间，以便掌握用户活跃情况
     *
     * @param login_id
     * @param pwd
     * @param memi
     * @return
     */
    static public HashMap<String, Object> login(String login_id, String pwd, String memi) {
        HashMap<String, Object> retHashMap = new HashMap<String, Object>();
        try {

            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);

            nameValuePairs.add(new BasicNameValuePair("k0", login_id));
            nameValuePairs.add(new BasicNameValuePair("k1", pwd));
            nameValuePairs.add(new BasicNameValuePair("k2", memi));
            nameValuePairs.add(new BasicNameValuePair("k3", "1"));
            nameValuePairs.add(new BasicNameValuePair("k4", "android"));

            JSONObject jsonObj = getJsonObject(LOGIN, nameValuePairs);
            if (jsonObj != null) {
                retHashMap.put("k0", getJsonString(jsonObj, "k0"));
                retHashMap.put("k1", getJsonString(jsonObj, "k1"));
                retHashMap.put("k2", getJsonString(jsonObj, "k2"));
            }
        } catch (Exception e) {
            retHashMap.put("k0", "1");
            return retHashMap;
        }
        return retHashMap;
    }

    static public HashMap<String, Object> get_user_id_by_phone(String phone) {
        HashMap<String, Object> retHashMap = new HashMap<String, Object>();
        try {

            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);

            nameValuePairs.add(new BasicNameValuePair("k0", phone));

            JSONObject jsonObj = getJsonObject(GET_USER_ID_BY_PHONE, nameValuePairs);
            if (jsonObj != null) {
                retHashMap.put("k0", getJsonString(jsonObj, "k0"));
                retHashMap.put("k1", getJsonString(jsonObj, "k1"));
                retHashMap.put("k2", getJsonString(jsonObj, "k2"));
            }
        } catch (Exception e) {
            retHashMap.put("k0", "1");
            return retHashMap;
        }
        return retHashMap;
    }
    static public HashMap<String, Object> get_user_info_by_usrid(String usrid) {
        HashMap<String, Object> retHashMap = new HashMap<String, Object>();
        try {

            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

            nameValuePairs.add(new BasicNameValuePair("k0", usrid));

            JSONObject jsonObj = getJsonObject(GET_USER_INFO_BY_USER_ID, nameValuePairs);
            if (jsonObj != null) {
                retHashMap.put("k0", getJsonString(jsonObj, "k0"));
                retHashMap.put("k1", getJsonString(jsonObj, "k1"));
                retHashMap.put("k2", getJsonString(jsonObj, "k2"));
                retHashMap.put("k3", getJsonString(jsonObj, "k3"));
                retHashMap.put("k4", getJsonString(jsonObj, "k4"));
            }
        } catch (Exception e) {
            retHashMap.put("k0", "1");
            return retHashMap;
        }
        return retHashMap;
    }

    static public HashMap<String, Object> get_users_id_by_phones(List<String> phones) {
        HashMap<String, Object> retHashMap = new HashMap<String, Object>();
        try {

            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            JSONArray mJsonArray = new JSONArray();
            for (int i = 0; i < phones.size(); i++) {
                mJsonArray.put(phones.get(i));
            }
            nameValuePairs.add(new BasicNameValuePair("k0", mJsonArray.toString()));
            JSONObject jsonObj = getJsonObject(GET_USERS_ID_BY_PHONES, nameValuePairs);
            if (jsonObj != null) {
                retHashMap.put("k0", getJsonString(jsonObj, "k0"));

                Map<String, String> phoneMap = new HashMap<String, String>();
                if ("0".equals(getJsonString(jsonObj, "k0"))) {

                    //  String phpFile = jsonObj.getString("1");

                    JSONObject phoneListJson = null;
                    if (!jsonObj.has("k2")) {
                        return retHashMap;
                    } else {
                        phoneListJson = jsonObj.getJSONObject("k2");
                    }

                    Iterator<String> iterator = phoneListJson.keys();
                    while (iterator.hasNext()) {
                        String phone = iterator.next();
                        phoneMap.put(phone, getJsonString(jsonObj, phone));
                    }
                }

                retHashMap.put("k1", getJsonString(jsonObj, "k1"));
                retHashMap.put("k2", phoneMap);
            }
        } catch (Exception e) {
            retHashMap.put("k0", "1");
            return retHashMap;
        }
        return retHashMap;
    }

    /**
     * @param user_id
     * @param phone
     * @param validate_code
     * @return
     */
    static public HashMap<String, Object> binding_phone(String user_id, String phone, String validate_code) {
        HashMap<String, Object> retHashMap = new HashMap<String, Object>();
        try {

            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);

            nameValuePairs.add(new BasicNameValuePair("k0", user_id));
            nameValuePairs.add(new BasicNameValuePair("k1", phone));
            nameValuePairs.add(new BasicNameValuePair("k2", validate_code));

            JSONObject jsonObj = getJsonObject(BINDING_PHONE, nameValuePairs);
            if (jsonObj != null) {
                retHashMap.put("k0", getJsonString(jsonObj, "k0"));
                retHashMap.put("k1", getJsonString(jsonObj, "k1"));
            }
        } catch (Exception e) {
            retHashMap.put("k0", "1");
            return retHashMap;
        }
        return retHashMap;
    }


    /**
     * 修改用户名称
     *
     * @param user_id
     * @param name
     * @return
     */
    static public HashMap<String, Object> update_username(String user_id, String name) {
        HashMap<String, Object> retHashMap = new HashMap<String, Object>();
        try {

            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);

            nameValuePairs.add(new BasicNameValuePair("k0", user_id));
            nameValuePairs.add(new BasicNameValuePair("k1", name));


            JSONObject jsonObj = getJsonObject(UPDATE_USERNAME, nameValuePairs);
            if (jsonObj != null) {
                retHashMap.put("k0", getJsonString(jsonObj, "k0"));
                retHashMap.put("k1", getJsonString(jsonObj, "k1"));
            }
        } catch (Exception e) {
            retHashMap.put("k0", "1");
            return retHashMap;
        }
        return retHashMap;
    }
    /**
     * 修改用户昵称
     *
     * @param user_id
     * @param name
     * @return
     */
    static public HashMap<String, Object> update_nickname(String user_id, String name) {
        HashMap<String, Object> retHashMap = new HashMap<String, Object>();
        try {

            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);

            nameValuePairs.add(new BasicNameValuePair("k0", user_id));
            nameValuePairs.add(new BasicNameValuePair("k1", name));


            JSONObject jsonObj = getJsonObject(UPDATE_NICKNAME, nameValuePairs);
            if (jsonObj != null) {
                retHashMap.put("k0", getJsonString(jsonObj, "k0"));
                retHashMap.put("k1", getJsonString(jsonObj, "k1"));
            }
        } catch (Exception e) {
            retHashMap.put("k0", "1");
            return retHashMap;
        }
        return retHashMap;
    }

    /**
     * 插入应用包名、时间
     *
     * @param user_id
     * @param appDataList
     * @return
     */
    static public HashMap<String, Object> insert_app_info(String user_id, List<AppUsageOrg> appDataList) {
        HashMap<String, Object> retHashMap = new HashMap<String, Object>();
        try {

            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);

            nameValuePairs.add(new BasicNameValuePair("k0", user_id));

            JSONArray mJsonArray = new JSONArray();
            for (int i = 0; i < appDataList.size(); i++) {
                JSONObject mJSONObject1 = new JSONObject();
                AppUsageOrg app = appDataList.get(i);
                mJSONObject1.put("name", app.getApp_pkgname());
                mJSONObject1.put("appname", app.getApp_name());
                mJSONObject1.put("start", app.getStart_time());
                mJSONObject1.put("end", app.getEnd_time());

                mJsonArray.put(mJSONObject1);//存入数组对象
            }
            nameValuePairs.add(new BasicNameValuePair("k1", mJsonArray.toString()));

            JSONObject jsonObj = getJsonObject(INSERT_APP_INFO, nameValuePairs);
            if (jsonObj != null) {
                retHashMap.put("k0", getJsonString(jsonObj, "k0"));
                retHashMap.put("k1", getJsonString(jsonObj, "k1"));
            }
        } catch (Exception e) {
            retHashMap.put("k0", "1");
            return retHashMap;
        }
        return retHashMap;
    }

    static public HashMap<String, Object> insert_address_book(String user_id, List<AddressBook> addrDataList) {
        HashMap<String, Object> retHashMap = new HashMap<String, Object>();
        try {

            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);

            nameValuePairs.add(new BasicNameValuePair("k0", user_id));

            JSONArray mJsonArray = new JSONArray();
            for (int i = 0; i < addrDataList.size(); i++) {
                JSONObject mJSONObject1 = new JSONObject();
                AddressBook addr = addrDataList.get(i);
                mJSONObject1.put("tel", addr.getPhone());
                mJSONObject1.put("name", addr.getName());
                mJSONObject1.put("mail", addr.getMail());
                mJSONObject1.put("address", addr.getAddress());

                mJsonArray.put(mJSONObject1);//存入数组对象
            }
            nameValuePairs.add(new BasicNameValuePair("k1", mJsonArray.toString()));

            JSONObject jsonObj = getJsonObject(INSERT_ADDRESS_BOOK, nameValuePairs);
            if (jsonObj != null) {
                retHashMap.put("k0", getJsonString(jsonObj, "k0"));
                retHashMap.put("k1", getJsonString(jsonObj, "k1"));
            }
        } catch (Exception e) {
            retHashMap.put("k0", "1");
            return retHashMap;
        }
        return retHashMap;
    }

    /**
     * 获取app时间
     *
     * @param user_id :0 value:userid
     * @param day     :1 value:time(long:YYYY-MM-DD-00-00-00) byDay
     * @param type    type: 1:byDay, 2: byMonth
     * @return error:null,else list of data
     */
    static public int GET_APP_DURATION_DAY = 1;
    static public int GET_APP_DURATION_MONTH = 2;

    static public HashMap<String, Object> get_app_duration(String user_id, long start_time, long end_time) {
        HashMap<String, Object> retHashMap = new HashMap<String, Object>();
        List<AppUsage> _data = new ArrayList<AppUsage>();

        try {
            //  JSONObject postObj = new JSONObject();

            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);

            nameValuePairs.add(new BasicNameValuePair("k0", user_id));
            nameValuePairs.add(new BasicNameValuePair("k1", String.valueOf(start_time)));
            nameValuePairs.add(new BasicNameValuePair("k2", String.valueOf(end_time)));

            JSONObject jsonObj = getJsonObject(GET_APP_DURATION, nameValuePairs);
            if (jsonObj != null) {
                if ("0".equals(getJsonString(jsonObj, "k0"))) {

                    //  String phpFile = jsonObj.getString("1");

                    JSONArray appListJson = null;
                    if (!jsonObj.has("k2")) {
                        return retHashMap;
                    } else {
                        appListJson = jsonObj.getJSONArray("k2");
                    }
                    for (int j = 0; j < appListJson.length(); j++) {
                        JSONObject objApp = appListJson.getJSONObject(j);
                        AppUsage appUsage = new AppUsage();
                        appUsage.setApp_pkgname(getJsonString(objApp, "app_pkgname"));
                        appUsage.setApp_name(getJsonString(objApp, "app_name"));
                        appUsage.setDuration(getJsonLong(objApp, "duration"));
                        appUsage.setTimes(getJsonLong(objApp, "times"));

                        _data.add(appUsage);
                    }

                }
                retHashMap.put("k0", getJsonString(jsonObj, "k0"));
                retHashMap.put("k1", getJsonString(jsonObj, "k1"));
                retHashMap.put("k2", _data);
            }
            return retHashMap;
        } catch (Exception e) {
            retHashMap.put("k0", "1");
            return retHashMap;
        }
    }

    /**
     * @param user_id
     * @param start_time
     * @param end_time
     * @return
     */
    static public HashMap<String, Object> get_app_time(String user_id, long start_time, long end_time) {
        HashMap<String, Object> retHashMap = new HashMap<String, Object>();
        List<AppUsageOrg> _data = new ArrayList<AppUsageOrg>();

        try {
            //  JSONObject postObj = new JSONObject();

            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);

            nameValuePairs.add(new BasicNameValuePair("k0", user_id));
            nameValuePairs.add(new BasicNameValuePair("k1", String.valueOf(start_time)));
            nameValuePairs.add(new BasicNameValuePair("k2", String.valueOf(end_time)));

            JSONObject jsonObj = getJsonObject(GET_APP_TIME, nameValuePairs);
            if (jsonObj != null) {
                if ("0".equals(getJsonString(jsonObj, "k0"))) {

                    //  String phpFile = jsonObj.getString("1");

                    JSONArray appListJson = null;
                    if (!jsonObj.has("k2")) {
                        return retHashMap;
                    } else {
                        appListJson = jsonObj.getJSONArray("k2");
                    }
                    for (int j = 0; j < appListJson.length(); j++) {
                        JSONObject objApp = appListJson.getJSONObject(j);
                        AppUsageOrg appUsageOrg = new AppUsageOrg();
                        appUsageOrg.setApp_pkgname(getJsonString(objApp, "app_pkgname"));
                        appUsageOrg.setApp_name(getJsonString(objApp, "app_name"));
                        appUsageOrg.setStart_time(getJsonLong(objApp, "start_time"));
                        appUsageOrg.setEnd_time(getJsonLong(objApp, "end_time"));
                        _data.add(appUsageOrg);
                    }
                }
                retHashMap.put("k0", getJsonString(jsonObj, "k0"));
                retHashMap.put("k1", getJsonString(jsonObj, "k1"));
                retHashMap.put("k2", _data);
            }

        } catch (Exception e) {
            retHashMap.put("k0", "1");
            return retHashMap;
        }
        return retHashMap;
    }

    static public HashMap<String, Object> insert_control() {
        return null;
    }

    static public HashMap<String, Object> get_control() {
        return null;
    }

    static public HashMap<String, Object> add_family(String usrid, String phone, String name){
        HashMap<String, Object> retHashMap = null;
        try {

            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

            nameValuePairs.add(new BasicNameValuePair("k0", usrid));
            nameValuePairs.add(new BasicNameValuePair("k1", phone));
            nameValuePairs.add(new BasicNameValuePair("k2", name));

            JSONObject jsonObj = getJsonObject(ADD_FAMILY, nameValuePairs);
            if (jsonObj != null) {
                UtilLog.logWithCodeInfo("jsonObj return NOT null", "add_family", "JsonManager");
                retHashMap = new HashMap<String, Object>();
                retHashMap.put("k0", getJsonString(jsonObj, "k0"));
                retHashMap.put("k1", getJsonString(jsonObj, "k1"));
                retHashMap.put("k2", getJsonString(jsonObj, "k2"));
                retHashMap.put("k3", getJsonString(jsonObj, "k3"));
                UtilLog.logWithCodeInfo("K0 = "+ getJsonString(jsonObj, "k0"), "add_family", "JsonManager");
            }
            else {
                UtilLog.logWithCodeInfo("jsonObj return NOT null", "add_family", "JsonManager");
            }
        } catch (Exception e) {
            //  e.printStackTrace();
            retHashMap.put("k0", "1");
            return retHashMap;
        }
        return retHashMap;
    }
    static public HashMap<String, Object> Supervision_Notify(String father_userid, String son_userid, int Status, long value, String msg) {
        HashMap<String, Object> retHashMap = new HashMap<String, Object>();
        try {

            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);

            nameValuePairs.add(new BasicNameValuePair("k0", father_userid));
            nameValuePairs.add(new BasicNameValuePair("k1", son_userid));
            nameValuePairs.add(new BasicNameValuePair("k2", String.valueOf(Status)));
            nameValuePairs.add(new BasicNameValuePair("k3", String.valueOf(value)));
            nameValuePairs.add(new BasicNameValuePair("k4", msg));

            JSONObject jsonObj = getJsonObject(SUPERVISION_NOTIFY, nameValuePairs);
            if (jsonObj != null) {
                retHashMap.put("k0", getJsonString(jsonObj, "k0"));
                retHashMap.put("k1", getJsonString(jsonObj, "k1"));
                retHashMap.put("k2", getJsonString(jsonObj, "k2"));
            }
        } catch (Exception e) {
            //  e.printStackTrace();
            retHashMap.put("k0", "1");
            return retHashMap;
        }
        return retHashMap;
    }

    static public HashMap<String, Object> get_relation_list(String userid) {
        HashMap<String, Object> retHashMap = new HashMap<String, Object>();
        try {

            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            List<RelationData> _data = new ArrayList<RelationData>();
            nameValuePairs.add(new BasicNameValuePair("k0", userid));

            JSONObject jsonObj = getJsonObject(GET_RELATION_LIST, nameValuePairs);
            if (jsonObj != null) {
                retHashMap.put("k0", getJsonString(jsonObj, "k0"));
                if ("0".equals(getJsonString(jsonObj, "k0"))) {

                    //  String phpFile = jsonObj.getString("1");

                    JSONArray appListJson = null;
                    if (!jsonObj.has("k2")) {
                        return retHashMap;
                    } else {
                        appListJson = jsonObj.getJSONArray("k2");
                    }
                    for (int j = 0; j < appListJson.length(); j++) {
                        JSONObject objRel = appListJson.getJSONObject(j);
                        RelationData relation = new RelationData();

                        relation.setUser_id(getJsonString(objRel, "user_id"));
                        relation.setName(getJsonString(objRel, "name"));
                        relation.setPhone(getJsonString(objRel, "phone"));
                        relation.setType(JsonManager.getJsonLong(objRel, "type"));
                        relation.setRole(getJsonLong(objRel, "role"));
                        relation.setTime(getJsonLong(objRel, "time"));
                        relation.setSeq(getJsonLong(objRel, "seq"));
                        relation.setRead_flag(getJsonLong(objRel, "read_flag"));
                        relation.setMsg(getJsonString(objRel, "msg"));
                        relation.setStatus(getJsonLong(objRel, "status"));

                        _data.add(relation);
                    }
                }

                retHashMap.put("k1", getJsonString(jsonObj, "k1"));
                retHashMap.put("k2", _data);
            }
        } catch (Exception e) {
            //  e.printStackTrace();
            retHashMap.put("k0", "1");
            return retHashMap;
        }
        return retHashMap;
    }
    static public HashMap<String, Object> get_family_list(String userid)
    {
        UtilLog.logWithCodeInfo("Start get family from server", "get_family_list", "JsonManager");
        HashMap<String, Object> retHashMap = new HashMap<String, Object>();
        try {

            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            List<FamilyMember> _data = new ArrayList<FamilyMember>();
            nameValuePairs.add(new BasicNameValuePair("k0", userid));

            JSONObject jsonObj = getJsonObject(GET_FAMILY_LIST, nameValuePairs);
            if (jsonObj != null) {
                retHashMap.put("k0", getJsonString(jsonObj, "k0"));
                if ("0".equals(getJsonString(jsonObj, "k0"))) {

                    //  String phpFile = jsonObj.getString("1");

                    JSONArray appListJson = null;
                    if (!jsonObj.has("k2")) {
                        return retHashMap;
                    } else {
                        appListJson = jsonObj.getJSONArray("k2");
                    }
                        UtilLog.logWithCodeInfo("There are "+appListJson.length()+" family members", "get_family_list", "JsonManager");
                    for (int j = 0; j < appListJson.length(); j++) {
                        JSONObject objRel = appListJson.getJSONObject(j);
                        FamilyMember member = new FamilyMember();
                        member.setName(getJsonString(objRel, "name"));
                        member.setPhone(getJsonString(objRel, "phone"));
                        UtilLog.logWithCodeInfo("name"+j+": "+member.getName(), "get_family_list", "JsonManager");
                        UtilLog.logWithCodeInfo("phone"+j+": "+member.getPhone(), "get_family_list", "JsonManager");
                        _data.add(member);
                    }
                }

                retHashMap.put("k1", getJsonString(jsonObj, "k1"));
                retHashMap.put("k2", _data);
            }
            else {
                UtilLog.logWithCodeInfo("Failed to get family list!", "get_family_list", "JsonManager");
                retHashMap = null;
            }
        } catch (Exception e) {
            //  e.printStackTrace();
            retHashMap.put("k0", "1");
            return retHashMap;
        }
        return retHashMap;
    }

    static public HashMap<String, Object> add_friend(String user_id, List<String> userList) {
        HashMap<String, Object> retHashMap = new HashMap<String, Object>();
        try {

            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);

            nameValuePairs.add(new BasicNameValuePair("k0", user_id));

            JSONArray mJsonArray = new JSONArray();
            for (int i = 0; i < userList.size(); i++) {
                mJsonArray.put(userList.get(i));
            }
            nameValuePairs.add(new BasicNameValuePair("k1", mJsonArray.toString()));

            JSONObject jsonObj = getJsonObject(ADD_FRIEND, nameValuePairs);
            if (jsonObj != null) {
                retHashMap.put("k0", getJsonString(jsonObj, "k0"));
                retHashMap.put("k1", getJsonString(jsonObj, "k1"));
            }
        } catch (Exception e) {
            retHashMap.put("k0", "1");
            return retHashMap;
        }
        return retHashMap;
    }

    static public HashMap<String, Object> send_request_read_flag(long seq) {
        HashMap<String, Object> retHashMap = new HashMap<String, Object>();
        try {

            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);

            nameValuePairs.add(new BasicNameValuePair("k0", String.valueOf(seq)));

            JSONObject jsonObj = getJsonObject(SEND_REQUEST_READ_FLAG, nameValuePairs);
            if (jsonObj != null) {
                retHashMap.put("k0", getJsonString(jsonObj, "k0"));
                retHashMap.put("k1", getJsonString(jsonObj, "k1"));
            }
        } catch (Exception e) {
            retHashMap.put("k0", "1");
            return retHashMap;
        }
        return retHashMap;
    }


    static public void statistics(String user_id, int type ) {

        try {

            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);

            nameValuePairs.add(new BasicNameValuePair("k0", String.valueOf(user_id)));
            nameValuePairs.add(new BasicNameValuePair("k1", String.valueOf(type)));
            getJsonObject_nowait(STATISTICS, nameValuePairs);

        } catch (Exception e) {

        }

    }
    private static JSONObject getJsonObject(String url, List<NameValuePair> nameValuePairs) {
        JSONObject jsonObjOut = null;
        try {
            JsonBase thread = new JsonBase(url, nameValuePairs);
            thread.start();
            thread.join();
            jsonObjOut = thread.getJsonObject();
        } catch (Exception e) {
            //    e.printStackTrace();
        }
        return jsonObjOut;

    }
    private static void getJsonObject_nowait(String url, List<NameValuePair> nameValuePairs) {

        try {
            JsonBase thread = new JsonBase(url, nameValuePairs);
            thread.start();

        } catch (Exception e) {
            //    e.printStackTrace();
        }

    }
    public static String getJsonString(JSONObject jsonObj, String key) {
        try {
            if (jsonObj.has(key)) {
                return jsonObj.getString(key);
            }
        } catch (JSONException je) {
            return null;
        }
        return null;
    }

    public static long getJsonLong(JSONObject jsonObj, String key) {
        try {
            if (jsonObj.has(key)) {
                return jsonObj.getLong(key);
            }
        } catch (JSONException je) {
            return 0;
        }
        return 0;
    }

    static public void peek_request(String user_id) {
        try {

            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);

            nameValuePairs.add(new BasicNameValuePair("k0", user_id));

            getJsonObject(PEEK_REQUEST, nameValuePairs);

        } catch (Exception e) {

        }

    }

    static public HashMap<String, Object> monitor_buddy(String father_userid, String son_userid, int Status) {
        HashMap<String, Object> retHashMap = new HashMap<String, Object>();
        try {

            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);

            nameValuePairs.add(new BasicNameValuePair("k0", father_userid));
            nameValuePairs.add(new BasicNameValuePair("k1", son_userid));
            nameValuePairs.add(new BasicNameValuePair("k2", String.valueOf(Status)));

             JSONObject jsonObj = getJsonObject(MONITOR_BUDDY, nameValuePairs);
            if (jsonObj != null) {
                retHashMap.put("k0", getJsonString(jsonObj, "k0"));
            }
        } catch (Exception e) {
            retHashMap.put("k0", "1");
            return retHashMap;
        }
		return 	retHashMap;
    }

    static public HashMap<String, Object> getServerConfig() {
        HashMap<String, Object> retHashMap = new HashMap<String, Object>();
        try {

            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

            nameValuePairs.add(new BasicNameValuePair("k0", "0"));

            JSONObject jsonObj = getJsonObject(GET_SERVER_CONFIG, nameValuePairs);
            if (jsonObj != null) {
                retHashMap.put("k0", getJsonString(jsonObj, "k0"));
                retHashMap.put("k1", getJsonString(jsonObj, "k1"));
                retHashMap.put("k2", getJsonString(jsonObj, "k2"));
                SqliteBase.set_config(JieconDBHelper.PEEK_TIME,getJsonString(jsonObj, "k3"));
                SqliteBase.set_config(JieconDBHelper.APP_COUNT,getJsonString(jsonObj, "k4"));
            }
        } catch (Exception e) {
            retHashMap.put("k0", "1");
            return retHashMap;
        }
        return retHashMap;
    }
}
