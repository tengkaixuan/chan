package com.dobi.jiecon.datacontroller;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;

import com.dobi.jiecon.App;
import com.dobi.jiecon.UtilLog;
import com.dobi.jiecon.activities.MockLockActivity;
import com.dobi.jiecon.data.FamilyMember;
import com.dobi.jiecon.data.RelationData;
import com.dobi.jiecon.database.JieconDBHelper;
import com.dobi.jiecon.database.json.IJsonCallback;
import com.dobi.jiecon.database.json.JsonManager;
import com.dobi.jiecon.database.sqlite.SqliteBase;
import com.dobi.jiecon.service.LockReceiver;
import com.dobi.jiecon.utils.DebugControl;

import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rock on 15/1/31.
 */
public class SupervisionManager {

    public final static String SUPERVISION_TIMEOUT_YES = "SUPERVISIONTIMEOUTYES";
    public final static String SUPERVISION_TIMEOUT_NO = "SUPERVISIONTIMEOUTNO";
    public final static String LOCK_SCREEN_YES = "LOCKSCREENYES";
    public final static String LOCK_SCREEN_NO = "LOCKSCREENNO";
    public final static String MSG_NEWARRIVAL = "NEWMSG";
    public final static String MSG_NO = "NOMSG";

    /**
     * @param phone
     * @return may be is null
     */
    static public String get_user_id_by_phone(String phone) {
        UtilLog.logWithCodeInfo("Phone number is " + phone, "get_user_id_by_phone", "SupervisionManager");
        //Get from add_family table first
        String ret = SqliteBase.get_usrid_from_family_list(phone);
        if (ret == null) {
            HashMap<String, Object> retHashMap = JsonManager.get_user_id_by_phone(phone);
            if ("0".equals(retHashMap.get("k0"))) {
                String usrid = (String) retHashMap.get("k2");
                if (null != usrid && !"".equals(usrid)) {
                    SqliteBase.update_usrid_by_phone(phone, usrid);
                }
                ret = usrid;
            } else {
                UtilLog.logWithCodeInfo("Failed to get usrid by phone = " + phone, "get_user_id_by_phone", "SupervisionManager");
            }
        }
        return ret;
    }

