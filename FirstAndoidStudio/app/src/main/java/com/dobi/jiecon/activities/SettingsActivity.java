package com.dobi.jiecon.activities;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.dobi.jiecon.App;
import com.dobi.jiecon.R;
import com.dobi.jiecon.UtilLog;
import com.dobi.jiecon.database.JieconDBHelper;
import com.dobi.jiecon.database.sqlite.SqliteBase;
import com.dobi.jiecon.datacontroller.RegistrationManager;
import com.dobi.jiecon.datacontroller.SupervisionManager;
import com.dobi.jiecon.service.WindowChangeDetectingService;

public class SettingsActivity extends ControlViewActivity {
    private RelativeLayout m_tv_incoming_leadboard;
    private Context mContext;
    // binding phone
    private RelativeLayout m_rl_binding;

    //检查新版本
    private RelativeLayout m_rl_check_version;
    //给戒戒评分
    private RelativeLayout m_rl_comment;
    //用户协议
    private RelativeLayout m_rl_policy;
    //反馈
    private RelativeLayout m_rl_feedbook;
    //帮助
    private RelativeLayout m_rl_help;
    private SettingsActivity _this;
    /**
     * An intent for launching the system settings.
     */
    private static final Intent sSettingsIntent =
            new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);

    public void onCreate(Bundle savedInstanceState) {
        mType = 1;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.more);
        mContext = this;

        _this = this;
        TelephonyManager tMgr = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        String mPhoneNumber = tMgr.getLine1Number();
        if (mPhoneNumber == null) {
            mPhoneNumber = "";
        }

        TextView bind_phone = (TextView) findViewById(R.id.lbl_bind_phone);
        String change_phone = getResources().getString(R.string.setting_change_phone);
        String label = String.format(change_phone, mPhoneNumber);
        bind_phone.setText(label);


        RelativeLayout rl_bind_phone = (RelativeLayout) findViewById(R.id.bind_phone);

        rl_bind_phone.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                RegistrationManager.statistics(RegistrationManager.STATISTICS_TYPE_BINDING);
                final EditText inputServer = new EditText(mContext);
                inputServer.setInputType(InputType.TYPE_CLASS_PHONE);
                TelephonyManager tMgr = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
                String mPhoneNumber = tMgr.getLine1Number();
                if (mPhoneNumber != null) {
                    inputServer.setText(mPhoneNumber);
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                String binding = getResources().getString(R.string.setting_binding_phone);
                String binding_ok = getResources().getString(R.string.setting_ok);
                String binding_cancel = getResources().getString(R.string.setting_cancel);
                builder.setTitle(binding).setIcon(android.R.drawable.ic_dialog_info).setView(inputServer)
                        .setNegativeButton(binding_cancel, null);
                builder.setPositiveButton(binding_ok, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        Editable editPhone = inputServer.getText();

                        if (editPhone == null) {
                            return;
                        }
                        String phone = inputServer.getText().toString();
                        if ("0".equals(RegistrationManager.binding_phone(phone))) {

                        } else {
                            Resources res = getResources();
                            String hint_title = res.getString(R.string.family_bind_dialog_monitor_title);
                            String binding_hint = res.getString(R.string.family_bind_failed);
                            UtilLog.showAlert(mContext, hint_title, binding_hint);
                        }
                    }
                });
                builder.show();


            }
        });
        RelativeLayout change_password = (RelativeLayout) findViewById(R.id.change_password);

        change_password.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


            }
        });

        RelativeLayout change_nickname = (RelativeLayout) findViewById(R.id.change_nickname);
        change_nickname.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final EditText inputServer = new EditText(mContext);
                String nickname = SqliteBase.get_config(JieconDBHelper.NICKNAME_KEY);
                if (nickname != null) {
                    inputServer.setText(nickname);
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                final String nickname_hint = getResources().getString(R.string.setting_nickname_hint);
                final String nickname_sucess = getResources().getString(R.string.setting_nickname_modify_success);
                final String nickname_failed = getResources().getString(R.string.setting_nickname_modify_failed);
                final String nickname_label = getResources().getString(R.string.setting_nickname_label);

                String res_nickname = getResources().getString(R.string.setting_nickname_input);
                String binding_ok = getResources().getString(R.string.setting_ok);
                String binding_cancel = getResources().getString(R.string.setting_cancel);
                builder.setTitle(res_nickname).setIcon(android.R.drawable.ic_dialog_info).setView(inputServer)
                        .setNegativeButton(binding_cancel, null);
                builder.setPositiveButton(binding_ok, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        Editable editPhone = inputServer.getText();
                        if (editPhone == null) {
                            return;
                        }
                        String nickname = inputServer.getText().toString();
                        if (RegistrationManager.setNickname(nickname)) {
                            UtilLog.showAlert(mContext, nickname_hint, nickname_sucess);
                            TextView lbl_change_nickname = (TextView) findViewById(R.id.lbl_change_nickname);
                            final String label = String.format(nickname_label, nickname);
                            lbl_change_nickname.setText(label);
                        } else {

                            UtilLog.showAlert(mContext, nickname_hint, nickname_failed);
                        }
                    }
                });
                builder.show();

            }
        });


        initEditText(R.id.text_limit_1, JieconDBHelper.TIME_WEEK_1);
        initEditText(R.id.text_limit_2, JieconDBHelper.TIME_WEEK_2);
        initEditText(R.id.text_limit_3, JieconDBHelper.TIME_WEEK_3);
        initEditText(R.id.text_limit_4, JieconDBHelper.TIME_WEEK_4);
        initEditText(R.id.text_limit_5, JieconDBHelper.TIME_WEEK_5);
        initEditText(R.id.text_limit_6, JieconDBHelper.TIME_WEEK_6);
        initEditText(R.id.text_limit_7, JieconDBHelper.TIME_WEEK_7);
        CheckBox outside_limited_lock = (CheckBox) findViewById(R.id.outside_limited_lock);

        outside_limited_lock.setOnCheckedChangeListener(onCheckedChangeListener);
        if ("1".equals(SqliteBase.get_config(JieconDBHelper.HAS_LOCK))) {
            outside_limited_lock.setChecked(true);
        } else {
            outside_limited_lock.setChecked(false);
        }

        outside_limited_lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegistrationManager.statistics(RegistrationManager.STATISTICS_TYPE_LOCK_SETTING);
            }
        });

        CheckBox outside_limited_alert = (CheckBox) findViewById(R.id.outside_limited_alert);

        outside_limited_alert.setOnCheckedChangeListener(onCheckedChangeListener);
        if ("1".equals(SqliteBase.get_config(JieconDBHelper.HAS_ALARM))) {
            outside_limited_alert.setChecked(true);
        } else {
            outside_limited_alert.setChecked(false);
        }
