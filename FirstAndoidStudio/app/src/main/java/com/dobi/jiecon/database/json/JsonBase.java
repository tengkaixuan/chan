package com.dobi.jiecon.database.json;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;

import com.dobi.jiecon.App;
import com.dobi.jiecon.R;
import com.dobi.jiecon.UtilLog;
import com.dobi.jiecon.activities.TabSample;
import com.dobi.jiecon.data.RelationData;
import com.dobi.jiecon.database.sqlite.SqliteBase;
import com.dobi.jiecon.datacontroller.RegistrationManager;
import com.dobi.jiecon.datacontroller.SupervisionManager;
import com.dobi.jiecon.utils.NotifiyMe;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class JsonBase extends Thread {
    private static final String BASE_URL = "http://www.ggduobi.com/jiecon/";
    private String url = null;
    private JSONObject jsonObjIn = null;
    private List<NameValuePair> nameValuePairs = null;
    private JSONObject jsonObjOut = null;

    public JsonBase(String url, JSONObject jsonObjIn, List<NameValuePair> nameValuePairs) {
        super();
        this.url = url;
        this.jsonObjIn = jsonObjIn;
        this.nameValuePairs = nameValuePairs;
    }

    public JsonBase(String url, List<NameValuePair> nameValuePairs) {
        super();
        this.url = url;
        this.nameValuePairs = nameValuePairs;
    }

    public JSONObject getJsonObject() {
        return jsonObjOut;
    }

    @Override
    public void run() {
        jsonObjOut = _getJsonObject();
    }

    private JSONObject _getJsonObject() {

        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(BASE_URL + url);
        if (nameValuePairs == null) {
            nameValuePairs = new ArrayList<NameValuePair>(1);
        }
        nameValuePairs.add(new BasicNameValuePair("char", HTTP.UTF_8));
        try {
            post.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
            if (jsonObjIn != null) {
                StringEntity se = new StringEntity(jsonObjIn.toString());
                post.setEntity(se);
            }
            HttpResponse response = client.execute(post);

            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()), 8192);
            String line = "";
            JSONObject jsonObject = null;
            StringBuffer sb = new StringBuffer("");
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            jsonObject = new JSONObject(sb.toString());
            checkCommInfo(jsonObject);
            rd.close();
            return jsonObject;
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    void checkCommInfo(JSONObject jsonObject) {
        if (jsonObject == null) {
            return;
        }
        JSONArray jsonArray;

        try {
            if (jsonObject.has("a1")) {
                jsonArray = jsonObject.getJSONArray("a1");
                Map<String, Long> filterList = new Hashtable<String, Long>();
                for (int j = 0; j < jsonArray.length(); j++) {
                    JSONObject objRel = jsonArray.getJSONObject(j);
                    String pkgName = JsonManager.getJsonString(objRel, "app_pkgname");
                    filterList.put(pkgName, JsonManager.getJsonLong(objRel, "level"));
                }
                if (filterList.size() > 0) {
                    SqliteBase.update_filter_app(filterList);
                }
            }

            if (jsonObject.has("a2")) {

                JSONArray appListJson = jsonObject.getJSONArray("a2");


                JSONObject objRel = appListJson.getJSONObject(0);
                RelationData relation = new RelationData();

                relation.setUser_id(JsonManager.getJsonString(objRel, "user_id"));
                relation.setName(JsonManager.getJsonString(objRel, "name"));
                relation.setPhone(JsonManager.getJsonString(objRel, "phone"));
                relation.setRole(JsonManager.getJsonLong(objRel, "role"));
                relation.setTime(JsonManager.getJsonLong(objRel, "time"));
                relation.setSeq(JsonManager.getJsonLong(objRel, "seq_r"));
                relation.setType(JsonManager.getJsonLong(objRel, "type"));
                relation.setRead_flag(JsonManager.getJsonLong(objRel, "read_flag"));
                relation.setMsg(JsonManager.getJsonString(objRel, "msg"));
                relation.setStatus(JsonManager.getJsonLong(objRel, "status"));

                //Rock start adding
                //update the sqlite
                SqliteBase.update_relation(JsonManager.getJsonLong(objRel, "seq_m"), relation);
                int id = 0;
                boolean msg_appendix = false;
                if (relation.getStatus() == RelationData.RELATION_FATHER_SUPERVISION_REQUEST) {
                    msg_appendix = true;
                    id = R.string.status_10;
                } else if (relation.getStatus() == RelationData.RELATION_SON_AGREE_SUPERVISION) {
                    id = R.string.status_11;
                } else if (relation.getStatus() == RelationData.RELATION_SON_DISAGREE_SUPERVISION) {
                    id = R.string.status_12;
                } else if (relation.getStatus() == RelationData.RELATION_SON_CANCEL_SUPERVISION_REQUEST) {
                    msg_appendix = true;
                    id = R.string.status_20;
                } else if (relation.getStatus() == RelationData.RELATION_FATHER_APPROVE_SUPERVISION_FREE) {
                    id = R.string.status_21;
                } else if (relation.getStatus() == RelationData.RELATION_FATHER_DISAPPROVE_SUPERVISION_FREE) {
                    id = R.string.status_22;
                } else if (relation.getStatus() == RelationData.RELATION_SON_UNLOCK_REQUEST) {
                    msg_appendix = true;
                    id = R.string.status_30;
                } else if (relation.getStatus() == RelationData.RELATION_FATHER_APPROVE_UNLOCK) {
                    if (relation.getTime() == -25 * 60 * 60) {
                        id = R.string.status_31_2;
                    } else if (relation.getTime() == 25 * 60 * 60) {
                        id = R.string.status_31_3;
                    } else {
                        id = R.string.status_31_1;
                    }
                } else if (relation.getStatus() == RelationData.RELATION_FATHER_DISAPPROVE_UNLOCK) {
                    id = R.string.status_32;
                }
                Resources res = App.getAppContext().getResources();
                String msg = relation.getMsg();
                if (msg == null || msg.length() == 0 || "null".equals(msg)) {
                    msg = res.getString(id);
                } else {
                    msg = res.getString(id, msg) + res.getString(R.string.status_msg, relation.getMsg());
                }

                Bundle bundle = new Bundle();

                bundle.putInt("FLAG", 1);
                //save the update status
                UtilLog.logWithCodeInfo("New message arrived!", "checkCommInfo", "JsonBase");
                SupervisionManager.setNewMsgFlag(SupervisionManager.MSG_NEWARRIVAL);
                SupervisionManager.setNotificationContactsId(relation.getUser_id());
                SupervisionManager.setNotificationContactsName(relation.getName());
                SupervisionManager.setNotificationContactsPhone(relation.getPhone());

                UtilLog.logWithCodeInfo("Name is " + relation.getName(), "checkCommInfo", "JsonBase");
                UtilLog.logWithCodeInfo("Phone is " + relation.getPhone(), "checkCommInfo", "JsonBase");
                if (msg_appendix == true)
                    msg += res.getString(R.string.family_please_check_family_tab_message);
                //Rock end adding
                // 发出通知
                id = (int) ((relation.getSeq() + NotifiyMe.PEEK_BASE) % Integer.MAX_VALUE);
                String name = JsonManager.getJsonString(objRel, "name");
                NotifiyMe.notifyMsg(id, name, msg, bundle, true, relation.getStatus());
                SupervisionManager.send_request_read_flag(relation.getSeq());

                SupervisionManager.update_relation_list(TabSample.mTab, RegistrationManager.getUserId(), 0);
            }


        } catch (JSONException ex) {

        }
    }


    // for test
    public static void testNotify() {

        int ID = 1;
        String service = App.getAppContext().NOTIFICATION_SERVICE;
        NotificationManager nManager = (NotificationManager) App.getAppContext().getSystemService(service);

        Notification notification = new Notification();
        String tickerText = App.getAppContext().getString(R.string.app_name); // 通知提示
        // 显示时间
        long when = System.currentTimeMillis();

        notification.icon = R.drawable.jiejie_icon32;// 设置通知的图标
        notification.tickerText = tickerText; // 显示在状态栏中的文字
        notification.when = when; // 设置来通知时的时间
        //  notification.sound = Uri.parse("android.resource://com.sun.alex/raw/dida"); // 自定义声音
        notification.flags = Notification.FLAG_NO_CLEAR; // 点击清除按钮时就会清除消息通知,但是点击通知栏的通知时不会消失
        notification.flags = Notification.FLAG_ONGOING_EVENT; // 点击清除按钮不会清除消息通知,可以用来表示在正在运行
        notification.flags |= Notification.FLAG_AUTO_CANCEL; // 点击清除按钮或点击通知后会自动消失
        notification.flags |= Notification.FLAG_ONLY_ALERT_ONCE; // 一直进行，比如音乐一直播放，知道用户响应
//        notification.defaults = Notification.DEFAULT_SOUND; // 调用系统自带声音
//        notification.defaults = Notification.DEFAULT_SOUND;// 设置默认铃声
//        notification.defaults = Notification.DEFAULT_VIBRATE;// 设置默认震动
//        notification.defaults = Notification.DEFAULT_ALL; // 设置铃声震动
        notification.defaults = Notification.DEFAULT_ALL; // 把所有的属性设置成默认

        try {
            // notify request
            Intent intent = new Intent(App.getAppContext(), TabSample.class);
            // intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            // 获取PendingIntent,点击时发送该Intent

            PendingIntent pIntent = PendingIntent.getActivity(App.getAppContext(), 0,
                    intent, PendingIntent.FLAG_UPDATE_CURRENT);
            // 设置通知的标题和内容
            notification.setLatestEventInfo(App.getAppContext(), "戒戒标题",
                    "你有新消息！", pIntent);
            // 发出通知
            nManager.cancel(ID);
            nManager.notify(ID, notification);

        } catch (Exception e) {

        }


    }
}