    /**
     * @param phone
     * @return
     */
    static public void get_user_id_by_phone_async(final String phone, final IJsonCallback cbk) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String usrid = get_user_id_by_phone(phone);
                if (cbk != null)
                    cbk.refreshData(App.ID_GET_USER_BY_PHONE, usrid);
            }
        }).start();
    }

    /**
     * @param phone
     * @return
     */
    static public void update_user_id_by_phone_async(final String phone) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                UtilLog.logWithCodeInfo("Phone number is " + phone, "update_user_id_by_phone_async", "SupervisionManager");
                //Get from add_family table first
                HashMap<String, Object> retHashMap = JsonManager.get_user_id_by_phone(phone);
                if ("0".equals(retHashMap.get("k0"))) {
                    String usrid = (String) retHashMap.get("k2");
                    if (usrid != null) {
                        FamilyMember fm = new FamilyMember();
                        fm.setPhone(phone);
                        fm.setUser_id(usrid);
                        SqliteBase.update_add_unilateral_family(fm);
                        UtilLog.logWithCodeInfo("[PROGRESS] Update usrid to sqlite " +usrid, "update_user_id_by_phone_async", "SupervisionManager");
                    }
                } else {
                    UtilLog.logWithCodeInfo("[PROGRESS] Failed to get usrid by phone = " + phone, "update_user_id_by_phone_async", "SupervisionManager");
                }
            }
        }).start();
    }

    static public boolean is_friend_by_phone(String phone) {
        boolean ret = true;
        if (null == SqliteBase.get_userid_from_relation_list(phone)) {
            ret = false;
        }
        return ret;
    }

    /**
     * @param phones
     * @return
     */
    static public Map<String, String> get_users_id_by_phones(List<String> phones) {
        HashMap<String, Object> retHashMap = JsonManager.get_users_id_by_phones(phones);
        if ("0".equals(retHashMap.get("k0"))) {
            return (Map<String, String>) retHashMap.get("k2");
        } else {
            return null;
        }
    }

    /**
     * The request is used to set how much longer Father will supervise the Son in a minute unit
     *
     * @param father_userid
     * @param son_userid
     * @param Status
     * @param value:        an integer value, Son will be locked over this time by second
     * @return OK, Failed
     */
    static private boolean Supervision_Notify(String father_userid, String son_userid, int Status, long value, String msg) {
        HashMap<String, Object> retHashMap = JsonManager.Supervision_Notify(father_userid, son_userid, Status, value, msg);
        if ("0".equals(retHashMap.get("k0")) || "2".equals(retHashMap.get("k0"))) {
            RelationData relation = new RelationData();
            if (father_userid.equals(RegistrationManager.getUsrIdFromSqlite())) {
                relation.setUser_id(son_userid);
                relation.setRole(1);
            } else {
                relation.setUser_id(father_userid);
                relation.setRole(0);
            }
            relation.setTime(value);
            relation.setStatus(Status);
            relation.setMsg(msg);
            if (retHashMap.containsKey("k2")) {
                if (retHashMap.get("k2") != null) {
                    relation.setSeq(Long.valueOf((String) retHashMap.get("k2")));
                }
            }
            long seq_m = 0;
            if (retHashMap.containsKey("k3")) {
                if (retHashMap.get("k3") != null) {
                    seq_m = Long.valueOf((String) retHashMap.get("k3"));
                }
            }
            SqliteBase.update_relation(seq_m, relation);
            return true;
        } else {
            return false;
        }
    }


    static public boolean supervision_request(String father_userid, String son_userid, long value, String msg) {
        return Supervision_Notify(father_userid, son_userid, RelationData.RELATION_FATHER_SUPERVISION_REQUEST, value, msg);
    }

    static public boolean agree_supervision_request_with_activity(Activity cxt, String father_userid, String son_userid, String msg) {
        if (false == DebugControl.DEBUG_FLAG) {
            if (!get_lock_screen_policy(cxt)) {
                return false;
            }
        }
        return Supervision_Notify(father_userid, son_userid, RelationData.RELATION_SON_AGREE_SUPERVISION, 0, msg);
    }

    static public boolean disagree_supervision_request(String father_userid, String son_userid, String msg) {
        return Supervision_Notify(father_userid, son_userid, RelationData.RELATION_SON_DISAGREE_SUPERVISION, 0, msg);
    }


    static public boolean request_stop_supervision(String father_userid, String son_userid, String msg) {
        return Supervision_Notify(father_userid, son_userid, RelationData.RELATION_SON_CANCEL_SUPERVISION_REQUEST, 0, msg);
    }


    static public boolean agree_stop_supervision(String father_userid, String son_userid, String msg) {
        return Supervision_Notify(father_userid, son_userid, RelationData.RELATION_FATHER_APPROVE_SUPERVISION_FREE, 0, msg);
    }

    static public boolean disagree_stop_supervision(String father_userid, String son_userid, String msg) {
        return Supervision_Notify(father_userid, son_userid, RelationData.RELATION_FATHER_DISAPPROVE_SUPERVISION_FREE, 0, msg);
    }

    static public boolean request_unlock(String father_userid, String son_userid, String msg) {
        return Supervision_Notify(father_userid, son_userid, RelationData.RELATION_SON_UNLOCK_REQUEST, 0, msg);
    }

    static public boolean agree_request_unlock(String father_userid, String son_userid, int add_time, String msg) {
        return Supervision_Notify(father_userid, son_userid, RelationData.RELATION_FATHER_APPROVE_UNLOCK, add_time, msg);
    }

    static public boolean disagree_request_unlock(String father_userid, String son_userid, String msg) {
        return Supervision_Notify(father_userid, son_userid, RelationData.RELATION_FATHER_DISAPPROVE_UNLOCK, 0, msg);
    }

    static public boolean unlock_son(String father_userid, String son_userid) {
        HashMap<String, Object> retHashMap = JsonManager.monitor_buddy(father_userid, son_userid, 1);
        if ("0".equals(retHashMap.get("k0"))) {
            return true;
        } else {
            return false;
        }
    }

    static public boolean lock_son(String father_userid, String son_userid) {
        HashMap<String, Object> retHashMap = JsonManager.monitor_buddy(father_userid, son_userid, 0);
        if ("0".equals(retHashMap.get("k0"))) {
            return true;
        } else {
            return false;
        }
    }

    static public void update_relation_list(Activity context, String user_id, int type) {
        HashMap<String, Object> retHashMap = JsonManager.get_relation_list(user_id);
        if ("0".equals(retHashMap.get("k0"))) {
            List<RelationData> ret = (List<RelationData>) retHashMap.get("k2");
            SqliteBase.update_relation_list(ret);
            if (get_recent_supervision_relation() != null) {
                get_lock_screen_policy(context);
            }
        }
        return;
    }
    static public void update_relation_list_async(final Activity context, final String user_id, final int type){
        new Thread(new Runnable() {
            @Override
            public void run() {
                update_relation_list(context, user_id, type);
            }
        }).start();
    }
    static public List<FamilyMember> get_families() {
        List<FamilyMember> ret = null;
        ret = SqliteBase.get_unilateral_families();
        if (ret == null) {
            HashMap<String, Object> retHashMap = JsonManager.get_family_list(RegistrationManager.getUserId());
            if (retHashMap != null && retHashMap.get("k0").equals("0")) {
                ret = (List<FamilyMember>) retHashMap.get("k2");
                if (ret != null) {
                    for (FamilyMember m : ret) {
                        SqliteBase.update_add_unilateral_family(m);
                    }
                }
            }
        }
        return ret;
    }

    static public List<RelationData> get_individual_list(String user_id) {
        return SqliteBase.get_individual_relation_list(user_id);
    }

    static public int get_individual_relation(String user_id) {
        int ret = RelationData.RELATION_ROLE_FRIEND;
        List<RelationData> contacts = SqliteBase.get_individual_relation_list(user_id);
        if (contacts != null) {
            for (RelationData item : contacts) {
                if (item.getStatus() == RelationData.RELATION_SUPERVISION_NO) {
                    break;
                }
                if (RelationData.RELATION_ROLE_SON == item.getRole()) {
                    ret = RelationData.RELATION_ROLE_SON;
                    break;
                }
                if (RelationData.RELATION_ROLE_FATHER == item.getRole()) {
                    ret = RelationData.RELATION_ROLE_FATHER;
                    break;
                }
            }
        }
        return ret;

    }

    static public boolean checkJiejieMemberByPhone(String phone) {
        boolean ret = true;
        if (null == get_user_id_by_phone(phone)) {
            ret = false;
        }
        return ret;
    }

    static public boolean add_family(String name, String phone) {
        boolean ret = false;
        UtilLog.logWithCodeInfo("Parameter phone = " + phone, "add_family", "SupervisionManager");
        UtilLog.logWithCodeInfo("parameter name = " + name, "add_family", "SupervisionManager");
        FamilyMember member = new FamilyMember();
        member.setName(name);
        member.setPhone(phone);
        SqliteBase.update_add_unilateral_family(member);
        UtilLog.logWithCodeInfo("[PROGRESS]Added contacts to Sqlite", "add_family", "SupervisionManager");
        HashMap<String, Object> retHashMap = JsonManager.add_family(RegistrationManager.getUserId(), phone, name);
        if ((retHashMap != null) && "0".equals(retHashMap.get("k0"))) {
            ret = true;
        } else {
            UtilLog.logWithCodeInfo("[PROGRESS]JsonManager.add_family return fail", "add_family", "SupervisionManager");
        }
        return ret;
    }

    static public void add_family_async(final String name, final String phone) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                add_family(name, phone);
            }
        }
        ).start();
    }

    public static boolean setContactName(String contact_id, String name) {
        HashMap<String, Object> retHashMap = JsonManager.update_nickname(contact_id, name);
        if (retHashMap != null && "0".equals(retHashMap.get("k0"))) {
            return true;
        } else {
            return false;
        }
    }

    public static void setContactNameAsync(final String contact_id, final String name) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                setContactName(contact_id, name);
            }
        }).start();
    }

    static public boolean add_friend(String userid, List<String> userList) {
        HashMap<String, Object> retHashMap = JsonManager.add_friend(userid, userList);
        if ("0".equals(retHashMap.get("k0"))) {
            UtilLog.logWithCodeInfo("Add friend is good!", "add_family", "SupervisionManager");
            return true;
        } else {
            return false;
        }
    }

    static public void add_friend_async(final String userid, final List<String> userList, final IJsonCallback cbk) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean ret = add_friend(userid, userList);
                if (cbk != null)
                    cbk.refreshData(App.ID_ADD_FRIEND, ret);
            }
        }).start();
    }

    static public boolean send_request_read_flag(long seq) {
        HashMap<String, Object> retHashMap = JsonManager.send_request_read_flag(seq);
        if ("0".equals(retHashMap.get("k0"))) {
            return true;
        } else {
            return false;
        }
    }

    static public RelationData get_recent_supervision_relation() {
        return SqliteBase.get_recent_supervision_relation();
    }

    static public Map<String, Long> get_filter_map() {
        return SqliteBase.get_filter_map();
    }

    static public boolean has_lock_screen_policy() {

        DevicePolicyManager policyManager = (DevicePolicyManager) App.getAppContext().getSystemService(Context.DEVICE_POLICY_SERVICE);
        ComponentName componentName = new ComponentName(App.getAppContext(), LockReceiver.class);

        return policyManager.isAdminActive(componentName);
    }

    public static void setSupervisionTimeout(String flag) {
        SqliteBase.set_config(JieconDBHelper.SUPERVISION_TIMEOUT, flag);
    }

    public static String getSupervisionTimeout() {
        return SqliteBase.get_config(JieconDBHelper.SUPERVISION_TIMEOUT);
    }

    public static void setNotificationContactsId(String contactsId) {
        SqliteBase.set_config(JieconDBHelper.NOTIFICATION_USR_ID, contactsId);
    }

    public static String getNotificationContactsId() {
        return SqliteBase.get_config(JieconDBHelper.NOTIFICATION_USR_ID);
    }

    public static void setNotificationContactsName(String name) {
        SqliteBase.set_config(JieconDBHelper.NOTIFICATION_NAME, name);
    }

    public static String getNotificationContactsName() {
        return SqliteBase.get_config(JieconDBHelper.NOTIFICATION_NAME);
    }

    public static void setNotificationContactsPhone(String phone) {
        SqliteBase.set_config(JieconDBHelper.NOTIFICATION_PHONE, phone);
    }

    public static String getNotificationContactsPhone() {
        return SqliteBase.get_config(JieconDBHelper.NOTIFICATION_PHONE);
    }

    //flag should be "MSG_NEWARRIVAL" or "MSG_NO"
    public static void setNewMsgFlag(String flag) {
        SqliteBase.set_config(JieconDBHelper.MSGFLAG_KEY, flag);
    }

    public static String getMsgFlag() {
        return SqliteBase.get_config(JieconDBHelper.MSGFLAG_KEY);
    }

    public static void setLockScreen(String flag) {
        SqliteBase.set_config(JieconDBHelper.LOCK_SCREEN, flag);
    }

    public static String getLockScreen() {
        return SqliteBase.get_config(JieconDBHelper.LOCK_SCREEN);
    }

    static public boolean get_lock_screen_policy(Activity context) {

        DevicePolicyManager policyManager = (DevicePolicyManager) App.getAppContext().getSystemService(Context.DEVICE_POLICY_SERVICE);
        ComponentName componentName = new ComponentName(App.getAppContext(), LockReceiver.class);
        if (policyManager.isAdminActive(componentName)) {
            return true;
        }
        //使用隐式意图调用系统方法来激活指定的设备管理器
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "lock_screen");
        context.startActivity(intent);
        return policyManager.isAdminActive(componentName);
    }

    static public void remove_lock_screen_policy() {
        try {
            if (get_recent_supervision_relation() == null) {
                DevicePolicyManager policyManager = (DevicePolicyManager) App.getAppContext().getSystemService(Context.DEVICE_POLICY_SERVICE);
                ComponentName componentName = new ComponentName(App.getAppContext(), LockReceiver.class);

                policyManager.removeActiveAdmin(componentName);
            }
        } catch (Exception ex) {

        }
    }

    static public void lock_screen() {
        try {
            setLockScreen(SupervisionManager.LOCK_SCREEN_YES);
            if (DebugControl.DEBUG_FLAG) {
                Intent i = new Intent();
                i.setClass(App.getAppContext(), MockLockActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                App.getAppContext().startActivity(i);
            } else {
                DevicePolicyManager policyManager = (DevicePolicyManager) App.getAppContext().getSystemService(Context.DEVICE_POLICY_SERVICE);
                policyManager.lockNow();
            }
        } catch (Exception ex) {

        }
    }
}