//        m_rl_about = (RelativeLayout) findViewById(R.id.rl_about);
//        m_rl_about.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				Intent intent = new Intent(mContext, MoreAbout.class);
//				startActivity(intent);
//			}
//		});


        // Add a shortcut to the accessibility settings.
        RelativeLayout accessibility_service = (RelativeLayout) findViewById(R.id.accessibility_service);
        accessibility_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegistrationManager.statistics(RegistrationManager.STATISTICS_TYPE_ACCESSIBILITY);
                startActivity(sSettingsIntent);
            }
        });
        ScrollView scroll_top = (ScrollView) findViewById(R.id.scroll_top);
        //      scroll_top.setVisibility(View.GONE);


        //   showMonitor();
        /*
        RelativeLayout lbl_father_list = (RelativeLayout) findViewById(R.id.lbl_father_list);
        lbl_father_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mType = 1;
                ScrollView scroll_top = (ScrollView) findViewById(R.id.scroll_top);
                scroll_top.setVisibility(View.GONE);
                showMonitor();
                TabSample.mTab.findViewById(R.id.title_bar_back).setVisibility(View.VISIBLE);
            }
        });
        RelativeLayout lbl_son_list = (RelativeLayout) findViewById(R.id.lbl_son_list);
        lbl_son_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mType = 2;
                ScrollView scroll_top = (ScrollView) findViewById(R.id.scroll_top);
                scroll_top.setVisibility(View.GONE);
                showMonitor();
                TabSample.mTab.findViewById(R.id.title_bar_back).setVisibility(View.VISIBLE);
            }
        });
*/
        m_rl_binding = (RelativeLayout) findViewById(R.id.bind_phone);
        m_rl_binding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBindDialog();
            }
        });

        m_rl_check_version = (RelativeLayout) findViewById(R.id.rl_versioncheck);
        m_rl_check_version.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pkgname = App.getAppContext().getPackageName();
                try {
                    PackageInfo pInfo = App.getAppContext().getPackageManager().getPackageInfo(pkgname, 0);
                    int versionCode = pInfo.versionCode;
                    String serverCode = RegistrationManager.getVersionCode();
                    // local version code is small then the server, it should be updated
                    if (versionCode < Integer.valueOf(serverCode)) {
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + pkgname)));
                        } catch (android.content.ActivityNotFoundException anfe) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + pkgname)));
                        }
                    } else {
                        // show dialog it's the newest version, don't need updated.
                        // rock wcl to do
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

        m_rl_comment = (RelativeLayout) findViewById(R.id.rl_commentjiejie);
        m_rl_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pkgname = App.getAppContext().getPackageName();
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + pkgname)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + pkgname)));
                }
            }
        });

        m_rl_policy = (RelativeLayout) findViewById(R.id.rl_user_policy);
        m_rl_policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(this, TabSample.class));
                openPrivatePolicy();
            }
        });

        m_rl_feedbook = (RelativeLayout) findViewById(R.id.rl_feedback);
        m_rl_feedbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFeedback();
            }
        });

        m_rl_help = (RelativeLayout) findViewById(R.id.rl_help);
        m_rl_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openHelp();
            }
        });


        TabSample.mTab.findViewById(R.id.title_bar_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScrollView scroll_top = (ScrollView) findViewById(R.id.scroll_top);
                scroll_top.setVisibility(View.VISIBLE);
            }
        });

    }

    private void openPrivatePolicy() {
        startActivity(new Intent(this, PrivatePolicy.class));
    }

    private void openFeedback() {
        String pkgname = App.getAppContext().getPackageName();
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + pkgname)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + pkgname)));
        }
    }

    private void openHelp() {
        startActivity(new Intent(this, SettingHelp.class));
    }

    private void initEditText(int editId, int id) {
        try {
            EditText ed = (EditText) findViewById(editId);
            ed.addTextChangedListener(watcher);
            String sText = SqliteBase.get_config(id);
            if (sText != null && sText.length() > 0) {
                ed.setText(sText);
            }
        } catch (Exception e) {

        }
    }


    CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView,
                                     boolean isChecked) {
            // TODO Auto-generated method stub
            switch (buttonView.getId()) {
                case R.id.outside_limited_alert:
                    if (isChecked) {
                        SqliteBase.insert_update_config(JieconDBHelper.HAS_ALARM, "1");
                    } else {
                        SqliteBase.insert_update_config(JieconDBHelper.HAS_ALARM, "0");
                    }
                    break;
                case R.id.outside_limited_lock:
                    if (isChecked) {
                        SupervisionManager.get_lock_screen_policy(_this);
                        SqliteBase.insert_update_config(JieconDBHelper.HAS_LOCK, "1");
                    } else {
                        SqliteBase.insert_update_config(JieconDBHelper.HAS_LOCK, "0");
                    }
                    break;
            }

        }
    };


    @Override
    public void onResume() {
        super.onResume();
        ScrollView scroll_top = (ScrollView) findViewById(R.id.scroll_top);
        scroll_top.setVisibility(View.VISIBLE);

        CheckBox outside_limited_lock = (CheckBox) findViewById(R.id.outside_limited_lock);
        if ("1".equals(SqliteBase.get_config(JieconDBHelper.HAS_LOCK)) && SupervisionManager.has_lock_screen_policy()) {
            outside_limited_lock.setChecked(true);
        } else {
            outside_limited_lock.setChecked(false);
        }

        TextView lbl_accessibility_service = (TextView) findViewById(R.id.lbl_accessibility_service);
        if (isServiceRunning()) {
            String start = getResources().getString(R.string.setting_start_monitor);

            lbl_accessibility_service.setText(start);
        } else {
            String stop = getResources().getString(R.string.setting_stop_monitor);
            lbl_accessibility_service.setText(stop);
        }
        TextView lbl_change_nickname = (TextView) findViewById(R.id.lbl_change_nickname);
        String nickname = SqliteBase.get_config(JieconDBHelper.NICKNAME_KEY);
        if (nickname == null) {
            nickname = "";
        }
        final String nickname_label = getResources().getString(R.string.setting_nickname_label);
        String label = String.format(nickname_label, nickname);
        lbl_change_nickname.setText(label);

        TextView lbl_user_id = (TextView) findViewById(R.id.lbl_userid);
        String userid = RegistrationManager.getUserId();
        final String userid_label = getResources().getString(R.string.setting_userid_label);
        label = String.format(userid_label, userid);
        lbl_user_id.setText(label);

    }


    private TextWatcher watcher = new TextWatcher() {

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            EditText ed;
            View v = getCurrentFocus();
            if (v == null) {
                return;
            }
            switch (v.getId()) {
                case R.id.text_limit_1:
                    ed = (EditText) v;
                    SqliteBase.insert_update_config(JieconDBHelper.TIME_WEEK_1, (ed.getText() == null) ? "" : ed.getText().toString());
                    break;
                case R.id.text_limit_2:
                    ed = (EditText) v;
                    SqliteBase.insert_update_config(JieconDBHelper.TIME_WEEK_2, (ed.getText() == null) ? "" : ed.getText().toString());
                    break;
                case R.id.text_limit_3:
                    ed = (EditText) v;
                    SqliteBase.insert_update_config(JieconDBHelper.TIME_WEEK_3, (ed.getText() == null) ? "" : ed.getText().toString());
                    break;
                case R.id.text_limit_4:
                    ed = (EditText) v;
                    SqliteBase.insert_update_config(JieconDBHelper.TIME_WEEK_4, (ed.getText() == null) ? "" : ed.getText().toString());
                    break;
                case R.id.text_limit_5:
                    ed = (EditText) v;
                    SqliteBase.insert_update_config(JieconDBHelper.TIME_WEEK_5, (ed.getText() == null) ? "" : ed.getText().toString());
                    break;
                case R.id.text_limit_6:
                    ed = (EditText) v;
                    SqliteBase.insert_update_config(JieconDBHelper.TIME_WEEK_6, (ed.getText() == null) ? "" : ed.getText().toString());
                    break;
                case R.id.text_limit_7:
                    ed = (EditText) v;
                    SqliteBase.insert_update_config(JieconDBHelper.TIME_WEEK_7, (ed.getText() == null) ? "" : ed.getText().toString());
                    break;
            }
        }

    };

    public static boolean isServiceRunning() {
        ActivityManager manager = (ActivityManager) App.getAppContext().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (WindowChangeDetectingService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void showBindDialog() {
        RegistrationManager.statistics(RegistrationManager.STATISTICS_TYPE_BINDING);
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.pop_binding_phone);
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
                    UtilLog.showAlert(mContext, getString(R.string.family_bind_dialog_monitor_title), getString(R.string.family_phone_null));
                    return;
                }
                String ret = RegistrationManager.binding_phone(dur);
                if ("0".equals(ret)) {
                    UtilLog.showToast(mContext, getString(R.string.family_bind_passed), Toast.LENGTH_SHORT);
                } else {
                    UtilLog.showToast(mContext, getString(R.string.family_bind_failed), Toast.LENGTH_SHORT);
                }
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}