package com.dobi.jiecon.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dobi.jiecon.App;
import com.dobi.jiecon.R;
import com.dobi.jiecon.UtilLog;
import com.dobi.jiecon.data.RelationData;
import com.dobi.jiecon.database.json.IJsonCallback;
import com.dobi.jiecon.database.sqlite.SqliteBase;
import com.dobi.jiecon.datacontroller.RegistrationManager;
import com.dobi.jiecon.datacontroller.SupervisionManager;
import com.dobi.jiecon.utils.DialogBuilderMgr;
import com.dobi.jiecon.utils.IAlarmDialog;

import java.util.ArrayList;
import java.util.List;

public class ParentActivity extends Activity{
    private TextView v_jiecon_id;
    private TextView v_contact_role;
    private TextView v_txt_phone_number;
    private TextView v_contact_status;
    private TextView v_txt_contact_name;
    private Button v_btn_son_request_cancel_supervision;
    private Button v_btn_son_request_unlock;

    private ImageView v_img_title_bar_back;
    private String contacts_id;
    private String contacts_name;
    private String contacts_phone;
    private List<RelationData> relationList;

    private static Activity cxt;

    private final boolean isFirstVersion = true;
    private boolean pre_process() {

        boolean ret = true;

        if (contacts_id == null) {
            contacts_id = SqliteBase.get_usrid_from_family_list(contacts_phone);
            UtilLog.logWithCodeInfo("[PROGRESS] contacts_id is "+ contacts_id, "pre_process", "ParentActivity");
            if (contacts_id == null) {
                UtilLog.showToast(cxt, getString(R.string.supervision_warn), Toast.LENGTH_SHORT);
                return false;
            }
        }
        return ret;
    }
    // The phone user is as a role of son
    private View.OnClickListener Son_Supervision_Cancel_Request_Listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (false == pre_process())return;

