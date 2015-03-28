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
import com.dobi.jiecon.datacontroller.RegistrationManager;
import com.dobi.jiecon.datacontroller.SupervisionManager;
import com.dobi.jiecon.utils.DialogBuilderMgr;
import com.dobi.jiecon.utils.IAlarmDialog;

import java.util.ArrayList;
import java.util.List;

public class SupervisionDetailsActivity extends Activity implements IJsonCallback {
    private TextView v_jiecon_id;
    private TextView v_contact_role;
    private TextView v_txt_phone_number;
    private TextView v_contact_status;
    private TextView v_txt_contact_name;
    private Button v_btn_both_request_supervision;
    private Button v_btn_father_cancel_supervision;
    private Button v_btn_son_request_cancel_supervision;
    private Button v_btn_father_lock;
    private Button v_btn_son_request_unlock;
    private Button v_btn_see_daily; // add by harvey 3/18/15

    private ImageView v_img_title_bar_back;
    private String contacts_id;
    private String contacts_name;
    private String contacts_phone;
    private List<RelationData> relationList;

    private static Activity cxt;
    private boolean phone_is_bind = false;
    private boolean contacts_is_jiejie_user = false;

    private final boolean isFirstVersion = true;

    // The phone user is as a role of son
    private View.OnClickListener Son_Supervision_Cancel_Request_Listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
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
                                        showSonActions(false, true, false);
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

