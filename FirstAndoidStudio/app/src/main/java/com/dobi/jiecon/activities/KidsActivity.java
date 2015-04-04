package com.dobi.jiecon.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.Button;
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

public class KidsActivity extends Activity implements IJsonCallback {
    private TextView v_jiecon_id;
    private TextView v_contact_role;
    private TextView v_txt_phone_number;
    private TextView v_contact_status;
    private TextView v_txt_contact_name;
    private Button v_btn_father_cancel_supervision;
    private Button v_btn_father_lock;

    private ImageView v_img_title_bar_back;
    private String contacts_id;
    private String contacts_name;
    private String contacts_phone;
    private List<RelationData> relationList;

    private Button v_btn_see_daily; // add by harvey 3/18/15

    private static Activity cxt;

    private boolean pre_process() {

        boolean ret = true;

        if (contacts_id == null) {
            contacts_id = SqliteBase.get_usrid_from_family_list(contacts_phone);
            UtilLog.logWithCodeInfo("[PROGRESS] contacts_id is " + contacts_id, "pre_process", "ParentActivity");
            if (contacts_id == null) {
                UtilLog.showToast(cxt, getString(R.string.supervision_warn), Toast.LENGTH_SHORT);
                return false;
            }
        }
        return ret;
    }

    private View.OnClickListener Father_Cancel_Supervision_Listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (pre_process() == false) return;