            final String father_id = contacts_id;
            final String son_id = RegistrationManager.getUserId();
            DialogBuilderMgr.CreateAlertDialog(
                    cxt,
                    getString(R.string.family_no_monitor),
                    new IAlarmDialog() {
                        @Override
                        public void consume() {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    if (true == SupervisionManager.request_stop_supervision(father_id, son_id, "")) {
                                        UtilLog.showToast(cxt, getString(R.string.status_20_label_ok), Toast.LENGTH_LONG);
                                    } else {
                                        UtilLog.showToast(cxt, getString(R.string.status_20_label_fail), Toast.LENGTH_LONG);
                                    }
                                }
                            }).start();
                            return;
                        }
                    },

                    new IAlarmDialog() {
                        @Override
                        public void consume() {
                        }
                    },
                    null);

        }
    };

    // The phone user is as a role of son
    private View.OnClickListener Son_Unlock_Request_Listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (false == pre_process())return;

            final String father_id = contacts_id;
            final String son_id = RegistrationManager.getUserId();
            String msg = String.format(getString(R.string.family_apply_unlock), contacts_name);
            DialogBuilderMgr.CreateAlertDialog(
                    cxt,
                    msg,
                    new IAlarmDialog() {
                        @Override
                        public void consume() {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    if (true == SupervisionManager.request_unlock(father_id, son_id, "")) {
                                        UtilLog.showToast(cxt, getString(R.string.status_30_label_ok), Toast.LENGTH_LONG);
                                    } else {
                                        UtilLog.showToast(cxt, getString(R.string.status_30_label_fail), Toast.LENGTH_LONG);
                                    }
                                }
                            }).start();
                        }
                    },

                    new IAlarmDialog() {
                        @Override
                        public void consume() {
//                            if (true == SupervisionManager.disagree_request_unlock(father_id, son_id, "")) {
//                                Resources res = App.getAppContext().getResources();
//                                curBtn.setText(res.getString(R.string.status_22_label));
//                            }
                            return;
                        }
                    },
                    null);

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.family_individual_parent);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.supervision_detail_titlebar);
        v_img_title_bar_back = (ImageView) findViewById(R.id.supervision_title_bar_back);
        v_img_title_bar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(cxt, TabSample.class);
                startActivity(intent);
                finish();
            }
        });
        cxt = this;
        initProfileViews();
        initActionBtns();
    }

    @Override
    public void onResume() {
        super.onResume();
        relationList = SupervisionManager.get_individual_list(contacts_id);
        if (relationList == null) {
            relationList = new ArrayList<RelationData>();
            relationList.add(init());
        }
        UtilLog.logWithCodeInfo("Current relation list count is " + relationList.size(), "onResume", "SupervisionDetailsActivity");
        process1();
        process2();
    }

    private void initProfileViews() {
        v_txt_contact_name = (TextView) findViewById(R.id.contact_name);
        v_jiecon_id = (TextView) findViewById(R.id.contact_number);
        v_contact_role = (TextView) findViewById(R.id.contact_role);
        v_contact_role.setText(getString(R.string.family_parent));

        v_contact_status = (TextView) findViewById(R.id.contacts_status);
        v_txt_phone_number = (TextView) findViewById(R.id.phone_label);
        int from_cxt = getIntent().getIntExtra(App.KEY_FROM_CXT, 0);
        contacts_name = getIntent().getStringExtra(App.KEY_CURRENT_USR_NAME);
        contacts_phone = getIntent().getStringExtra(App.KEY_CURRENT_USR_PHONE);

        if (from_cxt == App.ID_NOTIFICATION) {
            contacts_id = getIntent().getStringExtra(App.KEY_CURRENT_CONTACTS_ID);
        } else if (from_cxt == App.ID_FAMILY_LIST_ADAPTER) {
            contacts_id = SqliteBase.get_usrid_from_family_list(contacts_phone);
        }
        v_txt_phone_number.setText(contacts_phone);
        v_txt_contact_name.setText(contacts_name);
        if (contacts_id == null)
        v_jiecon_id.setText("");
    }

    private void initActionBtns() {
        v_btn_son_request_cancel_supervision = (Button) findViewById(R.id.son_action_2);
        v_btn_son_request_cancel_supervision.setOnClickListener(Son_Supervision_Cancel_Request_Listener);
        v_btn_son_request_unlock = (Button) findViewById(R.id.son_action_3);
        v_btn_son_request_unlock.setOnClickListener(Son_Unlock_Request_Listener);
    }

    /*
    Only one record with this contacts
    */
    private void process1() {
        RelationData rd = relationList.get(0);
        long contacts_role = rd.getRole();
        long contacts_relation = rd.getStatus();
        if (contacts_name == null || contacts_name.equals("") || contacts_name.equals("null")) {
            contacts_name = contacts_id;
        }
        if (contacts_relation == RelationData.RELATION_SUPERVISION_YES) {
            if (contacts_role == RelationData.RELATION_ROLE_FATHER) {

                String flag = SupervisionManager.getLockScreen();
                if (null != flag && flag.equals(SupervisionManager.LOCK_SCREEN_YES)) {
                    v_btn_son_request_unlock.setVisibility(View.VISIBLE);
                }
                if (rd.getTime() != 0) {
                    v_contact_status.setVisibility(View.VISIBLE);
                    String msg1 = getString(R.string.time_limited);
                    v_contact_status.setText(msg1 + rd.getTime() / 60 + getString(R.string.MIN2));
                }
            }
        }

    }

    /* more than one records mean that there are outstanding request or notification*/
    private void process2() {
        for (int i = 1; i < relationList.size(); i++) {
            //notification or request
            RelationData item = relationList.get(i);
            long sts = item.getStatus();
            UtilLog.logWithCodeInfo("Current status is " + String.valueOf(sts), "onResume", "SupervisionDetailsActivity");
            UtilLog.logWithCodeInfo("Current contact role is FATHER", "onResume", "SupervisionDetailsActivity");
            ParentResponse(sts);
        }
    }

    private int getRole(List<RelationData> contacts) {
        int ret = RelationData.RELATION_ROLE_FATHER;
        for (RelationData item : contacts) {
            if (RelationData.RELATION_ROLE_SON == item.getRole()) {
                ret = RelationData.RELATION_ROLE_SON;
                break;
            }
        }
        return ret;
    }

    private RelationData init() {
        UtilLog.logWithCodeInfo("Initial RelationList", "init", "SupervisionDetailsActivity");
        RelationData rd = new RelationData();
        rd.setUser_id(contacts_id);
        rd.setStatus(RelationData.RELATION_SUPERVISION_NO);
        return rd;
    }

    private String role2Str(long role) {
        String ret = "";
        switch ((int) role) {
            case RelationData.RELATION_ROLE_SON:
                ret = getString(R.string.family_kid);
                break;
            case RelationData.RELATION_ROLE_FATHER:
                ret = getString(R.string.family_parent);
                break;
            default:
                ret = getString(R.string.family_friend);
        }
        return ret;
    }

    private void ParentResponse(long sts) {
        switch ((int) sts) {
            //Father
            case RelationData.RELATION_FATHER_SUPERVISION_REQUEST: //10
                break;
            case RelationData.RELATION_FATHER_APPROVE_SUPERVISION_FREE: //21
                UtilLog.showToast(cxt, contacts_name + getString(R.string.status_21_label_agree), Toast.LENGTH_LONG);
                backToFriend();
                backToFriend();
                break;
            case RelationData.RELATION_FATHER_DISAPPROVE_SUPERVISION_FREE: //22
                UtilLog.showToast(cxt, contacts_name + getString(R.string.status_22_label), Toast.LENGTH_LONG);
                break;
            case RelationData.RELATION_FATHER_APPROVE_UNLOCK:// = 31
                SupervisionManager.setLockScreen(SupervisionManager.LOCK_SCREEN_NO);
                UtilLog.showToast(cxt, contacts_name + getString(R.string.status_31_label), Toast.LENGTH_LONG);
                break;
            case RelationData.RELATION_FATHER_DISAPPROVE_UNLOCK:// = 32;
                UtilLog.showToast(cxt, contacts_name + getString(R.string.status_32_label), Toast.LENGTH_LONG);
                break;

            default:
        }
    }
    private void backToFriend(){
        Intent intent = new Intent(cxt, FriendActivity.class);
        intent.putExtra(App.KEY_CURRENT_USR_NAME, contacts_name);
        intent.putExtra(App.KEY_CURRENT_USR_PHONE, contacts_phone);
        intent.putExtra(App.KEY_FROM_CXT, App.ID_FAMILY_LIST_ADAPTER);
        cxt.startActivity(intent);
    }
}