    private Handler jason_async_handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case App.ID_CHECK_PHONE_BINDING:
                    if (phone_is_bind == false)
                        showBindDialog();
                    break;
                case App.ID_CHECK_JIEJIE_USER:
                    if (phone_is_bind == true && contacts_is_jiejie_user == false)
                        UtilLog.showToast(cxt, getString(R.string.supervision_warn), Toast.LENGTH_SHORT);
                    break;
                case App.ID_GET_USER_BY_PHONE:
                    UtilLog.logWithCodeInfo("Set contacts_id " + contacts_id, "handler.handleMessage", "SupervisionDetailsActivity");
                    v_jiecon_id.setText(contacts_id);
                    break;
                case App.ID_ADD_FRIEND:
                    break;
                default:
                    break;
            }
        }
    };
    private Handler internal_handle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case App.SUPERVISION_ACTIVITY_START_MONITOR:
                    RegistrationManager.check_phone_binding_async((IJsonCallback) cxt);
                    SupervisionManager.get_user_id_by_phone_async(contacts_phone, (IJsonCallback) cxt);
                    if (!SupervisionManager.is_friend_by_phone(contacts_phone)) {
                        UtilLog.logWithCodeInfo("contacts_phone=" + contacts_phone, "Request_Supervision_Listener", "SupervisionDetailsActivity");
                        List<String> userList = new ArrayList<String>();
                        userList.add(contacts_id);
                        SupervisionManager.add_friend_async(RegistrationManager.getUserId(), userList, (IJsonCallback) cxt);
                        SupervisionManager.setContactNameAsync(contacts_id, contacts_name);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    private View.OnClickListener Request_Supervision_Listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            UtilLog.logWithCodeInfo("contacts_phone=" + contacts_phone, "Request_Supervision_Listener", "SupervisionDetailsActivity");
            Message msg = new Message();
            msg.what = App.SUPERVISION_ACTIVITY_START_MONITOR;
            internal_handle.sendMessage(msg);

            final EditText inputDuration = new EditText(cxt);
            inputDuration.setInputType(InputType.TYPE_CLASS_NUMBER);
            AlertDialog.Builder builder = new AlertDialog.Builder(cxt);
            builder.setTitle(getString(R.string.control_supervision_mins_lable)).setIcon(android.R.drawable.ic_dialog_info).setView(inputDuration)
                    .setNegativeButton(getString(R.string.family_cancel), null);

            builder.setPositiveButton(getString(R.string.OK), new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    Editable editDur = inputDuration.getText();

                    if (editDur == null) {
                        return;
                    }
                    String dur = inputDuration.getText().toString();
                    if ("".equals(inputDuration.getText().toString())) {
                        UtilLog.showAlert(cxt, getString(R.string.family_bind_dialog_monitor_title), getString(R.string.supervision_time_setting_warn));
//                        UtilLog.showToast(cxt, getString(R.string.supervision_time_setting_warn), Toast.LENGTH_SHORT);
                        return;
                    }
                    int t1 = Integer.parseInt(dur);
                    //Set the t1, father id and son id to the server
//                SupervisionManager.Supervision_Notify(t1, RegistrationManager.getUserId(), m_btn_confirm.getTag().toString());
                    if (true == SupervisionManager.supervision_request(RegistrationManager.getUserId(), contacts_id, t1 * 60, "")) {
//                        showFatherActions(false,true,false);
                        UtilLog.showToast(cxt, getString(R.string.supervision_setting_ok), Toast.LENGTH_SHORT);
                    } else {
                        UtilLog.showToast(cxt, getString(R.string.supervision_setting_fail), Toast.LENGTH_SHORT);
                    }

                }
            });
            builder.show();
        }
    };

    // The phone user is as a role of son
    private View.OnClickListener Son_Unlock_Request_Listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
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
    // The phone user is as a role of Father
    private View.OnClickListener Father_Cancel_Supervision_Listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            final String father_id = RegistrationManager.getUserId();
            final String son_id = contacts_id;
            DialogBuilderMgr.CreateAlertDialog(
                    cxt,
                    getString(R.string.family_stop_monitor),
                    new IAlarmDialog() {
                        @Override
                        public void consume() {
                            if (true == SupervisionManager.agree_stop_supervision(father_id, son_id, "")) {
//                                showSonActions(true, false, false);
                                UtilLog.showToast(cxt, getString(R.string.status_21_label), Toast.LENGTH_LONG);
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
        setContentView(R.layout.family_individual_detail);
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
        hideAllActions();
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
        v_contact_status = (TextView) findViewById(R.id.contacts_status);
        v_txt_phone_number = (TextView) findViewById(R.id.phone_label);
        int from_cxt = getIntent().getIntExtra(App.KEY_FROM_CXT, 0);
        contacts_name = getIntent().getStringExtra(App.KEY_CURRENT_USR_NAME);
        contacts_phone = getIntent().getStringExtra(App.KEY_CURRENT_USR_PHONE);

        if (from_cxt == App.ID_NOTIFICATION) {
            contacts_id = getIntent().getStringExtra(App.KEY_CURRENT_CONTACTS_ID);
        } else if (from_cxt == App.ID_FAMILY_LIST_ADAPTER) {
            SupervisionManager.get_user_id_by_phone_async(contacts_phone, this);
            contacts_id = "";
        }
        v_txt_phone_number.setText(contacts_phone);
        v_txt_contact_name.setText(contacts_name);
        if (contacts_id == null) contacts_id = "";
        v_jiecon_id.setText(contacts_id);
    }


    public void refreshData(int id, Object obj) {
        UtilLog.logWithCodeInfo("Entry of callback", "refreshData", "SupervisionDetailsActivity");
        Message msg = new Message();
        msg.what = id;
        switch (id) {
            case App.ID_CHECK_JIEJIE_USER:
                contacts_is_jiejie_user = (Boolean) obj;
                break;
            case App.ID_CHECK_PHONE_BINDING:
                phone_is_bind = (Boolean) obj;
                break;
            case App.ID_GET_USER_BY_PHONE:
                contacts_id = (String) obj;
                break;
            case App.ID_ADD_FRIEND:
                break;
            default:
                break;
        }
        jason_async_handler.sendMessage(msg);
    }

    private void initActionBtns() {
        v_btn_both_request_supervision = (Button) findViewById(R.id.both_action_1);

        v_btn_son_request_cancel_supervision = (Button) findViewById(R.id.son_action_2);
        v_btn_son_request_unlock = (Button) findViewById(R.id.son_action_3);

        v_btn_father_cancel_supervision = (Button) findViewById(R.id.father_action_2);
        v_btn_father_lock = (Button) findViewById(R.id.father_action_3);
        v_btn_see_daily = (Button) findViewById(R.id.monitor_daily);
        v_btn_see_daily.setOnClickListener(see_daily_listener);
    }

    private void hideAllActions() {
        if(isFirstVersion) {
            v_btn_both_request_supervision.setVisibility(View.GONE);
            v_btn_son_request_cancel_supervision.setVisibility(View.GONE);
            v_btn_son_request_unlock.setVisibility(View.GONE);
            v_btn_father_cancel_supervision.setVisibility(View.GONE);
            v_btn_father_lock.setVisibility(View.GONE);
        } else {
            v_btn_both_request_supervision.setVisibility(View.GONE);
            v_btn_son_request_cancel_supervision.setVisibility(View.GONE);
            v_btn_son_request_unlock.setVisibility(View.GONE);
            v_btn_father_cancel_supervision.setVisibility(View.GONE);
            v_btn_father_lock.setVisibility(View.GONE);
            v_btn_see_daily.setVisibility(View.GONE);
        }
    }

    private void showAllActions() {
        if (isFirstVersion) {
            v_btn_see_daily.setVisibility(View.VISIBLE);
        } else {
            v_btn_both_request_supervision.setVisibility(View.VISIBLE);
            v_btn_son_request_cancel_supervision.setVisibility(View.VISIBLE);
            v_btn_son_request_unlock.setVisibility(View.VISIBLE);
            v_btn_father_cancel_supervision.setVisibility(View.VISIBLE);
            v_btn_father_lock.setVisibility(View.VISIBLE);
            v_btn_see_daily.setVisibility(View.VISIBLE);
        }
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
        if (contacts_relation == RelationData.RELATION_SUPERVISION_NO) {
            v_contact_role.setText(getString(R.string.family_friend));
            v_btn_both_request_supervision.setOnClickListener(Request_Supervision_Listener);
            if(!isFirstVersion)v_btn_both_request_supervision.setVisibility(View.VISIBLE);
            v_contact_status.setVisibility(View.GONE);
        } else if (contacts_relation == RelationData.RELATION_SUPERVISION_YES) {
            v_contact_role.setText(role2Str(contacts_role));
            if (contacts_role == RelationData.RELATION_ROLE_FATHER) {
                showSonActions(false, true, false);
                v_btn_son_request_cancel_supervision.setOnClickListener(Son_Supervision_Cancel_Request_Listener);
                v_btn_son_request_unlock.setOnClickListener(Son_Unlock_Request_Listener);
                String flag = SupervisionManager.getLockScreen();
                if (null != flag && flag.equals(SupervisionManager.LOCK_SCREEN_YES)) {
                    v_btn_son_request_unlock.setVisibility(View.VISIBLE);
                }
            } else if (contacts_role == RelationData.RELATION_ROLE_SON) {
                showFatherActions(false, true, false);
                v_btn_father_cancel_supervision.setOnClickListener(Father_Cancel_Supervision_Listener);
                v_btn_father_lock.setOnClickListener(Father_lock_Listener);
                v_btn_father_lock.setVisibility(View.VISIBLE);
            }
            if (rd.getTime() != 0) {
                v_contact_status.setVisibility(View.VISIBLE);
                String msg1 = getString(R.string.time_limited);
                v_contact_status.setText(msg1 + rd.getTime() / 60 + getString(R.string.MIN2));
            }
        }

    }

    /* more than one records mean that there are outstanding request or notification*/
    private void process2() {
        for (int i = 1; i < relationList.size(); i++) {
            //notification or request
            RelationData item = relationList.get(i);
            long contacts_role = item.getRole();
            long sts = item.getStatus();
            UtilLog.logWithCodeInfo("Current status is " + String.valueOf(sts), "onResume", "SupervisionDetailsActivity");
            if (contacts_role == RelationData.RELATION_ROLE_FATHER) {
                UtilLog.logWithCodeInfo("Current contact role is FATHER", "onResume", "SupervisionDetailsActivity");
                FatherDialogPop(sts);
            } else if (contacts_role == RelationData.RELATION_ROLE_SON) {
                UtilLog.logWithCodeInfo("Current contact role is SON", "onResume", "SupervisionDetailsActivity");
                SonDialogPop(sts);
            }
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

    private void FatherDialogPop(long sts) {
        switch ((int) sts) {
            //Father
            case RelationData.RELATION_FATHER_SUPERVISION_REQUEST: //10
                final String father_id = contacts_id;
                final String son_id = RegistrationManager.getUserId();
                String msg = String.format(getString(R.string.family_apply_monitor), contacts_name);
                DialogBuilderMgr.CreateAlertDialog(
                        cxt,
                        msg,
                        new IAlarmDialog() {
                            @Override
                            public void consume() {
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (true == SupervisionManager.agree_supervision_request_with_activity(cxt, father_id, son_id, "")) {
                                            showSonActions(false, true, false);
                                            SupervisionManager.add_family_async(contacts_name, contacts_phone);
                                            UtilLog.showToast(cxt, getString(R.string.status_11_label), Toast.LENGTH_LONG);
                                        } else {
                                            UtilLog.showToast(cxt, getString(R.string.status_11_label_fail), Toast.LENGTH_LONG);
                                        }
                                    }
                                }).start();
                            }
                        },

                        new IAlarmDialog() {
                            @Override
                            public void consume() {
                                SupervisionManager.disagree_supervision_request(father_id, son_id, "");
                                UtilLog.showToast(cxt, getString(R.string.status_12_label), Toast.LENGTH_LONG);
                            }
                        },
                        null);
                break;
            case RelationData.RELATION_FATHER_APPROVE_SUPERVISION_FREE: //21
                showSonActions(true, false, false);
                UtilLog.showToast(cxt, contacts_name + getString(R.string.status_21_label_agree), Toast.LENGTH_LONG);
                break;
            case RelationData.RELATION_FATHER_DISAPPROVE_SUPERVISION_FREE: //22
                showSonActions(false, true, false);
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

    private void showSonActions(boolean both_action_1, boolean action_2, boolean action_3) {
        if (both_action_1) {
            if(!isFirstVersion)v_btn_both_request_supervision.setVisibility(View.VISIBLE);
        } else {
            v_btn_both_request_supervision.setVisibility(View.GONE);
        }
        if (action_2) {
            v_btn_son_request_cancel_supervision.setVisibility(View.VISIBLE);
        } else {
            v_btn_son_request_cancel_supervision.setVisibility(View.GONE);
        }
/*        if (action_3) {
            v_btn_son_request_unlock.setVisibility(View.VISIBLE);
        } else {
            v_btn_son_request_unlock.setVisibility(View.GONE);
        }*/
    }

    private void showFatherActions(boolean both_action_1, boolean action_2, boolean action_3) {
        if (both_action_1) {
            if(!isFirstVersion)v_btn_both_request_supervision.setVisibility(View.VISIBLE);
        } else {
            v_btn_both_request_supervision.setVisibility(View.GONE);
        }
        if (action_2) {
            v_btn_father_cancel_supervision.setVisibility(View.VISIBLE);
            v_btn_see_daily.setVisibility(View.VISIBLE);
        } else {
            v_btn_father_cancel_supervision.setVisibility(View.GONE);
            v_btn_see_daily.setVisibility(View.GONE);
        }
/*        if (action_3) {
            v_btn_father_unlock.setVisibility(View.VISIBLE);
        } else {
            v_btn_father_unlock.setVisibility(View.GONE);
        }*/
    }

    private void SonDialogPop(long sts) {
        switch ((int) sts) {
            //son
            case RelationData.RELATION_SON_AGREE_SUPERVISION:// = 11;
                showFatherActions(false, true, false);
                UtilLog.showToast(cxt, contacts_name + getString(R.string.status_11_label), Toast.LENGTH_LONG);
                break;
            case RelationData.RELATION_SON_DISAGREE_SUPERVISION:// = 12;
                showFatherActions(true, false, false);
                UtilLog.showToast(cxt, contacts_name + getString(R.string.status_12_label), Toast.LENGTH_LONG);
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
                                    showFatherActions(true, false, false);
                                    UtilLog.showToast(cxt, contacts_name + getString(R.string.status_21_label), Toast.LENGTH_LONG);
                                } else {
                                    UtilLog.showToast(cxt, contacts_name + getString(R.string.status_21_label_fail), Toast.LENGTH_LONG);
                                }
                            }
                        },

                        new IAlarmDialog() {
                            @Override
                            public void consume() {
                                return;
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

    private boolean phoneBound() {
        boolean ret = true;
        if (null == RegistrationManager.getUserId()) {
            ret = false;
        }
        return ret;
    }

    private void showBindDialog() {
        final Dialog dialog = new Dialog(cxt);
        dialog.setContentView(R.layout.pop_binding_phone);

//        final AlertDialog.Builder builder = new AlertDialog.Builder(cxt);
        Button pos_button = (Button) dialog.findViewById(R.id.pos_button);

        Button nag_button = (Button) dialog.findViewById(R.id.nag_button);
        nag_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        final EditText phoneNumber = (EditText) dialog.findViewById(R.id.phone_nubmer);
        phoneNumber.setInputType(InputType.TYPE_CLASS_NUMBER);
        pos_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dur = phoneNumber.getText().toString();
                if ("".equals(dur)) {
                    UtilLog.showAlert(cxt, getString(R.string.family_bind_dialog_monitor_title), getString(R.string.family_phone_null));
                    return;
                }
                String ret = RegistrationManager.binding_phone(dur);
                if ("0".equals(ret)) {
                    UtilLog.showToast(cxt, getString(R.string.family_bind_passed), Toast.LENGTH_SHORT);
                } else {
                    UtilLog.showToast(cxt, getString(R.string.family_bind_failed), Toast.LENGTH_SHORT);
                }
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
