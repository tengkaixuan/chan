package com.dobi.jiecon.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import com.dobi.jiecon.database.sqlite.SqliteBase;
import com.dobi.jiecon.datacontroller.RegistrationManager;
import com.dobi.jiecon.datacontroller.SupervisionManager;
import com.dobi.jiecon.utils.DialogBuilderMgr;
import com.dobi.jiecon.utils.IAlarmDialog;

import java.util.ArrayList;
import java.util.List;

public class FriendActivity extends Activity{
    //Views
    private TextView v_jiecon_id;
    private TextView v_contact_role;
    private TextView v_txt_phone_number;
    private TextView v_txt_contact_name;
    //    private TextView v_contact_status;
    private Button v_btn_both_request_supervision;
    private ImageView v_img_title_bar_back;

    private String contacts_id;
    private String contacts_name;
    private String contacts_phone;
    private List<RelationData> relationList;

    private static Activity cxt;
    private boolean phone_is_bind = false;
    private boolean contacts_is_jiejie_user = false;

    private final boolean isFirstVersion = true;
    private boolean pre_process() {

        boolean ret = true;
        String flag = RegistrationManager.getBindingFlag();
        if (flag == null || !flag.equals(RegistrationManager.BIND_YES)) {
            showBindDialog();
            return false;
        }

        if (contacts_id == null) {
            contacts_id = SqliteBase.get_usrid_from_family_list(contacts_phone);
            UtilLog.logWithCodeInfo("[PROGRESS] contacts_id is "+ contacts_id, "pre_process", "FriendActivity");
            if (contacts_id == null) {
                UtilLog.showToast(cxt, getString(R.string.supervision_warn), Toast.LENGTH_SHORT);
                return false;
            }
        }
        if (!SupervisionManager.is_friend_by_phone(contacts_phone)) {
            UtilLog.logWithCodeInfo("contacts_phone=" + contacts_phone, "pre_process", "SupervisionDetailsActivity");
            List<String> userList = new ArrayList<String>();
            userList.add(contacts_id);
            UtilLog.logWithCodeInfo("[PROGRESS]Add contact to friend", "pre_process", "SupervisionDetailsActivity");
            SupervisionManager.add_friend_async(RegistrationManager.getUserId(), userList, null);
            UtilLog.logWithCodeInfo("[PROGRESS]Set contact name", "pre_process", "SupervisionDetailsActivity");
            SupervisionManager.setContactNameAsync(contacts_id, contacts_name);
        }
        return ret;
    }

    private View.OnClickListener Request_Supervision_Listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            UtilLog.logWithCodeInfo("[PROGRESS]Entry of Listener", "Request_Supervision_Listener", "SupervisionDetailsActivity");

