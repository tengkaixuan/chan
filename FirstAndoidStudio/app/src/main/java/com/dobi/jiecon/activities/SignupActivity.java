package com.dobi.jiecon.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dobi.jiecon.R;
import com.dobi.jiecon.datacontroller.RegistrationManager;
import com.dobi.jiecon.utils.DebugControl;
import com.dobi.jiecon.utils.DialogBuilderMgr;
import com.dobi.jiecon.utils.IAlarmDialog;
import com.dobi.jiecon.utils.SmsMgr;

import java.util.PriorityQueue;
import java.util.Timer;
import java.util.TimerTask;

public class SignupActivity extends Activity {
    private Button m_sign_up_btn;
    private Button m_get_code_btn;
    private Resources res;
    private TextView m_phone_txt_view;
    private TextView m_country_code_view;

    private EditText m_login_name_txt_view, m_password_txt_view,m_code_txt_view;

    private int count = 0;
    Timer timer;
    private final int TIME_PERIOD = 30;

    //    private String phone_number;
    private Activity cxt;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what > 0) {
                m_get_code_btn.setText(count + getString(R.string.reget_after));
            }else {
                m_get_code_btn.setText(R.string.reget_auth_code);
                timer.cancel();
                count = 0;
                m_get_code_btn.setClickable(true);
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        res = getResources();
        cxt = this;

        init();

        m_sign_up_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
    private void init(){
        //Login name
        m_login_name_txt_view = (EditText)findViewById(R.id.register_full_name);

        //Phone info
        m_country_code_view = (TextView) findViewById(R.id.register_country_code);
        m_phone_txt_view = (TextView) findViewById(R.id.register_phone_number);
        //Verification code
        m_code_txt_view = (EditText)findViewById(R.id.register_txt_veri_code);
        m_get_code_btn = (Button)findViewById(R.id.btnGetAuthCode);
        m_get_code_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetAuthCode(v);
            }
        });
        //Password
        m_password_txt_view =  (EditText)findViewById(R.id.RegisterPasswordEnterEditText);
        m_sign_up_btn = (Button) findViewById(R.id.sign_up_button);
    }

    private void signUpHandler(View v){
        //check the verification code
        if (m_code_txt_view.getText().equals(RegistrationManager.getCode())){
            String phone_number = m_phone_txt_view.getText().toString();
            String phone_number_with_code = m_country_code_view.getText().toString() + phone_number;
            //bind phone
           if ( "0".equals(RegistrationManager.binding_phone(phone_number_with_code))||"0".equals(RegistrationManager.binding_phone(phone_number))){

           }
        }
        else{
            Toast.makeText(cxt, res.getString(R.string.error_invalid_veri_code),Toast.LENGTH_SHORT);
        }
        return;
    }
    private IAlarmDialog getConfirmPhoneOK() {

        return new IAlarmDialog() {
            @Override
            public void consume() {
                String phone_number = m_phone_txt_view.getText().toString();
                String phone_number_with_code = m_country_code_view.getText().toString() + phone_number;
                if (DebugControl.DEBUG_FLAG) {
                    Toast.makeText(cxt, "When you see this message you should run the app under a emulator. Please set DebugControl.DEBUG_FLAG to false when you run on a real phone. With the real phone, there will be verification code sent out.", Toast.LENGTH_LONG);
                } else {
                    SmsMgr.sendSMS(phone_number_with_code, res.getString(R.string.sms_authentication_code_info) + RegistrationManager.getCode());
                }
                final EditText inputDuration = new EditText(cxt);
                inputDuration.setInputType(InputType.TYPE_CLASS_TEXT);
                AlertDialog.Builder builder = new AlertDialog.Builder(cxt);
                builder.setTitle(res.getString(R.string.control_supervision_mins_lable)).setIcon(android.R.drawable.ic_dialog_info).setView(inputDuration)
                        .setNegativeButton(res.getString(R.string.family_cancel), null);

                builder.setPositiveButton(res.getString(R.string.OK), new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        Editable editDur = inputDuration.getText();

                        if (editDur == null) {
                            return;
                        }
                        String v_code = inputDuration.getText().toString();
                        if (v_code.equals(RegistrationManager.getCode())) {
//                            startActivity(cxt, LoginActivity.class);
                        } else {

                        }
                    }
                });
                builder.show();
            }
        };
    }

    public void GetAuthCode(View view){
        if(count == 0){
            count = TIME_PERIOD;
            m_get_code_btn.setClickable(false);
            m_code_txt_view.setText("");
            startCount();
        }
    }
    public void startCount() {
        count = 0;
        timer = new Timer();
        TimerTask timerTask;
        timerTask = new TimerTask() {
            @Override
            public void run() {
                if (count > 0) count--;
                Message msg = new Message();
                msg.what = count;
                handler.sendMessage(msg);
            }
        };
        timer.schedule(timerTask, 0, 1000);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