            final String father_id = RegistrationManager.getUserId();
            final String son_id = contacts_id;
            DialogBuilderMgr.CreateAlertDialog(
                    cxt,
                    getString(R.string.family_stop_monitor),
                    new IAlarmDialog() {
                        @Override
                        public void consume() {
                            if (true == SupervisionManager.agree_stop_supervision(father_id, son_id, "")) {
                                UtilLog.showToast(cxt, getString(R.string.status_21_label), Toast.LENGTH_LONG);
                                backToFriend();
                            } else {
                                UtilLog.showToast(cxt, getString(R.string.status_21_label_fail), Toast.LENGTH_LONG);
                            }
                        }
                    },

                    new IAlarmDialog() {
                        @Override
                        public void consume() {
                            return;
                        }
                    },
                    null);

        }
    };


    // The phone user is as a role of Father
    private View.OnClickListener Father_Unlock_Listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (pre_process() == false) return;

            final String father_id = RegistrationManager.getUserId();
            final String son_id = contacts_id;
            String msg = String.format(getString(R.string.family_apply_stop_unlock), contacts_name);
            DialogBuilderMgr.CreateAlertDialog(
                    cxt,
                    msg,
                    new IAlarmDialog() {
                        @Override
                        public void consume() {
                            if (true == SupervisionManager.agree_request_unlock(father_id, son_id, 60 * 60 * 24, "")) {
                                UtilLog.showToast(cxt, getString(R.string.status_31_label), Toast.LENGTH_LONG);
                            } else {
                                UtilLog.showToast(cxt, getString(R.string.status_31_label_fail), Toast.LENGTH_LONG);
                            }
                        }
                    },

                    new IAlarmDialog() {
                        @Override
                        public void consume() {
                            UtilLog.showToast(cxt, getString(R.string.status_32_label), Toast.LENGTH_LONG);
                        }
                    },
                    null);

        }
    };
    // The phone user is as a role of Father
    private View.OnClickListener Father_lock_Listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (pre_process() == false) return;

            final String father_id = RegistrationManager.getUserId();
            final String son_id = contacts_id;
            String msg = String.format(getString(R.string.family_lock_who), contacts_name);
            DialogBuilderMgr.CreateAlertDialog(
                    cxt,
                    msg,
                    new IAlarmDialog() {
                        @Override
                        public void consume() {
                            if (true == SupervisionManager.lock_son(father_id, son_id)) {
                                UtilLog.showToast(cxt, getString(R.string.family_lock_success), Toast.LENGTH_LONG);
                            } else {
                                UtilLog.showToast(cxt, getString(R.string.family_lock_failed), Toast.LENGTH_LONG);
                            }
                        }
                    },

                    new IAlarmDialog() {
                        @Override
                        public void consume() {
                            return;
                        }
                    },
                    null);

        }
    };
    // See buddy's daily app statistic
    private View.OnClickListener see_daily_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(cxt, BuddyDayActivity.class);
            UtilLog.logWithCodeInfo("contacts_id  is " + contacts_id, "see_daily_listener", "SupervisionDetailsActivity");
            intent.putExtra(App.KEY_FAMILY2DAY_USERID, contacts_id);
            intent.putExtra(App.KEY_CURRENT_USR_NAME, contacts_name);
            cxt.startActivity(intent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.family_individual_kid);
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
        v_contact_role.setText(getString(R.string.family_kid));
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
        if (contacts_id == null) ;
        v_jiecon_id.setText("");
    }


    public void refreshData(int id, Object obj) {
        UtilLog.logWithCodeInfo("Entry of callback", "refreshData", "SupervisionDetailsActivity");
        Message msg = new Message();
        msg.what = id;
        switch (id) {
            case App.ID_CHECK_JIEJIE_USER:
                break;
            case App.ID_CHECK_PHONE_BINDING:
                break;
            case App.ID_GET_USER_BY_PHONE:
                contacts_id = (String) obj;
                break;
            case App.ID_ADD_FRIEND:
                break;
            default:
                break;
        }
    }

    private void initActionBtns() {
        v_btn_father_cancel_supervision = (Button) findViewById(R.id.father_action_2);
        v_btn_father_lock = (Button) findViewById(R.id.father_action_3);
        v_btn_see_daily = (Button) findViewById(R.id.monitor_daily);
        v_btn_see_daily.setOnClickListener(see_daily_listener);
        v_btn_father_cancel_supervision.setOnClickListener(Father_Cancel_Supervision_Listener);
        v_btn_father_lock.setOnClickListener(Father_lock_Listener);
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
        if ((contacts_relation == RelationData.RELATION_SUPERVISION_YES) && (contacts_role == RelationData.RELATION_ROLE_SON)) {
            if (rd.getTime() != 0) {
                v_contact_status.setVisibility(View.VISIBLE);
                String msg1 = getString(R.string.time_limited);
                v_contact_status.setText(msg1 + rd.getTime() / 60 + getString(R.string.MIN2));
            }
        } else {
            UtilLog.logWithCodeInfo("status is incorrect", "process1()", "FriendActivity");
        }

    }

    /* more than one records mean that there are outstanding request or notification*/
    private void process2() {
        for (int i = 1; i < relationList.size(); i++) {
            //notification or request
            RelationData item = relationList.get(i);
            long sts = item.getStatus();
            UtilLog.logWithCodeInfo("Current status is " + String.valueOf(sts), "onResume", "SupervisionDetailsActivity");
            SonResponse(sts);
        }
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

    private void SonResponse(long sts) {
        switch ((int) sts) {
            //son
            case RelationData.RELATION_SON_AGREE_SUPERVISION:// = 11;
                break;
            case RelationData.RELATION_SON_DISAGREE_SUPERVISION:// = 12;
                break;
            case RelationData.RELATION_SON_CANCEL_SUPERVISION_REQUEST:// = 20;
                final String father_id_2 = RegistrationManager.getUserId();
                final String son_id_2 = contacts_id;
                String msg = String.format(getString(R.string.family_apply_no_monitor), contacts_name);
                DialogBuilderMgr.CreateAgreeDialog(
                        cxt,
                        msg,
                        new IAlarmDialog() {
                            @Override
                            public void consume() {
                                if (true == SupervisionManager.agree_stop_supervision(father_id_2, son_id_2, "")) {
                                    UtilLog.showToast(cxt, contacts_name + getString(R.string.status_21_label), Toast.LENGTH_LONG);
                                    backToFriend();

                                } else {
                                    UtilLog.showToast(cxt, contacts_name + getString(R.string.status_21_label_fail), Toast.LENGTH_LONG);
                                }
                            }
                        },

                        new IAlarmDialog() {
                            @Override
                            public void consume() {
                                SupervisionManager.disagree_stop_supervision(father_id_2, son_id_2, "");
                                UtilLog.showToast(cxt, contacts_name + getString(R.string.status_22_label), Toast.LENGTH_LONG);
                            }
                        },
                        null).show();
                break;
            case RelationData.RELATION_SON_UNLOCK_REQUEST:// = 30;
                final String father_id_3 = RegistrationManager.getUserId();
                final String son_id_3 = contacts_id;
                String msg1 = String.format(getString(R.string.family_apply_unlock_who), contacts_name);
                DialogBuilderMgr.CreateAgreeDialog(
                        cxt,
                        msg1,
                        new IAlarmDialog() {
                            @Override
                            public void consume() {
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (true == SupervisionManager.agree_request_unlock(father_id_3, son_id_3, 60 * 60 * 24, "")) {
                                            UtilLog.showToast(cxt, contacts_name + getString(R.string.status_31_label), Toast.LENGTH_LONG);
                                        }
                                    }
                                }).start();
                            }
                        },

                        new IAlarmDialog() {
                            @Override
                            public void consume() {
                                if (true == SupervisionManager.disagree_request_unlock(father_id_3, son_id_3, "")) {
                                    UtilLog.showToast(cxt, contacts_name + getString(R.string.status_32_label), Toast.LENGTH_LONG);
                                }
                            }
                        },
                        null).show();
                break;
        }
    }

    private void backToFriend() {
        UtilLog.showToast(cxt, contacts_name + getString(R.string.status_21_label), Toast.LENGTH_LONG);
        Intent intent = new Intent(cxt, FriendActivity.class);
        intent.putExtra(App.KEY_CURRENT_USR_NAME, contacts_name);
        intent.putExtra(App.KEY_CURRENT_USR_PHONE, contacts_phone);
        intent.putExtra(App.KEY_FROM_CXT, App.ID_FAMILY_LIST_ADAPTER);
        cxt.startActivity(intent);
    }
}
