package com.dobi.jiecon.datacontroller;

import com.dobi.jiecon.App;
import com.dobi.jiecon.UtilLog;
import com.dobi.jiecon.data.AddressBook;
import com.dobi.jiecon.database.JieconDBHelper;
import com.dobi.jiecon.database.json.IJsonCallback;
import com.dobi.jiecon.database.json.JsonManager;
import com.dobi.jiecon.database.sqlite.SqliteBase;
import com.dobi.jiecon.utils.PhoneInfo;

import java.util.HashMap;
import java.util.List;

/**
 * Created by rock on 15/2/1.
 */
public class RegistrationManager {

    public final static String BIND_YES = "yes";
    public final static String BIND_NO = "no";


    //flag should be "MSG_NEWARRIVAL" or "MSG_NO"


    //flag should be "yes" or "no"
    public static void setBindingFlag(String flag) {
        SqliteBase.set_config(JieconDBHelper.BINDFLAG_KEY, flag);
    }

    public static String getBindingFlag() {
        return SqliteBase.get_config(JieconDBHelper.BINDFLAG_KEY);
    }

    public static String getPhoneNumber() {
        String phone_number;
        if (getBindingFlag() != null) {
            phone_number = getPhoneNumberFromSqlite();
        } else {
            phone_number = getUserInfoFromServer();
            if (phone_number.equals("")||phone_number.equals("null"))
                phone_number = null;
        }

        return phone_number;
    }

    //set Phone number
    public static void setPhoneNumber(String phoneNumber) {
        SqliteBase.set_config(JieconDBHelper.PHONENUMBER_KEY, phoneNumber);
    }

    public static String getPhoneNumberFromSqlite() {
        return SqliteBase.get_config(JieconDBHelper.PHONENUMBER_KEY);
    }

    public static void setCode(String code) {
        SqliteBase.set_config(JieconDBHelper.VALIDCODE_KEY, code);
    }

    public static String getCode() {
        return SqliteBase.get_config(JieconDBHelper.VALIDCODE_KEY);
    }

    public static String g_usrName = null;

    public static boolean setUsrId(String usrId) {
        if (SqliteBase.get_config(JieconDBHelper.USERID_KEY) != null) {
            return false;
        }
        SqliteBase.insert_config(JieconDBHelper.USERID_KEY, usrId);
        return true;
    }

    static private String getUserInfoFromServer() {
        UtilLog.logWithCodeInfo("Host userid is " + RegistrationManager.getUserId(), "getUserInfoFromServer", "RegistrationManager");
        HashMap<String, Object> retHashMap = JsonManager.get_user_info_by_usrid(RegistrationManager.getUserId());
        if (retHashMap != null && "0".equals(retHashMap.get("k0"))) {
            String phone_number = retHashMap.get("k2").toString();
            if (phone_number != null && !phone_number.toString().equals("") && !phone_number.toString().equals("null")) {
                RegistrationManager.setBindingFlag(BIND_YES);
                RegistrationManager.setPhoneNumber(phone_number);
            }
            UtilLog.logWithCodeInfo("Phone number on server is " + phone_number, "getUserInfoFromServer", "RegistrationManager");
            Object nickName = retHashMap.get("k3");
            UtilLog.logWithCodeInfo("Name on server is " + retHashMap.get("k3").toString(), "getUserInfoFromServer", "RegistrationManager");
            if (nickName != null && !nickName.toString().equals("") && !nickName.toString().equals("null")) {
                RegistrationManager.setNickNameToSqlite(nickName.toString());
            }
            return (String) retHashMap.get("k2");
        } else {
//            UtilLog.logWithCodeInfo("k0 is " + retHashMap.get("k0").toString(), "getUserInfoFromServer", "RegistrationManager");
            return "null";
        }
    }

    /**
     * @param memi
     * @param version
     * @return key:k0  value:返回码,0成功，其他表示错误码
     * key:k1  value:"register" php脚本名称
     * key:k2  value:user_id, 分配的id号
     * key:k3  value:validate code将来要的验证码
     * key:k4  value:nick name
     */
    static private String register(String memi, String version) {
        HashMap<String, Object> retHashMap = JsonManager.register(memi, version);
        if (retHashMap != null && "0".equals(retHashMap.get("k0"))) {
            RegistrationManager.setCode(retHashMap.get("k3").toString());
            Object nickName = retHashMap.get("k4");
            if (nickName != null && !nickName.toString().equals("")) {
                RegistrationManager.setNickNameToSqlite(nickName.toString());
            }
            return (String) retHashMap.get("k2");

        } else {
            return null;
        }
    }

    public static String getUsrIdFromSqlite() {
        return SqliteBase.get_config(JieconDBHelper.USERID_KEY);
    }

    public static void setUsrNameToSqlite(String usrName) {
        SqliteBase.set_config(JieconDBHelper.USERNAME_KEY, usrName);
    }

    public static String getUsrNameFromSqlite() {
        return SqliteBase.get_config(JieconDBHelper.USERNAME_KEY);
    }

    public static void setNickNameToSqlite(String nickName) {
        SqliteBase.set_config(JieconDBHelper.NICKNAME_KEY, nickName);
    }

    public static String getNickNameFromSqlite() {
        return SqliteBase.get_config(JieconDBHelper.NICKNAME_KEY);
    }

    public static String getPasswordFromSqlite() {
        return SqliteBase.get_config(JieconDBHelper.PASSWD_KEY);
    }