            if (pre_process() == false) return;

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
                    /*if (null == contacts_id) {
                        UtilLog.logWithCodeInfo("contacts_id is null", "Request_Supervision_Listener", "FriendActivity");
                        contacts_id = SupervisionManager.get_user_id_by_phone(contacts_phone);
                    }*/
                    if (contacts_id != null) {
                        if (true == SupervisionManager.supervision_request(RegistrationManager.getUserId(), contacts_id, t1 * 60, "")) {
//                        showFatherActions(false,true,false);
//                        v_contact_status.setText(getString(R.string.action_supervise));
                            UtilLog.showToast(cxt, getString(R.string.supervision_setting_ok), Toast.LENGTH_SHORT);
                        } else {
                            UtilLog.showToast(cxt, getString(R.string.supervision_setting_fail), Toast.LENGTH_SHORT);
                        }
                    }
                }
            });
            builder.show();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.family_individual_friend);
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
        UtilLog.logWithCodeInfo("[PROGRESS] Entry of FriendActivity", "onCreate", "FriendActivity");

        initProfileViews();
        initActionBtns();

    }

    @Override
    public void onResume() {
        super.onResume();
        relationList = SupervisionManager.get_individual_list(contacts_id);
        if (relationList == null) {
            UtilLog.logWithCodeInfo("[PROGRESS] Current relation list count is NULL", "onResume", "SupervisionDetailsActivity");
            relationList = new ArrayList<RelationData>();
            relationList.add(init());
        }
        UtilLog.logWithCodeInfo("[PROGRESS] Valid relation list count is " + relationList.size(), "onResume", "SupervisionDetailsActivity");
        process1();
        process2();
    }

    private void initProfileViews() {
        v_txt_contact_name = (TextView) findViewById(R.id.contact_name);
        v_jiecon_id = (TextView) findViewById(R.id.contact_number);
        v_contact_role = (TextView) findViewById(R.id.contact_role);
        v_contact_role.setText(getString(R.string.family_friend));

//        v_contact_status = (TextView) findViewById(R.id.contacts_status);
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

        if (contacts_id == null) {
            v_jiecon_id.setText("");
        } else {
            v_jiecon_id.setText(contacts_id);
        }

//        v_contact_status.setText(getString(R.string.family_friend));
    }
    private void initActionBtns() {
        v_btn_both_request_supervision = (Button) findViewById(R.id.both_action_1);
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
            v_btn_both_request_supervision.setOnClickListener(Request_Supervision_Listener);
//            if(!isFirstVersion)v_btn_both_request_supervision.setVisibility(View.VISIBLE);
//            v_contact_status.setVisibility(View.GONE);
        } else {
            UtilLog.logWithCodeInfo("status is incorrect", "process1()", "FriendActivity");
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
                UtilLog.logWithCodeInfo("[PROGRESS] The status is FATHER_SUPERVISION_REQUEST(10)", "onResume", "SupervisionDetailsActivity");
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
                                            SupervisionManager.add_family_async(contacts_name, contacts_phone);

                                            UtilLog.showToast(cxt, getString(R.string.status_11_label), Toast.LENGTH_LONG);
                                            goToParentActivity();
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
            default:
        }
    }

    private void SonDialogPop(long sts) {
        switch ((int) sts) {
            //son
            case RelationData.RELATION_SON_AGREE_SUPERVISION:// = 11;
                UtilLog.logWithCodeInfo("[PROGRESS] The status is SON_AGREE_SUPERVISION(11)", "onResume", "SupervisionDetailsActivity");
                UtilLog.showToast(cxt, contacts_name + getString(R.string.status_11_label), Toast.LENGTH_LONG);
                goToKidsActivity();
                break;
            case RelationData.RELATION_SON_DISAGREE_SUPERVISION:// = 12;
                UtilLog.logWithCodeInfo("[PROGRESS] The status is SON_DISAGREE_SUPERVISION(12)", "onResume", "SupervisionDetailsActivity");
                UtilLog.showToast(cxt, contacts_name + getString(R.string.status_12_label), Toast.LENGTH_LONG);
                break;
        }
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
                RegistrationManager.binding_phone_async(dur, null);
                UtilLog.showToast(cxt, getString(R.string.family_bind_passed), Toast.LENGTH_SHORT);
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    private void goToParentActivity(){
        Intent intent = new Intent(cxt, ParentActivity.class);
        intent.putExtra(App.KEY_CURRENT_USR_NAME, contacts_name);
        intent.putExtra(App.KEY_CURRENT_USR_PHONE, contacts_phone);
        intent.putExtra(App.KEY_FROM_CXT, App.ID_FAMILY_LIST_ADAPTER);
        cxt.startActivity(intent);
    }
    private void goToKidsActivity(){
        Intent intent = new Intent(cxt, KidsActivity.class);
        intent.putExtra(App.KEY_CURRENT_USR_NAME, contacts_name);
        intent.putExtra(App.KEY_CURRENT_USR_PHONE, contacts_phone);
        intent.putExtra(App.KEY_FROM_CXT, App.ID_FAMILY_LIST_ADAPTER);
        cxt.startActivity(intent);
    }
}