    public static void setPasswordToSqlite(String passwd) {
        SqliteBase.set_config(JieconDBHelper.PASSWD_KEY, passwd);
    }

    public static boolean login(String usrName, String passwd) {
        boolean ret = true;
        String usr_name = getUsrNameFromSqlite();
        if (usr_name != null && !usr_name.equals("") && !usrName.equals(usr_name)) {
            ret = false;
        } else {
            if (null == getPasswordFromSqlite()) {
                ret = setPassword(passwd);
            } else {
                ret = verifyPwd(passwd);
            }
        }
        return ret;
    }

    public static String getUserIdNoRegister() {
        return getUsrIdFromSqlite();
    }

    //Step 1: check userid
    public static String getUserId() {
        String user_id = getUsrIdFromSqlite();
        if (user_id == null) {
            String memi = PhoneInfo.getimei();
            String version = PhoneInfo.getJieconVersion();
            user_id = register(memi, version);

            if (user_id != null) {//save to SQLite
                setUsrId(user_id);
            }
        }
        return user_id;
    }

    //Step 2: check password
    public static boolean verifyPwd(String pwd) {
        boolean ret = true;
        String password = getPasswordFromSqlite();
        if (password == null) {
            //Need to check with server
            String usrid = login_onServer(getUsrIdFromSqlite(), pwd, PhoneInfo.getimei().toString());
            if (null != usrid && !usrid.equals("")) {
                setPasswordToSqlite(pwd);
            } else {
                ret = false; //passwd is incorrect
            }
        } else {
            if (!password.equals(pwd)) {
                ret = false;
            }
        }
        return ret;
    }

    //Step 3: update password
    public static boolean setPassword(String newpwd) {
        HashMap<String, Object> retHashMap = JsonManager.update_password(getUsrIdFromSqlite(), "", newpwd);
        if (retHashMap != null && "0".equals(retHashMap.get("k0"))) {
            setPasswordToSqlite(newpwd);
            return true;
        } else {
            return false;
        }
    }

    //Step 3: update password
    public static boolean passwordIsExisting() {
        if (getPasswordFromSqlite() != null)
            return true;
        HashMap<String, Object> retHashMap = JsonManager.update_password(getUsrIdFromSqlite(), "", "");
        if (retHashMap != null && "1".equals(retHashMap.get("k0"))) { //
            return true;
        } else {
            return false;
        }
    }

    //Step 4: update username
    public static boolean setName(String name) {
        HashMap<String, Object> retHashMap = JsonManager.update_nickname(getUsrIdFromSqlite(), name);
        if (retHashMap != null && "0".equals(retHashMap.get("k0"))) {
            setUsrNameToSqlite(name);
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
    static public String login_onServer(String login_id, String pwd, String memi) {
        HashMap<String, Object> retHashMap = JsonManager.login(login_id, pwd, memi);
        if ("0".equals(retHashMap.get("k0"))) {
            return (String) retHashMap.get("k2");
        } else {
            return null;
        }
    }

    /**
     * @param phone
     * @return 0:OK, 1: phone is invalid, 2: code is wrong 3: unknown
     */
    static public String binding_phone(String phone) {
        String ret; //
        HashMap<String, Object> retHashMap = JsonManager.binding_phone(getUserId(), phone, getCode());
        if (retHashMap == null) {
            ret = "3";
        }
        Object bind = retHashMap.get("k0");
        if (bind == null) {
            ret = "-1";
        } else {
            if ("0".equals(bind.toString()) || "2".equals(bind.toString())) {//binding is successful on server
                //save to sqlite
                setBindingFlag(BIND_YES);
                setPhoneNumber(phone);
                ret = "0";
            } else {
                setBindingFlag(BIND_NO);
                ret = bind.toString();
            }
        }
        return ret;
    }

    public static boolean phoneIsBinding() {
        boolean ret = false;
        //check sqlite
        if (getBindingFlag() != null && getBindingFlag().equals(BIND_YES)) {
            UtilLog.logWithCodeInfo("BIND_YES in Sqlite database", "phoneIsBinding", "RegistrationManager");
            ret = true;
        } else {
            String phone = RegistrationManager.getPhoneNumber();
            if (null != phone) {
                UtilLog.logWithCodeInfo("phone number " + phone + " is binding on server", "phoneIsBinding", "RegistrationManager");
                UtilLog.logWithCodeInfo("phone is binding on server", "phoneIsBinding", "RegistrationManager");
                ret = true;
            } else {
                UtilLog.logWithCodeInfo("phone is not binding on server", "phoneIsBinding", "RegistrationManager");
                ret = false;
            }
        }
        return ret;
    }

    public static void check_phone_binding_async( final IJsonCallback cbk) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean ret = phoneIsBinding();
                cbk.refreshData(App.ID_CHECK_PHONE_BINDING, ret);
            }
        }
        ).start();
    }

    public static void update_user_info_async() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                RegistrationManager.getPhoneNumber();
            }
        }).start();
    }

    static public boolean insert_address_book(String user_id, List<AddressBook> addrDataList) {
        HashMap<String, Object> retHashMap = JsonManager.insert_address_book(user_id, addrDataList);
        if ("0".equals(retHashMap.get("k0"))) {

            return true;
        } else {
            return false;
        }
    }

    /**
     * @return
     */
    static public String getVersionCode() {
        HashMap<String, Object> retHashMap = JsonManager.getVersionCode();
        if ("0".equals(retHashMap.get("k0"))) {
            return (String) retHashMap.get("k2");
        } else {
            return null;
        }
    }
}
